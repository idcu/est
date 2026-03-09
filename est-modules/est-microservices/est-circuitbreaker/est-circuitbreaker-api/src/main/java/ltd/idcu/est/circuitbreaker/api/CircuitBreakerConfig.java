package ltd.idcu.est.circuitbreaker.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CircuitBreakerConfig {
    private final int failureThreshold;
    private final long waitDurationMs;
    private final int successThreshold;
    private final long timeoutMs;
    private final int slidingWindowSize;
    private final long slidingWindowDurationMs;
    private final double failureRateThreshold;
    private final List<Predicate<Throwable>> recordFailurePredicates;
    private final List<Predicate<Throwable>> ignoreFailurePredicates;

    public CircuitBreakerConfig() {
        this(5, 30000, 3, 5000, 100, 60000, 0.5, new ArrayList<>(), new ArrayList<>());
    }

    public CircuitBreakerConfig(int failureThreshold, long waitDurationMs, int successThreshold, long timeoutMs) {
        this(failureThreshold, waitDurationMs, successThreshold, timeoutMs, 100, 60000, 0.5, new ArrayList<>(), new ArrayList<>());
    }

    public CircuitBreakerConfig(int failureThreshold, long waitDurationMs, int successThreshold, long timeoutMs,
                                 int slidingWindowSize, long slidingWindowDurationMs, double failureRateThreshold,
                                 List<Predicate<Throwable>> recordFailurePredicates,
                                 List<Predicate<Throwable>> ignoreFailurePredicates) {
        this.failureThreshold = failureThreshold;
        this.waitDurationMs = waitDurationMs;
        this.successThreshold = successThreshold;
        this.timeoutMs = timeoutMs;
        this.slidingWindowSize = slidingWindowSize;
        this.slidingWindowDurationMs = slidingWindowDurationMs;
        this.failureRateThreshold = failureRateThreshold;
        this.recordFailurePredicates = recordFailurePredicates;
        this.ignoreFailurePredicates = ignoreFailurePredicates;
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

    public int getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public long getSlidingWindowDurationMs() {
        return slidingWindowDurationMs;
    }

    public double getFailureRateThreshold() {
        return failureRateThreshold;
    }

    public List<Predicate<Throwable>> getRecordFailurePredicates() {
        return new ArrayList<>(recordFailurePredicates);
    }

    public List<Predicate<Throwable>> getIgnoreFailurePredicates() {
        return new ArrayList<>(ignoreFailurePredicates);
    }

    public boolean shouldRecordFailure(Throwable throwable) {
        if (!ignoreFailurePredicates.isEmpty()) {
            for (Predicate<Throwable> predicate : ignoreFailurePredicates) {
                if (predicate.test(throwable)) {
                    return false;
                }
            }
        }
        if (!recordFailurePredicates.isEmpty()) {
            for (Predicate<Throwable> predicate : recordFailurePredicates) {
                if (predicate.test(throwable)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int failureThreshold = 5;
        private long waitDurationMs = 30000;
        private int successThreshold = 3;
        private long timeoutMs = 5000;
        private int slidingWindowSize = 100;
        private long slidingWindowDurationMs = 60000;
        private double failureRateThreshold = 0.5;
        private final List<Predicate<Throwable>> recordFailurePredicates = new ArrayList<>();
        private final List<Predicate<Throwable>> ignoreFailurePredicates = new ArrayList<>();

        public Builder failureThreshold(int failureThreshold) {
            this.failureThreshold = failureThreshold;
            return this;
        }

        public Builder waitDurationMs(long waitDurationMs) {
            this.waitDurationMs = waitDurationMs;
            return this;
        }

        public Builder successThreshold(int successThreshold) {
            this.successThreshold = successThreshold;
            return this;
        }

        public Builder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder slidingWindowSize(int slidingWindowSize) {
            this.slidingWindowSize = slidingWindowSize;
            return this;
        }

        public Builder slidingWindowDurationMs(long slidingWindowDurationMs) {
            this.slidingWindowDurationMs = slidingWindowDurationMs;
            return this;
        }

        public Builder failureRateThreshold(double failureRateThreshold) {
            this.failureRateThreshold = failureRateThreshold;
            return this;
        }

        public Builder recordFailure(Predicate<Throwable> predicate) {
            this.recordFailurePredicates.add(predicate);
            return this;
        }

        public Builder ignoreFailure(Predicate<Throwable> predicate) {
            this.ignoreFailurePredicates.add(predicate);
            return this;
        }

        public CircuitBreakerConfig build() {
            return new CircuitBreakerConfig(failureThreshold, waitDurationMs, successThreshold, timeoutMs,
                    slidingWindowSize, slidingWindowDurationMs, failureRateThreshold,
                    recordFailurePredicates, ignoreFailurePredicates);
        }
    }
}
