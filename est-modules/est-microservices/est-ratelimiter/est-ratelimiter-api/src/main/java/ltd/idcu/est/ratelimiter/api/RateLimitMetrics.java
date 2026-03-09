package ltd.idcu.est.ratelimiter.api;

public class RateLimitMetrics {
    private final long totalRequests;
    private final long allowedRequests;
    private final long rejectedRequests;
    private final double successRate;
    private final double averageWaitTimeMs;

    public RateLimitMetrics(long totalRequests, long allowedRequests, long rejectedRequests, 
                           double successRate, double averageWaitTimeMs) {
        this.totalRequests = totalRequests;
        this.allowedRequests = allowedRequests;
        this.rejectedRequests = rejectedRequests;
        this.successRate = successRate;
        this.averageWaitTimeMs = averageWaitTimeMs;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public long getAllowedRequests() {
        return allowedRequests;
    }

    public long getRejectedRequests() {
        return rejectedRequests;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public double getAverageWaitTimeMs() {
        return averageWaitTimeMs;
    }

    @Override
    public String toString() {
        return "RateLimitMetrics{" +
                "totalRequests=" + totalRequests +
                ", allowed=" + allowedRequests +
                ", rejected=" + rejectedRequests +
                ", successRate=" + String.format("%.1f", successRate) + "%" +
                ", avgWaitTime=" + String.format("%.2f", averageWaitTimeMs) + "ms" +
                '}';
    }
}
