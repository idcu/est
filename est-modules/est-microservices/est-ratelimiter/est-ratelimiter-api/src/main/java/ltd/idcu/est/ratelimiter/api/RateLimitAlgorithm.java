package ltd.idcu.est.ratelimiter.api;

public enum RateLimitAlgorithm {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW
}
