package ltd.idcu.est.scheduler.cron;

import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.api.TaskState;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class CronTask implements Task {
    
    private final String id;
    private final String name;
    private final String description;
    private final Runnable runnable;
    private final CronExpression cronExpression;
    private final Instant createdAt;
    private final AtomicInteger executionCount;
    private final AtomicReference<TaskState> state;
    private final AtomicReference<Instant> lastExecutedAt;
    private final AtomicReference<Throwable> lastError;
    private final AtomicLong lastExecutionTime;
    
    public CronTask(Runnable runnable, String cronExpression) {
        this(runnable, cronExpression, null, null);
    }
    
    public CronTask(Runnable runnable, String cronExpression, String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name != null ? name : "cron-task-" + id.substring(0, 8);
        this.description = description != null ? description : "Cron scheduled task";
        this.runnable = runnable;
        this.cronExpression = new CronExpression(cronExpression);
        this.createdAt = Instant.now();
        this.executionCount = new AtomicInteger(0);
        this.state = new AtomicReference<>(TaskState.SCHEDULED);
        this.lastExecutedAt = new AtomicReference<>(null);
        this.lastError = new AtomicReference<>(null);
        this.lastExecutionTime = new AtomicLong(0);
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public void execute() {
        state.set(TaskState.RUNNING);
        long startTime = System.currentTimeMillis();
        try {
            runnable.run();
            lastExecutionTime.set(System.currentTimeMillis() - startTime);
            executionCount.incrementAndGet();
            lastExecutedAt.set(Instant.now());
            lastError.set(null);
            state.set(TaskState.SCHEDULED);
        } catch (Throwable e) {
            lastError.set(e);
            state.set(TaskState.FAILED);
            throw e;
        }
    }
    
    @Override
    public TaskState getState() {
        return state.get();
    }
    
    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public Instant getLastExecutedAt() {
        return lastExecutedAt.get();
    }
    
    @Override
    public long getExecutionCount() {
        return executionCount.get();
    }
    
    @Override
    public Throwable getLastError() {
        return lastError.get();
    }
    
    public CronExpression getCronExpression() {
        return cronExpression;
    }
    
    public long getLastExecutionTime() {
        return lastExecutionTime.get();
    }
    
    public Instant getNextExecutionTime() {
        return cronExpression.getNextExecutionTime(Instant.now());
    }
    
    public Instant getNextExecutionTime(Instant afterTime) {
        return cronExpression.getNextExecutionTime(afterTime);
    }
    
    void setState(TaskState newState) {
        state.set(newState);
    }
    
    void incrementExecutionCount() {
        executionCount.incrementAndGet();
    }
    
    void setLastExecutedAt(Instant time) {
        lastExecutedAt.set(time);
    }
    
    void setLastError(Throwable error) {
        lastError.set(error);
    }
}
