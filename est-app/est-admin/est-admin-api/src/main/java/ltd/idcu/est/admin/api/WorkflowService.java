package ltd.idcu.est.admin.api;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowInstance;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkflowService {
    
    List<WorkflowDefinition> getAllDefinitions();
    
    Optional<WorkflowDefinition> getDefinition(String definitionId);
    
    WorkflowDefinition createDefinition(String name, String description, String jsonDefinition);
    
    WorkflowDefinition updateDefinition(String definitionId, String name, String description, String jsonDefinition);
    
    void deleteDefinition(String definitionId);
    
    List<WorkflowInstance> getAllInstances();
    
    List<WorkflowInstance> getInstancesByDefinition(String definitionId);
    
    Optional<WorkflowInstance> getInstance(String instanceId);
    
    WorkflowInstance startWorkflow(String definitionId, Map<String, Object> variables);
    
    boolean pauseWorkflow(String instanceId);
    
    boolean resumeWorkflow(String instanceId);
    
    boolean cancelWorkflow(String instanceId);
    
    boolean retryWorkflow(String instanceId);
    
    Map<String, Object> getInstanceVariables(String instanceId);
    
    void setInstanceVariable(String instanceId, String key, Object value);
}
