package ltd.idcu.est.circuitbreaker.api;

import java.util.function.Supplier;

public interface CircuitBreaker {
    String getName();

    CircuitState getState();

    <T> T execute(Supplier<T> supplier) throws Exception;

    <T> T execute(Supplier<T> supplier, Supplier<T> fallback) throws Exception;

    void execute(Runnable runnable) throws Exception;

    void execute(Runnable runnable, Runnable fallback) throws Exception;

    void reset();

    CircuitBreakerMetrics getMetrics();
}
