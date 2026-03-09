package ltd.idcu.est.ratelimiter.api;

import java.util.function.Supplier;

public interface RateLimiter {
    String getName();

    RateLimitConfig getConfig();

    boolean tryAcquire();

    boolean tryAcquire(int permits);

    void acquire() throws InterruptedException;

    void acquire(int permits) throws InterruptedException;

    <T> T execute(Supplier<T> supplier) throws RateLimitExceededException;

    void execute(Runnable runnable) throws RateLimitExceededException;

    <T> T execute(Supplier<T> supplier, Supplier<T> fallback);

    void execute(Runnable runnable, Runnable fallback);

    RateLimitMetrics getMetrics();

    void reset();

    class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }

        public RateLimitExceededException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
