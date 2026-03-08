package ltd.idcu.est.workflow.core;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.workflow.api.Edge;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowDefinitionParser;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowRepository;
import ltd.idcu.est.workflow.api.builder.WorkflowBuilder;
import ltd.idcu.est.workflow.core.integration.EventDrivenWorkflowTrigger;
import ltd.idcu.est.workflow.core.integration.ScheduledWorkflowTrigger;
import ltd.idcu.est.workflow.core.node.ExclusiveGateway;
import ltd.idcu.est.workflow.core.node.InclusiveGateway;
import ltd.idcu.est.workflow.core.node.ParallelGateway;
import ltd.idcu.est.workflow.core.node.TaskNode;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Workflows {
    
    private Workflows() {
    }
    
    public static WorkflowEngine newWorkflowEngine() {
        return new DefaultWorkflowEngine();
    }
    
    public static WorkflowEngine newWorkflowEngine(WorkflowRepository repository) {
        WorkflowEngine engine = new DefaultWorkflowEngine();
        engine.setRepository(repository);
        return engine;
    }
    
    public static WorkflowRepository newMemoryRepository() {
        return new MemoryWorkflowRepository();
    }
    
    public static WorkflowDefinitionParser newJsonParser() {
        return new JsonWorkflowDefinitionParser();
    }
    
    public static WorkflowBuilder newWorkflowBuilder() {
        return DefaultWorkflowEngine.newWorkflowBuilder();
    }
    
    public static TaskNode newTaskNode(String id, String name, Consumer<WorkflowContext> task) {
        return new TaskNode(id, name, task);
    }
    
    public static ExclusiveGateway newExclusiveGateway(String id, String name) {
        return new ExclusiveGateway(id, name);
    }
    
    public static ParallelGateway newParallelGateway(String id, String name) {
        return new ParallelGateway(id, name);
    }
    
    public static InclusiveGateway newInclusiveGateway(String id, String name) {
        return new InclusiveGateway(id, name);
    }
    
    public static Edge newEdge(String id, String sourceNodeId, String targetNodeId) {
        return new DefaultEdge(id, sourceNodeId, targetNodeId);
    }
    
    public static Edge newEdge(String id, String sourceNodeId, String targetNodeId, String label) {
        return new DefaultEdge(id, sourceNodeId, targetNodeId, label);
    }
    
    public static Edge newEdge(String id, String sourceNodeId, String targetNodeId, String label, Predicate<WorkflowContext> condition) {
        return new DefaultEdge(id, sourceNodeId, targetNodeId, label, condition);
    }
    
    public static ScheduledWorkflowTrigger newScheduledTrigger(WorkflowEngine workflowEngine, Scheduler scheduler) {
        return new ScheduledWorkflowTrigger(workflowEngine, scheduler);
    }
    
    public static EventDrivenWorkflowTrigger newEventDrivenTrigger(WorkflowEngine workflowEngine, EventBus eventBus) {
        return new EventDrivenWorkflowTrigger(workflowEngine, eventBus);
    }
}
