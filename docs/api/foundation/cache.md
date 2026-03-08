# 缓存 API

## Cache 接口

缓存接口。

```java
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void put(K key, V value, long ttl, TimeUnit unit);
    void remove(K key);
    void clear();
    boolean contains(K key);
    int size();
    Set<K> keys();
}
```

### 方法说明

#### get(K key)
获取缓存值。

**参数：**
- `key` - 缓存键

**返回值：** 缓存值，如果不存在返回 null

**示例：**
```java
User user = cache.get("user:123");
```

---

#### put(K key, V value)
设置缓存值。

**参数：**
- `key` - 缓存键
- `value` - 缓存值

**示例：**
```java
cache.put("user:123", user);
```

---

#### put(K key, V value, long ttl, TimeUnit unit)
设置缓存值并指定过期时间。

**参数：**
- `key` - 缓存键
- `value` - 缓存值
- `ttl` - 过期时间
- `unit` - 时间单位

**示例：**
```java
cache.put("user:123", user, 1, TimeUnit.HOURS);
```

---

#### remove(K key)
移除缓存。

**参数：**
- `key` - 缓存键

**示例：**
```java
cache.remove("user:123");
```

---

#### clear()
清空所有缓存。

**示例：**
```java
cache.clear();
```

---

#### contains(K key)
检查缓存是否存在。

**参数：**
- `key` - 缓存键

**返回值：** 如果存在返回 true，否则返回 false

**示例：**
```java
if (cache.contains("user:123")) {
    // ...
}
```

---

#### size()
获取缓存数量。

**返回值：** 缓存数量

**示例：**
```java
int count = cache.size();
```

---

#### keys()
获取所有缓存键。

**返回值：** 缓存键集合

**示例：**
```java
Set<String> keys = cache.keys();
```

---

## CacheManager 接口

缓存管理器接口。

```java
public interface CacheManager {
    <K, V> Cache<K, V> getCache(String name);
    <K, V> Cache<K, V> getCache(String name, Class<K> keyType, Class<V> valueType);
    void destroyCache(String name);
    Collection<String> getCacheNames();
}
```

### 方法说明

#### getCache(String name)
获取或创建缓存。

**参数：**
- `name` - 缓存名称

**返回值：** 缓存实例

**示例：**
```java
Cache<String, User> userCache = cacheManager.getCache("users");
```

---

## 实现类

### MemoryCache
内存缓存实现。

```java
Cache<String, User> cache = new MemoryCache<>();
```

### FileCache
文件缓存实现。

```java
Cache<String, User> cache = new FileCache<>(Paths.get("/tmp/cache"));
```

### RedisCache
Redis 缓存实现。

```java
RedisConfig config = new RedisConfig();
config.setHost("localhost");
config.setPort(6379);

Cache<String, User> cache = new RedisCache<>(config);
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
