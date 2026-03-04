# Cache 缓存系统 API

缓存系统提供多层次的缓存实现，支持内存缓存、文件缓存和Redis缓存。

## 核心接口

```java
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void put(K key, V value, long ttlSeconds);
    void remove(K key);
    boolean contains(K key);
    void clear();
    CacheStats stats();
}
```

## 缓存实现

### 内存缓存 (MemoryCache)

使用 LRU 策略的内存缓存实现。

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

// 创建内存缓存
Cache<String, String> cache = MemoryCaches.create();
cache.put("key", "value");

// 创建带最大容量的LRU缓存
Cache<String, String> lruCache = MemoryCaches.createLru(1000);

// 创建带TTL的缓存项
cache.put("temp", "data", 3600); // 1小时过期
```

### 文件缓存 (FileCache)

基于文件系统的持久化缓存。

```java
import ltd.idcu.est.features.cache.file.FileCaches;

// 创建文件缓存
Cache<String, String> fileCache = FileCaches.create("./cache/data");
fileCache.put("persistent", "value");
```

### Redis缓存 (RedisCache)

Redis 分布式缓存实现。

```java
import ltd.idcu.est.features.cache.redis.RedisCaches;

// 创建Redis缓存
Cache<String, String> redisCache = RedisCaches.create("localhost", 6379);
```

## 缓存统计

```java
CacheStats stats = cache.stats();
System.out.println("Hits: " + stats.getHits());
System.out.println("Misses: " + stats.getMisses());
System.out.println("Hit Rate: " + stats.getHitRate());
```

## 事件监听

```java
cache.addListener(event -> {
    System.out.println("Cache event: " + event.getType());
});
```
