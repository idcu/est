package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.scheduler.api.ScheduleConfig;
import ltd.idcu.est.features.scheduler.api.ScheduleType;
import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.api.TaskState;
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;
import ltd.idcu.est.features.scheduler.fixed.FixedSchedulers;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CompleteSchedulerExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteSchedulerExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(70));
        System.out.println("EST 调度系统模块 - 完整示例");
        System.out.println("=".repeat(70));
        System.out.println("\n本示例将展示 EST 调度系统模块的各种功能：");
        System.out.println("  - 固定间隔调度（Fixed Rate）");
        System.out.println("  - Cron 表达式调度（灵活定时）");
        System.out.println("  - 任务管理（启动、暂停、取消）");
        System.out.println("  - 实际应用场景（数据备份、统计、清理）");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("第一部分：理解调度系统的作用");
        System.out.println("=".repeat(70));
        System.out.println("\n【为什么需要调度系统？】");
        System.out.println("  - 有些任务需要定时执行，不是手动触发");
        System.out.println("  - 比如：每天凌晨备份数据库");
        System.out.println("  - 比如：每小时统计一次业务数据");
        System.out.println("  - 比如：每分钟检查系统健康状态\n");
        
        fixedRateExample();
        cronExpressionExample();
        taskManagementExample();
        practicalScenariosExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("✓ 所有调度系统示例完成！");
        System.out.println("=".repeat(70));
    }
    
    private static void fixedRateExample() throws InterruptedException {
        System.out.println("\n--- 方式一：固定间隔调度（Fixed Rate）---");
        System.out.println("\n【固定间隔调度的特点】");
        System.out.println("  - 每隔固定时间执行一次");
        System.out.println("  - 简单、易懂");
        System.out.println("  - 适合：心跳检测、状态同步\n");
        
        System.out.println("步骤 1: 创建固定间隔调度器");
        Scheduler scheduler = FixedSchedulers.create();
        
        System.out.println("\n步骤 2: 创建任务");
        Task heartbeatTask = FixedSchedulers.wrap(() -> {
            System.out.println("   [" + new Date() + "] 💓 心跳检测：系统正常运行");
        });
        
        Task syncTask = FixedSchedulers.wrap(() -> {
            System.out.println("   [" + new Date() + "] 🔄 数据同步：正在同步...");
        });
        
        System.out.println("\n步骤 3: 配置调度规则");
        System.out.println("   心跳任务：每 2 秒执行一次");
        ScheduleConfig heartbeatConfig = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(0)
                .period(2)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        System.out.println("   同步任务：每 5 秒执行一次，首次延迟 3 秒");
        ScheduleConfig syncConfig = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(3)
                .period(5)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        System.out.println("\n步骤 4: 提交任务到调度器");
        String heartbeatTaskId = scheduler.schedule(heartbeatTask, heartbeatConfig);
        String syncTaskId = scheduler.schedule(syncTask, syncConfig);
        
        System.out.println("   心跳任务 ID: " + heartbeatTaskId);
        System.out.println("   同步任务 ID: " + syncTaskId);
        
        System.out.println("\n步骤 5: 启动调度器");
        scheduler.start();
        System.out.println("   调度器已启动，观察 10 秒...\n");
        
        Thread.sleep(10000);
        
        System.out.println("\n步骤 6: 停止调度器");
        scheduler.stop();
        System.out.println("   调度器已停止");
        
        logger.info("固定间隔调度示例完成");
    }
    
    private static void cronExpressionExample() {
        System.out.println("\n--- 方式二：Cron 表达式调度（最灵活）---");
        System.out.println("\n【Cron 表达式是什么？】");
        System.out.println("  - 一种用来描述定时任务的字符串");
        System.out.println("  - 可以精确到秒、分、时、日、月、周");
        System.out.println("  - 非常灵活，适合复杂的定时需求\n");
        
        System.out.println("【Cron 表达式格式】");
        System.out.println("   秒 分 时 日 月 周");
        System.out.println("   *  *  *  *  *  *");
        System.out.println();
        System.out.println("   每个位置的含义：");
        System.out.println("   - 秒：0-59");
        System.out.println("   - 分：0-59");
        System.out.println("   - 时：0-23");
        System.out.println("   - 日：1-31");
        System.out.println("   - 月：1-12");
        System.out.println("   - 周：1-7（1=周日，7=周六）\n");
        
        System.out.println("【常用 Cron 表达式示例】");
        System.out.println("   每秒执行：     * * * * * *");
        System.out.println("   每分钟执行：   0 * * * * *");
        System.out.println("   每小时执行：   0 0 * * * *");
        System.out.println("   每天凌晨 2 点：0 0 2 * * *");
        System.out.println("   每周一 9 点：   0 0 9 * * 1");
        System.out.println("   每月 1 号：     0 0 0 1 * *\n");
        
        System.out.println("【特殊符号含义】");
        System.out.println("   * ：任意值（比如 * 在分的位置表示每分钟）");
        System.out.println("   , ：多个值（比如 1,3,5 在分的位置表示第1、3、5分钟）");
        System.out.println("   - ：范围（比如 9-17 在时的位置表示 9点到17点）");
        System.out.println("   / ：间隔（比如 0/5 在分的位置表示每 5 分钟）\n");
        
        System.out.println("步骤 1: 创建 Cron 调度器");
        Scheduler cronScheduler = CronSchedulers.create();
        
        System.out.println("\n步骤 2: 创建任务");
        Task backupTask = CronSchedulers.wrap(() -> {
            System.out.println("   📦 数据库备份任务执行中...");
        });
        
        Task statsTask = CronSchedulers.wrap(() -> {
            System.out.println("   📊 每日统计任务执行中...");
        });
        
        System.out.println("\n步骤 3: 配置 Cron 调度");
        System.out.println("   备份任务：每 5 秒执行一次（演示用）");
        ScheduleConfig backupConfig = ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression("0/5 * * * * *")
                .build();
        
        System.out.println("   统计任务：每 10 秒执行一次（演示用）");
        ScheduleConfig statsConfig = ScheduleConfig.builder()
                .type(ScheduleType.CRON)
                .cronExpression("0/10 * * * * *")
                .build();
        
        System.out.println("\n💡 提示：生产环境应该用真正的 Cron 表达式，比如：");
        System.out.println("   每天凌晨 2 点备份：0 0 2 * * *");
        System.out.println("   每小时整点统计：0 0 * * * *");
        
        logger.info("Cron 表达式示例完成");
    }
    
    private static void taskManagementExample() {
        System.out.println("\n--- 方式三：任务管理 ---");
        System.out.println("\n【任务管理的功能】");
        System.out.println("  - 查看任务状态");
        System.out.println("  - 暂停任务");
        System.out.println("  - 恢复任务");
        System.out.println("  - 取消任务\n");
        
        Scheduler scheduler = FixedSchedulers.create();
        
        System.out.println("步骤 1: 创建并调度任务");
        Task task = FixedSchedulers.wrap(() -> {
            System.out.println("   任务执行: " + new Date());
        });
        
        ScheduleConfig config = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(0)
                .period(2)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        String taskId = scheduler.schedule(task, config);
        System.out.println("   任务已调度，ID: " + taskId);
        
        System.out.println("\n步骤 2: 启动调度器");
        scheduler.start();
        
        System.out.println("\n步骤 3: 查看任务状态");
        TaskState state = scheduler.getTaskState(taskId);
        System.out.println("   任务状态: " + state);
        
        System.out.println("\n步骤 4: 暂停任务");
        scheduler.pauseTask(taskId);
        System.out.println("   任务已暂停");
        
        System.out.println("\n步骤 5: 恢复任务");
        scheduler.resumeTask(taskId);
        System.out.println("   任务已恢复");
        
        System.out.println("\n步骤 6: 取消任务");
        scheduler.cancelTask(taskId);
        System.out.println("   任务已取消");
        
        System.out.println("\n步骤 7: 停止调度器");
        scheduler.stop();
        
        logger.info("任务管理示例完成");
    }
    
    private static void practicalScenariosExample() {
        System.out.println("\n--- 方式四：实际应用场景 ---");
        System.out.println("\n【场景 1：数据备份】");
        System.out.println("   需求：每天凌晨 2 点备份数据库");
        System.out.println("   Cron：0 0 2 * * *");
        System.out.println("   任务：");
        System.out.println("     1. 连接数据库");
        System.out.println("     2. 导出数据到文件");
        System.out.println("     3. 压缩文件");
        System.out.println("     4. 上传到云存储");
        System.out.println("     5. 删除 7 天前的旧备份\n");
        
        System.out.println("【场景 2：日志清理】");
        System.out.println("   需求：每天凌晨 3 点清理 30 天前的日志");
        System.out.println("   Cron：0 0 3 * * *");
        System.out.println("   任务：");
        System.out.println("     1. 遍历日志目录");
        System.out.println("     2. 检查文件最后修改时间");
        System.out.println("     3. 删除超过 30 天的文件\n");
        
        System.out.println("【场景 3：数据统计】");
        System.out.println("   需求：每小时统计一次业务数据");
        System.out.println("   Cron：0 0 * * * *");
        System.out.println("   任务：");
        System.out.println("     1. 查询上一小时的订单数");
        System.out.println("     2. 计算销售额");
        System.out.println("     3. 更新统计报表");
        System.out.println("     4. 发送通知给管理员\n");
        
        System.out.println("【场景 4：健康检查】");
        System.out.println("   需求：每分钟检查一次系统健康状态");
        System.out.println("   Cron：0 * * * * *");
        System.out.println("   任务：");
        System.out.println("     1. 检查数据库连接");
        System.out.println("     2. 检查 Redis 连接");
        System.out.println("     3. 检查磁盘空间");
        System.out.println("     4. 检查内存使用率");
        System.out.println("     5. 如果有问题，发送告警\n");
        
        System.out.println("【场景 5：缓存预热】");
        System.out.println("   需求：每天早上 8 点预热热点数据");
        System.out.println("   Cron：0 0 8 * * *");
        System.out.println("   任务：");
        System.out.println("     1. 查询热门商品");
        System.out.println("     2. 查询热门文章");
        System.out.println("     3. 加载到缓存中");
        System.out.println("     4. 提升用户访问速度\n");
        
        logger.info("实际应用场景示例完成");
    }
}
