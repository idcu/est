# Scheduler 调度系统 API

调度系统提供固定间隔和Cron表达式的任务调度功能。

## 核心接口

```java
public interface Scheduler {
    String schedule(Runnable task, long delay, long period, TimeUnit unit);
    String scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit);
    String scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit);
    String scheduleCron(Runnable task, String cronExpression);
    void cancel(String jobId);
    void cancelAll();
    void pause(String jobId);
    void resume(String jobId);
    List<ScheduledJob> getJobs();
    SchedulerStats stats();
}
```

## 固定间隔调度 (FixedRateScheduler)

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedSchedulers;

// 创建调度器
Scheduler scheduler = FixedSchedulers.create();

// 延迟执行
scheduler.schedule(() -> {
    System.out.println("Delayed task");
}, 5, TimeUnit.SECONDS);

// 固定速率执行
String jobId = scheduler.scheduleAtFixedRate(() -> {
    System.out.println("Fixed rate task");
}, 0, 1, TimeUnit.MINUTES); // 立即开始，每分钟执行

// 固定延迟执行
scheduler.scheduleWithFixedDelay(() -> {
    System.out.println("Fixed delay task");
}, 0, 30, TimeUnit.SECONDS); // 上次执行完成后30秒再执行
```

## Cron 表达式调度 (CronScheduler)

```java
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;

// 创建Cron调度器
Scheduler cronScheduler = CronSchedulers.create();

// Cron表达式示例
cronScheduler.scheduleCron(() -> {
    System.out.println("Every minute");
}, "* * * * *"); // 每分钟

cronScheduler.scheduleCron(() -> {
    System.out.println("Every day at 2 AM");
}, "0 2 * * *"); // 每天凌晨2点

cronScheduler.scheduleCron(() -> {
    System.out.println("Every Monday at 9 AM");
}, "0 9 * * MON"); // 每周一上午9点
```

## Cron 表达式语法

```
秒 分 时 日 月 周
```

| 字段 | 允许值 | 特殊字符 |
|------|--------|----------|
| 秒 | 0-59 | , - * / |
| 分 | 0-59 | , - * / |
| 时 | 0-23 | , - * / |
| 日 | 1-31 | , - * ? / L W |
| 月 | 1-12 or JAN-DEC | , - * / |
| 周 | 1-7 or SUN-SAT | , - * ? / L # |

## 常用 Cron 示例

| 表达式 | 说明 |
|--------|------|
| `0 * * * *` | 每分钟 |
| `0 0 * * *` | 每小时 |
| `0 0 0 * *` | 每天 |
| `0 0 0 * * SUN` | 每周日 |
| `0 0 0 1 * *` | 每月1号 |
| `0 0 9-17 * * MON-FRI` | 工作日9点-17点每小时 |

## 任务管理

```java
// 取消任务
scheduler.cancel(jobId);

// 暂停任务
scheduler.pause(jobId);

// 恢复任务
scheduler.resume(jobId);

// 获取所有任务
List<ScheduledJob> jobs = scheduler.getJobs();
for (ScheduledJob job : jobs) {
    System.out.println(job.getId() + ": " + job.getStatus());
}
```

## 任务监听

```java
scheduler.addListener(new SchedulerListener() {
    @Override
    public void onJobScheduled(ScheduledJob job) {
        System.out.println("Job scheduled: " + job.getId());
    }
    
    @Override
    public void onJobExecuted(ScheduledJob job, long duration) {
        System.out.println("Job executed: " + job.getId() + " in " + duration + "ms");
    }
    
    @Override
    public void onJobFailed(ScheduledJob job, Throwable error) {
        System.err.println("Job failed: " + job.getId());
        error.printStackTrace();
    }
});
```

## 调度统计

```java
SchedulerStats stats = scheduler.stats();
System.out.println("Total jobs: " + stats.getTotalJobs());
System.out.println("Active jobs: " + stats.getActiveJobs());
System.out.println("Executions: " + stats.getTotalExecutions());
System.out.println("Failures: " + stats.getFailedExecutions());
```
