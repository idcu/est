package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.*;
import ltd.idcu.est.workflow.api.builder.WorkflowBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultWorkflowEngine implements WorkflowEngine {
    
    private final Map<String, WorkflowDefinition> workflows;
    private final Map<String, WorkflowInstance> instances;
    private final List<WorkflowListener> workflowListeners;
    private final List<NodeListener> nodeListeners;
    private final ExecutorService executorService;
    private final AtomicLong instanceIdGenerator;
    private final AtomicLong executionIdGenerator;
    private volatile boolean running;
    private WorkflowRepository repository;
    
    public DefaultWorkflowEngine() {
        this.workflows = new HashMap<>();
        this.instances = new HashMap<>();
        this.workflowListeners = new CopyOnWriteArrayList<>();
        this.nodeListeners = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.instanceIdGenerator = new AtomicLong(1);
        this.executionIdGenerator = new AtomicLong(1);
        this.running = true;
    }
    
    @Override
    public void setRepository(WorkflowRepository repository) {
        this.repository = repository;
        for (WorkflowDefinition def : repository.findAllDefinitions()) {
            workflows.put(def.getId(), def);
        }
    }
    
    @Override
    public WorkflowRepository getRepository() {
        return repository;
    }
    
    public static WorkflowBuilder newWorkflowBuilder() {
        return new DefaultWorkflowBuilder();
    }
    
    @Override
    public void registerWorkflow(WorkflowDefinition workflow) {
        workflows.put(workflow.getId(), workflow);
        if (repository != null) {
            repository.saveDefinition(workflow);
        }
    }
    
    @Override
    public void unregisterWorkflow(String workflowId) {
        workflows.remove(workflowId);
    }
    
    @Override
    public Optional<WorkflowDefinition> getWorkflow(String workflowId) {
        return Optional.ofNullable(workflows.get(workflowId));
    }
    
    @Override
    public List<WorkflowDefinition> getRegisteredWorkflows() {
        return new ArrayList<>(workflows.values());
    }
    
    @Override
    public WorkflowInstance startWorkflow(String workflowId) {
        return startWorkflow(workflowId, null);
    }
    
    @Override
    public WorkflowInstance startWorkflow(String workflowId, WorkflowContext context) {
        WorkflowDefinition workflow = workflows.get(workflowId);
        if (workflow == null) {
            throw new WorkflowException("Workflow not found: " + workflowId);
        }
        
        String instanceId = generateInstanceId();
        if (context == null) {
            context = new DefaultWorkflowContext(instanceId);
        }
        
        DefaultWorkflowInstance instance = new DefaultWorkflowInstance(
                instanceId, workflowId, workflow.getName(), context);
        instances.put(instanceId, instance);
        if (repository != null) {
            repository.saveInstance(instance);
        }
        
        executeWorkflow(instance, workflow);
        return instance;
    }
    
    @Override
    public CompletableFuture<WorkflowInstance> startWorkflowAsync(String workflowId) {
        return startWorkflowAsync(workflowId, null);
    }
    
    @Override
    public CompletableFuture<WorkflowInstance> startWorkflowAsync(String workflowId, WorkflowContext context) {
        return CompletableFuture.supplyAsync(() -> startWorkflow(workflowId, context), executorService);
    }
    
    @Override
    public Optional<WorkflowInstance> getInstance(String instanceId) {
        return Optional.ofNullable(instances.get(instanceId));
    }
    
    @Override
    public List<WorkflowInstance> getInstances() {
        return new ArrayList<>(instances.values());
    }
    
    @Override
    public List<WorkflowInstance> getInstancesByStatus(WorkflowStatus status) {
        List<WorkflowInstance> result = new ArrayList<>();
        for (WorkflowInstance instance : instances.values()) {
            if (instance.getStatus() == status) {
                result.add(instance);
            }
        }
        return result;
    }
    
    @Override
    public List<WorkflowInstance> getInstancesByWorkflowId(String workflowId) {
        List<WorkflowInstance> result = new ArrayList<>();
        for (WorkflowInstance instance : instances.values()) {
            if (instance.getWorkflowId().equals(workflowId)) {
                result.add(instance);
            }
        }
        return result;
    }
    
    @Override
    public boolean pauseWorkflow(String instanceId) {
        DefaultWorkflowInstance instance = (DefaultWorkflowInstance) instances.get(instanceId);
        if (instance != null && instance.getStatus() == WorkflowStatus.RUNNING) {
            instance.setStatus(WorkflowStatus.PAUSED);
            notifyWorkflowPause(instance);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean resumeWorkflow(String instanceId) {
        DefaultWorkflowInstance instance = (DefaultWorkflowInstance) instances.get(instanceId);
        if (instance != null && instance.getStatus() == WorkflowStatus.PAUSED) {
            instance.setStatus(WorkflowStatus.RUNNING);
            notifyWorkflowResume(instance);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean cancelWorkflow(String instanceId) {
        DefaultWorkflowInstance instance = (DefaultWorkflowInstance) instances.get(instanceId);
        if (instance != null && 
            (instance.getStatus() == WorkflowStatus.RUNNING || instance.getStatus() == WorkflowStatus.PAUSED)) {
            instance.setStatus(WorkflowStatus.CANCELLED);
            instance.setEndTime(new Date());
            notifyWorkflowCancel(instance);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean retryWorkflow(String instanceId) {
        DefaultWorkflowInstance instance = (DefaultWorkflowInstance) instances.get(instanceId);
        if (instance != null && instance.getStatus() == WorkflowStatus.FAILED) {
            WorkflowDefinition workflow = workflows.get(instance.getWorkflowId());
            if (workflow != null) {
                instance.setStatus(WorkflowStatus.RUNNING);
                executeWorkflow(instance, workflow);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void addWorkflowListener(WorkflowListener listener) {
        workflowListeners.add(listener);
    }
    
    @Override
    public void removeWorkflowListener(WorkflowListener listener) {
        workflowListeners.remove(listener);
    }
    
    @Override
    public void addNodeListener(NodeListener listener) {
        nodeListeners.add(listener);
    }
    
    @Override
    public void removeNodeListener(NodeListener listener) {
        nodeListeners.remove(listener);
    }
    
    @Override
    public void shutdown() {
        running = false;
        executorService.shutdown();
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    private void executeWorkflow(DefaultWorkflowInstance instance, WorkflowDefinition workflow) {
        instance.setStatus(WorkflowStatus.RUNNING);
        instance.setStartTime(new Date());
        notifyWorkflowStart(instance);
        
        try {
            executeFromNode(workflow.getStartNode(), instance, workflow, new java.util.HashSet<>());
            
            if (instance.getStatus() == WorkflowStatus.RUNNING) {
                instance.setStatus(WorkflowStatus.COMPLETED);
                instance.setEndTime(new Date());
                notifyWorkflowComplete(instance);
            }
        } catch (Exception e) {
            instance.setStatus(WorkflowStatus.FAILED);
            instance.setEndTime(new Date());
            notifyWorkflowFail(instance, e);
        }
    }
    
    private void executeFromNode(Node node, DefaultWorkflowInstance instance, WorkflowDefinition workflow, java.util.Set<String> executedIds) {
        if (node == null || executedIds.contains(node.getId()) || instance.getStatus() != WorkflowStatus.RUNNING) {
            return;
        }
        
        executeNode(instance, node);
        executedIds.add(node.getId());
        
        if (node == workflow.getEndNode()) {
            return;
        }
        
        List<Edge> outgoingEdges = workflow.getOutgoingEdges(node.getId());
        
        if (node.getType().equals("PARALLEL_GATEWAY")) {
            if (!outgoingEdges.isEmpty()) {
                for (Edge edge : outgoingEdges) {
                    Node nextNode = workflow.getNode(edge.getTargetNodeId());
                    executeFromNode(nextNode, instance, workflow, executedIds);
                }
            }
        } else {
            Node nextNode = findNextNode(workflow, node, instance.getContext());
            executeFromNode(nextNode, instance, workflow, executedIds);
        }
    }
    
    private void executeNode(DefaultWorkflowInstance instance, Node node) {
        DefaultNodeExecution execution = new DefaultNodeExecution(
                generateExecutionId(), node.getId(), node.getName());
        execution.setStatus(NodeStatus.RUNNING);
        execution.setStartTime(new Date());
        instance.addExecution(execution);
        
        notifyNodeStart(instance, node);
        
        try {
            node.execute(instance.getContext());
            execution.setStatus(NodeStatus.COMPLETED);
            notifyNodeComplete(instance, node);
        } catch (Exception e) {
            execution.setStatus(NodeStatus.FAILED);
            execution.setError(e);
            notifyNodeFail(instance, node, e);
            throw e;
        } finally {
            execution.setEndTime(new Date());
        }
    }
    
    private Node findNextNode(WorkflowDefinition workflow, Node currentNode, WorkflowContext context) {
        List<Edge> outgoingEdges = workflow.getOutgoingEdges(currentNode.getId());
        
        if (!outgoingEdges.isEmpty()) {
            for (Edge edge : outgoingEdges) {
                if (edge.evaluate(context)) {
                    return workflow.getNode(edge.getTargetNodeId());
                }
            }
            return null;
        }
        
        List<Node> nodes = workflow.getNodes();
        int currentIndex = nodes.indexOf(currentNode);
        if (currentIndex >= 0 && currentIndex < nodes.size() - 1) {
            return nodes.get(currentIndex + 1);
        }
        return null;
    }
    
    private String generateInstanceId() {
        return "wf-inst-" + instanceIdGenerator.getAndIncrement();
    }
    
    private String generateExecutionId() {
        return "exec-" + executionIdGenerator.getAndIncrement();
    }
    
    private void notifyWorkflowStart(WorkflowInstance instance) {
        for (WorkflowListener listener : workflowListeners) {
            try {
                listener.onWorkflowStart(instance);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyWorkflowComplete(WorkflowInstance instance) {
        for (WorkflowListener listener : workflowListeners) {
            try {
                listener.onWorkflowComplete(instance);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyWorkflowFail(WorkflowInstance instance, Throwable error) {
        for (WorkflowListener listener : workflowListeners) {
            try {
                listener.onWorkflowFail(instance, error);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyWorkflowPause(WorkflowInstance instance) {
        for (WorkflowListener listener : workflowListeners) {
            try {
                listener.onWorkflowPause(instance);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyWorkflowResume(WorkflowInstance instance) {
        for (WorkflowListener listener : workflowListeners) {
            try {
                listener.onWorkflowResume(instance);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyWorkflowCancel(WorkflowInstance instance) {
        for (WorkflowListener listener : workflowListeners) {
            try {
                listener.onWorkflowCancel(instance);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyNodeStart(WorkflowInstance instance, Node node) {
        for (NodeListener listener : nodeListeners) {
            try {
                listener.onNodeStart(instance, node);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyNodeComplete(WorkflowInstance instance, Node node) {
        for (NodeListener listener : nodeListeners) {
            try {
                listener.onNodeComplete(instance, node);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyNodeFail(WorkflowInstance instance, Node node, Throwable error) {
        for (NodeListener listener : nodeListeners) {
            try {
                listener.onNodeFail(instance, node, error);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
    
    private void notifyNodeSkip(WorkflowInstance instance, Node node) {
        for (NodeListener listener : nodeListeners) {
            try {
                listener.onNodeSkip(instance, node);
            } catch (Exception e) {
                // Log error but don't fail workflow
            }
        }
    }
}
