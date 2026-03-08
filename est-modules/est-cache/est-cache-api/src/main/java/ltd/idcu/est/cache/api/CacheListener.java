package ltd.idcu.est.cache.api;

public interface CacheListener<K, V> {
    
    void onPut(K key, V value);
    
    void onRemove(K key, V value);
    
    void onEvict(K key, V value);
    
    void onExpire(K key, V value);
    
    void onClear();
}
