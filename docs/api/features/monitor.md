# Monitor 监控系统 API

监控系统提供JVM监控、系统监控和健康检查功能。

## 核心概念

| 概念 | 说明 |
|------|------|
| **HealthCheck** | 健康检查接口，用于检查系统组件的健康状态 |
| **HealthCheckRegistry** | 健康检查注册中心，管理多个健康检查 |
| **HealthStatus** | 健康状态枚举（HEALTHY、DEGRADED、UNHEALTHY、UNKNOWN） |
| **HealthCheckResult** | 健康检查结果 |
| **JvmMonitor** | JVM 监控器，提供 JVM 运行时信息 |
| **SystemMonitor** | 系统监控器，提供操作系统和硬件信息 |

---

## JVM 监控 (JvmMonitor)

```java
import ltd.idcu.est.features.monitor.jvm.JvmMetrics;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;

// 获取 JVM 监控器单例
JvmMonitor jvmMonitor = JvmMonitor.getInstance();

// 获取 JVM 信息
System.out.println("JVM 信息: " + jvmMonitor.getJvmInfo());
System.out.println("运行时间: " + jvmMonitor.getUptime() + "ms");

// 获取 JVM 指标
JvmMetrics metrics = jvmMonitor.getJvmMetrics();
System.out.println("堆内存使用: " + metrics.getHeapMemoryUsed() + " / " + metrics.getHeapMemoryMax());
System.out.println("非堆内存使用: " + metrics.getNonHeapMemoryUsed());
System.out.println("活动线程数: " + metrics.getThreadCount());
System.out.println("GC 次数: " + metrics.getGcCount());

// 执行健康检查
HealthCheckResult healthResult = jvmMonitor.checkHealth();
System.out.println("健康状态: " + healthResult.getStatus().getName());
System.out.println("健康消息: " + healthResult.getMessage());
```

---

## 系统监控 (SystemMonitor)

```java
import ltd.idcu.est.features.monitor.system.SystemMetrics;
import ltd.idcu.est.features.monitor.system.SystemMonitor;

// 获取系统监控器单例
SystemMonitor systemMonitor = SystemMonitor.getInstance();

// 获取操作系统信息
System.out.println("操作系统: " + systemMonitor.getOsInfo());

// 获取系统指标
SystemMetrics metrics = systemMonitor.getSystemMetrics();
System.out.println("CPU 核心数: " + metrics.getAvailableProcessors());
System.out.println("系统负载: " + metrics.getSystemLoadAverage());
System.out.println("可用内存: " + metrics.getFreeMemory());
System.out.println("总内存: " + metrics.getTotalMemory());
System.out.println("操作系统名称: " + metrics.getOsName());
System.out.println("操作系统版本: " + metrics.getOsVersion());

// 执行健康检查
HealthCheckResult healthResult = systemMonitor.checkHealth();
System.out.println("健康状态: " + healthResult.getStatus().getName());
System.out.println("健康消息: " + healthResult.getMessage());
```

---

## 健康检查 (HealthCheck)

### 创建自定义健康检查

```java
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthStatus;

public class DatabaseHealthCheck implements HealthCheck {
    
    @Override
    public HealthStatus check() {
        try {
            // 检查数据库连接
            boolean isConnected = checkDatabaseConnection();
            if (isConnected) {
                return HealthStatus.healthy("数据库连接正常");
            } else {
                return HealthStatus.unhealthy("数据库连接失败");
            }
        } catch (Exception e) {
            return HealthStatus.unhealthy("数据库检查异常: " + e.getMessage(), e);
        }
    }
    
    @Override
    public HealthStatus getStatus() {
        return check();
    }
    
    @Override
    public String getName() {
        return "database";
    }
    
    @Override
    public String getDescription() {
        return "数据库连接健康检查";
    }
    
    private boolean checkDatabaseConnection() {
        // 模拟数据库连接检查
        return true;
    }
}
```

### 使用 HealthCheckRegistry

```java
import ltd.idcu.est.features.monitor.api.HealthCheckRegistry;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;
import ltd.idcu.est.features.monitor.jvm.JvmHealthCheck;
import ltd.idcu.est.features.monitor.system.SystemHealthCheck;

import java.util.Map;

// 创建健康检查注册中心
HealthCheckRegistry registry = new HealthCheckRegistry();

// 注册内置健康检查
registry.register(new JvmHealthCheck());
registry.register(new SystemHealthCheck());

// 注册自定义健康检查
registry.register(new DatabaseHealthCheck());

// 获取已注册的健康检查名称
System.out.println("已注册: " + registry.getNames());

// 执行单个健康检查
HealthCheckResult result = registry.check("database");
System.out.println(result.getName() + ": " + result.getStatus().getName());

// 执行所有健康检查
Map<String, HealthCheckResult> allResults = registry.checkAll();
for (Map.Entry<String, HealthCheckResult> entry : allResults.entrySet()) {
    HealthCheckResult r = entry.getValue();
    System.out.printf("[%s] %s - %s%n", 
        entry.getKey(), 
        r.getStatus().getName(), 
        r.getMessage());
}

// 获取聚合健康状态
HealthStatus aggregateStatus = registry.getAggregateStatus();
System.out.println("整体状态: " + aggregateStatus.getName());
System.out.println("状态消息: " + aggregateStatus.getMessage());

// 获取健康检查的 Map 格式（可用于 JSON 序列化）
Map<String, Object> healthMap = registry.toMap();
System.out.println(healthMap);
```

---

## 健康状态 (HealthStatus)

