package ltd.idcu.est.circuitbreaker.api;

import java.util.function.Supplier;

public interface CircuitBreaker {
    String getName();

    CircuitState getState();

    CircuitBreakerConfig getConfig();

    <T> T execute(Supplier<T> supplier) throws Exception;

    void execute(Runnable runnable) throws Exception;

    <T> T execute(Supplier<T> supplier, Supplier<T> fallback);

    void execute(Runnable runnable, Runnable fallback);

    void reset();

    CircuitBreakerMetrics getMetrics();

    void addListener(CircuitBreakerListener listener);

    void removeListener(CircuitBreakerListener listener);
}
