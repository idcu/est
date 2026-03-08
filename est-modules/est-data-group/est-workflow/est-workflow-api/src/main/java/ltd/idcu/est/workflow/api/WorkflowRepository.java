package ltd.idcu.est.workflow.api;

import java.util.List;
import java.util.Optional;

public interface WorkflowRepository {
    
    void saveDefinition(WorkflowDefinition workflow);
    
    Optional<WorkflowDefinition> findDefinitionById(String workflowId);
    
    List<WorkflowDefinition> findAllDefinitions();
    
    void deleteDefinition(String workflowId);
    
    void saveInstance(WorkflowInstance instance);
    
    Optional<WorkflowInstance> findInstanceById(String instanceId);
    
    List<WorkflowInstance> findInstancesByWorkflowId(String workflowId);
    
    List<WorkflowInstance> findInstancesByStatus(WorkflowStatus status);
    
    List<WorkflowInstance> findAllInstances();
    
    void deleteInstance(String instanceId);
}
