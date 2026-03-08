package ltd.idcu.est.workflow.api;

public interface WorkflowListener {
    
    void onWorkflowStart(WorkflowInstance instance);
    
    void onWorkflowComplete(WorkflowInstance instance);
    
    void onWorkflowFail(WorkflowInstance instance, Throwable error);
    
    void onWorkflowPause(WorkflowInstance instance);
    
    void onWorkflowResume(WorkflowInstance instance);
    
    void onWorkflowCancel(WorkflowInstance instance);
}
