package ltd.idcu.est.circuitbreaker.impl;

import ltd.idcu.est.circuitbreaker.api.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final List<CircuitBreakerListener> listeners = new CopyOnWriteArrayList<>();

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
    public CircuitBreakerConfig getConfig() {
        return config;
    }

    @Override
    public CircuitState getState() {
        checkAndTransitionState();
        return state.get();
    }

    @Override
    public <T> T execute(Supplier<T> supplier) throws Exception {
        long startTime = System.currentTimeMillis();
        totalRequests.incrementAndGet();
        checkAndTransitionState();

        CircuitState currentState = state.get();
        if (currentState == CircuitState.OPEN) {
            rejectedRequests.incrementAndGet();
            notifyOnReject();
            throw new CircuitBreakerOpenException("Circuit breaker is open: " + name);
        }

        try {
            T result = supplier.get();
            long duration = System.currentTimeMillis() - startTime;
            recordSuccess(duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            if (config.shouldRecordFailure(e)) {
                recordFailure(e, duration);
            } else {
                recordSuccess(duration);
            }
            throw e;
        }
    }

    @Override
    public <T> T execute(Supplier<T> supplier, Supplier<T> fallback) {
        long startTime = System.currentTimeMillis();
        totalRequests.incrementAndGet();
        checkAndTransitionState();

        CircuitState currentState = state.get();
        if (currentState == CircuitState.OPEN) {
            rejectedRequests.incrementAndGet();
            notifyOnReject();
            return fallback.get();
        }

        try {
            T result = supplier.get();
            long duration = System.currentTimeMillis() - startTime;
            recordSuccess(duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            if (config.shouldRecordFailure(e)) {
                recordFailure(e, duration);
            } else {
                recordSuccess(duration);
            }
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
        CircuitState oldState = state.get();
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
        totalExecutionTime.set(0);
        notifyOnStateChange(oldState, CircuitState.CLOSED);
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

    @Override
    public void addListener(CircuitBreakerListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(CircuitBreakerListener listener) {
        listeners.remove(listener);
    }

    private void recordSuccess(long durationMs) {
        successCount.incrementAndGet();
        totalExecutionTime.addAndGet(durationMs);
        lastSuccessTime.set(System.currentTimeMillis());
        notifyOnSuccess(durationMs);

        CircuitState currentState = state.get();
        if (currentState == CircuitState.HALF_OPEN) {
            long currentHalfOpenSuccesses = halfOpenSuccessCount.incrementAndGet();
            if (currentHalfOpenSuccesses >= config.getSuccessThreshold()) {
                transitionState(CircuitState.CLOSED);
                failureCount.set(0);
                halfOpenSuccessCount.set(0);
            }
        }
    }

    private void recordFailure(Throwable throwable, long durationMs) {
        failureCount.incrementAndGet();
        totalExecutionTime.addAndGet(durationMs);
        lastFailureTime.set(System.currentTimeMillis());
        notifyOnFailure(throwable, durationMs);

        CircuitState currentState = state.get();
        if (currentState == CircuitState.CLOSED) {
            long currentFailures = failureCount.get();
            if (currentFailures >= config.getFailureThreshold()) {
                transitionState(CircuitState.OPEN);
                openTime.set(System.currentTimeMillis());
            }
        } else if (currentState == CircuitState.HALF_OPEN) {
            transitionState(CircuitState.OPEN);
            openTime.set(System.currentTimeMillis());
            halfOpenSuccessCount.set(0);
        }
    }

    private void checkAndTransitionState() {
        CircuitState currentState = state.get();
        if (currentState == CircuitState.OPEN) {
            long now = System.currentTimeMillis();
            if (now - openTime.get() >= config.getWaitDurationMs()) {
                transitionState(CircuitState.HALF_OPEN);
                halfOpenSuccessCount.set(0);
            }
        }
    }

    private void transitionState(CircuitState newState) {
        CircuitState oldState = state.getAndSet(newState);
        if (oldState != newState) {
            notifyOnStateChange(oldState, newState);
        }
    }

    private void notifyOnStateChange(CircuitState oldState, CircuitState newState) {
        for (CircuitBreakerListener listener : listeners) {
            try {
                listener.onStateChange(name, oldState, newState);
            } catch (Exception e) {
            }
        }
    }

    private void notifyOnSuccess(long durationMs) {
        for (CircuitBreakerListener listener : listeners) {
            try {
                listener.onSuccess(name, durationMs);
            } catch (Exception e) {
            }
        }
    }

    private void notifyOnFailure(Throwable throwable, long durationMs) {
        for (CircuitBreakerListener listener : listeners) {
            try {
                listener.onFailure(name, throwable, durationMs);
            } catch (Exception e) {
            }
        }
    }

    private void notifyOnReject() {
        for (CircuitBreakerListener listener : listeners) {
            try {
                listener.onReject(name);
            } catch (Exception e) {
            }
        }
    }

    public static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\":\"").append(escapeJson(name)).append("\",");
        sb.append("\"state\":\"").append(state.get().name()).append("\",");
        sb.append("\"failureThreshold\":").append(config.getFailureThreshold()).append(",");
        sb.append("\"waitDurationMs\":").append(config.getWaitDurationMs()).append(",");
        sb.append("\"successThreshold\":").append(config.getSuccessThreshold()).append(",");
        sb.append("\"timeoutMs\":").append(config.getTimeoutMs()).append(",");
        sb.append("\"failureCount\":").append(failureCount.get()).append(",");
        sb.append("\"successCount\":").append(successCount.get()).append(",");
        sb.append("\"timeoutCount\":").append(timeoutCount.get()).append(",");
        sb.append("\"totalRequests\":").append(totalRequests.get()).append(",");
        sb.append("\"rejectedRequests\":").append(rejectedRequests.get()).append(",");
        sb.append("\"lastFailureTime\":").append(lastFailureTime.get()).append(",");
        sb.append("\"lastSuccessTime\":").append(lastSuccessTime.get()).append(",");
        sb.append("\"halfOpenSuccessCount\":").append(halfOpenSuccessCount.get()).append(",");
        sb.append("\"openTime\":").append(openTime.get());
        sb.append("}");
        return sb.toString();
    }

    public static DefaultCircuitBreaker fromJson(String json) {
        String name = extractJsonString(json, "name");
        String stateStr = extractJsonString(json, "state");
        int failureThreshold = Integer.parseInt(extractJsonValue(json, "failureThreshold"));
        long waitDurationMs = Long.parseLong(extractJsonValue(json, "waitDurationMs"));
        int successThreshold = Integer.parseInt(extractJsonValue(json, "successThreshold"));
        long timeoutMs = Long.parseLong(extractJsonValue(json, "timeoutMs"));
        
        CircuitBreakerConfig config = new CircuitBreakerConfig(failureThreshold, waitDurationMs, successThreshold, timeoutMs);
        DefaultCircuitBreaker circuitBreaker = new DefaultCircuitBreaker(name, config);
        
        circuitBreaker.state.set(CircuitState.valueOf(stateStr));
        circuitBreaker.failureCount.set(Long.parseLong(extractJsonValue(json, "failureCount")));
        circuitBreaker.successCount.set(Long.parseLong(extractJsonValue(json, "successCount")));
        circuitBreaker.timeoutCount.set(Long.parseLong(extractJsonValue(json, "timeoutCount")));
        circuitBreaker.totalRequests.set(Long.parseLong(extractJsonValue(json, "totalRequests")));
        circuitBreaker.rejectedRequests.set(Long.parseLong(extractJsonValue(json, "rejectedRequests")));
        circuitBreaker.lastFailureTime.set(Long.parseLong(extractJsonValue(json, "lastFailureTime")));
        circuitBreaker.lastSuccessTime.set(Long.parseLong(extractJsonValue(json, "lastSuccessTime")));
        circuitBreaker.halfOpenSuccessCount.set(Long.parseLong(extractJsonValue(json, "halfOpenSuccessCount")));
        circuitBreaker.openTime.set(Long.parseLong(extractJsonValue(json, "openTime")));
        
        return circuitBreaker;
    }

    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private static String extractJsonString(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int quoteStart = json.indexOf("\"", start);
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        return unescapeJson(json.substring(quoteStart + 1, quoteEnd));
    }

    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }
        return json.substring(start, end).trim();
    }

    private static String unescapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }
}
