package ltd.idcu.est.features.cache.api;

import java.util.concurrent.TimeUnit;

public class CacheConfig {
    
    private int maxSize = 1000;
    private long defaultTtl = -1;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    private boolean expireOnAccess = false;
    private boolean refreshOnAccess = false;
    private long cleanupInterval = 60000;
    private String evictionPolicy = "LRU";
    
    public CacheConfig() {
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public CacheConfig setMaxSize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive");
        }
        this.maxSize = maxSize;
        return this;
    }
    
    public long getDefaultTtl() {
        return defaultTtl;
    }
    
    public CacheConfig setDefaultTtl(long defaultTtl) {
        if (defaultTtl < -1) {
            throw new IllegalArgumentException("Default TTL must be >= -1");
        }
        this.defaultTtl = defaultTtl;
        return this;
    }
    
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    
    public CacheConfig setTimeUnit(TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit cannot be null");
        }
        this.timeUnit = timeUnit;
        return this;
    }
    
    public boolean isExpireOnAccess() {
        return expireOnAccess;
    }
    
    public CacheConfig setExpireOnAccess(boolean expireOnAccess) {
        this.expireOnAccess = expireOnAccess;
        return this;
    }
    
    public boolean isRefreshOnAccess() {
        return refreshOnAccess;
    }
    
    public CacheConfig setRefreshOnAccess(boolean refreshOnAccess) {
        this.refreshOnAccess = refreshOnAccess;
        return this;
    }
    
    public long getCleanupInterval() {
        return cleanupInterval;
    }
    
    public CacheConfig setCleanupInterval(long cleanupInterval) {
        if (cleanupInterval < 0) {
            throw new IllegalArgumentException("Cleanup interval must be >= 0");
        }
        this.cleanupInterval = cleanupInterval;
        return this;
    }
    
    public String getEvictionPolicy() {
        return evictionPolicy;
    }
    
    public CacheConfig setEvictionPolicy(String evictionPolicy) {
        if (evictionPolicy == null || evictionPolicy.isEmpty()) {
            throw new IllegalArgumentException("Eviction policy cannot be null or empty");
        }
        this.evictionPolicy = evictionPolicy;
        return this;
    }
    
    public long getDefaultTtlMillis() {
        if (defaultTtl < 0) {
            return -1;
        }
        return timeUnit.toMillis(defaultTtl);
    }
    
    public static CacheConfig defaultConfig() {
        return new CacheConfig();
    }
    
    public static CacheConfig of(int maxSize) {
        return new CacheConfig().setMaxSize(maxSize);
    }
    
    public static CacheConfig of(int maxSize, long ttl, TimeUnit timeUnit) {
        return new CacheConfig()
                .setMaxSize(maxSize)
                .setDefaultTtl(ttl)
                .setTimeUnit(timeUnit);
    }
    
    @Override
    public String toString() {
        return "CacheConfig{" +
                "maxSize=" + maxSize +
                ", defaultTtl=" + defaultTtl +
                ", timeUnit=" + timeUnit +
                ", evictionPolicy='" + evictionPolicy + '\'' +
                '}';
    }
}
