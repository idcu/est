package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.Metric;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;
import ltd.idcu.est.features.monitor.system.SystemMonitor;

import java.util.List;
import java.util.Map;

public class EnhancedMonitorExample {
    
    public static void main(String[] args) {
        System.out.println("\n=== Enhanced Monitor Examples ===");
        
        jvmMonitorExample();
        systemMonitorExample();
        combinedHealthCheckExample();
    }
    
    private static void jvmMonitorExample() {
        System.out.println("\n--- JVM Monitor Example ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        System.out.println("JVM Info: " + monitor.getJvmInfo());
        System.out.println("Uptime: " + monitor.getUptime() + "ms");
        
        Map<String, Object> metrics = monitor.getAllMetrics();
        System.out.println("\nJVM Metrics:");
        metrics.forEach((key, value) -> {
            System.out.println("  " + key + ": " + value);
        });
        
        Object heapMemory = monitor.getMetric("jvm.memory.heap.used");
        System.out.println("\nHeap Memory Used: " + heapMemory);
        
        monitor.registerMetric("custom.metric", 42);
        System.out.println("\nCustom Metric registered: custom.metric = " + monitor.getMetric("custom.metric"));
        
        monitor.unregisterMetric("custom.metric");
    }
    
    private static void systemMonitorExample() {
        System.out.println("\n--- System Monitor Example ---");
        
        SystemMonitor monitor = SystemMonitor.getInstance();
        
        Map<String, Object> systemMetrics = monitor.getAllMetrics();
        System.out.println("System Metrics:");
        systemMetrics.forEach((key, value) -> {
            System.out.println("  " + key + ": " + value);
        });
        
        Object cpuLoad = monitor.getMetric("system.cpu.load");
        Object availableMemory = monitor.getMetric("system.memory.available");
        System.out.println("\nCPU Load: " + cpuLoad);
        System.out.println("Available Memory: " + availableMemory);
    }
    
    private static void combinedHealthCheckExample() {
        System.out.println("\n--- Combined Health Check Example ---");
        
        JvmMonitor jvmMonitor = JvmMonitor.getInstance();
        SystemMonitor systemMonitor = SystemMonitor.getInstance();
        
        HealthCheckResult jvmHealth = jvmMonitor.checkHealth();
        System.out.println("JVM Health: " + jvmHealth.getStatus());
        System.out.println("JVM Health Message: " + jvmHealth.getMessage());
        
        List<HealthCheckResult> jvmHealthDetails = jvmMonitor.checkAllHealth();
        System.out.println("\nJVM Health Details:");
        jvmHealthDetails.forEach(result -> {
            System.out.println("  " + result.getName() + ": " + result.getStatus() + " - " + result.getMessage());
        });
        
        HealthCheckResult systemHealth = systemMonitor.checkHealth();
        System.out.println("\nSystem Health: " + systemHealth.getStatus());
        System.out.println("System Health Message: " + systemHealth.getMessage());
        
        boolean overallHealthy = jvmHealth.isHealthy() && systemHealth.isHealthy();
        System.out.println("\nOverall System Healthy: " + overallHealthy);
    }
}
