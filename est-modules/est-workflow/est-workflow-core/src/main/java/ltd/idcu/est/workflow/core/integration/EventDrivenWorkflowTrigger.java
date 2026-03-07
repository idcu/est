package ltd.idcu.est.workflow.core.integration;

import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowException;

import java.util.HashMap;
import java.util.Map;

public class EventDrivenWorkflowTrigger {
    
    private final WorkflowEngine workflowEngine;
    private final EventBus eventBus;
    private final Map<String, String> eventWorkflowMappings;
    
    public EventDrivenWorkflowTrigger(WorkflowEngine workflowEngine, EventBus eventBus) {
        this.workflowEngine = workflowEngine;
        this.eventBus = eventBus;
        this.eventWorkflowMappings = new HashMap<>();
    }
    
    public void registerEventTrigger(String eventType, String workflowId) {
        eventWorkflowMappings.put(eventType, workflowId);
        eventBus.subscribe(eventType, (EventListener<Object>) (event, data) -> {
            try {
                WorkflowContext context = createContext(workflowId, data);
                workflowEngine.startWorkflow(workflowId, context);
            } catch (Exception e) {
                throw new WorkflowException("Failed to start event-driven workflow: " + workflowId, e);
            }
        });
    }
    
    public void registerEventTrigger(String eventType, String workflowId, 
                                     EventPayloadExtractor payloadExtractor) {
        eventWorkflowMappings.put(eventType, workflowId);
        eventBus.subscribe(eventType, (EventListener<Object>) (event, data) -> {
            try {
                Map<String, Object> variables = payloadExtractor.extract(data);
                WorkflowContext context = createContext(workflowId, variables);
                workflowEngine.startWorkflow(workflowId, context);
            } catch (Exception e) {
                throw new WorkflowException("Failed to start event-driven workflow: " + workflowId, e);
            }
        });
    }
    
    public void unregisterEventTrigger(String eventType) {
        String workflowId = eventWorkflowMappings.remove(eventType);
        if (workflowId != null) {
            eventBus.unsubscribeAll(eventType);
        }
    }
    
    public void unregisterAll() {
        for (String eventType : eventWorkflowMappings.keySet()) {
            eventBus.unsubscribeAll(eventType);
        }
        eventWorkflowMappings.clear();
    }
    
    private WorkflowContext createContext(String workflowId, Object event) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("event", event);
        return createContext(workflowId, variables);
    }
    
    private WorkflowContext createContext(String workflowId, Map<String, Object> variables) {
        WorkflowContext context = new ltd.idcu.est.workflow.core.DefaultWorkflowContext(
                "event-driven-" + System.currentTimeMillis());
        if (variables != null) {
            context.setVariables(variables);
        }
        return context;
    }
    
    @FunctionalInterface
    public interface EventPayloadExtractor {
        Map<String, Object> extract(Object event);
    }
}
