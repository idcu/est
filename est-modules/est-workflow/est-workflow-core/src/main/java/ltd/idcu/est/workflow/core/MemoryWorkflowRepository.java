package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowRepository;
import ltd.idcu.est.workflow.api.WorkflowStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemoryWorkflowRepository implements WorkflowRepository {
    
    private final Map<String, WorkflowDefinition> definitions;
    private final Map<String, WorkflowInstance> instances;
    
    public MemoryWorkflowRepository() {
        this.definitions = new HashMap<>();
        this.instances = new HashMap<>();
    }
    
    @Override
    public void saveDefinition(WorkflowDefinition workflow) {
        definitions.put(workflow.getId(), workflow);
    }
    
    @Override
    public Optional<WorkflowDefinition> findDefinitionById(String workflowId) {
        return Optional.ofNullable(definitions.get(workflowId));
    }
    
    @Override
    public List<WorkflowDefinition> findAllDefinitions() {
        return new ArrayList<>(definitions.values());
    }
    
    @Override
    public void deleteDefinition(String workflowId) {
        definitions.remove(workflowId);
    }
    
    @Override
    public void saveInstance(WorkflowInstance instance) {
        instances.put(instance.getInstanceId(), instance);
    }
    
    @Override
    public Optional<WorkflowInstance> findInstanceById(String instanceId) {
        return Optional.ofNullable(instances.get(instanceId));
    }
    
    @Override
    public List<WorkflowInstance> findInstancesByWorkflowId(String workflowId) {
        return instances.values().stream()
                .filter(i -> i.getWorkflowId().equals(workflowId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkflowInstance> findInstancesByStatus(WorkflowStatus status) {
        return instances.values().stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkflowInstance> findAllInstances() {
        return new ArrayList<>(instances.values());
    }
    
    @Override
    public void deleteInstance(String instanceId) {
        instances.remove(instanceId);
    }
}
