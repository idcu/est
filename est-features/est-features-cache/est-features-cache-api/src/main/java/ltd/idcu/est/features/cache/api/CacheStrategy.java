package ltd.idcu.est.features.cache.api;

import java.util.Optional;

public interface CacheStrategy<K, V> {
    
    void onAccess(K key, CacheEntry<V> entry);
    
    void onPut(K key, CacheEntry<V> entry);
    
    Optional<K> evict();
    
    int size();
    
    int capacity();
    
    void clear();
    
    void setCapacity(int capacity);
}
