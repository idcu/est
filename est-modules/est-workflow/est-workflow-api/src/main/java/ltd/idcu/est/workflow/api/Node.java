package ltd.idcu.est.workflow.api;

public interface Node {
    
    String getId();
    
    String getName();
    
    String getType();
    
    void execute(WorkflowContext context) throws WorkflowException;
}
