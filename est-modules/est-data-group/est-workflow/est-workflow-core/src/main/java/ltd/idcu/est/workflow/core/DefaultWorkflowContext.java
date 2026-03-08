package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.WorkflowContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultWorkflowContext implements WorkflowContext {
    
    private final String workflowInstanceId;
    private final Map<String, Object> variables;
    
    public DefaultWorkflowContext(String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
        this.variables = new ConcurrentHashMap<>();
    }
    
    public DefaultWorkflowContext(String workflowInstanceId, Map<String, Object> initialVariables) {
        this.workflowInstanceId = workflowInstanceId;
        this.variables = new ConcurrentHashMap<>(initialVariables);
    }
    
    @Override
    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }
    
    @Override
    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }
    
    @Override
    public Optional<Object> getVariable(String key) {
        return Optional.ofNullable(variables.get(key));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getVariable(String key, Class<T> type) {
        Object value = variables.get(key);
        if (value != null && type.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }
    
    @Override
    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }
    
    @Override
    public void setVariables(Map<String, Object> variables) {
        this.variables.clear();
        this.variables.putAll(variables);
    }
    
    @Override
    public boolean hasVariable(String key) {
        return variables.containsKey(key);
    }
    
    @Override
    public void removeVariable(String key) {
        variables.remove(key);
    }
    
    @Override
    public void clearVariables() {
        variables.clear();
    }
}
