package ltd.idcu.est.features.monitor.system;

import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.Metrics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class SystemMonitor {
    
    private static volatile SystemMonitor instance;
    
    private final SystemMetrics metrics;
    private final List<HealthCheck> healthChecks;
    
    private SystemMonitor() {
        this.metrics = new SystemMetrics();
        this.healthChecks = new CopyOnWriteArrayList<>();
        this.healthChecks.add(new SystemHealthCheck());
    }
    
    public static SystemMonitor getInstance() {
        if (instance == null) {
            synchronized (SystemMonitor.class) {
                if (instance == null) {
                    instance = new SystemMonitor();
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
            if (healthCheck instanceof SystemHealthCheck systemHealthCheck) {
                systemHealthCheck.check();
                HealthCheckResult result = systemHealthCheck.getLastResult();
                if (result != null && !result.isHealthy()) {
                    return result;
                }
            } else {
                healthCheck.check();
            }
        }
        return HealthCheckResult.healthy("system", "All system health checks passed");
    }
    
    public List<HealthCheckResult> checkAllHealth() {
        return healthChecks.stream()
                .map(healthCheck -> {
                    healthCheck.check();
                    if (healthCheck instanceof SystemHealthCheck systemHealthCheck) {
                        return systemHealthCheck.getLastResult();
                    }
                    return null;
                })
                .filter(result -> result != null)
                .toList();
    }
    
    public String getOsInfo() {
        return String.format("%s %s (%s)", 
                metrics.getOsName(),
                metrics.getOsVersion(),
                metrics.getOsArch());
    }
    
    public void reset() {
        metrics.reset();
    }
}
