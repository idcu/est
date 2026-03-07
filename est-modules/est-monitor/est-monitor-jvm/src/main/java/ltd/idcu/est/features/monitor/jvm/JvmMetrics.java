package ltd.idcu.est.features.monitor.jvm;

import ltd.idcu.est.features.monitor.api.Metric;
import ltd.idcu.est.features.monitor.api.Metrics;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class JvmMetrics implements Metrics {
    
    private final Map<String, Object> customMetrics;
    private final Map<String, AtomicLong> counters;
    private final Map<String, List<Long>> histograms;
    private final Map<String, Number> gauges;
    private final RuntimeMXBean runtimeMXBean;
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;
    private final ClassLoadingMXBean classLoadingMXBean;
    private final CompilationMXBean compilationMXBean;
    private final GarbageCollectorMXBean[] gcMXBeans;
    
    public JvmMetrics() {
        this.customMetrics = new ConcurrentHashMap<>();
        this.counters = new ConcurrentHashMap<>();
        this.histograms = new ConcurrentHashMap<>();
        this.gauges = new ConcurrentHashMap<>();
        this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
        this.classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        this.compilationMXBean = ManagementFactory.getCompilationMXBean();
        this.gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans()
                .toArray(new GarbageCollectorMXBean[0]);
    }
    
    @Override
    public Object getMetric(String name) {
        return switch (name) {
            case "jvm.uptime" -> getUptime();
            case "jvm.startTime" -> getStartTime();
            case "jvm.name" -> getJvmName();
            case "jvm.version" -> getJvmVersion();
            case "jvm.heap.used" -> getHeapUsed();
            case "jvm.heap.max" -> getHeapMax();
            case "jvm.heap.committed" -> getHeapCommitted();
            case "jvm.heap.usage" -> getHeapUsage();
            case "jvm.nonheap.used" -> getNonHeapUsed();
            case "jvm.nonheap.max" -> getNonHeapMax();
            case "jvm.nonheap.committed" -> getNonHeapCommitted();
            case "jvm.thread.count" -> getThreadCount();
            case "jvm.thread.daemon.count" -> getDaemonThreadCount();
            case "jvm.thread.peak.count" -> getPeakThreadCount();
            case "jvm.thread.total.started" -> getTotalStartedThreadCount();
            case "jvm.class.loaded" -> getLoadedClassCount();
            case "jvm.class.total.loaded" -> getTotalLoadedClassCount();
            case "jvm.class.unloaded" -> getUnloadedClassCount();
            case "jvm.gc.count" -> getGcCount();
            case "jvm.gc.time" -> getGcTime();
            case "jvm.cpu.processors" -> getAvailableProcessors();
            case "jvm.cpu.time" -> getProcessCpuTime();
            case "jvm.cpu.load" -> getProcessCpuLoad();
            default -> customMetrics.get(name);
        };
    }
    
    @Override
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("jvm.uptime", getUptime());
        metrics.put("jvm.startTime", getStartTime());
        metrics.put("jvm.name", getJvmName());
        metrics.put("jvm.version", getJvmVersion());
        
        metrics.put("jvm.heap.used", getHeapUsed());
        metrics.put("jvm.heap.max", getHeapMax());
        metrics.put("jvm.heap.committed", getHeapCommitted());
        metrics.put("jvm.heap.usage", getHeapUsage());
        metrics.put("jvm.nonheap.used", getNonHeapUsed());
        metrics.put("jvm.nonheap.max", getNonHeapMax());
        metrics.put("jvm.nonheap.committed", getNonHeapCommitted());
        
        metrics.put("jvm.thread.count", getThreadCount());
        metrics.put("jvm.thread.daemon.count", getDaemonThreadCount());
        metrics.put("jvm.thread.peak.count", getPeakThreadCount());
        metrics.put("jvm.thread.total.started", getTotalStartedThreadCount());
        
        metrics.put("jvm.class.loaded", getLoadedClassCount());
        metrics.put("jvm.class.total.loaded", getTotalLoadedClassCount());
        metrics.put("jvm.class.unloaded", getUnloadedClassCount());
        
        metrics.put("jvm.gc.count", getGcCount());
        metrics.put("jvm.gc.time", getGcTime());
        
        metrics.put("jvm.cpu.processors", getAvailableProcessors());
        metrics.put("jvm.cpu.time", getProcessCpuTime());
        metrics.put("jvm.cpu.load", getProcessCpuLoad());
        
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
        return name.startsWith("jvm.");
    }
    
    public long getUptime() {
        return runtimeMXBean.getUptime();
    }
    
    public long getStartTime() {
        return runtimeMXBean.getStartTime();
    }
    
    public String getJvmName() {
        return runtimeMXBean.getVmName();
    }
    
    public String getJvmVersion() {
        return runtimeMXBean.getVmVersion();
    }
    
    public long getHeapUsed() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }
    
    public long getHeapMax() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }
    
    public long getHeapCommitted() {
        return memoryMXBean.getHeapMemoryUsage().getCommitted();
    }
    
    public double getHeapUsage() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        long used = heapUsage.getUsed();
        long max = heapUsage.getMax();
        if (max <= 0) {
            return 0.0;
        }
        return (double) used / max;
    }
    
    public long getNonHeapUsed() {
        return memoryMXBean.getNonHeapMemoryUsage().getUsed();
    }
    
    public long getNonHeapMax() {
        return memoryMXBean.getNonHeapMemoryUsage().getMax();
    }
    
    public long getNonHeapCommitted() {
        return memoryMXBean.getNonHeapMemoryUsage().getCommitted();
    }
    
    public int getThreadCount() {
        return threadMXBean.getThreadCount();
    }
    
    public int getDaemonThreadCount() {
        return threadMXBean.getDaemonThreadCount();
    }
    
    public int getPeakThreadCount() {
        return threadMXBean.getPeakThreadCount();
    }
    
    public long getTotalStartedThreadCount() {
        return threadMXBean.getTotalStartedThreadCount();
    }
    
    public int getLoadedClassCount() {
        return classLoadingMXBean.getLoadedClassCount();
    }
    
    public long getTotalLoadedClassCount() {
        return classLoadingMXBean.getTotalLoadedClassCount();
    }
    
    public long getUnloadedClassCount() {
        return classLoadingMXBean.getUnloadedClassCount();
    }
    
    public long getGcCount() {
        long total = 0;
        for (GarbageCollectorMXBean gcBean : gcMXBeans) {
            total += gcBean.getCollectionCount();
        }
        return total;
    }
    
    public long getGcTime() {
        long total = 0;
        for (GarbageCollectorMXBean gcBean : gcMXBeans) {
            total += gcBean.getCollectionTime();
        }
        return total;
    }
    
    public int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }
    
    public long getProcessCpuTime() {
        if (getOperatingSystemMXBean() instanceof com.sun.management.OperatingSystemMXBean osBean) {
            return osBean.getProcessCpuTime();
        }
        return -1;
    }
    
    public double getProcessCpuLoad() {
        if (getOperatingSystemMXBean() instanceof com.sun.management.OperatingSystemMXBean osBean) {
            return osBean.getProcessCpuLoad();
        }
        return -1;
    }
    
    private OperatingSystemMXBean getOperatingSystemMXBean() {
        return ManagementFactory.getOperatingSystemMXBean();
    }
    
    public Metric getHeapMetric() {
        return new Metric("jvm.heap", Metric.Type.CUSTOM, getAllMetrics(), "bytes", "JVM heap memory metrics");
    }
    
    public Metric getThreadMetric() {
        return new Metric("jvm.thread", Metric.Type.CUSTOM, getAllMetrics(), "count", "JVM thread metrics");
    }
    
    public Metric getClassLoadingMetric() {
        return new Metric("jvm.class", Metric.Type.CUSTOM, getAllMetrics(), "count", "JVM class loading metrics");
    }
    
    public Metric getGcMetric() {
        return new Metric("jvm.gc", Metric.Type.CUSTOM, getAllMetrics(), "mixed", "JVM garbage collection metrics");
    }

    @Override
    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong(0)).incrementAndGet();
    }

    @Override
    public void decrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong(0)).decrementAndGet();
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
        for (Map.Entry<String, AtomicLong> entry : counters.entrySet()) {
            details.put(entry.getKey(), new Metric(entry.getKey(), Metric.Type.COUNTER, entry.getValue().get(), "count"));
        }
        for (Map.Entry<String, Number> entry : gauges.entrySet()) {
            details.put(entry.getKey(), new Metric(entry.getKey(), Metric.Type.GAUGE, entry.getValue()));
        }
        return details;
    }

    @Override
    public long getCounter(String name) {
        AtomicLong counter = counters.get(name);
        return counter != null ? counter.get() : 0;
    }

    @Override
    public double getHistogramPercentile(String name, double percentile) {
        List<Long> values = histograms.get(name);
        if (values == null || values.isEmpty()) {
            return 0;
        }
        List<Long> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int index = (int) Math.ceil(percentile * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }
}
