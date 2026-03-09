package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.WorkflowService;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowRepository;
import ltd.idcu.est.workflow.core.Workflows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultWorkflowService implements WorkflowService {
    
    private final WorkflowEngine workflowEngine;
    private final WorkflowRepository workflowRepository;
    private final WorkflowDefinitionParser jsonParser;
    
    public DefaultWorkflowService() {
        this.workflowRepository = Workflows.newMemoryRepository();
        this.workflowEngine = Workflows.newWorkflowEngine(workflowRepository);
        this.jsonParser = Workflows.newJsonParser();
        initializeSampleWorkflows();
    }
    
    private void initializeSampleWorkflows() {
        try {
            WorkflowDefinition approvalWorkflow = Workflows.newWorkflowBuilder()
                    .id("leave-approval")
                    .name("请假审批流程")
                    .description("员工请假审批工作流")
                    .startNode(Workflows.newTaskNode("submit", "提交申请", ctx -> {
                        System.out.println("员工提交请假申请");
                    }))
                    .addNode(Workflows.newTaskNode("manager-approve", "经理审批", ctx -> {
                        System.out.println("经理审批中...");
                    }))
                    .addNode(Workflows.newTaskNode("hr-approve", "HR审批", ctx -> {
                        System.out.println("HR审批中...");
                    }))
                    .endNode(Workflows.newTaskNode("complete", "完成", ctx -> {
                        System.out.println("审批流程完成");
                    }))
                    .connect("submit", "manager-approve")
                    .connect("manager-approve", "hr-approve")
                    .connect("hr-approve", "complete")
                    .build();
            
            workflowRepository.saveDefinition(approvalWorkflow);
            workflowEngine.registerWorkflow(approvalWorkflow);
            
        } catch (Exception e) {
            System.err.println("Failed to initialize sample workflows: " + e.getMessage());
        }
    }
    
    @Override
    public List<WorkflowDefinition> getAllDefinitions() {
        return workflowEngine.getRegisteredWorkflows();
    }
    
    @Override
    public Optional<WorkflowDefinition> getDefinition(String definitionId) {
        return workflowEngine.getWorkflow(definitionId);
    }
    
    @Override
    public WorkflowDefinition createDefinition(String name, String description, String jsonDefinition) {
        WorkflowDefinition definition = jsonParser.parse(jsonDefinition);
        workflowRepository.saveDefinition(definition);
        workflowEngine.registerWorkflow(definition);
        return definition;
    }
    
    @Override
    public WorkflowDefinition updateDefinition(String definitionId, String name, String description, String jsonDefinition) {
        workflowEngine.unregisterWorkflow(definitionId);
        WorkflowDefinition definition = jsonParser.parse(jsonDefinition);
        workflowRepository.saveDefinition(definition);
        workflowEngine.registerWorkflow(definition);
        return definition;
    }
    
    @Override
    public void deleteDefinition(String definitionId) {
        workflowEngine.unregisterWorkflow(definitionId);
    }
    
    @Override
    public List<WorkflowInstance> getAllInstances() {
        return workflowEngine.getInstances();
    }
    
    @Override
    public List<WorkflowInstance> getInstancesByDefinition(String definitionId) {
        return workflowEngine.getInstancesByWorkflowId(definitionId);
    }
    
    @Override
    public Optional<WorkflowInstance> getInstance(String instanceId) {
        return workflowEngine.getInstance(instanceId);
    }
    
    @Override
    public WorkflowInstance startWorkflow(String definitionId, Map<String, Object> variables) {
        WorkflowContext context = new DefaultWorkflowContext();
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        return workflowEngine.startWorkflow(definitionId, context);
    }
    
    @Override
    public boolean pauseWorkflow(String instanceId) {
        return workflowEngine.pauseWorkflow(instanceId);
    }
    
    @Override
    public boolean resumeWorkflow(String instanceId) {
        return workflowEngine.resumeWorkflow(instanceId);
    }
    
    @Override
    public boolean cancelWorkflow(String instanceId) {
        return workflowEngine.cancelWorkflow(instanceId);
    }
    
    @Override
    public boolean retryWorkflow(String instanceId) {
        return workflowEngine.retryWorkflow(instanceId);
    }
    
    @Override
    public Map<String, Object> getInstanceVariables(String instanceId) {
        Optional<WorkflowInstance> instanceOpt = workflowEngine.getInstance(instanceId);
        if (instanceOpt.isPresent()) {
            WorkflowInstance instance = instanceOpt.get();
            WorkflowContext context = instance.getContext();
            Map<String, Object> variables = new HashMap<>();
            context.getVariableNames().forEach(name -> {
                context.getVariable(name, Object.class).ifPresent(value -> {
                    variables.put(name, value);
                });
            });
            return variables;
        }
        return new HashMap<>();
    }
    
    @Override
    public void setInstanceVariable(String instanceId, String key, Object value) {
        Optional<WorkflowInstance> instanceOpt = workflowEngine.getInstance(instanceId);
        if (instanceOpt.isPresent()) {
            WorkflowInstance instance = instanceOpt.get();
            instance.getContext().setVariable(key, value);
        }
    }
}
