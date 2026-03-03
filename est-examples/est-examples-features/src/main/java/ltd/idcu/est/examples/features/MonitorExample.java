package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.monitor.jvm.JvmMonitor;

public class MonitorExample {
    public static void main(String[] args) {
        // 获取JVM监控单例
        var monitor = JvmMonitor.getInstance();
        
        // 获取所有JVM指标
        var metrics = monitor.getAllMetrics();
        
        System.out.println("=== JVM Metrics ===");
        metrics.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        
        // 健康检查
        var healthCheckResult = monitor.checkHealth();
        System.out.println("\n=== Health Check ===");
        System.out.println("Status: " + (healthCheckResult.isHealthy() ? "Healthy" : "Unhealthy"));
        System.out.println("Message: " + healthCheckResult.getMessage());
        
        // JVM信息
        System.out.println("\n=== JVM Info ===");
        System.out.println("JVM: " + monitor.getJvmInfo());
        System.out.println("Uptime: " + monitor.getUptime() + " ms");
    }
}