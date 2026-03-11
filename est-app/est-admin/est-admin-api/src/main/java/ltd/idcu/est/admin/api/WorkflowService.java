package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkflowService {
    
    List<?> getAllDefinitions();
    
    Optional<?> getDefinition(String definitionId);
    
    Object createDefinition(String name, String description, String jsonDefinition);
    
    Object updateDefinition(String definitionId, String name, String description, String jsonDefinition);
    
    void deleteDefinition(String definitionId);
    
    List<?> getAllInstances();
    
    List<?> getInstancesByDefinition(String definitionId);
    
    Optional<?> getInstance(String instanceId);
    
    Object startWorkflow(String definitionId, Map<String, Object> variables);
    
    boolean pauseWorkflow(String instanceId);
    
    boolean resumeWorkflow(String instanceId);
    
    boolean cancelWorkflow(String instanceId);
    
    boolean retryWorkflow(String instanceId);
    
    Map<String, Object> getInstanceVariables(String instanceId);
    
    void setInstanceVariable(String instanceId, String key, Object value);
}
