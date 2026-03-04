package ltd.idcu.est.features.monitor.jvm;

import ltd.idcu.est.features.monitor.api.AbstractMonitor;
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;

import java.lang.management.ManagementFactory;
import java.util.List;

public final class JvmMonitor extends AbstractMonitor {
    
    private static volatile JvmMonitor instance;
    
    private JvmMonitor() {
        super(new JvmMetrics());
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
    
    @Override
    protected void initializeDefaultHealthChecks() {
        addHealthCheck(new JvmHealthCheck());
    }
    
    public JvmMetrics getJvmMetrics() {
        return (JvmMetrics) getMetrics();
    }
    
    @Override
    public HealthCheckResult checkHealth() {
        for (HealthCheck healthCheck : getInternalHealthChecks()) {
            if (healthCheck instanceof JvmHealthCheck jvmHealthCheck) {
                jvmHealthCheck.check();
                HealthCheckResult result = jvmHealthCheck.getLastResult();
                if (result != null && !result.isHealthy()) {
                    return result;
                }
            } else {
                healthCheck.check();
            }
        }
        return HealthCheckResult.healthy("jvm", "All JVM health checks passed");
    }
    
    @Override
    public List<HealthCheckResult> checkAllHealth() {
        return getInternalHealthChecks().stream()
                .map(healthCheck -> {
                    healthCheck.check();
                    if (healthCheck instanceof JvmHealthCheck jvmHealthCheck) {
                        return jvmHealthCheck.getLastResult();
                    }
                    return null;
                })
                .filter(result -> result != null)
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
}
