package ltd.idcu.est.features.data.api;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class QueryCache {
    private final ConcurrentHashMap<String, CacheEntry> cache;
    private final long defaultTtlMillis;
    
    public QueryCache() {
        this(30000);
    }
    
    public QueryCache(long defaultTtlMillis) {
        this.cache = new ConcurrentHashMap<>();
        this.defaultTtlMillis = defaultTtlMillis;
    }
    
    public void put(String key, Object value) {
        put(key, value, defaultTtlMillis);
    }
    
    public void put(String key, Object value, long ttlMillis) {
        long expireTime = System.currentTimeMillis() + ttlMillis;
        cache.put(key, new CacheEntry(value, expireTime));
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (System.currentTimeMillis() > entry.expireTime) {
            cache.remove(key);
            return null;
        }
        return (T) entry.value;
    }
    
    public boolean contains(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        if (System.currentTimeMillis() > entry.expireTime) {
            cache.remove(key);
            return false;
        }
        return true;
    }
    
    public void remove(String key) {
        cache.remove(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public void evictExpired() {
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> now > entry.getValue().expireTime);
    }
    
    public int size() {
        evictExpired();
        return cache.size();
    }
    
    private static class CacheEntry {
        final Object value;
        final long expireTime;
        
        CacheEntry(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
    }
}
