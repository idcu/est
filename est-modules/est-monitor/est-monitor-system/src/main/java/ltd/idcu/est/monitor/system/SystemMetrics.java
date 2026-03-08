package ltd.idcu.est.monitor.system;

import ltd.idcu.est.monitor.api.Metric;
import ltd.idcu.est.monitor.api.Metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMetrics implements Metrics {
    
    private final Map<String, Object> customMetrics;
    private final Map<String, java.util.concurrent.atomic.AtomicLong> counters;
    private final Map<String, java.util.List<Long>> histograms;
    private final Map<String, Number> gauges;
    private final OperatingSystemMXBean osBean;
    
    public SystemMetrics() {
        this.customMetrics = new ConcurrentHashMap<>();
        this.counters = new ConcurrentHashMap<>();
        this.histograms = new ConcurrentHashMap<>();
        this.gauges = new ConcurrentHashMap<>();
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
    }
    
    @Override
    public Object getMetric(String name) {
        return switch (name) {
            case "system.os.name" -> getOsName();
            case "system.os.version" -> getOsVersion();
            case "system.os.arch" -> getOsArch();
            case "system.cpu.cores" -> getAvailableCpuCores();
            case "system.cpu.load" -> getSystemCpuLoad();
            case "system.cpu.process.load" -> getProcessCpuLoad();
            case "system.memory.total" -> getTotalPhysicalMemory();
            case "system.memory.free" -> getFreePhysicalMemory();
            case "system.memory.used" -> getUsedPhysicalMemory();
            case "system.memory.usage" -> getMemoryUsage();
            case "system.swap.total" -> getTotalSwapSpace();
            case "system.swap.free" -> getFreeSwapSpace();
            case "system.swap.used" -> getUsedSwapSpace();
            case "system.disk.total" -> getTotalDiskSpace();
            case "system.disk.free" -> getFreeDiskSpace();
            case "system.disk.used" -> getUsedDiskSpace();
            case "system.disk.usage" -> getDiskUsage();
            case "system.user" -> getSystemUser();
            case "system.home" -> getSystemHome();
            case "system.temp" -> getSystemTemp();
            default -> customMetrics.get(name);
        };
    }
    
    @Override
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("system.os.name", getOsName());
        metrics.put("system.os.version", getOsVersion());
        metrics.put("system.os.arch", getOsArch());
        
        metrics.put("system.cpu.cores", getAvailableCpuCores());
        metrics.put("system.cpu.load", getSystemCpuLoad());
        metrics.put("system.cpu.process.load", getProcessCpuLoad());
        
        metrics.put("system.memory.total", getTotalPhysicalMemory());
        metrics.put("system.memory.free", getFreePhysicalMemory());
        metrics.put("system.memory.used", getUsedPhysicalMemory());
        metrics.put("system.memory.usage", getMemoryUsage());
        
        metrics.put("system.swap.total", getTotalSwapSpace());
        metrics.put("system.swap.free", getFreeSwapSpace());
        metrics.put("system.swap.used", getUsedSwapSpace());
        
        metrics.put("system.disk.total", getTotalDiskSpace());
        metrics.put("system.disk.free", getFreeDiskSpace());
        metrics.put("system.disk.used", getUsedDiskSpace());
        metrics.put("system.disk.usage", getDiskUsage());
        
        metrics.put("system.user", getSystemUser());
        metrics.put("system.home", getSystemHome());
        metrics.put("system.temp", getSystemTemp());
        
        metrics.putAll(customMetrics);
        
        return metrics;
    }
    
    @Override
    public void registerMetric(String name, Object value) {
        customMetrics.put(name, value);
    }
    
    @Override
    public void unregisterMetric(String name) {
        customMetrics.remove(name);
    }
    
    @Override
    public boolean hasMetric(String name) {
        return customMetrics.containsKey(name) || isBuiltInMetric(name);
    }
    
    @Override
    public void reset() {
        customMetrics.clear();
    }
    
    private boolean isBuiltInMetric(String name) {
        return name.startsWith("system.");
    }
    
    public String getOsName() {
        return osBean.getName();
    }
    
    public String getOsVersion() {
        return osBean.getVersion();
    }
    
    public String getOsArch() {
        return osBean.getArch();
    }
    
    public int getAvailableCpuCores() {
        return osBean.getAvailableProcessors();
    }
    
    public double getSystemCpuLoad() {
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            double load = sunOsBean.getCpuLoad();
            return load < 0 ? -1 : load;
        }
        return osBean.getSystemLoadAverage();
    }
    
    public double getProcessCpuLoad() {
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            double load = sunOsBean.getProcessCpuLoad();
            return load < 0 ? -1 : load;
        }
        return -1;
    }
    
    public long getTotalPhysicalMemory() {
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            return sunOsBean.getTotalMemorySize();
        }
        return -1;
    }
    
    public long getFreePhysicalMemory() {
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            return sunOsBean.getFreeMemorySize();
        }
        return -1;
    }
    
    public long getUsedPhysicalMemory() {
        long total = getTotalPhysicalMemory();
        long free = getFreePhysicalMemory();
        if (total < 0 || free < 0) {
            return -1;
        }
        return total - free;
    }
    
    public double getMemoryUsage() {
        long total = getTotalPhysicalMemory();
        long free = getFreePhysicalMemory();
        if (total <= 0 || free < 0) {
            return -1;
        }
        return (double) (total - free) / total;
    }
    
    public long getTotalSwapSpace() {
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            return sunOsBean.getTotalSwapSpaceSize();
        }
        return -1;
    }
    
    public long getFreeSwapSpace() {
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            return sunOsBean.getFreeSwapSpaceSize();
        }
        return -1;
    }
    
    public long getUsedSwapSpace() {
        long total = getTotalSwapSpace();
        long free = getFreeSwapSpace();
        if (total < 0 || free < 0) {
            return -1;
        }
        return total - free;
    }
    
    public long getTotalDiskSpace() {
        long total = 0;
        try {
            for (Path root : FileSystems.getDefault().getRootDirectories()) {
                FileStore store = Files.getFileStore(root);
                total += store.getTotalSpace();
            }
        } catch (Exception e) {
            return -1;
        }
        return total;
    }
    
    public long getFreeDiskSpace() {
        long free = 0;
        try {
            for (Path root : FileSystems.getDefault().getRootDirectories()) {
                FileStore store = Files.getFileStore(root);
                free += store.getUsableSpace();
            }
        } catch (Exception e) {
            return -1;
        }
        return free;
    }
    
    public long getUsedDiskSpace() {
        long total = getTotalDiskSpace();
        long free = getFreeDiskSpace();
        if (total < 0 || free < 0) {
            return -1;
        }
        return total - free;
    }
    
    public double getDiskUsage() {
        long total = getTotalDiskSpace();
        long free = getFreeDiskSpace();
        if (total <= 0 || free < 0) {
            return -1;
        }
        return (double) (total - free) / total;
    }
    
    public String getSystemUser() {
        return System.getProperty("user.name");
    }
    
    public String getSystemHome() {
        return System.getProperty("user.home");
    }
    
    public String getSystemTemp() {
        return System.getProperty("java.io.tmpdir");
    }
    
    public Metric getCpuMetric() {
        Map<String, Object> cpuMetrics = new HashMap<>();
        cpuMetrics.put("cores", getAvailableCpuCores());
        cpuMetrics.put("systemLoad", getSystemCpuLoad());
        cpuMetrics.put("processLoad", getProcessCpuLoad());
        return new Metric("system.cpu", Metric.Type.CUSTOM, cpuMetrics, "mixed", "System CPU metrics");
    }
    
    public Metric getMemoryMetric() {
        Map<String, Object> memMetrics = new HashMap<>();
        memMetrics.put("total", getTotalPhysicalMemory());
        memMetrics.put("free", getFreePhysicalMemory());
        memMetrics.put("used", getUsedPhysicalMemory());
        memMetrics.put("usage", getMemoryUsage());
        return new Metric("system.memory", Metric.Type.CUSTOM, memMetrics, "bytes", "System memory metrics");
    }
    
    public Metric getDiskMetric() {
        Map<String, Object> diskMetrics = new HashMap<>();
        diskMetrics.put("total", getTotalDiskSpace());
        diskMetrics.put("free", getFreeDiskSpace());
        diskMetrics.put("used", getUsedDiskSpace());
        diskMetrics.put("usage", getDiskUsage());
        return new Metric("system.disk", Metric.Type.CUSTOM, diskMetrics, "bytes", "System disk metrics");
    }
    
    public Map<String, Object> getDiskInfo() {
        Map<String, Object> diskInfo = new HashMap<>();
        try {
            for (Path root : FileSystems.getDefault().getRootDirectories()) {
                FileStore store = Files.getFileStore(root);
                Map<String, Object> info = new HashMap<>();
                info.put("name", store.name());
                info.put("type", store.type());
                info.put("total", store.getTotalSpace());
                info.put("free", store.getUsableSpace());
                info.put("used", store.getTotalSpace() - store.getUsableSpace());
                diskInfo.put(root.toString(), info);
            }
        } catch (Exception e) {
            diskInfo.put("error", e.getMessage());
        }
        return diskInfo;
    }

    @Override
    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new java.util.concurrent.atomic.AtomicLong(0)).incrementAndGet();
    }

    @Override
    public void decrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new java.util.concurrent.atomic.AtomicLong(0)).decrementAndGet();
    }

    @Override
    public void recordHistogram(String name, long value) {
        histograms.computeIfAbsent(name, k -> new java.util.concurrent.CopyOnWriteArrayList<>()).add(value);
    }

    @Override
    public void recordTimer(String name, long milliseconds) {
        recordHistogram(name, milliseconds);
    }

    @Override
    public void recordGauge(String name, Number value) {
        gauges.put(name, value);
    }

    @Override
    public Map<String, Metric> getMetricDetails() {
        Map<String, Metric> details = new HashMap<>();
        Map<String, Object> allMetrics = getAllMetrics();
        for (Map.Entry<String, Object> entry : allMetrics.entrySet()) {
            Metric.Type type = entry.getValue() instanceof Number ? Metric.Type.GAUGE : Metric.Type.CUSTOM;
            details.put(entry.getKey(), new Metric(entry.getKey(), type, entry.getValue()));
        }
        for (Map.Entry<String, java.util.concurrent.atomic.AtomicLong> entry : counters.entrySet()) {
            details.put(entry.getKey(), new Metric(entry.getKey(), Metric.Type.COUNTER, entry.getValue().get(), "count"));
        }
        for (Map.Entry<String, Number> entry : gauges.entrySet()) {
            details.put(entry.getKey(), new Metric(entry.getKey(), Metric.Type.GAUGE, entry.getValue()));
        }
        return details;
    }

    @Override
    public long getCounter(String name) {
        java.util.concurrent.atomic.AtomicLong counter = counters.get(name);
        return counter != null ? counter.get() : 0;
    }

    @Override
    public double getHistogramPercentile(String name, double percentile) {
        List<Long> values = histograms.get(name);
        if (values == null || values.isEmpty()) {
            return 0;
        }
        List<Long> sorted = new ArrayList<>(values);
        java.util.Collections.sort(sorted);
        int index = (int) Math.ceil(percentile * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }
}
