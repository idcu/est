package ltd.idcu.est.features.monitor.system;

import ltd.idcu.est.features.monitor.api.AbstractMonitor;
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.Metrics;

import java.util.List;

public final class SystemMonitor extends AbstractMonitor {
    
    private static volatile SystemMonitor instance;
    
    private SystemMonitor() {
        super(new SystemMetrics());
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
    
    @Override
    protected void initializeDefaultHealthChecks() {
        addHealthCheck(new SystemHealthCheck());
    }
    
    public SystemMetrics getSystemMetrics() {
        return (SystemMetrics) getMetrics();
    }
    
    @Override
    public HealthCheckResult checkHealth() {
        for (HealthCheck healthCheck : getInternalHealthChecks()) {
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
    
    @Override
    public List<HealthCheckResult> checkAllHealth() {
        return getInternalHealthChecks().stream()
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
        SystemMetrics metrics = getSystemMetrics();
        return String.format("%s %s (%s)", 
                metrics.getOsName(),
                metrics.getOsVersion(),
                metrics.getOsArch());
    }
}
