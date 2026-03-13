package ltd.idcu.est.examples.features;

import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.api.HealthCheckResult;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.Map;

public class SimpleMonitorExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleMonitorExample.class);
    
    public static void main(String[] args) {
        System.out.println("=== EST Monitor System Example ===");
        
        jvmMonitorExample();
        healthCheckExample();
        
        System.out.println("\n[X] All examples complete!");
    }
    
    private static void jvmMonitorExample() {
        System.out.println("\n--- JVM Monitoring ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        Map<String, Object> metrics = monitor.getAllMetrics();
        System.out.println("  JVM metrics:");
        int count = 0;
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            if (count++ < 5) {
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("    ... more metrics omitted");
        
        System.out.println("  JVM info: " + monitor.getJvmInfo());
        System.out.println("  Uptime: " + monitor.getUptime() + "ms");
        
        logger.info("JVM monitoring example complete");
    }
    
    private static void healthCheckExample() {
        System.out.println("\n--- Health Check ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        HealthCheckResult result = monitor.checkHealth();
        
        System.out.println("  Health status: " + result.getHealthStatus());
        System.out.println("  Status message: " + result.getMessage());
        
        logger.info("Health check example complete");
    }
}
