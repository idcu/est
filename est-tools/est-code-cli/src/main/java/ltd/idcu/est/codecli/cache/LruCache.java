package ltd.idcu.est.codecli.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    
    private final Map<K, CacheEntry<V>> cache;
    private final int maxSize;
    private final long defaultTtl;
    
    private static class CacheEntry<V> {
        final V value;
        final long createdAt;
        final long ttl;
        
        CacheEntry(V value, long ttl) {
            this.value = value;
            this.createdAt = System.currentTimeMillis();
            this.ttl = ttl;
        }
        
        boolean isExpired() {
            return ttl > 0 && System.currentTimeMillis() - createdAt > ttl;
        }
    }
    
    public LruCache(int maxSize) {
        this(maxSize, 0);
    }
    
    public LruCache(int maxSize, long defaultTtlMillis) {
        this.maxSize = maxSize;
        this.defaultTtl = defaultTtlMillis;
        this.cache = new LinkedHashMap<K, CacheEntry<V>>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                return size() > LruCache.this.maxSize;
            }
        };
    }
    
    public synchronized void put(K key, V value) {
        put(key, value, defaultTtl);
    }
    
    public synchronized void put(K key, V value, long ttlMillis) {
        cache.put(key, new CacheEntry<>(value, ttlMillis));
    }
    
    public synchronized V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.value;
    }
    
    public synchronized boolean containsKey(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return true;
    }
    
    public synchronized void remove(K key) {
        cache.remove(key);
    }
    
    public synchronized void clear() {
        cache.clear();
    }
    
    public synchronized int size() {
        cleanup();
        return cache.size();
    }
    
    public synchronized boolean isEmpty() {
        cleanup();
        return cache.isEmpty();
    }
    
    private void cleanup() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public long getDefaultTtl() {
        return defaultTtl;
    }
}
