package ltd.idcu.est.features.cache.memory;

import ltd.idcu.est.features.cache.api.*;
import ltd.idcu.est.utils.common.AssertUtils;
import ltd.idcu.est.utils.common.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MemoryCache<K, V> implements Cache<K, V> {
    
    private final Map<K, CacheEntry<V>> cache;
    private final CacheStrategy<K, V> strategy;
    private final CacheConfig config;
    private final CacheStats stats;
    private final List<CacheListener<K, V>> listeners;
    private final ScheduledExecutorService cleanupExecutor;
    private final AtomicBoolean running;
    private final Map<K, Long> ttlCache;
    private CacheLoader<K, V> loader;
    
    public MemoryCache() {
        this(CacheConfig.defaultConfig());
    }
    
    public MemoryCache(int maxSize) {
        this(CacheConfig.of(maxSize));
    }
    
    public MemoryCache(CacheConfig config) {
        this.config = config;
        this.stats = new CacheStats();
        this.listeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.running = new AtomicBoolean(true);
        this.cache = new ConcurrentHashMap<>();
        this.ttlCache = new ConcurrentHashMap<>();
        this.strategy = createStrategy(config);
        
        if (config.getCleanupInterval() > 0) {
            this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "cache-cleanup");
                t.setDaemon(true);
                return t;
            });
            this.cleanupExecutor.scheduleAtFixedRate(
                    this::cleanupExpired,
                    config.getCleanupInterval(),
                    config.getCleanupInterval(),
                    TimeUnit.MILLISECONDS
            );
        } else {
            this.cleanupExecutor = null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private CacheStrategy<K, V> createStrategy(CacheConfig config) {
        String policy = config.getEvictionPolicy().toUpperCase();
        return switch (policy) {
            case "LRU" -> (CacheStrategy<K, V>) new LruCacheStrategy<K, Object>(config.getMaxSize());
            default -> (CacheStrategy<K, V>) new LruCacheStrategy<K, Object>(config.getMaxSize());
        };
    }
    
    @Override
    public void put(K key, V value) {
        long ttl = config.getDefaultTtlMillis();
        if (ttl > 0) {
            put(key, value, ttl, TimeUnit.MILLISECONDS);
        } else {
            putInternal(key, new CacheEntry<>(value));
        }
    }
    
    @Override
    public void put(K key, V value, long ttl, TimeUnit timeUnit) {
        CacheEntry<V> entry = CacheEntry.of(value, ttl, timeUnit);
        putInternal(key, entry);
    }
    
    private void putInternal(K key, CacheEntry<V> entry) {
        AssertUtils.notNull(key, "Key cannot be null");
        
        while (strategy.size() >= config.getMaxSize()) {
            Optional<K> evictedKey = strategy.evict();
            evictedKey.ifPresent(k -> {
                CacheEntry<V> evictedEntry = cache.remove(k);
                ttlCache.remove(k);
                if (evictedEntry != null) {
                    stats.incrementEvictionCount();
                    notifyEvict(k, evictedEntry.getValue());
                }
            });
        }
        
        CacheEntry<V> oldEntry = cache.put(key, entry);
        if (entry.getTtlMillis() > 0) {
            ttlCache.put(key, entry.getCreatedAt() + entry.getTtlMillis());
        } else {
            ttlCache.remove(key);
        }
        strategy.onPut(key, entry);
        stats.incrementPutCount();
        
        if (oldEntry != null) {
            notifyRemove(key, oldEntry.getValue());
        }
        notifyPut(key, entry.getValue());
    }
    
    @Override
    public Optional<V> get(K key) {
        if (ObjectUtils.isNull(key)) {
            stats.incrementMissCount();
            return Optional.empty();
        }
        
        Long expiresAt = ttlCache.get(key);
        if (expiresAt != null && expiresAt <= System.currentTimeMillis()) {
            CacheEntry<V> expiredEntry = cache.remove(key);
            ttlCache.remove(key);
            strategy.evict();
            stats.incrementMissCount();
            if (expiredEntry != null) {
                notifyExpire(key, expiredEntry.getValue());
            }
            if (loader != null) {
                return loadValue(key);
            }
            return Optional.empty();
        }
        
        CacheEntry<V> entry = cache.get(key);
        
        if (entry == null) {
            stats.incrementMissCount();
            if (loader != null) {
                return loadValue(key);
            }
            return Optional.empty();
        }
        
        strategy.onAccess(key, entry);
        stats.incrementHitCount();
        
        if (config.isRefreshOnAccess()) {
            entry.setLastAccessedAt(System.currentTimeMillis());
        }
        
        return Optional.of(entry.getValue());
    }
    
    private Optional<V> loadValue(K key) {
        try {
            long start = System.currentTimeMillis();
            V value = loader.load(key);
            long loadTime = System.currentTimeMillis() - start;
            stats.addLoadTime(loadTime);
            
            if (ObjectUtils.isNotNull(value)) {
                put(key, value);
                return Optional.of(value);
            }
        } catch (Exception e) {
            throw new CacheException("Failed to load value for key: " + key, e);
        }
        return Optional.empty();
    }
    
    @Override
    public V get(K key, V defaultValue) {
        return get(key).orElse(defaultValue);
    }
    
    @Override
    public boolean containsKey(K key) {
        if (ObjectUtils.isNull(key)) {
            return false;
        }
        Long expiresAt = ttlCache.get(key);
        if (expiresAt != null && expiresAt <= System.currentTimeMillis()) {
            CacheEntry<V> expiredEntry = cache.remove(key);
            ttlCache.remove(key);
            if (expiredEntry != null) {
                notifyExpire(key, expiredEntry.getValue());
            }
            return false;
        }
        return cache.containsKey(key);
    }
    
    @Override
    public void remove(K key) {
        if (ObjectUtils.isNull(key)) {
            return;
        }
        CacheEntry<V> entry = cache.remove(key);
        ttlCache.remove(key);
        if (entry != null) {
            stats.incrementRemoveCount();
            notifyRemove(key, entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        cache.clear();
        ttlCache.clear();
        strategy.clear();
        notifyClear();
    }
    
    @Override
    public int size() {
        return cache.size();
    }
    
    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    @Override
    public CacheStats getStats() {
        return stats.snapshot();
    }
    
    public void setLoader(CacheLoader<K, V> loader) {
        this.loader = loader;
    }
    
    public void addListener(CacheListener<K, V> listener) {
        if (ObjectUtils.isNotNull(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(CacheListener<K, V> listener) {
        listeners.remove(listener);
    }
    
    public Set<K> keySet() {
        return new HashSet<>(cache.keySet());
    }
    
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (CacheEntry<V> entry : cache.values()) {
            if (!entry.isExpired()) {
                values.add(entry.getValue());
            }
        }
        return values;
    }
    
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            if (!entry.getValue().isExpired()) {
                entries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getValue()));
            }
        }
        return entries;
    }
    
    private void cleanupExpired() {
        if (!running.get()) {
            return;
        }
        long now = System.currentTimeMillis();
        List<K> expiredKeys = new ArrayList<>();
        for (Map.Entry<K, Long> entry : ttlCache.entrySet()) {
            if (entry.getValue() <= now) {
                expiredKeys.add(entry.getKey());
            }
        }
        for (K key : expiredKeys) {
            CacheEntry<V> entry = cache.remove(key);
            ttlCache.remove(key);
            if (entry != null) {
                stats.incrementEvictionCount();
                notifyExpire(key, entry.getValue());
            }
        }
    }
    
    public void shutdown() {
        running.set(false);
        if (cleanupExecutor != null) {
            cleanupExecutor.shutdown();
            try {
                if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    cleanupExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                cleanupExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        clear();
    }
    
    private void notifyPut(K key, V value) {
        for (CacheListener<K, V> listener : listeners) {
            try {
                listener.onPut(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyRemove(K key, V value) {
        for (CacheListener<K, V> listener : listeners) {
            try {
                listener.onRemove(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyEvict(K key, V value) {
        for (CacheListener<K, V> listener : listeners) {
            try {
                listener.onEvict(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyExpire(K key, V value) {
        for (CacheListener<K, V> listener : listeners) {
            try {
                listener.onExpire(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyClear() {
        for (CacheListener<K, V> listener : listeners) {
            try {
                listener.onClear();
            } catch (Exception ignored) {
            }
        }
    }
}
