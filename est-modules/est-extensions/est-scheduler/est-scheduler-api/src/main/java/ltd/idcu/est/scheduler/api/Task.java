package ltd.idcu.est.scheduler.api;

import java.time.Instant;

public interface Task {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    void execute();
    
    TaskState getState();
    
    Instant getCreatedAt();
    
    Instant getLastExecutedAt();
    
    long getExecutionCount();
    
    Throwable getLastError();
    
    default Runnable asRunnable() {
        return this::execute;
    }
}
