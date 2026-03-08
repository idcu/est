package ltd.idcu.est.cache.redis;

import ltd.idcu.est.cache.api.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCache implements Cache<String, String> {
    
    private final RedisClient client;
    private final CacheStats stats;
    private final List<CacheListener<String, String>> listeners;
    private final String keyPrefix;
    private CacheLoader<String, String> loader;
    
    public RedisCache() {
        this(new RedisClient());
    }
    
    public RedisCache(String host, int port) {
        this(new RedisClient(host, port));
    }
    
    public RedisCache(String host, int port, String password, int database) {
        this(new RedisClient(host, port, password, database));
    }
    
    public RedisCache(RedisClient client) {
        this(client, "");
    }
    
    public RedisCache(RedisClient client, String keyPrefix) {
        this.client = client;
        this.keyPrefix = keyPrefix != null ? keyPrefix : "";
        this.stats = new CacheStats();
        this.listeners = new ArrayList<>();
    }
    
    private String prefixKey(String key) {
        return keyPrefix.isEmpty() ? key : keyPrefix + ":" + key;
    }
    
    @Override
    public void put(String key, String value) {
        try {
            String prefixedKey = prefixKey(key);
            client.set(prefixedKey, value);
            stats.incrementPutCount();
            notifyPut(key, value);
        } catch (IOException e) {
            throw new CacheException("Failed to put value for key: " + key, e);
        }
    }
    
    @Override
    public void put(String key, String value, long ttl, TimeUnit timeUnit) {
        try {
            String prefixedKey = prefixKey(key);
            long ttlSeconds = timeUnit.toSeconds(ttl);
            if (ttlSeconds > 0) {
                client.set(prefixedKey, value, ttlSeconds);
            } else {
                client.set(prefixedKey, value);
            }
            stats.incrementPutCount();
            notifyPut(key, value);
        } catch (IOException e) {
            throw new CacheException("Failed to put value for key: " + key, e);
        }
    }
    
    @Override
    public Optional<String> get(String key) {
        try {
            String prefixedKey = prefixKey(key);
            String value = client.get(prefixedKey);
            
            if (value == null) {
                stats.incrementMissCount();
                if (loader != null) {
                    return loadValue(key);
                }
                return Optional.empty();
            }
            
            stats.incrementHitCount();
            return Optional.of(value);
        } catch (IOException e) {
            stats.incrementMissCount();
            throw new CacheException("Failed to get value for key: " + key, e);
        }
    }
    
    private Optional<String> loadValue(String key) {
        try {
            long start = System.currentTimeMillis();
            String value = loader.load(key);
            long loadTime = System.currentTimeMillis() - start;
            stats.addLoadTime(loadTime);
            
            if (value != null) {
                put(key, value);
                return Optional.of(value);
            }
        } catch (Exception e) {
            throw new CacheException("Failed to load value for key: " + key, e);
        }
        return Optional.empty();
    }
    
    @Override
    public String get(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }
    
    @Override
    public boolean containsKey(String key) {
        try {
            String prefixedKey = prefixKey(key);
            return client.exists(prefixedKey);
        } catch (IOException e) {
            throw new CacheException("Failed to check existence for key: " + key, e);
        }
    }
    
    @Override
    public void remove(String key) {
        try {
            String prefixedKey = prefixKey(key);
            String value = client.get(prefixedKey);
            client.del(prefixedKey);
            stats.incrementRemoveCount();
            if (value != null) {
                notifyRemove(key, value);
            }
        } catch (IOException e) {
            throw new CacheException("Failed to remove key: " + key, e);
        }
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clear operation is not supported for Redis cache. Use flushDb() instead.");
    }
    
    public void flushDb() {
        try {
            client.flushDb();
            notifyClear();
        } catch (IOException e) {
            throw new CacheException("Failed to flush database", e);
        }
    }
    
    @Override
    public int size() {
        try {
            long dbSize = client.dbSize();
            return dbSize > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) dbSize;
        } catch (IOException e) {
            throw new CacheException("Failed to get cache size", e);
        }
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public CacheStats getStats() {
        return stats.snapshot();
    }
    
    public void setLoader(CacheLoader<String, String> loader) {
        this.loader = loader;
    }
    
    public void addListener(CacheListener<String, String> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(CacheListener<String, String> listener) {
        listeners.remove(listener);
    }
    
    public long getTtl(String key) {
        try {
            String prefixedKey = prefixKey(key);
            return client.ttl(prefixedKey);
        } catch (IOException e) {
            throw new CacheException("Failed to get TTL for key: " + key, e);
        }
    }
    
    public boolean setExpire(String key, long ttl, TimeUnit timeUnit) {
        try {
            String prefixedKey = prefixKey(key);
            long result = client.expire(prefixedKey, timeUnit.toSeconds(ttl));
            return result == 1;
        } catch (IOException e) {
            throw new CacheException("Failed to set expire for key: " + key, e);
        }
    }
    
    public long increment(String key) {
        try {
            String prefixedKey = prefixKey(key);
            return client.incr(prefixedKey);
        } catch (IOException e) {
            throw new CacheException("Failed to increment key: " + key, e);
        }
    }
    
    public long increment(String key, long delta) {
        try {
            String prefixedKey = prefixKey(key);
            return client.incrBy(prefixedKey, delta);
        } catch (IOException e) {
            throw new CacheException("Failed to increment key: " + key, e);
        }
    }
    
    public long decrement(String key) {
        try {
            String prefixedKey = prefixKey(key);
            return client.decr(prefixedKey);
        } catch (IOException e) {
            throw new CacheException("Failed to decrement key: " + key, e);
        }
    }
    
    public boolean setIfAbsent(String key, String value) {
        try {
            String prefixedKey = prefixKey(key);
            String result = client.setNx(prefixedKey, value);
            return result != null;
        } catch (IOException e) {
            throw new CacheException("Failed to set if absent for key: " + key, e);
        }
    }
    
    public boolean setIfAbsent(String key, String value, long ttl, TimeUnit timeUnit) {
        try {
            String prefixedKey = prefixKey(key);
            client.set(prefixedKey, value, timeUnit.toSeconds(ttl));
            return true;
        } catch (IOException e) {
            throw new CacheException("Failed to set if absent for key: " + key, e);
        }
    }
    
    public RedisClient getClient() {
        return client;
    }
    
    public void connect() {
        try {
            client.connect();
        } catch (IOException e) {
            throw new CacheException("Failed to connect to Redis", e);
        }
    }
    
    public void disconnect() {
        client.close();
    }
    
    public boolean isConnected() {
        return client.isConnected();
    }
    
    public String ping() {
        try {
            return client.ping();
        } catch (IOException e) {
            throw new CacheException("Failed to ping Redis", e);
        }
    }
    
    private void notifyPut(String key, String value) {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onPut(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyRemove(String key, String value) {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onRemove(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyClear() {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onClear();
            } catch (Exception ignored) {
            }
        }
    }
}
