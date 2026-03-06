# Scheduler 调度系统 API

调度系统提供固定间隔和Cron表达式的任务调度功能。

## 核心概念

| 概念 | 说明 |
|------|------|
| **Scheduler** | 调度器接口，用于调度任务 |
| **Task** | 任务接口，表示要执行的任务 |
| **TaskState** | 任务状态（SCHEDULED、RUNNING、FAILED、COMPLETED） |
| **SchedulerStats** | 调度统计信息 |
| **FixedRateScheduler** | 固定速率调度器 |
| **CronScheduler** | Cron表达式调度器 |

---

## 固定速率调度 (FixedRateScheduler)

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.api.TaskState;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

// 创建固定速率调度器
FixedRateScheduler scheduler = FixedRateSchedulers.create();

// 或者指定线程池大小
FixedRateScheduler scheduler = FixedRateSchedulers.create(5);

// 启动调度器
scheduler.start();

// 创建固定速率执行（立即开始，每秒执行一次）
Task task1 = FixedRateSchedulers.fixedRateTask(() -> {
    System.out.println("固定速率任务执行: " + System.currentTimeMillis());
}, 1, TimeUnit.SECONDS);

String taskId1 = scheduler.scheduleAtFixedRate(task1, 0, 1, TimeUnit.SECONDS);

// 固定延迟执行（上次完成后延迟1秒再执行）
Task task2 = FixedRateSchedulers.fixedDelayTask(() -> {
    System.out.println("固定延迟任务执行: " + System.currentTimeMillis());
}, 1, TimeUnit.SECONDS);

String taskId2 = scheduler.scheduleWithFixedDelay(task2, 0, 1, TimeUnit.SECONDS);

// 带初始延迟的固定速率任务
Task task3 = FixedRateSchedulers.fixedRateTask(() -> {
    System.out.println("5秒后开始执行");
}, 5, 1, TimeUnit.SECONDS);

String taskId3 = scheduler.scheduleAtFixedRate(task3, 5, 1, TimeUnit.SECONDS);

// 检查任务状态
if (scheduler.hasTask(taskId1)) {
    Task task = scheduler.getTask(taskId1);
    System.out.println("任务名称: " + task.getName());
    System.out.println("任务状态: " + task.getState());
    System.out.println("执行次数: " + task.getExecutionCount());
}

// 获取所有任务ID
System.out.println("所有任务: " + scheduler.getTaskIds());
System.out.println("任务数量: " + scheduler.getTaskCount());

// 停止调度器
// scheduler.stop();
```

---

## Cron 表达式调度 (CronScheduler)

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.cron.CronScheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;

// 创建Cron调度器
CronScheduler cronScheduler = CronSchedulers.create();

// 或者指定线程池大小
CronScheduler cronScheduler = CronSchedulers.create(3);

// 启动调度器
cronScheduler.start();

// 验证Cron表达式
boolean valid = CronSchedulers.isValidCronExpression("0 * * * *");
System.out.println("Cron表达式有效: " + valid);

// 每分钟执行
Task task1 = CronSchedulers.task(() -> {
    System.out.println("每分钟执行: " + System.currentTimeMillis());
}, "0 * * * *");

String taskId1 = cronScheduler.scheduleCron(task1, "0 * * * *");

// 每天凌晨2点执行
Task task2 = CronSchedulers.task(() -> {
    System.out.println("每天凌晨2点执行备份");
}, "0 2 * * *", "daily-backup");

String taskId2 = cronScheduler.scheduleCron(task2, "0 2 * * *");

// 每周一上午9点执行
Task task3 = CronSchedulers.task(() -> {
    System.out.println("每周一上午9点执行");
}, "0 9 * * MON", "weekly-report", "每周报告任务");

String taskId3 = cronScheduler.scheduleCron(task3, "0 9 * * MON");

// 停止调度器
// cronScheduler.stop();
```

---

## Cron 表达式语法

Cron表达式格式：`分 时 日 月 周`

