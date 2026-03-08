# 缂撳瓨 API

## Cache 鎺ュ彛

缂撳瓨鎺ュ彛銆?
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

### 鏂规硶璇存槑

#### get(K key)
鑾峰彇缂撳瓨鍊笺€?
**鍙傛暟锛?*
- `key` - 缂撳瓨閿?
**杩斿洖鍊硷細** 缂撳瓨鍊硷紝濡傛灉涓嶅瓨鍦ㄨ繑鍥?null

**绀轰緥锛?*
```java
User user = cache.get("user:123");
```

---

#### put(K key, V value)
璁剧疆缂撳瓨鍊笺€?
**鍙傛暟锛?*
- `key` - 缂撳瓨閿?- `value` - 缂撳瓨鍊?
**绀轰緥锛?*
```java
cache.put("user:123", user);
```

---

#### put(K key, V value, long ttl, TimeUnit unit)
璁剧疆缂撳瓨鍊煎苟鎸囧畾杩囨湡鏃堕棿銆?
**鍙傛暟锛?*
- `key` - 缂撳瓨閿?- `value` - 缂撳瓨鍊?- `ttl` - 杩囨湡鏃堕棿
- `unit` - 鏃堕棿鍗曚綅

**绀轰緥锛?*
```java
cache.put("user:123", user, 1, TimeUnit.HOURS);
```

---

#### remove(K key)
绉婚櫎缂撳瓨銆?
**鍙傛暟锛?*
- `key` - 缂撳瓨閿?
**绀轰緥锛?*
```java
cache.remove("user:123");
```

---

#### clear()
娓呯┖鎵€鏈夌紦瀛樸€?
**绀轰緥锛?*
```java
cache.clear();
```

---

#### contains(K key)
妫€鏌ョ紦瀛樻槸鍚﹀瓨鍦ㄣ€?
**鍙傛暟锛?*
- `key` - 缂撳瓨閿?
**杩斿洖鍊硷細** 濡傛灉瀛樺湪杩斿洖 true锛屽惁鍒欒繑鍥?false

**绀轰緥锛?*
```java
if (cache.contains("user:123")) {
    // ...
}
```

---

#### size()
鑾峰彇缂撳瓨鏁伴噺銆?
**杩斿洖鍊硷細** 缂撳瓨鏁伴噺

**绀轰緥锛?*
```java
int count = cache.size();
```

---

#### keys()
鑾峰彇鎵€鏈夌紦瀛橀敭銆?
**杩斿洖鍊硷細** 缂撳瓨閿泦鍚?
**绀轰緥锛?*
```java
Set<String> keys = cache.keys();
```

---

## CacheManager 鎺ュ彛

缂撳瓨绠＄悊鍣ㄦ帴鍙ｃ€?
```java
public interface CacheManager {
    <K, V> Cache<K, V> getCache(String name);
    <K, V> Cache<K, V> getCache(String name, Class<K> keyType, Class<V> valueType);
    void destroyCache(String name);
    Collection<String> getCacheNames();
}
```

### 鏂规硶璇存槑

#### getCache(String name)
鑾峰彇鎴栧垱寤虹紦瀛樸€?
**鍙傛暟锛?*
- `name` - 缂撳瓨鍚嶇О

**杩斿洖鍊硷細** 缂撳瓨瀹炰緥

**绀轰緥锛?*
```java
Cache<String, User> userCache = cacheManager.getCache("users");
```

---

## 瀹炵幇绫?
### MemoryCache
鍐呭瓨缂撳瓨瀹炵幇銆?
```java
Cache<String, User> cache = new MemoryCache<>();
```

### FileCache
鏂囦欢缂撳瓨瀹炵幇銆?
```java
Cache<String, User> cache = new FileCache<>(Paths.get("/tmp/cache"));
```

### RedisCache
Redis 缂撳瓨瀹炵幇銆?
```java
RedisConfig config = new RedisConfig();
config.setHost("localhost");
config.setPort(6379);

Cache<String, User> cache = new RedisCache<>(config);
```

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
