package ltd.idcu.est.examples.features;

import ltd.idcu.est.scheduler.api.ScheduleConfig;
import ltd.idcu.est.scheduler.api.ScheduleType;
import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.scheduler.fixed.FixedSchedulers;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SimpleSchedulerExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleSchedulerExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Scheduler Example ===");
        
        fixedRateExample();
        
        System.out.println("\n[X] All examples complete!");
    }
    
    private static void fixedRateExample() throws InterruptedException {
        System.out.println("\n--- Fixed Rate Scheduling ---");
        
        Scheduler scheduler = FixedSchedulers.create();
        
        Task task = FixedSchedulers.wrap(() -> {
            System.out.println("  Task executed: " + new Date());
        });
        
        ScheduleConfig config = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(0)
                .period(2)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        scheduler.schedule(task, config);
        scheduler.start();
        
        System.out.println("  Scheduler started, observing for 5 seconds...");
        Thread.sleep(5000);
        
        scheduler.stop();
        System.out.println("  Scheduler stopped");
        
        logger.info("Fixed rate scheduling example complete");
    }
}
