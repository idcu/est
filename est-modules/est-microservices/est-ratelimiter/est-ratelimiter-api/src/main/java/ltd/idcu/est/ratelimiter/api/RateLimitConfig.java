package ltd.idcu.est.ratelimiter.api;

public class RateLimitConfig {
    private final int permitsPerSecond;
    private final int burstCapacity;
    private final RateLimitAlgorithm algorithm;
    private final long warmupPeriodMs;

    public RateLimitConfig() {
        this(100, 100, RateLimitAlgorithm.TOKEN_BUCKET, 0);
    }

    public RateLimitConfig(int permitsPerSecond) {
        this(permitsPerSecond, permitsPerSecond, RateLimitAlgorithm.TOKEN_BUCKET, 0);
    }

    public RateLimitConfig(int permitsPerSecond, int burstCapacity) {
        this(permitsPerSecond, burstCapacity, RateLimitAlgorithm.TOKEN_BUCKET, 0);
    }

    public RateLimitConfig(int permitsPerSecond, int burstCapacity, RateLimitAlgorithm algorithm) {
        this(permitsPerSecond, burstCapacity, algorithm, 0);
    }

    public RateLimitConfig(int permitsPerSecond, int burstCapacity, RateLimitAlgorithm algorithm, long warmupPeriodMs) {
        if (permitsPerSecond <= 0) {
            throw new IllegalArgumentException("permitsPerSecond must be positive");
        }
        if (burstCapacity < permitsPerSecond) {
            throw new IllegalArgumentException("burstCapacity must be >= permitsPerSecond");
        }
        this.permitsPerSecond = permitsPerSecond;
        this.burstCapacity = burstCapacity;
        this.algorithm = algorithm;
        this.warmupPeriodMs = warmupPeriodMs;
    }

    public int getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public int getBurstCapacity() {
        return burstCapacity;
    }

    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }

    public long getWarmupPeriodMs() {
        return warmupPeriodMs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int permitsPerSecond = 100;
        private int burstCapacity = 100;
        private RateLimitAlgorithm algorithm = RateLimitAlgorithm.TOKEN_BUCKET;
        private long warmupPeriodMs = 0;

        public Builder permitsPerSecond(int permitsPerSecond) {
            this.permitsPerSecond = permitsPerSecond;
            return this;
        }

        public Builder burstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        public Builder algorithm(RateLimitAlgorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder warmupPeriodMs(long warmupPeriodMs) {
            this.warmupPeriodMs = warmupPeriodMs;
            return this;
        }

        public RateLimitConfig build() {
            return new RateLimitConfig(permitsPerSecond, burstCapacity, algorithm, warmupPeriodMs);
        }
    }
}
