package ltd.idcu.est.features.scheduler.cron;

import ltd.idcu.est.features.scheduler.api.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CronScheduler implements Scheduler {
    
    private final ScheduledExecutorService executor;
    private final Map<String, ScheduledTask> tasks;
    private final List<TaskListener> listeners;
    private final AtomicBoolean running;
    private final AtomicInteger scheduledCount;
    private final AtomicInteger completedCount;
    private final AtomicInteger cancelledCount;
    private final AtomicInteger failedCount;
    private final AtomicLong totalExecutions;
    private final AtomicLong successfulExecutions;
    private final AtomicLong failedExecutions;
    private final AtomicLong totalExecutionTime;
    
    public CronScheduler() {
        this(Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setName("cron-scheduler-" + t.getId());
                    return t;
                }));
    }
    
    public CronScheduler(ScheduledExecutorService executor) {
        this.executor = executor;
        this.tasks = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.running = new AtomicBoolean(false);
        this.scheduledCount = new AtomicInteger(0);
        this.completedCount = new AtomicInteger(0);
        this.cancelledCount = new AtomicInteger(0);
        this.failedCount = new AtomicInteger(0);
        this.totalExecutions = new AtomicLong(0);
        this.successfulExecutions = new AtomicLong(0);
        this.failedExecutions = new AtomicLong(0);
        this.totalExecutionTime = new AtomicLong(0);
    }
    
    @Override
    public String schedule(Task task, ScheduleConfig config) {
        if (config.getType() == ScheduleType.CRON) {
            return scheduleCron(task, config.getCronExpression());
        } else if (config.getType() == ScheduleType.FIXED_RATE) {
            return scheduleAtFixedRate(task, config.getInitialDelay(), config.getPeriod(), config.getTimeUnit());
        } else if (config.getType() == ScheduleType.FIXED_DELAY) {
            return scheduleWithFixedDelay(task, config.getInitialDelay(), config.getPeriod(), config.getTimeUnit());
        } else {
            return scheduleOneTime(task, config.getInitialDelay(), config.getTimeUnit());
        }
    }
    
    private String scheduleOneTime(Task task, long delay, TimeUnit unit) {
        String taskId = task.getId();
        ScheduledFuture<?> future = executor.schedule(() -> executeTask(task), delay, unit);
        
        ScheduledTask scheduledTask = new ScheduledTask(task, future, ScheduleType.ONE_TIME);
        tasks.put(taskId, scheduledTask);
        scheduledCount.incrementAndGet();
        
        notifyScheduled(taskId, ScheduleConfig.builder()
                .type(ScheduleType.ONE_TIME)
                .initialDelay(delay)
                .timeUnit(unit)
                .build());
        
        return taskId;
    }
    
    @Override
    public String scheduleAtFixedRate(Task task, long initialDelay, long period, TimeUnit unit) {
        String taskId = task.getId();
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(
                () -> executeTask(task),
                initialDelay, period, unit);
        
        ScheduledTask scheduledTask = new ScheduledTask(task, future, ScheduleType.FIXED_RATE);
        tasks.put(taskId, scheduledTask);
        scheduledCount.incrementAndGet();
        
        notifyScheduled(taskId, ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(initialDelay)
                .period(period)
                .timeUnit(unit)
                .fixedRate(true)
                .build());
        
        return taskId;
    }
    
    @Override
    public String scheduleWithFixedDelay(Task task, long initialDelay, long delay, TimeUnit unit) {
        String taskId = task.getId();
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(
                () -> executeTask(task),
                initialDelay, delay, unit);
        
        ScheduledTask scheduledTask = new ScheduledTask(task, future, ScheduleType.FIXED_DELAY);
        tasks.put(taskId, scheduledTask);
        scheduledCount.incrementAndGet();
        
        notifyScheduled(taskId, ScheduleConfig.builder()
                .type(ScheduleType.FIXED_DELAY)
                .initialDelay(initialDelay)
                .period(delay)
                .timeUnit(unit)
                .fixedRate(false)
                .build());
        
        return taskId;
    }
    
    @Override
    public String scheduleCron(Task task, String cronExpression) {
        CronExpression cron = new CronExpression(cronExpression);
        String taskId = task.getId();
        
        CronRunnable cronRunnable = new CronRunnable(task, cron);
        ScheduledFuture<?> future = executor.schedule(cronRunnable, 0, TimeUnit.MILLISECONDS);
        
        ScheduledTask scheduledTask = new ScheduledTask(task, future, ScheduleType.CRON, cron);
        tasks.put(taskId, scheduledTask);
        scheduledCount.incrementAndGet();
        
        notifyScheduled(taskId, ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression(cronExpression)
                .build());
        
        return taskId;
    }
    
    private void executeTask(Task task) {
        String taskId = task.getId();
        long startTime = System.currentTimeMillis();
        
        notifyBeforeExecute(taskId, task);
        
        try {
            task.execute();
            long executionTime = System.currentTimeMillis() - startTime;
            
            totalExecutions.incrementAndGet();
            successfulExecutions.incrementAndGet();
            totalExecutionTime.addAndGet(executionTime);
            
            notifyAfterExecute(taskId, task, executionTime);
            notifySuccess(taskId, task, executionTime);
        } catch (Throwable e) {
            failedExecutions.incrementAndGet();
            totalExecutions.incrementAndGet();
            failedCount.incrementAndGet();
            
            notifyFailure(taskId, task, e);
            throw e;
        }
    }
    
    @Override
    public boolean cancel(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        if (scheduledTask == null) {
            return false;
        }
        
        boolean cancelled = scheduledTask.future().cancel(false);
        if (cancelled) {
            cancelledCount.incrementAndGet();
            notifyCancelled(taskId);
        }
        
        return cancelled;
    }
    
    @Override
    public boolean hasTask(String taskId) {
        return tasks.containsKey(taskId);
    }
    
    @Override
    public Task getTask(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        return scheduledTask != null ? scheduledTask.task() : null;
    }
    
    @Override
    public Set<String> getTaskIds() {
        return new HashSet<>(tasks.keySet());
    }
    
    @Override
    public int getTaskCount() {
        return tasks.size();
    }
    
    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            
        }
    }
    
    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            for (ScheduledTask task : tasks.values()) {
                task.future().cancel(false);
            }
            tasks.clear();
            executor.shutdown();
        }
    }
    
    @Override
    public boolean isRunning() {
        return running.get();
    }
    
    @Override
    public SchedulerStats getStats() {
        int scheduled = 0, running = 0, completed = 0, cancelled = 0, failed = 0;
        
        for (ScheduledTask task : tasks.values()) {
            switch (task.task().getState()) {
                case SCHEDULED -> scheduled++;
                case RUNNING -> running++;
                case COMPLETED -> completed++;
                case CANCELLED -> cancelled++;
                case FAILED -> failed++;
            }
        }
        
        long totalExec = totalExecutions.get();
        long avgExecTime = totalExec > 0 ? totalExecutionTime.get() / totalExec : 0;
        
        return new SchedulerStats(
                tasks.size(),
                scheduled,
                running,
                completed,
                cancelled,
                failed,
                totalExec,
                successfulExecutions.get(),
                failedExecutions.get(),
                avgExecTime
        );
    }
    
    public void addListener(TaskListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(TaskListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyScheduled(String taskId, ScheduleConfig config) {
        for (TaskListener listener : listeners) {
            try {
                listener.onScheduled(taskId, config);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyBeforeExecute(String taskId, Task task) {
        for (TaskListener listener : listeners) {
            try {
                listener.onBeforeExecute(taskId, task);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyAfterExecute(String taskId, Task task, long executionTime) {
        for (TaskListener listener : listeners) {
            try {
                listener.onAfterExecute(taskId, task, executionTime);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifySuccess(String taskId, Task task, long executionTime) {
        for (TaskListener listener : listeners) {
            try {
                listener.onSuccess(taskId, task, executionTime);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyFailure(String taskId, Task task, Throwable error) {
        for (TaskListener listener : listeners) {
            try {
                listener.onFailure(taskId, task, error);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyCancelled(String taskId) {
        for (TaskListener listener : listeners) {
            try {
                listener.onCancelled(taskId);
            } catch (Exception ignored) {
            }
        }
    }
    
    private record ScheduledTask(Task task, ScheduledFuture<?> future, ScheduleType type, CronExpression cron) {
        ScheduledTask(Task task, ScheduledFuture<?> future, ScheduleType type) {
            this(task, future, type, null);
        }
    }
    
    private class CronRunnable implements Runnable {
        private final Task task;
        private final CronExpression cron;
        
        CronRunnable(Task task, CronExpression cron) {
            this.task = task;
            this.cron = cron;
        }
        
        @Override
        public void run() {
            Instant now = Instant.now();
            Instant nextExecution = cron.getNextExecutionTime(now);
            
            if (nextExecution != null) {
                long delay = nextExecution.toEpochMilli() - now.toEpochMilli();
                if (delay <= 0) {
                    executeTask(task);
                }
                
                ScheduledFuture<?> future = executor.schedule(this, Math.max(delay, 0), TimeUnit.MILLISECONDS);
                ScheduledTask scheduledTask = tasks.get(task.getId());
                if (scheduledTask != null) {
                    tasks.put(task.getId(), new ScheduledTask(task, future, ScheduleType.CRON, cron));
                }
            }
        }
    }
}
