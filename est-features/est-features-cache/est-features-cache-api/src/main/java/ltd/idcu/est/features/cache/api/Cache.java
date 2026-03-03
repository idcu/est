package ltd.idcu.est.features.cache.api;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {
    
    void put(K key, V value);
    
    void put(K key, V value, long ttl, TimeUnit timeUnit);
    
    Optional<V> get(K key);
    
    V get(K key, V defaultValue);
    
    boolean containsKey(K key);
    
    void remove(K key);
    
    void clear();
    
    int size();
    
    boolean isEmpty();
    
    CacheStats getStats();
}
