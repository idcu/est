package ltd.idcu.est.circuitbreaker.api;

public class CircuitBreakerConfig {
    private final int failureThreshold;
    private final long waitDurationMs;
    private final int successThreshold;
    private final long timeoutMs;
    private final long slidingWindowMs;
    private final int slidingWindowSize;

    public CircuitBreakerConfig() {
        this(5, 30000, 3, 5000, 60000, 10);
    }

    public CircuitBreakerConfig(int failureThreshold, long waitDurationMs, int successThreshold, long timeoutMs) {
        this(failureThreshold, waitDurationMs, successThreshold, timeoutMs, 60000, 10);
    }

    public CircuitBreakerConfig(int failureThreshold, long waitDurationMs, int successThreshold, long timeoutMs,
                               long slidingWindowMs, int slidingWindowSize) {
        this.failureThreshold = failureThreshold;
        this.waitDurationMs = waitDurationMs;
        this.successThreshold = successThreshold;
        this.timeoutMs = timeoutMs;
        this.slidingWindowMs = slidingWindowMs;
        this.slidingWindowSize = slidingWindowSize;
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

    public long getSlidingWindowMs() {
        return slidingWindowMs;
    }

    public int getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int failureThreshold = 5;
        private long waitDurationMs = 30000;
        private int successThreshold = 3;
        private long timeoutMs = 5000;
        private long slidingWindowMs = 60000;
        private int slidingWindowSize = 10;

        public Builder failureThreshold(int failureThreshold) {
            this.failureThreshold = failureThreshold;
            return this;
        }

        public Builder waitDuration(long waitDurationMs) {
            this.waitDurationMs = waitDurationMs;
            return this;
        }

        public Builder successThreshold(int successThreshold) {
            this.successThreshold = successThreshold;
            return this;
        }

        public Builder timeout(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder slidingWindow(long slidingWindowMs) {
            this.slidingWindowMs = slidingWindowMs;
            return this;
        }

        public Builder slidingWindowSize(int slidingWindowSize) {
            this.slidingWindowSize = slidingWindowSize;
            return this;
        }

        public CircuitBreakerConfig build() {
            return new CircuitBreakerConfig(failureThreshold, waitDurationMs, successThreshold, timeoutMs,
                                          slidingWindowMs, slidingWindowSize);
        }
    }
}
