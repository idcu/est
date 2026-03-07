package ltd.idcu.est.circuitbreaker.impl;

import ltd.idcu.est.circuitbreaker.api.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class SlidingWindowCircuitBreaker implements CircuitBreaker {
    private final String name;
    private final CircuitBreakerConfig config;
    private final AtomicReference<CircuitState> state = new AtomicReference<>(CircuitState.CLOSED);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Queue<RequestRecord> requestWindow = new LinkedList<>();
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong rejectedRequests = new AtomicLong(0);
    private final AtomicLong halfOpenSuccessCount = new AtomicLong(0);
    private final AtomicLong openTime = new AtomicLong(0);

    private static class RequestRecord {
        final long timestamp;
        final boolean success;

        RequestRecord(long timestamp, boolean success) {
            this.timestamp = timestamp;
            this.success = success;
        }
    }

    public SlidingWindowCircuitBreaker(String name) {
        this(name, new CircuitBreakerConfig());
    }

    public SlidingWindowCircuitBreaker(String name, CircuitBreakerConfig config) {
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
            throw new DefaultCircuitBreaker.CircuitBreakerOpenException("Circuit breaker is open: " + name);
        }

        long startTime = System.currentTimeMillis();
        try {
            T result = supplier.get();
            recordSuccess(startTime);
            return result;
        } catch (Exception e) {
            recordFailure(startTime);
            throw e;
        }
    }

    @Override
    public <T> T execute(Supplier<T> supplier, Supplier<T> fallback) {
        totalRequests.incrementAndGet();
        checkAndTransitionState();

        CircuitState currentState = state.get();
        if (currentState == CircuitState.OPEN) {
            rejectedRequests.incrementAndGet();
            return fallback.get();
        }

        long startTime = System.currentTimeMillis();
        try {
            T result = supplier.get();
            recordSuccess(startTime);
            return result;
        } catch (Exception e) {
            recordFailure(startTime);
            return fallback.get();
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
    public void execute(Runnable runnable, Runnable fallback) {
        this.execute(() -> {
            runnable.run();
            return null;
        }, () -> {
            fallback.run();
            return null;
        });
    }

    @Override
    public void reset() {
        lock.writeLock().lock();
        try {
            state.set(CircuitState.CLOSED);
            requestWindow.clear();
            totalRequests.set(0);
            rejectedRequests.set(0);
            halfOpenSuccessCount.set(0);
            openTime.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public CircuitBreakerMetrics getMetrics() {
        lock.readLock().lock();
        try {
            long successCount = requestWindow.stream().filter(r -> r.success).count();
            long failureCount = requestWindow.size() - successCount;
            long lastSuccessTime = requestWindow.stream()
                    .filter(r -> r.success)
                    .mapToLong(r -> r.timestamp)
                    .max()
                    .orElse(0);
            long lastFailureTime = requestWindow.stream()
                    .filter(r -> !r.success)
                    .mapToLong(r -> r.timestamp)
                    .max()
                    .orElse(0);

            return new CircuitBreakerMetrics(
                    successCount,
                    failureCount,
                    0,
                    totalRequests.get(),
                    rejectedRequests.get(),
                    lastFailureTime,
                    lastSuccessTime
            );
        } finally {
            lock.readLock().unlock();
        }
    }

    private void recordSuccess(long timestamp) {
        lock.writeLock().lock();
        try {
            requestWindow.add(new RequestRecord(timestamp, true));
            cleanupOldRequests(timestamp);

            CircuitState currentState = state.get();
            if (currentState == CircuitState.HALF_OPEN) {
                long currentHalfOpenSuccesses = halfOpenSuccessCount.incrementAndGet();
                if (currentHalfOpenSuccesses >= config.getSuccessThreshold()) {
                    state.set(CircuitState.CLOSED);
                    halfOpenSuccessCount.set(0);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void recordFailure(long timestamp) {
        lock.writeLock().lock();
        try {
            requestWindow.add(new RequestRecord(timestamp, false));
            cleanupOldRequests(timestamp);

            CircuitState currentState = state.get();
            if (currentState == CircuitState.CLOSED) {
                if (shouldOpenCircuit()) {
                    state.set(CircuitState.OPEN);
                    openTime.set(System.currentTimeMillis());
                }
            } else if (currentState == CircuitState.HALF_OPEN) {
                state.set(CircuitState.OPEN);
                openTime.set(System.currentTimeMillis());
                halfOpenSuccessCount.set(0);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void cleanupOldRequests(long currentTime) {
        long cutoffTime = currentTime - config.getSlidingWindowMs();
        while (!requestWindow.isEmpty() && requestWindow.peek().timestamp < cutoffTime) {
            requestWindow.poll();
        }
    }

    private boolean shouldOpenCircuit() {
        long failureCount = requestWindow.stream().filter(r -> !r.success).count();
        return failureCount >= config.getFailureThreshold();
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
}
