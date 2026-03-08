package ltd.idcu.est.workflow.api;

public class WorkflowException extends RuntimeException {
    
    public WorkflowException(String message) {
        super(message);
    }
    
    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}
