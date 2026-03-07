package ltd.idcu.est.features.performance.api;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

public class GCTuner {
    private final List<GarbageCollectorMXBean> gcBeans;
    private final MemoryMXBean memoryBean;
    private long lastYoungGcCount = 0;
    private long lastYoungGcTime = 0;
    private long lastOldGcCount = 0;
    private long lastOldGcTime = 0;
    private long lastMeasurementTime = System.currentTimeMillis();

    public GCTuner() {
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }

    public GCMetrics collectMetrics() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastMeasurementTime;

        long youngGcCount = 0;
        long youngGcTime = 0;
        long oldGcCount = 0;
        long oldGcTime = 0;

        for (GarbageCollectorMXBean bean : gcBeans) {
            String name = bean.getName().toLowerCase();
            long count = bean.getCollectionCount();
            long time = bean.getCollectionTime();

            if (name.contains("young") || name.contains("copy") || 
                name.contains("ps scavenge") || name.contains("g1 young")) {
                youngGcCount += count;
                youngGcTime += time;
            } else if (name.contains("old") || name.contains("mark") || 
                       name.contains("ps marksweep") || name.contains("g1 old")) {
                oldGcCount += count;
                oldGcTime += time;
            }
        }

        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();

        long youngGcDeltaTime = youngGcTime - lastYoungGcTime;
        long oldGcDeltaTime = oldGcTime - lastOldGcTime;
        long totalGcDeltaTime = youngGcDeltaTime + oldGcDeltaTime;

        double gcOverheadPercent = timeElapsed > 0 
                ? (double) totalGcDeltaTime / timeElapsed * 100 
                : 0;

        lastYoungGcCount = youngGcCount;
        lastYoungGcTime = youngGcTime;
        lastOldGcCount = oldGcCount;
        lastOldGcTime = oldGcTime;
        lastMeasurementTime = currentTime;

        return new GCMetrics(
                youngGcCount, youngGcTime, oldGcCount, oldGcTime,
                youngGcCount + oldGcCount, youngGcTime + oldGcTime,
                heapUsage.getUsed(), heapUsage.getMax(), heapUsage.getCommitted(),
                nonHeapUsage.getUsed(), nonHeapUsage.getMax(), nonHeapUsage.getCommitted(),
                gcOverheadPercent
        );
    }

    public GCRecommendation getRecommendation(GCMetrics metrics) {
        List<String> recommendations = new ArrayList<>();
        GCRecommendation.Priority priority = GCRecommendation.Priority.LOW;

        if (metrics.getGcOverheadPercent() > 10) {
            recommendations.add("GC overhead is high (" + String.format("%.1f", metrics.getGcOverheadPercent()) + 
                               "%). Consider increasing heap size or optimizing object allocation.");
            priority = GCRecommendation.Priority.HIGH;
        } else if (metrics.getGcOverheadPercent() > 5) {
            recommendations.add("GC overhead is moderate (" + String.format("%.1f", metrics.getGcOverheadPercent()) + 
                               "%). Monitor for potential issues.");
            if (priority.ordinal() < GCRecommendation.Priority.MEDIUM.ordinal()) {
                priority = GCRecommendation.Priority.MEDIUM;
            }
        }

        if (metrics.getHeapUsagePercent() > 90) {
            recommendations.add("Heap usage is very high (" + String.format("%.1f", metrics.getHeapUsagePercent()) + 
                               "%). Consider increasing -Xmx.");
            priority = GCRecommendation.Priority.HIGH;
        } else if (metrics.getHeapUsagePercent() > 75) {
            recommendations.add("Heap usage is high (" + String.format("%.1f", metrics.getHeapUsagePercent()) + 
                               "%). Monitor memory pressure.");
            if (priority.ordinal() < GCRecommendation.Priority.MEDIUM.ordinal()) {
                priority = GCRecommendation.Priority.MEDIUM;
            }
        }

        if (metrics.getOldGcCount() > 0 && metrics.getOldGcTime() > 5000) {
            recommendations.add("Old GC collections are taking significant time. Consider tuning old generation.");
            if (priority.ordinal() < GCRecommendation.Priority.MEDIUM.ordinal()) {
                priority = GCRecommendation.Priority.MEDIUM;
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("GC performance is good. Continue monitoring.");
        }

        return new GCRecommendation(priority, recommendations, metrics);
    }

    public String getJVMInfo() {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();
        sb.append("JVM Information:\n");
        sb.append("  Java Version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("  Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");
        sb.append("  OS: ").append(System.getProperty("os.name")).append("\n");
        sb.append("  Processors: ").append(runtime.availableProcessors()).append("\n");
        sb.append("  Max Memory: ").append(formatBytes(runtime.maxMemory())).append("\n");
        sb.append("  Total Memory: ").append(formatBytes(runtime.totalMemory())).append("\n");
        sb.append("  Free Memory: ").append(formatBytes(runtime.freeMemory())).append("\n");
        return sb.toString();
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        else return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