```java
import ltd.idcu.est.features.monitor.api.HealthStatus;

// 创建健康状态
HealthStatus healthy = HealthStatus.healthy("服务正常运行");
HealthStatus degraded = HealthStatus.degraded("服务运行但性能下降");
HealthStatus unhealthy = HealthStatus.unhealthy("服务不可用", new Exception("连接超时"));
HealthStatus unknown = HealthStatus.unknown("无法确定状态");

// 检查状态
System.out.println("是否健康: " + healthy.isHealthy());
System.out.println("是否降级: " + degraded.isDegraded());
System.out.println("是否不健康: " + unhealthy.isUnhealthy());

// 获取状态信息
System.out.println("状态名称: " + healthy.getName());
System.out.println("状态描述: " + healthy.getDescription());
System.out.println("状态消息: " + healthy.getMessage());
System.out.println("错误信息: " + unhealthy.getError());
```

---

## 内置健康检查

### JVM 健康检查

```java
import ltd.idcu.est.features.monitor.jvm.JvmHealthCheck;

JvmHealthCheck jvmHealthCheck = new JvmHealthCheck();
HealthStatus status = jvmHealthCheck.check();
System.out.println("JVM 健康状态: " + status.getName());
```

### 系统健康检查

```java
import ltd.idcu.est.features.monitor.system.SystemHealthCheck;

SystemHealthCheck systemHealthCheck = new SystemHealthCheck();
HealthStatus status = systemHealthCheck.check();
System.out.println("系统健康状态: " + status.getName());
```

### 内存健康检查

```java
import ltd.idcu.est.features.monitor.api.MemoryHealthCheck;

MemoryHealthCheck memoryHealthCheck = new MemoryHealthCheck();
HealthStatus status = memoryHealthCheck.check();
System.out.println("内存健康状态: " + status.getName());
```

### 线程健康检查

```java
import ltd.idcu.est.features.monitor.api.ThreadHealthCheck;

ThreadHealthCheck threadHealthCheck = new ThreadHealthCheck();
HealthStatus status = threadHealthCheck.check();
System.out.println("线程健康状态: " + status.getName());
```

### 磁盘空间健康检查

```java
import ltd.idcu.est.features.monitor.api.DiskSpaceHealthCheck;

DiskSpaceHealthCheck diskHealthCheck = new DiskSpaceHealthCheck();
HealthStatus status = diskHealthCheck.check();
System.out.println("磁盘空间健康状态: " + status.getName());
```

---

## 健康检查结果 (HealthCheckResult)

```java
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;

// 创建健康检查结果
HealthCheckResult healthyResult = HealthCheckResult.healthy("database", "连接正常");
HealthCheckResult unhealthyResult = HealthCheckResult.unhealthy("redis", "连接失败", new Exception("超时"));

// 检查结果状态
System.out.println("是否健康: " + healthyResult.isHealthy());
System.out.println("是否不健康: " + unhealthyResult.isUnhealthy());

// 获取结果信息
System.out.println("名称: " + healthyResult.getName());
System.out.println("状态: " + healthyResult.getStatus().getName());
System.out.println("消息: " + healthyResult.getMessage());
System.out.println("时间戳: " + healthyResult.getTimestamp());
System.out.println("详情: " + healthyResult.getDetails());
System.out.println("错误: " + unhealthyResult.getError());
```

---

## 完整示例

```java
import ltd.idcu.est.features.monitor.api.*;
import ltd.idcu.est.features.monitor.jvm.*;
import ltd.idcu.est.features.monitor.system.*;

import java.util.Map;

public class CompleteMonitorExample {
    public static void main(String[] args) {
        // 1. JVM 监控
        JvmMonitor jvmMonitor = JvmMonitor.getInstance();
        JvmMetrics jvmMetrics = jvmMonitor.getJvmMetrics();
        System.out.println("=== JVM 信息 ===");
        System.out.println("JVM: " + jvmMonitor.getJvmInfo());
        System.out.println("堆内存: " + jvmMetrics.getHeapMemoryUsed() + "/" + jvmMetrics.getHeapMemoryMax());
        
        // 2. 系统监控
        SystemMonitor systemMonitor = SystemMonitor.getInstance();
        SystemMetrics systemMetrics = systemMonitor.getSystemMetrics();
        System.out.println("\n=== 系统信息 ===");
        System.out.println("OS: " + systemMonitor.getOsInfo());
        System.out.println("CPU: " + systemMetrics.getAvailableProcessors() + " cores");
        
        // 3. 健康检查
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register(new JvmHealthCheck());
        registry.register(new SystemHealthCheck());
        
        System.out.println("\n=== 健康检查 ===");
        Map<String, HealthCheckResult> results = registry.checkAll();
        for (Map.Entry<String, HealthCheckResult> entry : results.entrySet()) {
            HealthCheckResult r = entry.getValue();
            System.out.printf("%s: %s - %s%n", 
                entry.getKey(), r.getStatus().getName(), r.getMessage());
        }
        
        // 4. 聚合状态
        HealthStatus aggregate = registry.getAggregateStatus();
        System.out.println("\n=== 整体状态 ===");
        System.out.println(aggregate.getName() + ": " + aggregate.getMessage());
    }
}
```

---

## 最佳实践

1. **定期健康检查**：结合调度系统定期执行健康检查
2. **监控告警**：当健康检查失败时发送告警通知
3. **聚合多个检查**：使用 HealthCheckRegistry 管理多个健康检查
4. **提供详细信息**：在健康检查结果中包含足够的调试信息
5. **自定义检查**：为关键组件创建自定义健康检查
