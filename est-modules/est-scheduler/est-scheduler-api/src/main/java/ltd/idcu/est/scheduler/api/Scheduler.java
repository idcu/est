package ltd.idcu.est.scheduler.api;

public interface Scheduler {
    
    String schedule(Task task, ScheduleConfig config);
    
    String scheduleAtFixedRate(Task task, long initialDelay, long period, java.util.concurrent.TimeUnit unit);
    
    String scheduleWithFixedDelay(Task task, long initialDelay, long delay, java.util.concurrent.TimeUnit unit);
    
    String scheduleCron(Task task, String cronExpression);
    
    boolean cancel(String taskId);
    
    boolean hasTask(String taskId);
    
    Task getTask(String taskId);
    
    java.util.Set<String> getTaskIds();
    
    int getTaskCount();
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    SchedulerStats getStats();
}
