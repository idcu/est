package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.monitor.jvm.JvmMonitor;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.Map;

public class SimpleMonitorExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleMonitorExample.class);
    
    public static void main(String[] args) {
        System.out.println("=== EST 监控系统示例 ===");
        
        jvmMonitorExample();
        healthCheckExample();
        
        System.out.println("\n✓ 所有示例完成！");
    }
    
    private static void jvmMonitorExample() {
        System.out.println("\n--- JVM 监控 ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        Map<String, Object> metrics = monitor.getAllMetrics();
        System.out.println("  JVM 指标:");
        int count = 0;
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            if (count++ < 5) {
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("    ... 更多指标省略");
        
        System.out.println("  JVM 信息: " + monitor.getJvmInfo());
        System.out.println("  运行时间: " + monitor.getUptime() + "ms");
        
        logger.info("JVM 监控示例完成");
    }
    
    private static void healthCheckExample() {
        System.out.println("\n--- 健康检查 ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        HealthCheckResult result = monitor.checkHealth();
        
        System.out.println("  健康状态: " + result.getHealthStatus());
        System.out.println("  状态消息: " + result.getMessage());
        
        logger.info("健康检查示例完成");
    }
}
