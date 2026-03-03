package ltd.idcu.est.features.monitor.jvm;

import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;

import java.lang.management.*;
import java.util.HashMap;
import java.util.Map;

public class JvmHealthCheck implements HealthCheck {
    
    private final String name;
    private final String description;
    private final double memoryThreshold;
    private final double cpuThreshold;
    private volatile HealthCheckResult lastResult;
    
    public JvmHealthCheck() {
        this("jvm", "JVM Health Check", 0.9, 0.9);
    }
    
    public JvmHealthCheck(String name, String description, 
                         double memoryThreshold, double cpuThreshold) {
        this.name = name;
        this.description = description;
        this.memoryThreshold = memoryThreshold;
        this.cpuThreshold = cpuThreshold;
        this.lastResult = null;
    }
    
    @Override
    public HealthStatus check() {
        Map<String, Object> details = new HashMap<>();
        
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        long heapUsed = heapUsage.getUsed();
        long heapMax = heapUsage.getMax();
        double heapUsagePercent = heapMax > 0 ? (double) heapUsed / heapMax : 0;
        
        details.put("heap.used", formatBytes(heapUsed));
        details.put("heap.max", formatBytes(heapMax));
        details.put("heap.usage", String.format("%.2f%%", heapUsagePercent * 100));
        
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int threadCount = threadMXBean.getThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        
        details.put("thread.count", threadCount);
        details.put("thread.peak", peakThreadCount);
        details.put("thread.daemon", daemonThreadCount);
        
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        details.put("class.loaded", classLoadingMXBean.getLoadedClassCount());
        
        double cpuLoad = getCpuLoad();
        if (cpuLoad >= 0) {
            details.put("cpu.load", String.format("%.2f%%", cpuLoad * 100));
        }
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        details.put("memory.total", formatBytes(totalMemory));
        details.put("memory.free", formatBytes(freeMemory));
        details.put("memory.used", formatBytes(usedMemory));
        
        HealthStatus status = determineHealthStatus(heapUsagePercent, cpuLoad);
        String message = buildHealthMessage(status, heapUsagePercent, cpuLoad);
        
        lastResult = new HealthCheckResult(status, name, message, details);
        return status;
    }
    
    @Override
    public HealthStatus getStatus() {
        return lastResult != null ? lastResult.getStatus() : HealthStatus.UNKNOWN;
    }
    
    public HealthCheckResult getLastResult() {
        return lastResult;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    private HealthStatus determineHealthStatus(double heapUsagePercent, double cpuLoad) {
        boolean memoryCritical = heapUsagePercent >= memoryThreshold;
        boolean cpuCritical = cpuLoad >= 0 && cpuLoad >= cpuThreshold;
        
        if (memoryCritical || cpuCritical) {
            return HealthStatus.UNHEALTHY;
        }
        
        boolean memoryWarning = heapUsagePercent >= memoryThreshold * 0.8;
        boolean cpuWarning = cpuLoad >= 0 && cpuLoad >= cpuThreshold * 0.8;
        
        if (memoryWarning || cpuWarning) {
            return HealthStatus.DEGRADED;
        }
        
        return HealthStatus.HEALTHY;
    }
    
    private String buildHealthMessage(HealthStatus status, double heapUsagePercent, double cpuLoad) {
        return switch (status) {
            case HEALTHY -> "JVM is operating normally";
            case DEGRADED -> {
                StringBuilder sb = new StringBuilder("JVM is degraded: ");
                if (heapUsagePercent >= memoryThreshold * 0.8) {
                    sb.append(String.format("heap usage at %.1f%% (warning) ", heapUsagePercent * 100));
                }
                if (cpuLoad >= 0 && cpuLoad >= cpuThreshold * 0.8) {
                    sb.append(String.format("CPU load at %.1f%% (warning)", cpuLoad * 100));
                }
                yield sb.toString().trim();
            }
            case UNHEALTHY -> {
                StringBuilder sb = new StringBuilder("JVM is unhealthy: ");
                if (heapUsagePercent >= memoryThreshold) {
                    sb.append(String.format("heap usage at %.1f%% (critical) ", heapUsagePercent * 100));
                }
                if (cpuLoad >= 0 && cpuLoad >= cpuThreshold) {
                    sb.append(String.format("CPU load at %.1f%% (critical)", cpuLoad * 100));
                }
                yield sb.toString().trim();
            }
            case UNKNOWN -> "JVM health status unknown";
        };
    }
    
    private double getCpuLoad() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            return sunOsBean.getProcessCpuLoad();
        }
        return -1;
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}
