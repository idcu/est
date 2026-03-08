# EST Monitor - 监控系统

## 📚 目录

- [快速入门](#快速入�?
- [基础篇](#基础�?
- [进阶篇](#进阶�?
- [最佳实践](#最佳实�?

---

## 🚀 快速入�?

### 什么是监控系统�?

想象一下，你在管理一个大型工厂。工厂里有很多机器，你需要时刻监控：
- 机器的温度正常吗�?
- 机器的运转速度怎么样？
- 有没有机器出故障�?
- 用电量是多少�?

**监控系统**就像工厂的监控室，它可以实时监控程序的运行状态：
- JVM 内存使用情况
- CPU 使用�?
- 系统健康状�?
- 各项性能指标

当程序出现问题时，监控系统可以及时发现并报警�?

### 第一个例�?

让我们用 3 分钟写一个简单的监控程序�?

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-jvm</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-system</artifactId>
    <version>2.1.0</version>
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
        System.out.println("堆内存使�? " + jvmMetrics.getHeapMemoryUsed() + " / " + jvmMetrics.getHeapMemoryMax());
        System.out.println("非堆内存使用: " + jvmMetrics.getNonHeapMemoryUsed());
        System.out.println("活动线程�? " + jvmMetrics.getThreadCount());
        
        // 系统监控
        System.out.println("\n--- 系统信息 ---");
        SystemMetrics systemMetrics = SystemMetrics.create();
        System.out.println("操作系统: " + systemMetrics.getOsName());
        System.out.println("CPU 核心�? " + systemMetrics.getAvailableProcessors());
        System.out.println("系统负载: " + systemMetrics.getSystemLoadAverage());
        
        System.out.println("\n�?监控示例完成�?);
    }
}
```

运行这个程序，你会看到系统的监控信息�?

🎉 恭喜你！你已经学会了使用监控系统�?

---

## 📖 基础�?

### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **指标（Metric�?* | 需要监控的数据�?| 温度、压力、速度 |
| **健康检�?* | 检查系统是否正常运�?| 体检 |
| **JVM 监控** | 监控 Java 虚拟机的运行状�?| 检查汽车发动机 |
| **系统监控** | 监控操作系统的运行状�?| 检查汽车整体状�?|

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
        System.out.println("堆内存已�? " + metrics.getHeapMemoryUsed());
        System.out.println("堆内存最�? " + metrics.getHeapMemoryMax());
        System.out.println("线程�? " + metrics.getThreadCount());
        System.out.println("GC 次数: " + metrics.getGcCount());
        
        // 健康检�?
        JvmHealthCheck healthCheck = monitor.checkHealth();
        System.out.println("健康状�? " + healthCheck.getStatus());
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
        System.out.println("CPU 核心�? " + metrics.getAvailableProcessors());
        System.out.println("系统负载: " + metrics.getSystemLoadAverage());
        System.out.println("可用内存: " + metrics.getFreeMemory());
        
        // 健康检�?
        SystemHealthCheck healthCheck = monitor.checkHealth();
        System.out.println("健康状�? " + healthCheck.getStatus());
    }
}
```

---

## 🔧 进阶�?

### 1. 自定义健康检�?

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
                return HealthCheckResult.healthy("数据库连接正�?);
            } else {
                return HealthCheckResult.unhealthy("数据库连接失�?);
            }
        } catch (Exception e) {
            return HealthCheckResult.unhealthy("数据库检查异�? " + e.getMessage());
        }
    }
    
    private boolean checkDatabaseConnection() {
        // 模拟数据库连接检�?
        return true;
    }
}
```

### 2. �?EST Collection 集成

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
        
        System.out.println("=== 系统健康检�?===");
        
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

## 💡 最佳实�?

### 1. 定期健康检�?

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
        
        // �?30 秒执行一次健康检�?
        scheduler.scheduleAtFixedRate(() -> {
            HealthCheckResult result = monitor.checkHealth();
            System.out.println("健康检�? " + result.getStatus() + " - " + result.getMessage());
        }, 0, 30, TimeUnit.SECONDS);
    }
}
```

---

## 🎯 总结

监控系统就像程序�?体检医生"，定期检查程序的健康状况，及时发现问题！

下一章，我们将学�?EST Scheduler 调度系统！�?
