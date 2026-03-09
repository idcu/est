package ltd.idcu.est.codecli.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceMonitor {
    
    private static class Metric {
        final AtomicLong count = new AtomicLong(0);
        final AtomicLong totalTime = new AtomicLong(0);
        final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
        final AtomicLong maxTime = new AtomicLong(0);
        
        void record(long timeNs) {
            count.incrementAndGet();
            totalTime.addAndGet(timeNs);
            
            long currentMin;
            do {
                currentMin = minTime.get();
                if (timeNs >= currentMin) break;
            } while (!minTime.compareAndSet(currentMin, timeNs));
            
            long currentMax;
            do {
                currentMax = maxTime.get();
                if (timeNs <= currentMax) break;
            } while (!maxTime.compareAndSet(currentMax, timeNs));
        }
        
        MetricSnapshot snapshot() {
            long cnt = count.get();
            long total = totalTime.get();
            long min = minTime.get();
            long max = maxTime.get();
            double avg = cnt > 0 ? (double) total / cnt : 0;
            return new MetricSnapshot(cnt, total, min == Long.MAX_VALUE ? 0 : min, max, avg);
        }
    }
    
    public record MetricSnapshot(
        long count,
        long totalTimeNs,
        long minTimeNs,
        long maxTimeNs,
        double avgTimeNs
    ) {
        public double totalTimeMs() { return totalTimeNs / 1_000_000.0; }
        public double minTimeMs() { return minTimeNs / 1_000_000.0; }
        public double maxTimeMs() { return maxTimeNs / 1_000_000.0; }
        public double avgTimeMs() { return avgTimeNs / 1_000_000.0; }
    }
    
    private final Map<String, Metric> metrics = new ConcurrentHashMap<>();
    private final long startTime = System.nanoTime();
    
    public void record(String operationName, long durationNs) {
        metrics.computeIfAbsent(operationName, k -> new Metric()).record(durationNs);
    }
    
    public <T> T measure(String operationName, java.util.function.Supplier<T> action) {
        long start = System.nanoTime();
        try {
            return action.get();
        } finally {
            record(operationName, System.nanoTime() - start);
        }
    }
    
    public void measure(String operationName, Runnable action) {
        long start = System.nanoTime();
        try {
            action.run();
        } finally {
            record(operationName, System.nanoTime() - start);
        }
    }
    
    public MetricSnapshot getSnapshot(String operationName) {
        Metric metric = metrics.get(operationName);
        return metric != null ? metric.snapshot() : null;
    }
    
    public Map<String, MetricSnapshot> getAllSnapshots() {
        Map<String, MetricSnapshot> result = new ConcurrentHashMap<>();
        metrics.forEach((name, metric) -> result.put(name, metric.snapshot()));
        return result;
    }
    
    public long getUptimeNs() {
        return System.nanoTime() - startTime;
    }
    
    public double getUptimeMs() {
        return getUptimeNs() / 1_000_000.0;
    }
    
    public void reset() {
        metrics.clear();
    }
    
    public void reset(String operationName) {
        metrics.remove(operationName);
    }
    
    public List<String> getOperationNames() {
        return new ArrayList<>(metrics.keySet());
    }
    
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Performance Report ===\n");
        sb.append(String.format("Uptime: %.2f ms\n", getUptimeMs()));
        sb.append("\n");
        
        for (Map.Entry<String, MetricSnapshot> entry : getAllSnapshots().entrySet()) {
            MetricSnapshot snapshot = entry.getValue();
            sb.append(String.format("Operation: %s\n", entry.getKey()));
            sb.append(String.format("  Count: %d\n", snapshot.count()));
            sb.append(String.format("  Total: %.2f ms\n", snapshot.totalTimeMs()));
            sb.append(String.format("  Avg: %.4f ms\n", snapshot.avgTimeMs()));
            sb.append(String.format("  Min: %.4f ms\n", snapshot.minTimeMs()));
            sb.append(String.format("  Max: %.4f ms\n", snapshot.maxTimeMs()));
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
