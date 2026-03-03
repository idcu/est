package ltd.idcu.est.features.scheduler.api;

public class SchedulerStats {
    
    private final int totalTasks;
    private final int scheduledTasks;
    private final int runningTasks;
    private final int completedTasks;
    private final int cancelledTasks;
    private final int failedTasks;
    private final long totalExecutions;
    private final long successfulExecutions;
    private final long failedExecutions;
    private final long averageExecutionTime;
    
    public SchedulerStats(int totalTasks, int scheduledTasks, int runningTasks,
                          int completedTasks, int cancelledTasks, int failedTasks,
                          long totalExecutions, long successfulExecutions, long failedExecutions,
                          long averageExecutionTime) {
        this.totalTasks = totalTasks;
        this.scheduledTasks = scheduledTasks;
        this.runningTasks = runningTasks;
        this.completedTasks = completedTasks;
        this.cancelledTasks = cancelledTasks;
        this.failedTasks = failedTasks;
        this.totalExecutions = totalExecutions;
        this.successfulExecutions = successfulExecutions;
        this.failedExecutions = failedExecutions;
        this.averageExecutionTime = averageExecutionTime;
    }
    
    public int getTotalTasks() {
        return totalTasks;
    }
    
    public int getScheduledTasks() {
        return scheduledTasks;
    }
    
    public int getRunningTasks() {
        return runningTasks;
    }
    
    public int getCompletedTasks() {
        return completedTasks;
    }
    
    public int getCancelledTasks() {
        return cancelledTasks;
    }
    
    public int getFailedTasks() {
        return failedTasks;
    }
    
    public long getTotalExecutions() {
        return totalExecutions;
    }
    
    public long getSuccessfulExecutions() {
        return successfulExecutions;
    }
    
    public long getFailedExecutions() {
        return failedExecutions;
    }
    
    public long getAverageExecutionTime() {
        return averageExecutionTime;
    }
    
    public double getSuccessRate() {
        if (totalExecutions == 0) {
            return 0.0;
        }
        return (double) successfulExecutions / totalExecutions * 100;
    }
    
    @Override
    public String toString() {
        return "SchedulerStats{" +
                "totalTasks=" + totalTasks +
                ", scheduledTasks=" + scheduledTasks +
                ", runningTasks=" + runningTasks +
                ", completedTasks=" + completedTasks +
                ", cancelledTasks=" + cancelledTasks +
                ", failedTasks=" + failedTasks +
                ", totalExecutions=" + totalExecutions +
                ", successfulExecutions=" + successfulExecutions +
                ", failedExecutions=" + failedExecutions +
                ", averageExecutionTime=" + averageExecutionTime + "ms" +
                ", successRate=" + String.format("%.2f", getSuccessRate()) + "%" +
                '}';
    }
}
