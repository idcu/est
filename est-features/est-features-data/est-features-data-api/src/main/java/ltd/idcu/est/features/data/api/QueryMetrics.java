package ltd.idcu.est.features.data.api;

import java.util.concurrent.atomic.AtomicLong;

public class QueryMetrics {
    private final AtomicLong queryCount = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final AtomicLong maxExecutionTime = new AtomicLong(0);
    private final AtomicLong minExecutionTime = new AtomicLong(Long.MAX_VALUE);
    
    public void recordQuery(long executionTimeMs) {
        queryCount.incrementAndGet();
        totalExecutionTime.addAndGet(executionTimeMs);
        
        long currentMax;
        do {
            currentMax = maxExecutionTime.get();
            if (executionTimeMs <= currentMax) break;
        } while (!maxExecutionTime.compareAndSet(currentMax, executionTimeMs));
        
        long currentMin;
        do {
            currentMin = minExecutionTime.get();
            if (executionTimeMs >= currentMin) break;
        } while (!minExecutionTime.compareAndSet(currentMin, executionTimeMs));
    }
    
    public long getQueryCount() {
        return queryCount.get();
    }
    
    public long getTotalExecutionTime() {
        return totalExecutionTime.get();
    }
    
    public long getMaxExecutionTime() {
        return maxExecutionTime.get() == Long.MAX_VALUE ? 0 : maxExecutionTime.get();
    }
    
    public long getMinExecutionTime() {
        return minExecutionTime.get() == Long.MAX_VALUE ? 0 : minExecutionTime.get();
    }
    
    public double getAverageExecutionTime() {
        long count = queryCount.get();
        return count == 0 ? 0 : (double) totalExecutionTime.get() / count;
    }
    
    public void reset() {
        queryCount.set(0);
        totalExecutionTime.set(0);
        maxExecutionTime.set(0);
        minExecutionTime.set(Long.MAX_VALUE);
    }
    
    @Override
    public String toString() {
        return String.format(
            "QueryMetrics{count=%d, total=%dms, avg=%.2fms, min=%dms, max=%dms}",
            getQueryCount(),
            getTotalExecutionTime(),
            getAverageExecutionTime(),
            getMinExecutionTime(),
            getMaxExecutionTime()
        );
    }
}
