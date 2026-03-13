package ltd.idcu.est.examples.features;

import ltd.idcu.est.scheduler.api.ScheduleConfig;
import ltd.idcu.est.scheduler.api.ScheduleType;
import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.cron.CronSchedulers;

public class SchedulerExample {
    public static void main(String[] args) throws InterruptedException {
        // Create Cron scheduler
        var scheduler = CronSchedulers.create();
        
        // Create task
        Task task = CronSchedulers.wrap(() -> {
            System.out.println("Task executed at: " + new java.util.Date());
        });
        
        // Schedule task (execute every 5 seconds)
        var config = ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression("*/5 * * * * *")
                .build();
        scheduler.schedule(task, config);
        
        // Start scheduler
        scheduler.start();
        System.out.println("Scheduler started. Press Ctrl+C to stop.");
        
        // Run for a while
        Thread.sleep(20000);
        
        // Stop scheduler
        scheduler.stop();
        System.out.println("Scheduler stopped.");
    }
}
