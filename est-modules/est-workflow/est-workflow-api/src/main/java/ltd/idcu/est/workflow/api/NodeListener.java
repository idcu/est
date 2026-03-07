package ltd.idcu.est.workflow.api;

public interface NodeListener {
    
    void onNodeStart(WorkflowInstance instance, Node node);
    
    void onNodeComplete(WorkflowInstance instance, Node node);
    
    void onNodeFail(WorkflowInstance instance, Node node, Throwable error);
    
    void onNodeSkip(WorkflowInstance instance, Node node);
}