| 字段 | 允许值 | 特殊字符 |
|------|---------|-----------|
| **分** | 0-59 | , - * / |
| **时** | 0-23 | , - * / |
| **日** | 1-31 | , - * ? / L W |
| **月** | 1-12 或 JAN-DEC | , - * / |
| **周** | 1-7 或 SUN-SAT | , - * ? / L # |

### 常用 Cron 示例

| 表达式 | 说明 |
|----------|------|
| `* * * * *` | 每分钟 |
| `0 * * * *` | 每小时（整点） |
| `0 0 * * *` | 每天（午夜） |
| `0 0 0 * * *` | 每天（午夜） - 带秒 |
| `0 0 0 * * SUN` | 每周日午夜 |
| `0 0 0 1 * *` | 每月1号午夜 |
| `0 0 9-17 * * MON-FRI` | 工作日9点-17点每小时 |
| `0 0/5 * * * *` | 每5分钟 |
| `0 0 8,12,18 * * *` | 每天8点、12点、18点 |

---

## 任务管理

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

FixedRateScheduler scheduler = FixedRateSchedulers.create();
scheduler.start();

// 调度任务
Task task = FixedRateSchedulers.fixedRateTask(() -> {
    System.out.println("任务执行");
}, 1, TimeUnit.SECONDS);

String taskId = scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

// 检查任务是否存在
System.out.println("任务存在: " + scheduler.hasTask(taskId));

// 获取任务
Task retrievedTask = scheduler.getTask(taskId);
if (retrievedTask != null) {
    System.out.println("任务ID: " + retrievedTask.getId());
    System.out.println("任务名称: " + retrievedTask.getName());
    System.out.println("任务描述: " + retrievedTask.getDescription());
    System.out.println("任务状态: " + retrievedTask.getState());
    System.out.println("创建时间: " + retrievedTask.getCreatedAt());
    System.out.println("上次执行时间: " + retrievedTask.getLastExecutedAt());
    System.out.println("执行次数: " + retrievedTask.getExecutionCount());
    System.out.println("上次错误: " + retrievedTask.getLastError());
}

// 取消任务
boolean cancelled = scheduler.cancel(taskId);
System.out.println("任务取消: " + cancelled);

// 获取所有任务ID
System.out.println("所有任务: " + scheduler.getTaskIds());

// 获取任务数量
System.out.println("任务数量: " + scheduler.getTaskCount());

// 检查调度器是否运行
System.out.println("调度器运行中: " + scheduler.isRunning());

// 停止调度器
scheduler.stop();
```

---

## 调度统计

```java
import ltd.idcu.est.features.scheduler.api.SchedulerStats;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

FixedRateScheduler scheduler = FixedRateSchedulers.create();
scheduler.start();

// 调度一些任务
for (int i = 0; i < 5; i++) {
    Task task = FixedRateSchedulers.fixedRateTask(() -> {
        // 任务执行
    }, 1, TimeUnit.SECONDS);
    scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
}

// 获取统计信息
SchedulerStats stats = scheduler.getStats();

