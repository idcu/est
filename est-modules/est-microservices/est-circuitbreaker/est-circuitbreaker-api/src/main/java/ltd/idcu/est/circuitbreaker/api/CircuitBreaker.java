package ltd.idcu.est.circuitbreaker.api;

import java.util.function.Supplier;

public interface CircuitBreaker {
    String getName();

    CircuitState getState();

    <T> T execute(Supplier<T> supplier) throws Exception;

    void execute(Runnable runnable) throws Exception;

    void reset();

    CircuitBreakerMetrics getMetrics();
}
