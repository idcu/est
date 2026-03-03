package ltd.idcu.est.features.cache.memory;

import ltd.idcu.est.features.cache.api.CacheEntry;
import ltd.idcu.est.features.cache.api.CacheStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class LruCacheStrategy<K, V> implements CacheStrategy<K, V> {
    
    private final LinkedHashMap<K, CacheEntry<V>> cache;
    private int capacity;
    
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
        return cache.get(key);
    }
    
    public void remove(K key) {
        cache.remove(key);
    }
}