System.out.println("总任务数: " + stats.getTotalTaskCount());
System.out.println("活跃任务数: " + stats.getActiveTaskCount());
System.out.println("已完成任务数: " + stats.getCompletedTaskCount());
System.out.println("失败任务数: " + stats.getFailedTaskCount());
System.out.println("总执行次数: " + stats.getTotalExecutionCount());
System.out.println("成功执行次数: " + stats.getSuccessfulExecutionCount());
System.out.println("失败执行次数: " + stats.getFailedExecutionCount());
System.out.println("平均执行时间: " + stats.getAverageExecutionTimeMs() + "ms");
System.out.println("调度器启动时间: " + stats.getStartTime());
```

---

## 完整示例：电商系统任务调度

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.api.SchedulerStats;
import ltd.idcu.est.features.scheduler.api.Task;
import ltd.idcu.est.features.scheduler.cron.CronScheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class ECommerceSchedulerExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Scheduler Example ===\n");
        
        // 1. 创建固定速率调度器 - 用于实时任务
        FixedRateScheduler realtimeScheduler = FixedRateSchedulers.create(2);
        realtimeScheduler.start();
        
        // 2. 创建Cron调度器 - 用于定时任务
        CronScheduler scheduledScheduler = CronSchedulers.create(1);
        scheduledScheduler.start();
        
        // 3. 实时任务：每分钟检查订单状态
        Task orderCheckTask = FixedRateSchedulers.fixedRateTask(() -> {
            System.out.println("[实时] 检查订单状态...");
        }, 1, TimeUnit.MINUTES);
        
        String orderCheckId = realtimeScheduler.scheduleAtFixedRate(
            orderCheckTask, 0, 1, TimeUnit.MINUTES);
        
        // 4. 实时任务：每5秒更新缓存
        Task cacheUpdateTask = FixedRateSchedulers.fixedDelayTask(() -> {
            System.out.println("[实时] 更新缓存数据...");
        }, 5, TimeUnit.SECONDS);
        
        String cacheUpdateId = realtimeScheduler.scheduleWithFixedDelay(
            cacheUpdateTask, 0, 5, TimeUnit.SECONDS);
        
        // 5. 定时任务：每天凌晨2点备份数据库
        Task backupTask = CronSchedulers.task(() -> {
            System.out.println("[定时] 执行数据库备份...");
        }, "0 2 * * *", "daily-backup", "每日数据库备份");
        
        String backupId = scheduledScheduler.scheduleCron(backupTask, "0 2 * * *");
        
        // 6. 定时任务：每周一上午9点生成周报
        Task reportTask = CronSchedulers.task(() -> {
            System.out.println("[定时] 生成周销售报告...");
        }, "0 9 * * MON", "weekly-report", "每周销售报告");
        
        String reportId = scheduledScheduler.scheduleCron(reportTask, "0 9 * * MON");
        
        // 显示任务信息
        System.out.println("\n=== 已调度任务:");
        System.out.println("实时任务数: " + realtimeScheduler.getTaskCount());
        System.out.println("定时任务数: " + scheduledScheduler.getTaskCount());
        System.out.println();
        
        // 运行一段时间
        System.out.println("调度器运行中... (10秒后停止)\n");
        Thread.sleep(10000);
        
        // 显示统计信息
        System.out.println("=== 实时调度器统计:");
        SchedulerStats realtimeStats = realtimeScheduler.getStats();
        System.out.println("总执行次数: " + realtimeStats.getTotalExecutionCount());
        System.out.println("成功执行次数: " + realtimeStats.getSuccessfulExecutionCount());
        
        System.out.println("\n=== 定时调度器统计:");
        SchedulerStats scheduledStats = scheduledScheduler.getStats();
        System.out.println("总任务数: " + scheduledStats.getTotalTaskCount());
        
        // 停止调度器
        System.out.println("\n停止调度器...");
        realtimeScheduler.stop();
        scheduledScheduler.stop();
        
        System.out.println("\n=== Example Complete ===");
    }
}
```

---

## 最佳实践

1. **选择合适的调度器**：
   - 使用 FixedRateScheduler 处理需要高频执行的任务
   - 使用 CronScheduler 处理定时任务
   
2. **合理设置线程池大小**：
   ```java
   // 根据任务数量和执行时间选择合适的线程池大小
   FixedRateScheduler scheduler = FixedRateSchedulers.create(5);
   ```

3. **给任务起有意义的名称**：
   ```java
   Task task = CronSchedulers.task(runnable, "0 2 * * *", "daily-backup");
   ```

4. **监控任务执行情况**：
   ```java
   // 定期检查调度统计
   SchedulerStats stats = scheduler.getStats();
   if (stats.getFailedExecutionCount() > 0) {
       // 处理失败的任务
   }
   ```

5. **合理使用固定速率和固定延迟**：
   - fixedRate：按固定时间间隔执行，不管上一次是否完成
   - fixedDelay：在上一次完成后延迟指定时间再执行

6. **优雅关闭**：
   ```java
   // 在应用关闭时停止调度器
   Runtime.getRuntime().addShutdownHook(new Thread(() -> {
       scheduler.stop();
   }));
   ```
