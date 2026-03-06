package ltd.idcu.est.features.performance.api;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RequestMetrics {
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    private final AtomicLong maxResponseTime = new AtomicLong(0);
    private final AtomicLong minResponseTime = new AtomicLong(Long.MAX_VALUE);
    private final ConcurrentHashMap<Integer, AtomicLong> statusCodes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> pathCounts = new ConcurrentHashMap<>();
    private volatile long startTime = System.currentTimeMillis();

    public RequestMetrics() {
    }

    public void recordRequest(String path, int statusCode, long responseTimeMs) {
        totalRequests.incrementAndGet();
        
        if (statusCode >= 200 && statusCode < 400) {
            successfulRequests.incrementAndGet();
        } else {
            failedRequests.incrementAndGet();
        }

        totalResponseTime.addAndGet(responseTimeMs);
        
        long currentMax;
        do {
            currentMax = maxResponseTime.get();
            if (responseTimeMs <= currentMax) break;
        } while (!maxResponseTime.compareAndSet(currentMax, responseTimeMs));
        
        long currentMin;
        do {
            currentMin = minResponseTime.get();
            if (responseTimeMs >= currentMin) break;
        } while (!minResponseTime.compareAndSet(currentMin, responseTimeMs));

        statusCodes.computeIfAbsent(statusCode, k -> new AtomicLong(0)).incrementAndGet();
        pathCounts.computeIfAbsent(path, k -> new AtomicLong(0)).incrementAndGet();
    }

    public long getTotalRequests() {
        return totalRequests.get();
    }

    public long getSuccessfulRequests() {
        return successfulRequests.get();
    }

    public long getFailedRequests() {
        return failedRequests.get();
    }

    public double getSuccessRate() {
        long total = totalRequests.get();
        if (total == 0) return 100.0;
        return (double) successfulRequests.get() / total * 100;
    }

    public double getAverageResponseTime() {
        long total = totalRequests.get();
        if (total == 0) return 0.0;
        return (double) totalResponseTime.get() / total;
    }

    public long getMaxResponseTime() {
        long max = maxResponseTime.get();
        return max == 0 ? 0 : max;
    }

    public long getMinResponseTime() {
        long min = minResponseTime.get();
        return min == Long.MAX_VALUE ? 0 : min;
    }

    public long getUptimeMs() {
        return System.currentTimeMillis() - startTime;
    }

    public double getRequestsPerSecond() {
        long uptime = getUptimeMs();
        if (uptime == 0) return 0.0;
        return (double) totalRequests.get() / (uptime / 1000.0);
    }

    public ConcurrentHashMap<Integer, AtomicLong> getStatusCodes() {
        return new ConcurrentHashMap<>(statusCodes);
    }

    public ConcurrentHashMap<String, AtomicLong> getPathCounts() {
        return new ConcurrentHashMap<>(pathCounts);
    }

    public void reset() {
        totalRequests.set(0);
        successfulRequests.set(0);
        failedRequests.set(0);
        totalResponseTime.set(0);
        maxResponseTime.set(0);
        minResponseTime.set(Long.MAX_VALUE);
        statusCodes.clear();
        pathCounts.clear();
        startTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "RequestMetrics{" +
                "totalRequests=" + totalRequests.get() +
                ", successful=" + successfulRequests.get() +
                ", failed=" + failedRequests.get() +
                ", successRate=" + String.format("%.1f", getSuccessRate()) + "%" +
                ", avgResponseTime=" + String.format("%.2f", getAverageResponseTime()) + "ms" +
                ", maxResponseTime=" + getMaxResponseTime() + "ms" +
                ", minResponseTime=" + getMinResponseTime() + "ms" +
                ", rps=" + String.format("%.2f", getRequestsPerSecond()) +
                '}';
    }
}
