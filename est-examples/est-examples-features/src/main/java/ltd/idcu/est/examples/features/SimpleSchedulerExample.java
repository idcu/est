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
        System.out.println("=== EST 调度系统示例 ===");
        
        fixedRateExample();
        
        System.out.println("\n�?所有示例完成！");
    }
    
    private static void fixedRateExample() throws InterruptedException {
        System.out.println("\n--- 固定间隔调度 ---");
        
        Scheduler scheduler = FixedSchedulers.create();
        
        Task task = FixedSchedulers.wrap(() -> {
            System.out.println("  任务执行: " + new Date());
        });
        
        ScheduleConfig config = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(0)
                .period(2)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        scheduler.schedule(task, config);
        scheduler.start();
        
        System.out.println("  调度器已启动，观�?5 �?..");
        Thread.sleep(5000);
        
        scheduler.stop();
        System.out.println("  调度器已停止");
        
        logger.info("固定间隔调度示例完成");
    }
}
