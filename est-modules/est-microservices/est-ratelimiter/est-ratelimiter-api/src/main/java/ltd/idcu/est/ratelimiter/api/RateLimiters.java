package ltd.idcu.est.ratelimiter.api;

public class RateLimiters {
    private static final RateLimiterRegistry DEFAULT_REGISTRY = new DefaultRateLimiterRegistry();

    private RateLimiters() {
    }

    public static RateLimiter create(String name) {
        return create(name, new RateLimitConfig());
    }

    public static RateLimiter create(String name, int permitsPerSecond) {
        return create(name, new RateLimitConfig(permitsPerSecond));
    }

    public static RateLimiter create(String name, RateLimitConfig config) {
        return new TokenBucketRateLimiter(name, config);
    }

    public static RateLimiterRegistry createRegistry() {
        return new DefaultRateLimiterRegistry();
    }

    public static RateLimiterRegistry getDefaultRegistry() {
        return DEFAULT_REGISTRY;
    }

    public static RateLimiter getOrCreate(String name, RateLimitConfig config) {
        return DEFAULT_REGISTRY.getOrCreate(name, config);
    }

    public static RateLimiter get(String name) {
        return DEFAULT_REGISTRY.get(name);
    }
}
