package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.scheduler.api.ScheduleConfig;
import ltd.idcu.est.features.scheduler.api.ScheduleType;
import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;

public class SchedulerExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建Cron调度器
        var scheduler = CronSchedulers.create();
        
        // 创建任务
        Task task = CronSchedulers.wrap(() -> {
            System.out.println("Task executed at: " + new java.util.Date());
        });
        
        // 调度任务（每5秒执行一次）
        var config = ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression("*/5 * * * * *")
                .build();
        scheduler.schedule(task, config);
        
        // 启动调度器
        scheduler.start();
        System.out.println("Scheduler started. Press Ctrl+C to stop.");
        
        // 运行一段时间
        Thread.sleep(20000);
        
        // 停止调度器
        scheduler.stop();
        System.out.println("Scheduler stopped.");
    }
}