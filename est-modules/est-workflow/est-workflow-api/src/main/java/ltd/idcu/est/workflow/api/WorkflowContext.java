package ltd.idcu.est.workflow.api;

import java.util.Map;
import java.util.Optional;

public interface WorkflowContext {
    
    String getWorkflowInstanceId();
    
    void setVariable(String key, Object value);
    
    Optional<Object> getVariable(String key);
    
    <T> Optional<T> getVariable(String key, Class<T> type);
    
    Map<String, Object> getVariables();
    
    void setVariables(Map<String, Object> variables);
    
    boolean hasVariable(String key);
    
    void removeVariable(String key);
    
    void clearVariables();
}
