package ltd.idcu.est.ratelimiter.api;

import java.util.function.Supplier;

public interface FallbackStrategy<T> {
    
    T handleRateLimitExceeded(String resource, RateLimiter rateLimiter);
    
    static <T> FallbackStrategy<T> fixed(T defaultValue) {
        return (resource, rateLimiter) -> defaultValue;
    }
    
    static <T> FallbackStrategy<T> supplier(Supplier<T> supplier) {
        return (resource, rateLimiter) -> supplier.get();
    }
    
    static <T> FallbackStrategy<T> delayRetry(long delayMs) {
        return (resource, rateLimiter) -> {
            try {
                Thread.sleep(delayMs);
                return null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        };
    }
    
    static <T> FallbackStrategy<T> throwException(RuntimeException exception) {
        return (resource, rateLimiter) -> {
            throw exception;
        };
    }
}
