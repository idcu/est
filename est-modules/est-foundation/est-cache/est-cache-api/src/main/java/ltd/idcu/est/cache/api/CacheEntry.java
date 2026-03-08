package ltd.idcu.est.cache.api;

import java.util.concurrent.TimeUnit;

public class CacheEntry<V> {
    
    private final V value;
    private final long createdAt;
    private final long ttlMillis;
    private volatile long lastAccessedAt;
    private volatile int accessCount;
    
    public CacheEntry(V value) {
        this(value, -1);
    }
    
    public CacheEntry(V value, long ttlMillis) {
        this.value = value;
        this.createdAt = System.currentTimeMillis();
        this.ttlMillis = ttlMillis;
        this.lastAccessedAt = this.createdAt;
        this.accessCount = 0;
    }
    
    public V getValue() {
        this.lastAccessedAt = System.currentTimeMillis();
        this.accessCount++;
        return value;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public long getLastAccessedAt() {
        return lastAccessedAt;
    }
    
    public void setLastAccessedAt(long lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
    
    public int getAccessCount() {
        return accessCount;
    }
    
    public void incrementAccessCount() {
        this.accessCount++;
    }
    
    public long getTtlMillis() {
        return ttlMillis;
    }
    
    public boolean isExpired() {
        if (ttlMillis < 0) {
            return false;
        }
        return System.currentTimeMillis() > (createdAt + ttlMillis);
    }
    
    public long getRemainingTtl() {
        if (ttlMillis < 0) {
            return -1;
        }
        long remaining = (createdAt + ttlMillis) - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
    
    public static <V> CacheEntry<V> of(V value) {
        return new CacheEntry<>(value);
    }
    
    public static <V> CacheEntry<V> of(V value, long ttl, TimeUnit timeUnit) {
        return new CacheEntry<>(value, timeUnit.toMillis(ttl));
    }
}
