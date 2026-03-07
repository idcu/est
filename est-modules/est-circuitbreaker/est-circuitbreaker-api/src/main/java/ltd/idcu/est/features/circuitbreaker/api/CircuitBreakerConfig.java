package ltd.idcu.est.features.circuitbreaker.api;

public class CircuitBreakerConfig {
    private final int failureThreshold;
    private final long waitDurationMs;
    private final int successThreshold;
    private final long timeoutMs;

    public CircuitBreakerConfig() {
        this(5, 30000, 3, 5000);
    }

    public CircuitBreakerConfig(int failureThreshold, long waitDurationMs, int successThreshold, long timeoutMs) {
        this.failureThreshold = failureThreshold;
        this.waitDurationMs = waitDurationMs;
        this.successThreshold = successThreshold;
        this.timeoutMs = timeoutMs;
    }

    public int getFailureThreshold() {
        return failureThreshold;
    }

    public long getWaitDurationMs() {
        return waitDurationMs;
    }

    public int getSuccessThreshold() {
        return successThreshold;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }
}
