# EST Monitor - 监控系统

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是监控系统？

想象一下，你在管理一个大型工厂。工厂里有很多机器，你需要时刻监控：
- 机器的温度正常吗？
- 机器的运转速度怎么样？
- 有没有机器出故障？
- 用电量是多少？

**监控系统**就像工厂的监控室，它可以实时监控程序的运行状态：
- JVM 内存使用情况
- CPU 使用率
- 系统健康状态
- 各项性能指标

当程序出现问题时，监控系统可以及时发现并报警！

### 第一个例子

让我们用 3 分钟写一个简单的监控程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-jvm</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-system</artifactId>
    <version>2.0.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.monitor.jvm.JvmMetrics;
import ltd.idcu.est.features.monitor.system.SystemMetrics;

public class MonitorFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 系统监控示例 ===\n");
        
        // JVM 监控
        System.out.println("--- JVM 信息 ---");
        JvmMetrics jvmMetrics = JvmMetrics.create();
        System.out.println("堆内存使用: " + jvmMetrics.getHeapMemoryUsed() + " / " + jvmMetrics.getHeapMemoryMax());
        System.out.println("非堆内存使用: " + jvmMetrics.getNonHeapMemoryUsed());
        System.out.println("活动线程数: " + jvmMetrics.getThreadCount());
        
        // 系统监控
        System.out.println("\n--- 系统信息 ---");
        SystemMetrics systemMetrics = SystemMetrics.create();
        System.out.println("操作系统: " + systemMetrics.getOsName());
        System.out.println("CPU 核心数: " + systemMetrics.getAvailableProcessors());
        System.out.println("系统负载: " + systemMetrics.getSystemLoadAverage());
        
        System.out.println("\n✅ 监控示例完成！");
    }
}
```

运行这个程序，你会看到系统的监控信息！

🎉 恭喜你！你已经学会了使用监控系统！

---

## 📖 基础篇

### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **指标（Metric）** | 需要监控的数据项 | 温度、压力、速度 |
| **健康检查** | 检查系统是否正常运行 | 体检 |
| **JVM 监控** | 监控 Java 虚拟机的运行状态 | 检查汽车发动机 |
| **系统监控** | 监控操作系统的运行状态 | 检查汽车整体状况 |

### 2. JVM 监控

```java
import ltd.idcu.est.features.monitor.jvm.JvmHealthCheck;
import ltd.idcu.est.features.monitor.jvm.JvmMetrics;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;

public class JvmMonitorExample {
    public static void main(String[] args) {
        // 创建 JVM 监控
        JvmMonitor monitor = new JvmMonitor();
        
        // 获取 JVM 指标
        JvmMetrics metrics = monitor.getMetrics();
        System.out.println("堆内存已用: " + metrics.getHeapMemoryUsed());
        System.out.println("堆内存最大: " + metrics.getHeapMemoryMax());
        System.out.println("线程数: " + metrics.getThreadCount());
        System.out.println("GC 次数: " + metrics.getGcCount());
        
        // 健康检查
        JvmHealthCheck healthCheck = monitor.checkHealth();
        System.out.println("健康状态: " + healthCheck.getStatus());
        System.out.println("健康消息: " + healthCheck.getMessage());
    }
}
```

### 3. 系统监控

```java
import ltd.idcu.est.features.monitor.system.SystemHealthCheck;
import ltd.idcu.est.features.monitor.system.SystemMetrics;
import ltd.idcu.est.features.monitor.system.SystemMonitor;

public class SystemMonitorExample {
    public static void main(String[] args) {
        // 创建系统监控
        SystemMonitor monitor = new SystemMonitor();
        
        // 获取系统指标
        SystemMetrics metrics = monitor.getMetrics();
        System.out.println("操作系统: " + metrics.getOsName());
        System.out.println("CPU 核心数: " + metrics.getAvailableProcessors());
        System.out.println("系统负载: " + metrics.getSystemLoadAverage());
        System.out.println("可用内存: " + metrics.getFreeMemory());
        
        // 健康检查
        SystemHealthCheck healthCheck = monitor.checkHealth();
        System.out.println("健康状态: " + healthCheck.getStatus());
    }
}
```

---

## 🔧 进阶篇

### 1. 自定义健康检查

```java
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;

public class DatabaseHealthCheck implements HealthCheck {
    @Override
    public String getName() {
        return "database";
    }
    
    @Override
    public HealthCheckResult check() {
        try {
            // 检查数据库连接
            boolean isConnected = checkDatabaseConnection();
            if (isConnected) {
                return HealthCheckResult.healthy("数据库连接正常");
            } else {
                return HealthCheckResult.unhealthy("数据库连接失败");
            }
        } catch (Exception e) {
            return HealthCheckResult.unhealthy("数据库检查异常: " + e.getMessage());
        }
    }
    
    private boolean checkDatabaseConnection() {
        // 模拟数据库连接检查
        return true;
    }
}
```

### 2. 与 EST Collection 集成

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.jvm.JvmHealthCheck;
import ltd.idcu.est.features.monitor.system.SystemHealthCheck;

import java.util.List;

public class MonitorCollectionIntegrationExample {
    public static void main(String[] args) {
        List<HealthCheck> checks = List.of(
                new JvmHealthCheck(),
                new SystemHealthCheck()
        );
        
        System.out.println("=== 系统健康检查 ===");
        
        Seqs.of(checks)
                .map(check -> {
                    HealthCheckResult result = check.check();
                    return String.format("[%s] %s: %s", 
                            result.getStatus(), check.getName(), result.getMessage());
                })
                .forEach(System.out::println);
    }
}
```

---

## 💡 最佳实践

### 1. 定期健康检查

```java
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicHealthCheckExample {
    public static void main(String[] args) {
        JvmMonitor monitor = new JvmMonitor();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // 每 30 秒执行一次健康检查
        scheduler.scheduleAtFixedRate(() -> {
            HealthCheckResult result = monitor.checkHealth();
            System.out.println("健康检查: " + result.getStatus() + " - " + result.getMessage());
        }, 0, 30, TimeUnit.SECONDS);
    }
}
```

---

## 🎯 总结

监控系统就像程序的"体检医生"，定期检查程序的健康状况，及时发现问题！

下一章，我们将学习 EST Scheduler 调度系统！🎉
