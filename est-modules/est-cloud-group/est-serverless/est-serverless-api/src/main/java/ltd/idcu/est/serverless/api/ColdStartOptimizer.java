package ltd.idcu.est.serverless.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ColdStartOptimizer {
    
    private static final ColdStartOptimizer INSTANCE = new ColdStartOptimizer();
    
    private final Map<String, WarmupInfo> warmupInfo = new ConcurrentHashMap<>();
    private final AtomicLong totalColdStarts = new AtomicLong(0);
    private final AtomicLong totalWarmStarts = new AtomicLong(0);
    
    private ColdStartOptimizer() {
    }
    
    public static ColdStartOptimizer getInstance() {
        return INSTANCE;
    }
    
    public void recordColdStart(String functionName) {
        totalColdStarts.incrementAndGet();
        warmupInfo.computeIfAbsent(functionName, k -> new WarmupInfo())
            .recordColdStart();
    }
    
    public void recordWarmStart(String functionName) {
        totalWarmStarts.incrementAndGet();
        warmupInfo.computeIfAbsent(functionName, k -> new WarmupInfo())
            .recordWarmStart();
    }
    
    public boolean shouldWarmup(String functionName) {
        WarmupInfo info = warmupInfo.get(functionName);
        if (info == null) {
            return true;
        }
        return info.shouldWarmup();
    }
    
    public void preWarm(String functionName, Runnable warmupTask) {
        try {
            warmupTask.run();
            warmupInfo.computeIfAbsent(functionName, k -> new WarmupInfo())
                .markWarmed();
        } catch (Exception e) {
            System.err.println("Warmup failed for " + functionName + ": " + e.getMessage());
        }
    }
    
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("totalColdStarts", totalColdStarts.get());
        stats.put("totalWarmStarts", totalWarmStarts.get());
        stats.put("coldStartRate", calculateColdStartRate());
        stats.put("functions", warmupInfo);
        return stats;
    }
    
    private double calculateColdStartRate() {
        long total = totalColdStarts.get() + totalWarmStarts.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) totalColdStarts.get() / total;
    }
    
    public void reset() {
        warmupInfo.clear();
        totalColdStarts.set(0);
        totalWarmStarts.set(0);
    }
    
    private static class WarmupInfo {
        private final AtomicLong coldStarts = new AtomicLong(0);
        private final AtomicLong warmStarts = new AtomicLong(0);
        private volatile long lastWarmTime = 0;
        private static final long WARM_THRESHOLD_MS = 5 * 60 * 1000;
        
        void recordColdStart() {
            coldStarts.incrementAndGet();
        }
        
        void recordWarmStart() {
            warmStarts.incrementAndGet();
        }
        
        void markWarmed() {
            lastWarmTime = System.currentTimeMillis();
        }
        
        boolean shouldWarmup() {
            long now = System.currentTimeMillis();
            return (now - lastWarmTime) > WARM_THRESHOLD_MS;
        }
        
        @Override
        public String toString() {
            return "WarmupInfo{cold=" + coldStarts.get() + 
                   ", warm=" + warmStarts.get() + 
                   ", rate=" + calculateRate() + "}";
        }
        
        private double calculateRate() {
            long total = coldStarts.get() + warmStarts.get();
            if (total == 0) return 0.0;
            return (double) coldStarts.get() / total;
        }
    }
}
