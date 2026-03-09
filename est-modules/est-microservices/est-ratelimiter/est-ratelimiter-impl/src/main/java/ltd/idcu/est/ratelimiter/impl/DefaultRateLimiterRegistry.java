package ltd.idcu.est.ratelimiter.impl;

import ltd.idcu.est.ratelimiter.api.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRateLimiterRegistry implements RateLimiterRegistry {
    private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    @Override
    public RateLimiter getOrCreate(String name, RateLimitConfig config) {
        return rateLimiters.computeIfAbsent(name, k -> createRateLimiter(name, config));
    }

    @Override
    public RateLimiter get(String name) {
        return rateLimiters.get(name);
    }

    @Override
    public void register(RateLimiter rateLimiter) {
        rateLimiters.put(rateLimiter.getName(), rateLimiter);
    }

    @Override
    public void unregister(String name) {
        rateLimiters.remove(name);
    }

    @Override
    public Iterable<RateLimiter> getAll() {
        return rateLimiters.values();
    }

    @Override
    public void clear() {
        rateLimiters.clear();
    }

    private RateLimiter createRateLimiter(String name, RateLimitConfig config) {
        switch (config.getAlgorithm()) {
            case TOKEN_BUCKET:
            case LEAKY_BUCKET:
            case FIXED_WINDOW:
            case SLIDING_WINDOW:
            default:
                return new TokenBucketRateLimiter(name, config);
        }
    }
}
