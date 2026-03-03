package ltd.idcu.est.features.scheduler.api;

public class SchedulerException extends RuntimeException {
    
    private final String taskId;
    private final ErrorCode errorCode;
    
    public SchedulerException(String message) {
        super(message);
        this.taskId = null;
        this.errorCode = ErrorCode.UNKNOWN;
    }
    
    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
        this.taskId = null;
        this.errorCode = ErrorCode.UNKNOWN;
    }
    
    public SchedulerException(String taskId, String message) {
        super(message);
        this.taskId = taskId;
        this.errorCode = ErrorCode.UNKNOWN;
    }
    
    public SchedulerException(String taskId, ErrorCode errorCode, String message) {
        super(message);
        this.taskId = taskId;
        this.errorCode = errorCode;
    }
    
    public SchedulerException(String taskId, ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.taskId = taskId;
        this.errorCode = errorCode;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public enum ErrorCode {
        TASK_NOT_FOUND,
        TASK_ALREADY_EXISTS,
        INVALID_CRON_EXPRESSION,
        SCHEDULER_NOT_RUNNING,
        SCHEDULER_ALREADY_RUNNING,
        TASK_EXECUTION_FAILED,
        TASK_CANCELLATION_FAILED,
        INVALID_SCHEDULE_CONFIG,
        MAX_EXECUTIONS_REACHED
    }
}
