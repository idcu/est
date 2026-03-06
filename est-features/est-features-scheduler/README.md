# EST Scheduler - 调度系统

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是调度系统？

想象一下，你有一个智能闹钟：
- 每天早上 7 点叫你起床
- 每小时提醒你喝一次水
- 每周五下午 5 点提醒你写周报

**调度系统**就像程序的智能闹钟，它可以：
- 每隔固定时间执行任务
- 在指定时间执行任务
- 使用 Cron 表达式灵活调度
- 管理任务的生命周期

让程序自动化执行，解放你的双手！

### 第一个例子

让我们用 3 分钟写一个简单的调度程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-scheduler-api</artifactId>
    <version>1.3.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-scheduler-fixed</artifactId>
    <version>1.3.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-scheduler-cron</artifactId>
    <version>1.3.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class SchedulerFirstExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 调度系统示例 ===\n");
        
        // 创建固定速率调度器
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        // 每隔 1 秒执行一次任务
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("任务执行: " + System.currentTimeMillis());
        }, 0, 1, TimeUnit.SECONDS);
        
        // 运行 5 秒后停止
        Thread.sleep(5000);
        scheduler.shutdown();
        
        System.out.println("\n✅ 调度示例完成！");
    }
}
```

运行这个程序，你会看到任务每隔 1 秒执行一次！

🎉 恭喜你！你已经学会了使用调度系统！

---

## 📖 基础篇

### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **任务（Task）** | 需要执行的工作 | 打扫房间、写作业 |
| **调度器（Scheduler）** | 管理任务执行的组件 | 闹钟、日程表 |
| **固定速率** | 每隔固定时间执行 | 每小时喝一次水 |
| **Cron 表达式** | 灵活的时间表达式 | 每周一早上 9 点开会 |

### 2. 固定速率调度

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class FixedRateExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        // 立即开始，每隔 2 秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("固定速率任务执行: " + System.currentTimeMillis());
        }, 0, 2, TimeUnit.SECONDS);
        
        Thread.sleep(6000);
        scheduler.shutdown();
    }
}
```

### 3. Cron 表达式调度

```java
import ltd.idcu.est.features.scheduler.cron.CronScheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;

public class CronExample {
    public static void main(String[] args) throws InterruptedException {
        CronScheduler scheduler = CronSchedulers.newCronScheduler();
        
        // 每秒执行一次（Cron 表达式）
        scheduler.schedule("* * * * * ?", () -> {
            System.out.println("Cron 任务执行: " + System.currentTimeMillis());
        });
        
        Thread.sleep(5000);
        scheduler.shutdown();
    }
}
```

---

## 🔧 进阶篇

### 1. 任务监听

```java
import ltd.idcu.est.features.scheduler.api.TaskListener;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class TaskListenerExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        // 添加任务监听器
        scheduler.addTaskListener(new TaskListener() {
            @Override
            public void onTaskStart(String taskId) {
                System.out.println("任务开始: " + taskId);
            }
            
            @Override
            public void onTaskComplete(String taskId) {
                System.out.println("任务完成: " + taskId);
            }
            
            @Override
            public void onTaskError(String taskId, Throwable error) {
                System.out.println("任务出错: " + taskId + ", 错误: " + error.getMessage());
            }
        });
        
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("执行任务...");
        }, 0, 1, TimeUnit.SECONDS);
        
        Thread.sleep(3000);
        scheduler.shutdown();
    }
}
```

### 2. 与 EST Collection 集成

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SchedulerCollectionIntegrationExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        List<String> tasks = List.of("备份数据", "清理缓存", "发送报告");
        
        // 使用 Collection 批量创建任务
        Seqs.of(tasks)
                .forEach(taskName -> {
                    scheduler.scheduleAtFixedRate(() -> {
                        System.out.println("执行任务: " + taskName);
                    }, 0, 2, TimeUnit.SECONDS);
                });
        
        Thread.sleep(5000);
        scheduler.shutdown();
    }
}
```

---

## 💡 最佳实践

### 1. 优雅关闭调度器

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class GracefulShutdownExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("任务执行中...");
        }, 0, 1, TimeUnit.SECONDS);
        
        Thread.sleep(3000);
        
        // 优雅关闭，等待 5 秒
        System.out.println("开始关闭调度器...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            scheduler.shutdownNow();
        }
        System.out.println("调度器已关闭！");
    }
}
```

---

## 🎯 总结

调度系统就像程序的"智能闹钟"，让程序自动化执行任务，提高效率！

下一章，我们将学习 EST Security 安全系统！🎉
