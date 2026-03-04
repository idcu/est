# Monitor 监控系统 API

监控系统提供JVM监控、系统监控和健康检查功能。

## JVM 监控 (JvmMonitor)

```java
import ltd.idcu.est.features.monitor.api.JvmMonitor;
import ltd.idcu.est.features.monitor.jvm.JvmMonitors;

// 创建JVM监控
JvmMonitor jvmMonitor = JvmMonitors.create();

// 获取内存信息
MemoryInfo memory = jvmMonitor.getMemoryInfo();
System.out.println("Heap used: " + memory.getHeapUsed() + " / " + memory.getHeapMax());
System.out.println("Non-heap used: " + memory.getNonHeapUsed());

// 获取GC信息
List<GcInfo> gcInfos = jvmMonitor.getGcInfo();
for (GcInfo gc : gcInfos) {
    System.out.println(gc.getName() + ": " + gc.getCollectionCount() + " collections");
}

// 获取线程信息
ThreadInfo threadInfo = jvmMonitor.getThreadInfo();
System.out.println("Live threads: " + threadInfo.getLiveThreadCount());
System.out.println("Peak threads: " + threadInfo.getPeakThreadCount());

// 获取类加载信息
ClassLoadingInfo classLoading = jvmMonitor.getClassLoadingInfo();
System.out.println("Loaded classes: " + classLoading.getLoadedClassCount());

// 获取JVM概要
JvmSummary summary = jvmMonitor.getSummary();
System.out.println("Java version: " + summary.getJavaVersion());
System.out.println("Uptime: " + summary.getUptimeMs() + "ms");
```

## 系统监控 (SystemMonitor)

```java
import ltd.idcu.est.features.monitor.api.SystemMonitor;
import ltd.idcu.est.features.monitor.system.SystemMonitors;

// 创建系统监控
SystemMonitor systemMonitor = SystemMonitors.create();

// 获取CPU信息
CpuInfo cpu = systemMonitor.getCpuInfo();
System.out.println("Processors: " + cpu.getAvailableProcessors());
System.out.println("System load: " + cpu.getSystemLoadAverage());
System.out.println("Process CPU: " + cpu.getProcessCpuLoad());

// 获取内存信息
SystemMemoryInfo systemMemory = systemMonitor.getMemoryInfo();
System.out.println("Total memory: " + systemMemory.getTotalMemory());
System.out.println("Free memory: " + systemMemory.getFreeMemory());
System.out.println("Used memory: " + systemMemory.getUsedMemory());

// 获取磁盘信息
List<DiskInfo> disks = systemMonitor.getDiskInfo();
for (DiskInfo disk : disks) {
    System.out.println(disk.getPath() + ": " + disk.getUsed() + "/" + disk.getTotal());
}

// 获取网络信息
List<NetworkInfo> networks = systemMonitor.getNetworkInfo();
for (NetworkInfo network : networks) {
    System.out.println(network.getName() + ": " + network.getIpAddress());
}

// 获取操作系统信息
OsInfo os = systemMonitor.getOsInfo();
System.out.println("OS: " + os.getName() + " " + os.getVersion());
System.out.println("Arch: " + os.getArch());
```

## 健康检查 (HealthCheck)

```java
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthStatus;

// 创建健康检查
HealthCheck healthCheck = HealthCheck.create();

// 添加检查
healthCheck.addCheck("database", () -> {
    try {
        // 检查数据库连接
        return HealthStatus.up("Database connected");
    } catch (Exception e) {
        return HealthStatus.down("Database connection failed: " + e.getMessage());
    }
});

healthCheck.addCheck("redis", () -> {
    // 检查Redis
    return HealthStatus.up("Redis connected");
});

// 执行健康检查
HealthCheckResult result = healthCheck.check();
System.out.println("Overall status: " + result.getStatus());

// 检查各个组件
for (ComponentHealth component : result.getComponents()) {
    System.out.println(component.getName() + ": " + component.getStatus());
}
```

## 内置健康检查

```java
import ltd.idcu.est.features.monitor.health.BuiltInHealthChecks;

// JVM健康检查
healthCheck.addCheck("jvm", BuiltInHealthChecks.jvm());

// 内存健康检查
healthCheck.addCheck("memory", BuiltInHealthChecks.memory(0.9)); // 90%阈值

// 磁盘健康检查
healthCheck.addCheck("disk", BuiltInHealthChecks.disk(0.9));

// CPU健康检查
healthCheck.addCheck("cpu", BuiltInHealthChecks.cpu(0.8));
```

## 监控聚合

```java
import ltd.idcu.est.features.monitor.api.Monitor;

// 创建聚合监控
Monitor monitor = Monitor.create(jvmMonitor, systemMonitor, healthCheck);

// 获取完整快照
MonitorSnapshot snapshot = monitor.getSnapshot();

// 导出为JSON
String json = snapshot.toJson();

// 导出为Prometheus格式
String prometheus = snapshot.toPrometheus();
```

## Web 端点

```java
import ltd.idcu.est.web.WebApplication;
import ltd.idcu.est.features.monitor.web.MonitorEndpoints;

// 注册监控端点
MonitorEndpoints.register(app, monitor);

// 访问端点：
// /health - 健康检查
// /metrics - 指标
// /jvm - JVM信息
// /system - 系统信息
```

## 告警

```java
monitor.addAlert(Alert.builder()
    .name("high-memory")
    .condition(snapshot -> snapshot.getJvm().getMemoryUsed() > 0.9 * snapshot.getJvm().getMemoryMax())
    .action(() -> {
        System.err.println("High memory usage detected!");
        // 发送告警通知
    })
    .build());
```
