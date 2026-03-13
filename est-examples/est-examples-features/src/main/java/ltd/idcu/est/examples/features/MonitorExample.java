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

        System.out.println("--- 1. JVM Monitor Example ---\n");
        JvmMonitor jvmMonitor = JvmMonitor.getInstance();
        JvmMetrics jvmMetrics = jvmMonitor.getJvmMetrics();
        
        System.out.println("JVM Info: " + jvmMonitor.getJvmInfo());
        System.out.println("JVM Uptime: " + jvmMonitor.getUptime() + "ms");
        System.out.println("Heap Memory Used: " + jvmMetrics.getHeapMemoryUsed() + " / " + jvmMetrics.getHeapMemoryMax());
        System.out.println("Non-Heap Memory Used: " + jvmMetrics.getNonHeapMemoryUsed());
        System.out.println("Active Threads: " + jvmMetrics.getThreadCount());
        System.out.println("GC Count: " + jvmMetrics.getGcCount());
        System.out.println();

        System.out.println("--- 2. System Monitor Example ---\n");
        SystemMonitor systemMonitor = SystemMonitor.getInstance();
        SystemMetrics systemMetrics = systemMonitor.getSystemMetrics();
        
        System.out.println("OS Info: " + systemMonitor.getOsInfo());
        System.out.println("CPU Cores: " + systemMetrics.getAvailableProcessors());
        System.out.println("System Load: " + systemMetrics.getSystemLoadAverage());
        System.out.println("Free Memory: " + systemMetrics.getFreeMemory());
        System.out.println("Total Memory: " + systemMetrics.getTotalMemory());
        System.out.println();

        System.out.println("--- 3. Health Check Registry Example ---\n");
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        
        healthCheckRegistry.register(new JvmHealthCheck());
        healthCheckRegistry.register(new SystemHealthCheck());
        
        healthCheckRegistry.register(new HealthCheck() {
            @Override
            public HealthStatus check() {
                return HealthStatus.healthy("Custom check passed");
            }
            
            @Override
            public HealthStatus getStatus() {
                return HealthStatus.healthy("Status OK");
            }
            
            @Override
            public String getName() {
                return "custom-check";
            }
            
            @Override
            public String getDescription() {
                return "Custom health check";
            }
        });
        
        System.out.println("Registered health checks: " + healthCheckRegistry.getNames());
        System.out.println();

        System.out.println("--- 4. Execute Health Checks ---\n");
        Map<String, HealthCheckResult> results = healthCheckRegistry.checkAll();
        for (Map.Entry<String, HealthCheckResult> entry : results.entrySet()) {
            HealthCheckResult result = entry.getValue();
            System.out.printf("[%s] %s - %s%n", 
                entry.getKey(), 
                result.getStatus().getName(), 
                result.getMessage());
        }
        System.out.println();

        System.out.println("--- 5. Aggregate Health Status ---\n");
        HealthStatus aggregateStatus = healthCheckRegistry.getAggregateStatus();
        System.out.println("Overall status: " + aggregateStatus.getName());
        System.out.println("Status message: " + aggregateStatus.getMessage());
        System.out.println();

        System.out.println("--- 6. Health Check JSON Format ---\n");
        Map<String, Object> healthMap = healthCheckRegistry.toMap();
        System.out.println(healthMap);
        System.out.println();

        System.out.println("--- 7. JVM Health Check ---\n");
        HealthCheckResult jvmHealthResult = jvmMonitor.checkHealth();
        System.out.println("JVM Health Status: " + jvmHealthResult.getStatus().getName());
        System.out.println("JVM Health Message: " + jvmHealthResult.getMessage());
        System.out.println();

        System.out.println("--- 8. System Health Check ---\n");
        HealthCheckResult systemHealthResult = systemMonitor.checkHealth();
        System.out.println("System Health Status: " + systemHealthResult.getStatus().getName());
        System.out.println("System Health Message: " + systemHealthResult.getMessage());
        System.out.println();

        System.out.println("=== Example Complete ===");
    }
}
