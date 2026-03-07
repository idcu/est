package ltd.idcu.est.features.cache.api;

public interface CacheLoader<K, V> {
    
    V load(K key) throws Exception;
    
    default V reload(K key, V oldValue) throws Exception {
        return load(key);
    }
}
