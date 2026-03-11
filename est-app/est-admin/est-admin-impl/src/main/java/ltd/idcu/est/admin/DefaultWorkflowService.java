package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.WorkflowService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultWorkflowService implements WorkflowService {
    
    public DefaultWorkflowService() {
    }
    
    @Override
    public List<?> getAllDefinitions() {
        return List.of();
    }
    
    @Override
    public Optional<?> getDefinition(String definitionId) {
        return Optional.empty();
    }
    
    @Override
    public Object createDefinition(String name, String description, String jsonDefinition) {
        throw new UnsupportedOperationException("Workflow Service not available");
    }
    
    @Override
    public Object updateDefinition(String definitionId, String name, String description, String jsonDefinition) {
        throw new UnsupportedOperationException("Workflow Service not available");
    }
    
    @Override
    public void deleteDefinition(String definitionId) {
        throw new UnsupportedOperationException("Workflow Service not available");
    }
    
    @Override
    public List<?> getAllInstances() {
        return List.of();
    }
    
    @Override
    public List<?> getInstancesByDefinition(String definitionId) {
        return List.of();
    }
    
    @Override
    public Optional<?> getInstance(String instanceId) {
        return Optional.empty();
    }
    
    @Override
    public Object startWorkflow(String definitionId, Map<String, Object> variables) {
        throw new UnsupportedOperationException("Workflow Service not available");
    }
    
    @Override
    public boolean pauseWorkflow(String instanceId) {
        return false;
    }
    
    @Override
    public boolean resumeWorkflow(String instanceId) {
        return false;
    }
    
    @Override
    public boolean cancelWorkflow(String instanceId) {
        return false;
    }
    
    @Override
    public boolean retryWorkflow(String instanceId) {
        return false;
    }
    
    @Override
    public Map<String, Object> getInstanceVariables(String instanceId) {
        return new HashMap<>();
    }
    
    @Override
    public void setInstanceVariable(String instanceId, String key, Object value) {
        throw new UnsupportedOperationException("Workflow Service not available");
    }
}
