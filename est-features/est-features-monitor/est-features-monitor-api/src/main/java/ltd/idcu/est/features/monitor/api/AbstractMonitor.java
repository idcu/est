package ltd.idcu.est.features.monitor.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractMonitor {
    
    private final Metrics metrics;
    private final List<HealthCheck> healthChecks;
    
    protected AbstractMonitor(Metrics metrics) {
        this.metrics = metrics;
        this.healthChecks = new CopyOnWriteArrayList<>();
        initializeDefaultHealthChecks();
    }
    
    protected abstract void initializeDefaultHealthChecks();
    
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
    
    protected List<HealthCheck> getInternalHealthChecks() {
        return healthChecks;
    }
    
    public abstract HealthCheckResult checkHealth();
    
    public abstract List<HealthCheckResult> checkAllHealth();
    
    public void reset() {
        metrics.reset();
    }
}
