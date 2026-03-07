package ltd.idcu.est.workflow.api;

import java.util.function.Predicate;

public interface Edge {
    
    String getId();
    
    String getSourceNodeId();
    
    String getTargetNodeId();
    
    String getLabel();
    
    Predicate<WorkflowContext> getCondition();
    
    boolean evaluate(WorkflowContext context);
}
