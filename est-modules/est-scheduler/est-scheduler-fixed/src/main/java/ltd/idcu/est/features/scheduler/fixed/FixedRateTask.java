package ltd.idcu.est.features.scheduler.fixed;

import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.api.TaskState;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FixedRateTask implements Task {
    
    private final String id;
    private final String name;
    private final String description;
    private final Runnable runnable;
    private final long initialDelay;
    private final long period;
    private final TimeUnit timeUnit;
    private final boolean fixedRate;
    private final int maxExecutions;
    private final Instant createdAt;
    private final AtomicInteger executionCount;
    private final AtomicReference<TaskState> state;
    private final AtomicReference<Instant> lastExecutedAt;
    private final AtomicReference<Throwable> lastError;
    private final AtomicLong lastExecutionTime;
    private final AtomicReference<Instant> nextExecutionTime;
    
    public FixedRateTask(Runnable runnable, long period, TimeUnit unit) {
        this(runnable, 0, period, unit, true);
    }
    
    public FixedRateTask(Runnable runnable, long initialDelay, long period, TimeUnit unit, boolean fixedRate) {
        this(runnable, initialDelay, period, unit, fixedRate, -1, null, null);
    }
    
    public FixedRateTask(Runnable runnable, long initialDelay, long period, TimeUnit unit,
                         boolean fixedRate, int maxExecutions, String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name != null ? name : "fixed-task-" + id.substring(0, 8);
        this.description = description != null ? description : (fixedRate ? "Fixed rate scheduled task" : "Fixed delay scheduled task");
        this.runnable = runnable;
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = unit;
        this.fixedRate = fixedRate;
        this.maxExecutions = maxExecutions;
        this.createdAt = Instant.now();
        this.executionCount = new AtomicInteger(0);
        this.state = new AtomicReference<>(TaskState.SCHEDULED);
        this.lastExecutedAt = new AtomicReference<>(null);
        this.lastError = new AtomicReference<>(null);
        this.lastExecutionTime = new AtomicLong(0);
        this.nextExecutionTime = new AtomicReference<>(null);
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
            
            if (maxExecutions > 0 && executionCount.get() >= maxExecutions) {
                state.set(TaskState.COMPLETED);
            } else {
                state.set(TaskState.SCHEDULED);
            }
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
    
    public long getInitialDelay() {
        return initialDelay;
    }
    
    public long getPeriod() {
        return period;
    }
    
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    
    public boolean isFixedRate() {
        return fixedRate;
    }
    
    public int getMaxExecutions() {
        return maxExecutions;
    }
    
    public long getLastExecutionTime() {
        return lastExecutionTime.get();
    }
    
    public Instant getNextExecutionTime() {
        return nextExecutionTime.get();
    }
    
    public boolean hasReachedMaxExecutions() {
        return maxExecutions > 0 && executionCount.get() >= maxExecutions;
    }
    
    void setState(TaskState newState) {
        state.set(newState);
    }
    
    void setNextExecutionTime(Instant time) {
        nextExecutionTime.set(time);
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
