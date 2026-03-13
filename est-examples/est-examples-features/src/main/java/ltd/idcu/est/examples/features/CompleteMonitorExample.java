package ltd.idcu.est.examples.features;

import ltd.idcu.est.monitor.api.Metrics;
import ltd.idcu.est.monitor.api.HealthCheck;
import ltd.idcu.est.monitor.api.HealthCheckResult;
import ltd.idcu.est.monitor.api.HealthStatus;
import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.system.SystemMonitor;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.Map;

public class CompleteMonitorExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteMonitorExample.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST Monitor Module - Complete Example");
        System.out.println("=".repeat(70));
        System.out.println("\nThis example demonstrates various features of the EST Monitor module:");
        System.out.println("  - JVM Monitoring (Memory, CPU, Threads)");
        System.out.println("  - System Monitoring (Disk, Network)");
        System.out.println("  - Health Checks (Check if system is healthy)");
        System.out.println("  - Custom Monitoring Metrics");
        System.out.println("  - Integration with Alerting Systems");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("Part 1: Understanding the Role of Monitoring");
        System.out.println("=".repeat(70));
        System.out.println("\n[Why Do We Need a Monitoring System?]");
        System.out.println("  - Real-time understanding of application status");
        System.out.println("  - Early detection of issues, prevent problems before they occur");
        System.out.println("  - Performance analysis and optimization");
        System.out.println("  - Troubleshooting and diagnostics\n");
        
        jvmMonitorExample();
        systemMonitorExample();
        healthCheckExample();
        customMetricsExample();
        alertIntegrationExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("[X] All monitoring system examples completed!");
        System.out.println("=".repeat(70));
    }
    
    private static void jvmMonitorExample() {
        System.out.println("\n--- Approach 1: JVM Monitoring ---");
        System.out.println("\n[What Does JVM Monitoring Cover?]");
        System.out.println("  - Heap memory usage");
        System.out.println("  - Non-heap memory usage");
        System.out.println("  - Thread count");
        System.out.println("  - GC (Garbage Collection) status");
        System.out.println("  - JVM uptime\n");
        
        System.out.println("[Why Monitor JVM?]");
        System.out.println("  - Memory leaks can cause OOM (Out Of Memory)");
        System.out.println("  - Too many threads can slow down the system");
        System.out.println("  - Frequent GC affects performance\n");
        
        JvmMonitor jvmMonitor = JvmMonitor.getInstance();
        
        System.out.println("Step 1: Get all JVM metrics");
        Map<String, Object> allMetrics = jvmMonitor.getAllMetrics();
        System.out.println("   All JVM metrics:");
        for (Map.Entry<String, Object> entry : allMetrics.entrySet()) {
            System.out.println("     - " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\nStep 2: Get JVM info");
        String jvmInfo = jvmMonitor.getJvmInfo();
        System.out.println("   JVM info: " + jvmInfo);
        
        System.out.println("\nStep 3: Get uptime");
        long uptime = jvmMonitor.getUptime();
        System.out.println("   JVM has been running: " + uptime + " ms");
        System.out.println("   Approximately: " + (uptime / 1000) + " seconds");
        
        System.out.println("\nStep 4: Key metrics interpretation");
        System.out.println("   [Memory] Heap: If usage remains above 80%, possible memory leak");
        System.out.println("   [Threads] Thread count: If growing continuously, possible thread leak");
        System.out.println("   [GC] GC frequency: If frequent Full GC, performance will degrade");
        
        logger.info("JVM monitoring example completed");
    }
    
    private static void systemMonitorExample() {
        System.out.println("\n--- Approach 2: System Monitoring ---");
        System.out.println("\n[What Does System Monitoring Cover?]");
        System.out.println("  - CPU usage");
        System.out.println("  - Memory usage");
        System.out.println("  - Disk space");
        System.out.println("  - Network traffic");
        System.out.println("  - System load\n");
        
        System.out.println("[Why Monitor the System?]");
        System.out.println("  - Full disk can crash applications");
        System.out.println("  - 100% CPU can freeze the system");
        System.out.println("  - Insufficient memory can cause OOM\n");
        
        SystemMonitor systemMonitor = SystemMonitor.getInstance();
        
        System.out.println("Step 1: Get system metrics");
        Map<String, Object> systemMetrics = systemMonitor.getAllMetrics();
        System.out.println("   System metrics:");
        for (Map.Entry<String, Object> entry : systemMetrics.entrySet()) {
            System.out.println("     - " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\nStep 2: Practical application scenarios");
        System.out.println("   [Monitor] Disk space:");
        System.out.println("      If usage > 90%, send alert");
        System.out.println("      Auto-clean old log files");
        System.out.println();
        System.out.println("   [Monitor] CPU usage:");
        System.out.println("      If continuously > 80%, analyze which process is using it");
        System.out.println("      May need to scale up or optimize code");
        System.out.println();
        System.out.println("   [Monitor] Memory usage:");
        System.out.println("      If continuously > 90%, check for memory leaks");
        System.out.println("      May need to increase server memory");
        
        logger.info("System monitoring example completed");
    }
    
    private static void healthCheckExample() {
        System.out.println("\n--- Approach 3: Health Checks ---");
        System.out.println("\n[What is a Health Check?]");
        System.out.println("  - Checks if various system components are working properly");
        System.out.println("  - Returns healthy or unhealthy status");
        System.out.println("  - Can include detailed error messages\n");
        
        System.out.println("[Use Cases for Health Checks]");
        System.out.println("  - Load balancer: Only forward traffic to healthy nodes");
        System.out.println("  - Container orchestration: Auto-restart unhealthy containers");
        System.out.println("  - Alerting system: Notify admins immediately when unhealthy\n");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        System.out.println("Step 1: Perform health check");
        HealthCheckResult healthResult = monitor.checkHealth();
        
        System.out.println("\nStep 2: Check health status");
        HealthStatus status = healthResult.getHealthStatus();
        System.out.println("   Health status: " + status);
        System.out.println("   Status message: " + healthResult.getMessage());
        
        System.out.println("\nStep 3: Health status interpretation");
        if (status == HealthStatus.HEALTHY) {
            System.out.println("   [X] System healthy, everything normal!");
        } else if (status == HealthStatus.DEGRADED) {
            System.out.println("   [!] System performance degraded, but still working");
            System.out.println("   Suggestion: Check what's causing it");
        } else {
            System.out.println("   [X] System unhealthy, needs immediate attention!");
            System.out.println("   Suggestion: Check logs, find the cause");
        }
        
        System.out.println("\nStep 4: Custom health check example");
        System.out.println("   Database health check:");
        System.out.println("     1. Try to connect to database");
        System.out.println("     2. Execute simple query (SELECT 1)");
        System.out.println("     3. If successful, return HEALTHY");
        System.out.println("     4. If failed, return UNHEALTHY");
        System.out.println();
        System.out.println("   Redis health check:");
        System.out.println("     1. Try to connect to Redis");
        System.out.println("     2. Execute PING command");
        System.out.println("     3. If returns PONG, return HEALTHY");
        System.out.println("     4. Otherwise return UNHEALTHY");
        
        logger.info("Health check example completed");
    }
    
    private static void customMetricsExample() {
        System.out.println("\n--- Approach 4: Custom Monitoring Metrics ---");
        System.out.println("\n[Why Need Custom Metrics?]");
        System.out.println("  - JVM and system metrics are generic");
        System.out.println("  - You may need to monitor business-related metrics");
        System.out.println("  - Examples: Orders, users, conversion rate, etc.\n");
        
        System.out.println("[Common Business Metrics]");
        System.out.println("   [E-commerce Website]");
        System.out.println("      - Orders per minute");
        System.out.println("      - Transaction volume per minute");
        System.out.println("      - Cart conversion rate");
        System.out.println();
        System.out.println("   [Social Application]");
        System.out.println("      - New users per minute");
        System.out.println("      - Messages per minute");
        System.out.println("      - Daily active users");
        System.out.println();
        System.out.println("   [Gaming Application]");
        System.out.println("      - Online players");
        System.out.println("      - Games per minute");
        System.out.println("      - Recharge amount\n");
        
        System.out.println("[How to Use Custom Metrics]");
        System.out.println("   1. Define metric name and type (counter, gauge, etc.)");
        System.out.println("   2. Update metrics in code");
        System.out.println("   3. Monitoring system collects metrics");
        System.out.println("   4. Display on monitoring dashboard");
        System.out.println("   5. Set alert rules\n");
        
        System.out.println("[Example: Order Counter]");
        System.out.println("   ```java");
        System.out.println("   // Define order counter");
        System.out.println("   private static AtomicLong orderCount = new AtomicLong(0);");
        System.out.println();
        System.out.println("   // Increment for each order created");
        System.out.println("   public void createOrder(Order order) {");
        System.out.println("       // ... save order logic");
        System.out.println("       orderCount.incrementAndGet();");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // Monitoring system gets metric");
        System.out.println("   public long getOrderCount() {");
        System.out.println("       return orderCount.get();");
        System.out.println("   }");
        System.out.println("   ```");
        
        logger.info("Custom monitoring metrics example completed");
    }
    
    private static void alertIntegrationExample() {
        System.out.println("\n--- Approach 5: Integration with Alerting Systems ---");
        System.out.println("\n[Why Need Alerts?]");
        System.out.println("  - Monitoring just shows, alerts notify you");
        System.out.println("  - Can't watch monitoring dashboard 24/7");
        System.out.println("  - Need to know immediately when issues occur\n");
        
        System.out.println("[Common Alert Methods]");
        System.out.println("   [Email] Alerts: Suitable for non-urgent notifications");
        System.out.println("   [SMS] Alerts: Suitable for important notifications");
        System.out.println("   [Slack/Teams] Alerts: Common for team collaboration");
        System.out.println("   [Phone] Alerts: For very urgent situations\n");
        
        System.out.println("[Alert Rule Examples]");
        System.out.println("   Rule 1: CPU usage > 90% for 5 minutes");
        System.out.println("   Action: Send email notification to ops");
        System.out.println();
        System.out.println("   Rule 2: Memory usage > 95%");
        System.out.println("   Action: Send SMS to on-call personnel");
        System.out.println();
        System.out.println("   Rule 3: Disk space < 10%");
        System.out.println("   Action: Slack group alert + email");
        System.out.println();
        System.out.println("   Rule 4: Health check failed");
        System.out.println("   Action: Immediate phone call to person in charge\n");
        
        System.out.println("[Alert Severity Levels]");
        System.out.println("   P0 (Critical): System unavailable, immediate action");
        System.out.println("   P1 (High): Severe impact on functionality, handle ASAP");
        System.out.println("   P2 (Medium): Functionality affected, handle during work hours");
        System.out.println("   P3 (Low): Suggest optimization, handle when free\n");
        
        System.out.println("[Best Practices]");
        System.out.println("   1. Avoid alert storms: Don't send alerts every second");
        System.out.println("   2. Set quiet period: Only send once for same issue");
        System.out.println("   3. Tiered handling: Different levels, different methods");
        System.out.println("   4. Alerts must be handled: Each alert needs a response");
        System.out.println("   5. Regular review: Which alerts are useless, delete them");
        
        logger.info("Alert integration example completed");
    }
}
