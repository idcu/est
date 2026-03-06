package ltd.idcu.est.features.circuitbreaker.api;

public class CircuitBreakerMetrics {
    private final long successCount;
    private final long failureCount;
    private final long timeoutCount;
    private final long totalRequests;
    private final long rejectedRequests;
    private final long lastFailureTime;
    private final long lastSuccessTime;

    public CircuitBreakerMetrics(long successCount, long failureCount, long timeoutCount, 
                                  long totalRequests, long rejectedRequests, 
                                  long lastFailureTime, long lastSuccessTime) {
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.timeoutCount = timeoutCount;
        this.totalRequests = totalRequests;
        this.rejectedRequests = rejectedRequests;
        this.lastFailureTime = lastFailureTime;
        this.lastSuccessTime = lastSuccessTime;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public long getTimeoutCount() {
        return timeoutCount;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public long getRejectedRequests() {
        return rejectedRequests;
    }

    public long getLastFailureTime() {
        return lastFailureTime;
    }

    public long getLastSuccessTime() {
        return lastSuccessTime;
    }

    public double getFailureRate() {
        if (totalRequests == 0) {
            return 0.0;
        }
        return (double) (failureCount + timeoutCount) / totalRequests;
    }

    @Override
    public String toString() {
        return "CircuitBreakerMetrics{" +
                "successCount=" + successCount +
                ", failureCount=" + failureCount +
                ", timeoutCount=" + timeoutCount +
                ", totalRequests=" + totalRequests +
                ", rejectedRequests=" + rejectedRequests +
                ", failureRate=" + String.format("%.2f%%", getFailureRate() * 100) +
                '}';
    }
}
