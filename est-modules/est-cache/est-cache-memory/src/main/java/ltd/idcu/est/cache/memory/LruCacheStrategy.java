package ltd.idcu.est.cache.memory;

import ltd.idcu.est.cache.api.CacheEntry;
import ltd.idcu.est.cache.api.CacheStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class LruCacheStrategy<K, V> implements CacheStrategy<K, V> {
    
    private final LinkedHashMap<K, CacheEntry<V>> cache;
    private int capacity;
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    
    public LruCacheStrategy(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                return size() > LruCacheStrategy.this.capacity;
            }
        };
    }
    
    @Override
    public void onAccess(K key, CacheEntry<V> entry) {
        cache.get(key);
    }
    
    @Override
    public void onPut(K key, CacheEntry<V> entry) {
        cache.put(key, entry);
    }
    
    @Override
    public Optional<K> evict() {
        if (cache.isEmpty()) {
            return Optional.empty();
        }
        K eldestKey = cache.keySet().iterator().next();
        cache.remove(eldestKey);
        return Optional.of(eldestKey);
    }
    
    @Override
    public int size() {
        return cache.size();
    }
    
    @Override
    public int capacity() {
        return capacity;
    }
    
    @Override
    public void clear() {
        cache.clear();
        hitCount.set(0);
        missCount.set(0);
    }
    
    @Override
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }
    
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
    
    public CacheEntry<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null) {
            hitCount.incrementAndGet();
        } else {
            missCount.incrementAndGet();
        }
        return entry;
    }
    
    public void remove(K key) {
        cache.remove(key);
    }
    
    public long getHitCount() {
        return hitCount.get();
    }
    
    public long getMissCount() {
        return missCount.get();
    }
    
    public double getHitRate() {
        long hits = hitCount.get();
        long total = hits + missCount.get();
        return total == 0 ? 0.0 : (double) hits / total;
    }
    
    public void resetStats() {
        hitCount.set(0);
        missCount.set(0);
    }
}
