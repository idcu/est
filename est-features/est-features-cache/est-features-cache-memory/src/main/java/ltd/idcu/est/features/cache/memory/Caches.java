package ltd.idcu.est.features.cache.memory;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheConfig;

public final class Caches {
    
    private Caches() {
    }
    
    public static <K, V> Cache<K, V> newMemoryCache() {
        return new MemoryCache<>();
    }
    
    public static <K, V> Cache<K, V> newMemoryCache(int maxSize) {
        return new MemoryCache<>(maxSize);
    }
    
    public static <K, V> Cache<K, V> newMemoryCache(CacheConfig config) {
        return new MemoryCache<>(config);
    }
    
    public static <K, V> MemoryCacheBuilder<K, V> builder() {
        return new MemoryCacheBuilder<>();
    }
    
    public static class MemoryCacheBuilder<K, V> {
        private int maxSize = 1000;
        private long defaultTtl = -1;
        private long cleanupInterval = 60000;
        private String evictionPolicy = "LRU";
        
        public MemoryCacheBuilder<K, V> maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
        
        public MemoryCacheBuilder<K, V> defaultTtl(long ttl) {
            this.defaultTtl = ttl;
            return this;
        }
        
        public MemoryCacheBuilder<K, V> cleanupInterval(long interval) {
            this.cleanupInterval = interval;
            return this;
        }
        
        public MemoryCacheBuilder<K, V> evictionPolicy(String policy) {
            this.evictionPolicy = policy;
            return this;
        }
        
        public Cache<K, V> build() {
            CacheConfig config = new CacheConfig()
                    .setMaxSize(maxSize)
                    .setDefaultTtl(defaultTtl)
                    .setCleanupInterval(cleanupInterval)
                    .setEvictionPolicy(evictionPolicy);
            return new MemoryCache<>(config);
        }
    }
}
