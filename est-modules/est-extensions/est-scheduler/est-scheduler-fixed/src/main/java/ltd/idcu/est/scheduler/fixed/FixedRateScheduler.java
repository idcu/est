package ltd.idcu.est.scheduler.fixed;

import ltd.idcu.est.scheduler.api.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FixedRateScheduler implements Scheduler {
    
    private final ScheduledExecutorService executor;
    private final Map<String, ScheduledTaskWrapper> tasks;
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
    
    public FixedRateScheduler() {
        this(Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setName("fixed-rate-scheduler-" + t.threadId());
                    return t;
                }));
    }
    
    public FixedRateScheduler(ScheduledExecutorService executor) {
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
        if (config.getType() == ScheduleType.FIXED_RATE) {
            return scheduleAtFixedRate(task, config.getInitialDelay(), config.getPeriod(), config.getTimeUnit());
        } else if (config.getType() == ScheduleType.FIXED_DELAY) {
            return scheduleWithFixedDelay(task, config.getInitialDelay(), config.getPeriod(), config.getTimeUnit());
        } else if (config.getType() == ScheduleType.ONE_TIME) {
            return scheduleOneTime(task, config.getInitialDelay(), config.getTimeUnit());
        } else {
            throw new SchedulerException(task.getId(), 
                    SchedulerException.ErrorCode.INVALID_SCHEDULE_CONFIG,
                    "FixedRateScheduler only supports ONE_TIME, FIXED_RATE, and FIXED_DELAY schedule types");
        }
    }
    
    private String scheduleOneTime(Task task, long delay, TimeUnit unit) {
        String taskId = task.getId();
        ScheduledFuture<?> future = executor.schedule(() -> executeTask(task), delay, unit);
        
        ScheduledTaskWrapper wrapper = new ScheduledTaskWrapper(task, future, ScheduleType.ONE_TIME);
        tasks.put(taskId, wrapper);
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
        Instant nextTime = Instant.now().plusMillis(unit.toMillis(initialDelay));
        
        if (task instanceof FixedRateTask fixedTask) {
            fixedTask.setNextExecutionTime(nextTime);
        }
        
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(
                () -> executeTaskWithMaxCheck(task),
                initialDelay, period, unit);
        
        ScheduledTaskWrapper wrapper = new ScheduledTaskWrapper(task, future, ScheduleType.FIXED_RATE);
        tasks.put(taskId, wrapper);
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
        Instant nextTime = Instant.now().plusMillis(unit.toMillis(initialDelay));
        
        if (task instanceof FixedRateTask fixedTask) {
            fixedTask.setNextExecutionTime(nextTime);
        }
        
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(
                () -> executeTaskWithMaxCheck(task),
                initialDelay, delay, unit);
        
        ScheduledTaskWrapper wrapper = new ScheduledTaskWrapper(task, future, ScheduleType.FIXED_DELAY);
        tasks.put(taskId, wrapper);
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
        throw new SchedulerException(task.getId(),
                SchedulerException.ErrorCode.INVALID_SCHEDULE_CONFIG,
                "FixedRateScheduler does not support CRON expressions. Use CronScheduler instead.");
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
    
    private void executeTaskWithMaxCheck(Task task) {
        String taskId = task.getId();
        
        if (task instanceof FixedRateTask fixedTask) {
            if (fixedTask.hasReachedMaxExecutions()) {
                cancel(taskId);
                notifyMaxExecutionsReached(taskId, fixedTask.getMaxExecutions());
                return;
            }
        }
        
        executeTask(task);
        
        if (task instanceof FixedRateTask fixedTask) {
            if (fixedTask.hasReachedMaxExecutions()) {
                cancel(taskId);
                notifyMaxExecutionsReached(taskId, fixedTask.getMaxExecutions());
            }
        }
    }
    
    @Override
    public boolean cancel(String taskId) {
        ScheduledTaskWrapper wrapper = tasks.get(taskId);
        if (wrapper == null) {
            return false;
        }
        
        boolean cancelled = wrapper.future().cancel(false);
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
        ScheduledTaskWrapper wrapper = tasks.get(taskId);
        return wrapper != null ? wrapper.task() : null;
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
            for (ScheduledTaskWrapper wrapper : tasks.values()) {
                wrapper.future().cancel(false);
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
        
        for (ScheduledTaskWrapper wrapper : tasks.values()) {
            switch (wrapper.task().getState()) {
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
    
    private void notifyMaxExecutionsReached(String taskId, int maxExecutions) {
        for (TaskListener listener : listeners) {
            try {
                listener.onMaxExecutionsReached(taskId, maxExecutions);
            } catch (Exception ignored) {
            }
        }
    }
    
    private record ScheduledTaskWrapper(Task task, ScheduledFuture<?> future, ScheduleType type) {
    }
}
