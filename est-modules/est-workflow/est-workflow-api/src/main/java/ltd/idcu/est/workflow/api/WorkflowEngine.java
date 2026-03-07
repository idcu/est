package ltd.idcu.est.workflow.api;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface WorkflowEngine {
    
    void setRepository(WorkflowRepository repository);
    
    WorkflowRepository getRepository();
    
    void registerWorkflow(WorkflowDefinition workflow);
    
    void unregisterWorkflow(String workflowId);
    
    Optional<WorkflowDefinition> getWorkflow(String workflowId);
    
    List<WorkflowDefinition> getRegisteredWorkflows();
    
    WorkflowInstance startWorkflow(String workflowId);
    
    WorkflowInstance startWorkflow(String workflowId, WorkflowContext context);
    
    CompletableFuture<WorkflowInstance> startWorkflowAsync(String workflowId);
    
    CompletableFuture<WorkflowInstance> startWorkflowAsync(String workflowId, WorkflowContext context);
    
    Optional<WorkflowInstance> getInstance(String instanceId);
    
    List<WorkflowInstance> getInstances();
    
    List<WorkflowInstance> getInstancesByStatus(WorkflowStatus status);
    
    List<WorkflowInstance> getInstancesByWorkflowId(String workflowId);
    
    boolean pauseWorkflow(String instanceId);
    
    boolean resumeWorkflow(String instanceId);
    
    boolean cancelWorkflow(String instanceId);
    
    boolean retryWorkflow(String instanceId);
    
    void addWorkflowListener(WorkflowListener listener);
    
    void removeWorkflowListener(WorkflowListener listener);
    
    void addNodeListener(NodeListener listener);
    
    void removeNodeListener(NodeListener listener);
    
    void shutdown();
    
    boolean isRunning();
}
