package ltd.idcu.est.features.scheduler.fixed;

import ltd.idcu.est.features.scheduler.api.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class FixedRateSchedulers {
    
    private FixedRateSchedulers() {
    }
    
    public static FixedRateScheduler create() {
        return new FixedRateScheduler();
    }
    
    public static FixedRateScheduler create(int poolSize) {
        return new FixedRateScheduler(java.util.concurrent.Executors.newScheduledThreadPool(poolSize));
    }
    
    public static FixedRateTask task(Runnable runnable, long period, TimeUnit unit) {
        return new FixedRateTask(runnable, period, unit);
    }
    
    public static FixedRateTask task(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        return new FixedRateTask(runnable, initialDelay, period, unit, true);
    }
    
    public static FixedRateTask fixedRateTask(Runnable runnable, long period, TimeUnit unit) {
        return new FixedRateTask(runnable, 0, period, unit, true);
    }
    
    public static FixedRateTask fixedRateTask(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        return new FixedRateTask(runnable, initialDelay, period, unit, true);
    }
    
    public static FixedRateTask fixedDelayTask(Runnable runnable, long delay, TimeUnit unit) {
        return new FixedRateTask(runnable, 0, delay, unit, false);
    }
    
    public static FixedRateTask fixedDelayTask(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
        return new FixedRateTask(runnable, initialDelay, delay, unit, false);
    }
    
    public static FixedRateTask task(Runnable runnable, long initialDelay, long period, TimeUnit unit,
                                      boolean fixedRate, int maxExecutions, String name, String description) {
        return new FixedRateTask(runnable, initialDelay, period, unit, fixedRate, maxExecutions, name, description);
    }
    
    public static Task wrap(Runnable runnable) {
        return new SimpleTask(runnable);
    }
    
    public static Task wrap(Runnable runnable, String name) {
        return new SimpleTask(runnable, name);
    }
    
    private static class SimpleTask implements Task {
        private final String id;
        private final String name;
        private final String description;
        private final Runnable runnable;
        private final java.time.Instant createdAt;
        private volatile TaskState state = TaskState.SCHEDULED;
        private volatile java.time.Instant lastExecutedAt;
        private volatile long executionCount = 0;
        private volatile Throwable lastError;
        
        SimpleTask(Runnable runnable) {
            this(runnable, "simple-task");
        }
        
        SimpleTask(Runnable runnable, String name) {
            this.id = UUID.randomUUID().toString();
            this.name = name;
            this.description = "Simple task wrapper";
            this.runnable = runnable;
            this.createdAt = java.time.Instant.now();
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
            state = TaskState.RUNNING;
            try {
                runnable.run();
                executionCount++;
                lastExecutedAt = java.time.Instant.now();
                lastError = null;
                state = TaskState.SCHEDULED;
            } catch (Throwable e) {
                lastError = e;
                state = TaskState.FAILED;
                throw e;
            }
        }
        
        @Override
        public TaskState getState() {
            return state;
        }
        
        @Override
        public java.time.Instant getCreatedAt() {
            return createdAt;
        }
        
        @Override
        public java.time.Instant getLastExecutedAt() {
            return lastExecutedAt;
        }
        
        @Override
        public long getExecutionCount() {
            return executionCount;
        }
        
        @Override
        public Throwable getLastError() {
            return lastError;
        }
        
        void setState(TaskState newState) {
            this.state = newState;
        }
    }
}
