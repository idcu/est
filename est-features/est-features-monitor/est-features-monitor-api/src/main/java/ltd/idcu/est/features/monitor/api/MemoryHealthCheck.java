package ltd.idcu.est.features.monitor.api;

import java.util.HashMap;
import java.util.Map;

public class MemoryHealthCheck implements HealthCheck {
    private final double maxMemoryUsagePercent;

    public MemoryHealthCheck() {
        this(90.0);
    }

    public MemoryHealthCheck(double maxMemoryUsagePercent) {
        this.maxMemoryUsagePercent = maxMemoryUsagePercent;
    }

    @Override
    public HealthStatus check() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        double usagePercent = (double) usedMemory / maxMemory * 100;

        Map<String, Object> details = new HashMap<>();
        details.put("maxMemory", maxMemory);
        details.put("totalMemory", totalMemory);
        details.put("freeMemory", freeMemory);
        details.put("usedMemory", usedMemory);
        details.put("usagePercent", usagePercent);

        if (usagePercent > maxMemoryUsagePercent) {
            return HealthStatus.unhealthy("Memory usage exceeds " + maxMemoryUsagePercent + "%", details);
        } else if (usagePercent > maxMemoryUsagePercent * 0.8) {
            return HealthStatus.degraded("Memory usage is high: " + String.format("%.1f", usagePercent) + "%", details);
        } else {
            return HealthStatus.healthy("Memory usage is normal: " + String.format("%.1f", usagePercent) + "%", details);
        }
    }

    @Override
    public HealthStatus getStatus() {
        return check();
    }

    @Override
    public String getName() {
        return "memory";
    }

    @Override
    public String getDescription() {
        return "JVM memory usage health check";
    }
}
