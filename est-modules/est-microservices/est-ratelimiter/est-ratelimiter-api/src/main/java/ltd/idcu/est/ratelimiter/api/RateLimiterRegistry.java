package ltd.idcu.est.ratelimiter.api;

public interface RateLimiterRegistry {
    RateLimiter getOrCreate(String name, RateLimitConfig config);

    RateLimiter get(String name);

    void register(RateLimiter rateLimiter);

    void unregister(String name);

    Iterable<RateLimiter> getAll();

    void clear();
}
