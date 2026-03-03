package ltd.idcu.est.features.monitor.jvm;

import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.Metrics;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class JvmMonitor {
    
    private static volatile JvmMonitor instance;
    
    private final JvmMetrics metrics;
    private final List<HealthCheck> healthChecks;
    
    private JvmMonitor() {
        this.metrics = new JvmMetrics();
        this.healthChecks = new CopyOnWriteArrayList<>();
        this.healthChecks.add(new JvmHealthCheck());
    }
    
    public static JvmMonitor getInstance() {
        if (instance == null) {
            synchronized (JvmMonitor.class) {
                if (instance == null) {
                    instance = new JvmMonitor();
                }
            }
        }
        return instance;
    }
    
    public Metrics getMetrics() {
        return metrics;
    }
    
    public Object getMetric(String name) {
        return metrics.getMetric(name);
    }
    
    public Map<String, Object> getAllMetrics() {
        return metrics.getAllMetrics();
    }
    
    public void registerMetric(String name, Object value) {
        metrics.registerMetric(name, value);
    }
    
    public void unregisterMetric(String name) {
        metrics.unregisterMetric(name);
    }
    
    public void addHealthCheck(HealthCheck healthCheck) {
        healthChecks.add(healthCheck);
    }
    
    public void removeHealthCheck(HealthCheck healthCheck) {
        healthChecks.remove(healthCheck);
    }
    
    public List<HealthCheck> getHealthChecks() {
        return List.copyOf(healthChecks);
    }
    
    public HealthCheckResult checkHealth() {
        for (HealthCheck healthCheck : healthChecks) {
            HealthCheckResult result = healthCheck.check();
            if (!result.isHealthy()) {
                return result;
            }
        }
        return HealthCheckResult.healthy("jvm", "All JVM health checks passed");
    }
    
    public List<HealthCheckResult> checkAllHealth() {
        return healthChecks.stream()
                .map(HealthCheck::check)
                .toList();
    }
    
    public long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }
    
    public String getJvmInfo() {
        return String.format("%s (%s)", 
                ManagementFactory.getRuntimeMXBean().getVmName(),
                ManagementFactory.getRuntimeMXBean().getVmVersion());
    }
    
    public void reset() {
        metrics.reset();
    }
}
