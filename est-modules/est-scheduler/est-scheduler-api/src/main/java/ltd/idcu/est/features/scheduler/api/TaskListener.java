package ltd.idcu.est.features.scheduler.api;

import java.time.Instant;

public interface TaskListener {
    
    default void onScheduled(String taskId, ScheduleConfig config) {}
    
    default void onBeforeExecute(String taskId, Task task) {}
    
    default void onAfterExecute(String taskId, Task task, long executionTime) {}
    
    default void onSuccess(String taskId, Task task, long executionTime) {}
    
    default void onFailure(String taskId, Task task, Throwable error) {}
    
    default void onCancelled(String taskId) {}
    
    default void onCompleted(String taskId) {}
    
    default void onMaxExecutionsReached(String taskId, int maxExecutions) {}
    
    default void onEndTimeReached(String taskId, Instant endTime) {}
}
