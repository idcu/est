package ltd.idcu.est.scheduler.api;

import java.time.Instant;

public class ScheduleResult {
    
    private final String taskId;
    private final boolean success;
    private final Instant scheduledTime;
    private final Instant firstExecutionTime;
    private final String errorMessage;
    
    private ScheduleResult(Builder builder) {
        this.taskId = builder.taskId;
        this.success = builder.success;
        this.scheduledTime = builder.scheduledTime;
        this.firstExecutionTime = builder.firstExecutionTime;
        this.errorMessage = builder.errorMessage;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Instant getScheduledTime() {
        return scheduledTime;
    }
    
    public Instant getFirstExecutionTime() {
        return firstExecutionTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String taskId;
        private boolean success = true;
        private Instant scheduledTime;
        private Instant firstExecutionTime;
        private String errorMessage;
        
        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }
        
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }
        
        public Builder scheduledTime(Instant scheduledTime) {
            this.scheduledTime = scheduledTime;
            return this;
        }
        
        public Builder firstExecutionTime(Instant firstExecutionTime) {
            this.firstExecutionTime = firstExecutionTime;
            return this;
        }
        
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            this.success = false;
            return this;
        }
        
        public ScheduleResult build() {
            return new ScheduleResult(this);
        }
    }
}
