package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.Edge;
import ltd.idcu.est.workflow.api.WorkflowContext;

import java.util.function.Predicate;

public class DefaultEdge implements Edge {
    
    private final String id;
    private final String sourceNodeId;
    private final String targetNodeId;
    private final String label;
    private final Predicate<WorkflowContext> condition;
    
    public DefaultEdge(String id, String sourceNodeId, String targetNodeId) {
        this(id, sourceNodeId, targetNodeId, null, null);
    }
    
    public DefaultEdge(String id, String sourceNodeId, String targetNodeId, String label) {
        this(id, sourceNodeId, targetNodeId, label, null);
    }
    
    public DefaultEdge(String id, String sourceNodeId, String targetNodeId, String label, Predicate<WorkflowContext> condition) {
        this.id = id;
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.label = label;
        this.condition = condition != null ? condition : ctx -> true;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getSourceNodeId() {
        return sourceNodeId;
    }
    
    @Override
    public String getTargetNodeId() {
        return targetNodeId;
    }
    
    @Override
    public String getLabel() {
        return label;
    }
    
    @Override
    public Predicate<WorkflowContext> getCondition() {
        return condition;
    }
    
    @Override
    public boolean evaluate(WorkflowContext context) {
        return condition.test(context);
    }
}
