package ltd.idcu.est.utils.common;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PerformanceUtils {

    private static final Map<String, Long> timings = new ConcurrentHashMap<>();
    private static final Map<String, Long> timingCounts = new ConcurrentHashMap<>();
    private static final Map<String, Long> timingTotals = new ConcurrentHashMap<>();

    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    private PerformanceUtils() {
    }

    public static void startTiming(String name) {
        timings.put(name, System.nanoTime());
    }

    public static long stopTiming(String name) {
        Long startTime = timings.remove(name);
        if (startTime == null) {
            return -1;
        }
        long duration = System.nanoTime() - startTime;
        timingCounts.merge(name, 1L, Long::sum);
        timingTotals.merge(name, duration, Long::sum);
        return duration;
    }

    public static long getTiming(String name, TimeUnit unit) {
        Long startTime = timings.get(name);
        if (startTime == null) {
            return -1;
        }
        long duration = System.nanoTime() - startTime;
        return unit.convert(duration, TimeUnit.NANOSECONDS);
    }

    public static long getAverageTiming(String name, TimeUnit unit) {
        Long count = timingCounts.get(name);
        Long total = timingTotals.get(name);
        if (count == null || total == null || count == 0) {
            return -1;
        }
        long average = total / count;
        return unit.convert(average, TimeUnit.NANOSECONDS);
    }

    public static void clearTimings() {
        timings.clear();
        timingCounts.clear();
        timingTotals.clear();
    }

    public static Map<String, Object> getTimingStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        for (String name : timingCounts.keySet()) {
            Map<String, Object> timingStat = new ConcurrentHashMap<>();
            timingStat.put("count", timingCounts.get(name));
            timingStat.put("totalMs", getAverageTiming(name, TimeUnit.MILLISECONDS) * timingCounts.get(name));
            timingStat.put("averageMs", getAverageTiming(name, TimeUnit.MILLISECONDS));
            stats.put(name, timingStat);
        }
        return stats;
    }

    public static MemorySnapshot getMemorySnapshot() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();

        return new MemorySnapshot(
            heapUsage.getInit(),
            heapUsage.getUsed(),
            heapUsage.getCommitted(),
            heapUsage.getMax(),
            nonHeapUsage.getInit(),
            nonHeapUsage.getUsed(),
            nonHeapUsage.getCommitted(),
            nonHeapUsage.getMax()
        );
    }

    public static long getUsedHeapMemory() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    public static long getMaxHeapMemory() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }

    public static double getHeapMemoryUsagePercent() {
        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        if (usage.getMax() <= 0) {
            return 0;
        }
        return (double) usage.getUsed() / usage.getMax() * 100;
    }

    public static void gc() {
        System.gc();
    }

    public static List<MemoryOptimizationTip> getMemoryOptimizationTips() {
        List<MemoryOptimizationTip> tips = new ArrayList<>();
        MemorySnapshot snapshot = getMemorySnapshot();

        if (snapshot.getHeapUsedPercent() > 80) {
            tips.add(new MemoryOptimizationTip(
                "高堆内存使用",
                "当前堆内存使用率超过80%，考虑增加堆内存或优化对象创建",
                MemoryOptimizationTip.Severity.HIGH
            ));
        }

        if (snapshot.getHeapUsed() > 512 * 1024 * 1024) {
            tips.add(new MemoryOptimizationTip(
                "大堆内存使用",
                "堆内存使用超过512MB，考虑使用对象池或缓存优化",
                MemoryOptimizationTip.Severity.MEDIUM
            ));
        }

        tips.add(new MemoryOptimizationTip(
            "使用基本类型",
            "优先使用基本类型而非包装类型，减少自动装箱开销",
            MemoryOptimizationTip.Severity.LOW
        ));

        tips.add(new MemoryOptimizationTip(
            "避免不必要的对象创建",
            "重用对象，使用StringBuilder而非String拼接",
            MemoryOptimizationTip.Severity.LOW
        ));

        tips.add(new MemoryOptimizationTip(
            "及时释放资源",
            "使用try-with-resources确保资源及时释放",
            MemoryOptimizationTip.Severity.MEDIUM
        ));

        return tips;
    }

    public static List<StartupOptimizationTip> getStartupOptimizationTips() {
        List<StartupOptimizationTip> tips = new ArrayList<>();

        tips.add(new StartupOptimizationTip(
            "延迟初始化",
            "使用延迟初始化模式，只在需要时创建对象",
            StartupOptimizationTip.Severity.HIGH
        ));

        tips.add(new StartupOptimizationTip(
            "减少类路径扫描",
            "限制类路径扫描范围，只扫描必要的包",
            StartupOptimizationTip.Severity.HIGH
        ));

        tips.add(new StartupOptimizationTip(
            "禁用不必要的功能",
            "在开发环境禁用AOP、BeanPostProcessor等功能",
            StartupOptimizationTip.Severity.MEDIUM
        ));

        tips.add(new StartupOptimizationTip(
            "使用性能模式",
            "启用EST框架的性能模式，减少启动开销",
            StartupOptimizationTip.Severity.HIGH
        ));

        tips.add(new StartupOptimizationTip(
            "JVM参数优化",
            "使用合适的JVM参数：-Xms512m -Xmx512m -XX:+UseG1GC",
            StartupOptimizationTip.Severity.MEDIUM
        ));

        return tips;
    }

    public static class MemorySnapshot {
        private final long heapInit;
        private final long heapUsed;
        private final long heapCommitted;
        private final long heapMax;
        private final long nonHeapInit;
        private final long nonHeapUsed;
        private final long nonHeapCommitted;
        private final long nonHeapMax;
        private final long timestamp;

        public MemorySnapshot(long heapInit, long heapUsed, long heapCommitted, long heapMax,
                             long nonHeapInit, long nonHeapUsed, long nonHeapCommitted, long nonHeapMax) {
            this.heapInit = heapInit;
            this.heapUsed = heapUsed;
            this.heapCommitted = heapCommitted;
            this.heapMax = heapMax;
            this.nonHeapInit = nonHeapInit;
            this.nonHeapUsed = nonHeapUsed;
            this.nonHeapCommitted = nonHeapCommitted;
            this.nonHeapMax = nonHeapMax;
            this.timestamp = System.currentTimeMillis();
        }

        public long getHeapInit() { return heapInit; }
        public long getHeapUsed() { return heapUsed; }
        public long getHeapCommitted() { return heapCommitted; }
        public long getHeapMax() { return heapMax; }
        public double getHeapUsedPercent() {
            return heapMax > 0 ? (double) heapUsed / heapMax * 100 : 0;
        }
        public long getNonHeapInit() { return nonHeapInit; }
        public long getNonHeapUsed() { return nonHeapUsed; }
        public long getNonHeapCommitted() { return nonHeapCommitted; }
        public long getNonHeapMax() { return nonHeapMax; }
        public long getTimestamp() { return timestamp; }
    }

    public static class MemoryOptimizationTip {
        public enum Severity { LOW, MEDIUM, HIGH }

        private final String title;
        private final String description;
        private final Severity severity;

        public MemoryOptimizationTip(String title, String description, Severity severity) {
            this.title = title;
            this.description = description;
            this.severity = severity;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Severity getSeverity() { return severity; }
    }

    public static class StartupOptimizationTip {
        public enum Severity { LOW, MEDIUM, HIGH }

        private final String title;
        private final String description;
        private final Severity severity;

        public StartupOptimizationTip(String title, String description, Severity severity) {
            this.title = title;
            this.description = description;
            this.severity = severity;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Severity getSeverity() { return severity; }
    }
}
