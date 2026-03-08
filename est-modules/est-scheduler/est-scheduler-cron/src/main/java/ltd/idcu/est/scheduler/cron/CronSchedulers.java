package ltd.idcu.est.scheduler.cron;

import ltd.idcu.est.scheduler.api.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class CronSchedulers {
    
    private CronSchedulers() {
    }
    
    public static CronScheduler create() {
        return new CronScheduler();
    }
    
    public static CronScheduler create(int poolSize) {
        return new CronScheduler(java.util.concurrent.Executors.newScheduledThreadPool(poolSize));
    }
    
    public static CronTask task(Runnable runnable, String cronExpression) {
        return new CronTask(runnable, cronExpression);
    }
    
    public static CronTask task(Runnable runnable, String cronExpression, String name) {
        return new CronTask(runnable, cronExpression, name, null);
    }
    
    public static CronTask task(Runnable runnable, String cronExpression, String name, String description) {
        return new CronTask(runnable, cronExpression, name, description);
    }
    
    public static Task wrap(Runnable runnable) {
        return new SimpleTask(runnable);
    }
    
    public static Task wrap(Runnable runnable, String name) {
        return new SimpleTask(runnable, name);
    }
    
    public static boolean isValidCronExpression(String expression) {
        return CronExpression.isValid(expression);
    }
    
    public static CronExpression parseCron(String expression) {
        return new CronExpression(expression);
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
