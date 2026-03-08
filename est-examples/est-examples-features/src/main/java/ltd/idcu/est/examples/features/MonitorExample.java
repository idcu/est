package ltd.idcu.est.examples.features;

import ltd.idcu.est.monitor.api.*;
import ltd.idcu.est.monitor.jvm.JvmHealthCheck;
import ltd.idcu.est.monitor.jvm.JvmMetrics;
import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.system.SystemHealthCheck;
import ltd.idcu.est.monitor.system.SystemMetrics;
import ltd.idcu.est.monitor.system.SystemMonitor;

import java.util.Map;

public class MonitorExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Framework Monitor Example ===\n");

        System.out.println("--- 1. JVM Monitor 示例 ---\n");
        JvmMonitor jvmMonitor = JvmMonitor.getInstance();
        JvmMetrics jvmMetrics = jvmMonitor.getJvmMetrics();
        
        System.out.println("JVM 信息: " + jvmMonitor.getJvmInfo());
        System.out.println("JVM 运行时间: " + jvmMonitor.getUptime() + "ms");
        System.out.println("堆内存使�? " + jvmMetrics.getHeapMemoryUsed() + " / " + jvmMetrics.getHeapMemoryMax());
        System.out.println("非堆内存使用: " + jvmMetrics.getNonHeapMemoryUsed());
        System.out.println("活动线程�? " + jvmMetrics.getThreadCount());
        System.out.println("GC 次数: " + jvmMetrics.getGcCount());
        System.out.println();

        System.out.println("--- 2. System Monitor 示例 ---\n");
        SystemMonitor systemMonitor = SystemMonitor.getInstance();
        SystemMetrics systemMetrics = systemMonitor.getSystemMetrics();
        
        System.out.println("操作系统信息: " + systemMonitor.getOsInfo());
        System.out.println("CPU 核心�? " + systemMetrics.getAvailableProcessors());
        System.out.println("系统负载: " + systemMetrics.getSystemLoadAverage());
        System.out.println("可用内存: " + systemMetrics.getFreeMemory());
        System.out.println("总内�? " + systemMetrics.getTotalMemory());
        System.out.println();

        System.out.println("--- 3. Health Check Registry 示例 ---\n");
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        
        healthCheckRegistry.register(new JvmHealthCheck());
        healthCheckRegistry.register(new SystemHealthCheck());
        
        healthCheckRegistry.register(new HealthCheck() {
            @Override
            public HealthStatus check() {
                return HealthStatus.healthy("自定义检查通过");
            }
            
            @Override
            public HealthStatus getStatus() {
                return HealthStatus.healthy("状态正�?);
            }
            
            @Override
            public String getName() {
                return "custom-check";
            }
            
            @Override
            public String getDescription() {
                return "自定义健康检�?;
            }
        });
        
        System.out.println("已注册的健康检�? " + healthCheckRegistry.getNames());
        System.out.println();

        System.out.println("--- 4. 执行健康检�?---\n");
        Map<String, HealthCheckResult> results = healthCheckRegistry.checkAll();
        for (Map.Entry<String, HealthCheckResult> entry : results.entrySet()) {
            HealthCheckResult result = entry.getValue();
            System.out.printf("[%s] %s - %s%n", 
                entry.getKey(), 
                result.getStatus().getName(), 
                result.getMessage());
        }
        System.out.println();

        System.out.println("--- 5. 聚合健康状�?---\n");
        HealthStatus aggregateStatus = healthCheckRegistry.getAggregateStatus();
        System.out.println("整体状�? " + aggregateStatus.getName());
        System.out.println("状态消�? " + aggregateStatus.getMessage());
        System.out.println();

        System.out.println("--- 6. 健康检�?JSON 格式 ---\n");
        Map<String, Object> healthMap = healthCheckRegistry.toMap();
        System.out.println(healthMap);
        System.out.println();

        System.out.println("--- 7. JVM 健康检�?---\n");
        HealthCheckResult jvmHealthResult = jvmMonitor.checkHealth();
        System.out.println("JVM 健康状�? " + jvmHealthResult.getStatus().getName());
        System.out.println("JVM 健康消息: " + jvmHealthResult.getMessage());
        System.out.println();

        System.out.println("--- 8. 系统健康检�?---\n");
        HealthCheckResult systemHealthResult = systemMonitor.checkHealth();
        System.out.println("系统健康状�? " + systemHealthResult.getStatus().getName());
        System.out.println("系统健康消息: " + systemHealthResult.getMessage());
        System.out.println();

        System.out.println("=== Example Complete ===");
    }
}
