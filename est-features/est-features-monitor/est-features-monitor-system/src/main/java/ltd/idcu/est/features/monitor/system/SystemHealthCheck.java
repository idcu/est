package ltd.idcu.est.features.monitor.system;

import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SystemHealthCheck implements HealthCheck {
    
    private final String name;
    private final String description;
    private final double memoryThreshold;
    private final double cpuThreshold;
    private final double diskThreshold;
    private volatile HealthCheckResult lastResult;
    
    public SystemHealthCheck() {
        this("system", "System Health Check", 0.9, 0.9, 0.9);
    }
    
    public SystemHealthCheck(String name, String description,
                            double memoryThreshold, double cpuThreshold, double diskThreshold) {
        this.name = name;
        this.description = description;
        this.memoryThreshold = memoryThreshold;
        this.cpuThreshold = cpuThreshold;
        this.diskThreshold = diskThreshold;
        this.lastResult = null;
    }
    
    @Override
    public HealthStatus check() {
        Map<String, Object> details = new HashMap<>();
        
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        com.sun.management.OperatingSystemMXBean sunOsBean = 
                osBean instanceof com.sun.management.OperatingSystemMXBean 
                        ? (com.sun.management.OperatingSystemMXBean) osBean : null;
        
        double cpuLoad = -1;
        double memoryUsage = -1;
        double diskUsage = -1;
        
        details.put("os.name", osBean.getName());
        details.put("os.version", osBean.getVersion());
        details.put("os.arch", osBean.getArch());
        details.put("cpu.cores", osBean.getAvailableProcessors());
        
        if (sunOsBean != null) {
            cpuLoad = sunOsBean.getCpuLoad();
            if (cpuLoad >= 0) {
                details.put("cpu.load", String.format("%.2f%%", cpuLoad * 100));
            }
            
            long totalMemory = sunOsBean.getTotalMemorySize();
            long freeMemory = sunOsBean.getFreeMemorySize();
            if (totalMemory > 0) {
                memoryUsage = (double) (totalMemory - freeMemory) / totalMemory;
                details.put("memory.total", formatBytes(totalMemory));
                details.put("memory.free", formatBytes(freeMemory));
                details.put("memory.used", formatBytes(totalMemory - freeMemory));
                details.put("memory.usage", String.format("%.2f%%", memoryUsage * 100));
            }
            
            long totalSwap = sunOsBean.getTotalSwapSpaceSize();
            long freeSwap = sunOsBean.getFreeSwapSpaceSize();
            if (totalSwap > 0) {
                details.put("swap.total", formatBytes(totalSwap));
                details.put("swap.free", formatBytes(freeSwap));
                details.put("swap.used", formatBytes(totalSwap - freeSwap));
            }
        }
        
        try {
            long totalDisk = 0;
            long freeDisk = 0;
            for (Path root : FileSystems.getDefault().getRootDirectories()) {
                FileStore store = Files.getFileStore(root);
                totalDisk += store.getTotalSpace();
                freeDisk += store.getUsableSpace();
            }
            if (totalDisk > 0) {
                diskUsage = (double) (totalDisk - freeDisk) / totalDisk;
                details.put("disk.total", formatBytes(totalDisk));
                details.put("disk.free", formatBytes(freeDisk));
                details.put("disk.used", formatBytes(totalDisk - freeDisk));
                details.put("disk.usage", String.format("%.2f%%", diskUsage * 100));
            }
        } catch (Exception e) {
            details.put("disk.error", e.getMessage());
        }
        
        HealthStatus status = determineHealthStatus(cpuLoad, memoryUsage, diskUsage);
        String message = buildHealthMessage(status, cpuLoad, memoryUsage, diskUsage);
        
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
    
    private HealthStatus determineHealthStatus(double cpuLoad, double memoryUsage, double diskUsage) {
        boolean cpuCritical = cpuLoad >= 0 && cpuLoad >= cpuThreshold;
        boolean memoryCritical = memoryUsage >= 0 && memoryUsage >= memoryThreshold;
        boolean diskCritical = diskUsage >= 0 && diskUsage >= diskThreshold;
        
        if (cpuCritical || memoryCritical || diskCritical) {
            return HealthStatus.UNHEALTHY;
        }
        
        boolean cpuWarning = cpuLoad >= 0 && cpuLoad >= cpuThreshold * 0.8;
        boolean memoryWarning = memoryUsage >= 0 && memoryUsage >= memoryThreshold * 0.8;
        boolean diskWarning = diskUsage >= 0 && diskUsage >= diskThreshold * 0.8;
        
        if (cpuWarning || memoryWarning || diskWarning) {
            return HealthStatus.DEGRADED;
        }
        
        return HealthStatus.HEALTHY;
    }
    
    private String buildHealthMessage(HealthStatus status, 
                                      double cpuLoad, double memoryUsage, double diskUsage) {
        return switch (status) {
            case HEALTHY -> "System is operating normally";
            case DEGRADED -> {
                StringBuilder sb = new StringBuilder("System is degraded: ");
                if (cpuLoad >= 0 && cpuLoad >= cpuThreshold * 0.8) {
                    sb.append(String.format("CPU load at %.1f%% (warning) ", cpuLoad * 100));
                }
                if (memoryUsage >= 0 && memoryUsage >= memoryThreshold * 0.8) {
                    sb.append(String.format("Memory usage at %.1f%% (warning) ", memoryUsage * 100));
                }
                if (diskUsage >= 0 && diskUsage >= diskThreshold * 0.8) {
                    sb.append(String.format("Disk usage at %.1f%% (warning)", diskUsage * 100));
                }
                yield sb.toString().trim();
            }
            case UNHEALTHY -> {
                StringBuilder sb = new StringBuilder("System is unhealthy: ");
                if (cpuLoad >= 0 && cpuLoad >= cpuThreshold) {
                    sb.append(String.format("CPU load at %.1f%% (critical) ", cpuLoad * 100));
                }
                if (memoryUsage >= 0 && memoryUsage >= memoryThreshold) {
                    sb.append(String.format("Memory usage at %.1f%% (critical) ", memoryUsage * 100));
                }
                if (diskUsage >= 0 && diskUsage >= diskThreshold) {
                    sb.append(String.format("Disk usage at %.1f%% (critical)", diskUsage * 100));
                }
                yield sb.toString().trim();
            }
            case UNKNOWN -> "System health status unknown";
        };
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes < 1024L * 1024 * 1024 * 1024) {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        } else {
            return String.format("%.2f TB", bytes / (1024.0 * 1024 * 1024 * 1024));
        }
    }
}
