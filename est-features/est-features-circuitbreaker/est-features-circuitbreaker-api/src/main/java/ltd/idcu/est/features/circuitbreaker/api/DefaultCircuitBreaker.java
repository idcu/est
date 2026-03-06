package ltd.idcu.est.features.circuitbreaker.api;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class DefaultCircuitBreaker implements CircuitBreaker {
    private final String name;
    private final CircuitBreakerConfig config;
    private final AtomicReference<CircuitState> state = new AtomicReference<>(CircuitState.CLOSED);
    private final AtomicLong failureCount = new AtomicLong(0);
    private final AtomicLong successCount = new AtomicLong(0);
    private final AtomicLong timeoutCount = new AtomicLong(0);
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong rejectedRequests = new AtomicLong(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);
    private final AtomicLong lastSuccessTime = new AtomicLong(0);
    private final AtomicLong halfOpenSuccessCount = new AtomicLong(0);
    private final AtomicLong openTime = new AtomicLong(0);

    public DefaultCircuitBreaker(String name) {
        this(name, new CircuitBreakerConfig());
    }

    public DefaultCircuitBreaker(String name, CircuitBreakerConfig config) {
        this.name = name;
        this.config = config;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CircuitState getState() {
        checkAndTransitionState();
        return state.get();
    }

    @Override
    public <T> T execute(Supplier<T> supplier) throws Exception {
        totalRequests.incrementAndGet();
        checkAndTransitionState();

        CircuitState currentState = state.get();
        if (currentState == CircuitState.OPEN) {
            rejectedRequests.incrementAndGet();
            throw new CircuitBreakerOpenException("Circuit breaker is open: " + name);
        }

        try {
            T result = supplier.get();
            recordSuccess();
            return result;
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }

    @Override
    public void execute(Runnable runnable) throws Exception {
        this.execute(() -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public void reset() {
        state.set(CircuitState.CLOSED);
        failureCount.set(0);
        successCount.set(0);
        timeoutCount.set(0);
        totalRequests.set(0);
        rejectedRequests.set(0);
        halfOpenSuccessCount.set(0);
        lastFailureTime.set(0);
        lastSuccessTime.set(0);
        openTime.set(0);
    }

    @Override
    public CircuitBreakerMetrics getMetrics() {
        return new CircuitBreakerMetrics(
                successCount.get(),
                failureCount.get(),
                timeoutCount.get(),
                totalRequests.get(),
                rejectedRequests.get(),
                lastFailureTime.get(),
                lastSuccessTime.get()
        );
    }

    private void recordSuccess() {
        successCount.incrementAndGet();
        lastSuccessTime.set(System.currentTimeMillis());

        CircuitState currentState = state.get();
        if (currentState == CircuitState.HALF_OPEN) {
            long currentHalfOpenSuccesses = halfOpenSuccessCount.incrementAndGet();
            if (currentHalfOpenSuccesses >= config.getSuccessThreshold()) {
                state.set(CircuitState.CLOSED);
                failureCount.set(0);
                halfOpenSuccessCount.set(0);
            }
        }
    }

    private void recordFailure() {
        failureCount.incrementAndGet();
        lastFailureTime.set(System.currentTimeMillis());

        CircuitState currentState = state.get();
        if (currentState == CircuitState.CLOSED) {
            long currentFailures = failureCount.get();
            if (currentFailures >= config.getFailureThreshold()) {
                state.set(CircuitState.OPEN);
                openTime.set(System.currentTimeMillis());
            }
        } else if (currentState == CircuitState.HALF_OPEN) {
            state.set(CircuitState.OPEN);
            openTime.set(System.currentTimeMillis());
            halfOpenSuccessCount.set(0);
        }
    }

    private void checkAndTransitionState() {
        CircuitState currentState = state.get();
        if (currentState == CircuitState.OPEN) {
            long now = System.currentTimeMillis();
            if (now - openTime.get() >= config.getWaitDurationMs()) {
                state.set(CircuitState.HALF_OPEN);
                halfOpenSuccessCount.set(0);
            }
        }
    }

    public static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
}
