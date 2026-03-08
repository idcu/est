# EST Features Cache 缓存模块 - 小白从入门到精通

## 目录
1. [什么是 EST Features Cache？](#什么是-est-features-cache)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与 est-collection 集成](#与-est-collection-集成)
7. [最佳实践](#最佳实践)

---

## 什么是 EST Features Cache？

### 用大白话理解

EST Features Cache 就像是一个"超级储物柜"。想象一下你在做一个电商网站，每次用户查询产品信息都要去数据库查：

**传统方式**：每次都去数据库找，慢死了！数据库都累垮了。

**EST Features Cache 方式**：你把经常查询的东西先放到储物柜里，下次有人要直接给他！
- 储物柜满了？把最久没用的东西扔掉（LRU策略）
- 东西放太久？自动过期清理
- 想知道储物柜用得怎么样？有统计数据给你看

它支持多种存储方式：内存缓存、文件缓存、Redis 缓存，想用哪种用哪种！

### 核心特点

- 🎯 **简单易用** - 几行代码就能创建和使用缓存
- ⚡ **高性能** - 内存存储，访问速度极快
- 🔄 **自动过期** - 支持 TTL（Time To Live）设置
- 📊 **LRU 策略** - 自动淘汰最久未使用的数据
- 📈 **统计监控** - 提供命中率、过期数等详细统计

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-api</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个程序

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Cache 第一个示例 ===\n");
        
        Cache<String, String> cache = Caches.newMemoryCache();
        
        cache.put("product:1", "iPhone 15");
        cache.put("product:2", "MacBook Pro");
        cache.put("product:3", "iPad");
        
        System.out.println("product:1 = " + cache.get("product:1").orElse("未找到"));
        System.out.println("product:2 = " + cache.get("product:2").orElse("未找到"));
        System.out.println("product:4 = " + cache.get("product:4", "默认值"));
        
        System.out.println("\n缓存中共有 " + cache.size() + " 个数据");
    }
}
```

运行这个程序，你会看到：
```
=== EST Cache 第一个示例 ===

product:1 = iPhone 15
product:2 = MacBook Pro
product:4 = 默认值

缓存中共有 3 个数据
```

恭喜你！你已经成功使用 EST Features Cache 了！

---

## 基础篇

### 1. 什么是 Cache？

Cache 就是一个"键值对存储"接口，它的核心操作非常简单：

```java
public interface Cache<K, V> {
    void put(K key, V value);                    // 放入数据
    Optional<V> get(K key);                      // 获取数据
    V get(K key, V defaultValue);                // 获取数据（带默认值）
    boolean containsKey(K key);                  // 检查是否存在
    void remove(K key);                          // 删除数据
    void clear();                                // 清空缓存
    int size();                                  // 获取大小
    boolean isEmpty();                           // 检查是否为空
    CacheStats getStats();                       // 获取统计信息
}
```

### 2. 创建缓存的几种方式

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheConfig;
import ltd.idcu.est.features.cache.memory.Caches;

public class CreateCache {
    public static void main(String[] args) {
        System.out.println("--- 方式一：默认配置 ---");
        Cache<String, String> cache1 = Caches.newMemoryCache();
        cache1.put("key1", "value1");
        System.out.println("默认缓存大小：" + cache1.size());
        
        System.out.println("\n--- 方式二：指定最大容量 ---");
        Cache<String, String> cache2 = Caches.newMemoryCache(100);
        cache2.put("key2", "value2");
        System.out.println("指定容量缓存大小：" + cache2.size());
        
        System.out.println("\n--- 方式三：使用 Builder ---");
        Cache<String, String> cache3 = Caches.<String, String>builder()
            .maxSize(500)
            .defaultTtl(3600000)
            .evictionPolicy("LRU")
            .build();
        cache3.put("key3", "value3");
        System.out.println("Builder 创建的缓存大小：" + cache3.size());
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BasicOperations {
    public static void main(String[] args) {
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- 1. 放入数据 ---");
        cache.put("user:1", "张三");
        cache.put("user:2", "李四");
        System.out.println("放入 2 个用户");
        
        System.out.println("\n--- 2. 获取数据 ---");
        Optional<String> user1 = cache.get("user:1");
        user1.ifPresent(name -> System.out.println("user:1 = " + name));
        
        String user3 = cache.get("user:3", "未知用户");
        System.out.println("user:3 = " + user3);
        
        System.out.println("\n--- 3. 检查是否存在 ---");
        System.out.println("user:1 存在？" + cache.containsKey("user:1"));
        System.out.println("user:4 存在？" + cache.containsKey("user:4"));
        
        System.out.println("\n--- 4. 删除数据 ---");
        cache.remove("user:2");
        System.out.println("删除 user:2 后，缓存大小：" + cache.size());
        
        System.out.println("\n--- 5. 设置过期时间 ---");
        cache.put("temp:1", "临时数据", 10, TimeUnit.SECONDS);
        System.out.println("放入 10 秒过期的数据");
        
        System.out.println("\n--- 6. 清空缓存 ---");
        cache.clear();
        System.out.println("清空后，缓存大小：" + cache.size());
        System.out.println("缓存为空？" + cache.isEmpty());
    }
}
```

---

## 进阶篇

### 1. TTL（过期时间）

你可以为缓存数据设置过期时间，时间到了自动清理：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

import java.util.concurrent.TimeUnit;

public class TtlExample {
    public static void main(String[] args) throws InterruptedException {
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- TTL 示例 ---");
        
        cache.put("permanent", "永久数据");
        cache.put("short", "1秒过期", 1, TimeUnit.SECONDS);
        cache.put("medium", "5秒过期", 5, TimeUnit.SECONDS);
        
        System.out.println("初始状态：");
        System.out.println("permanent = " + cache.get("permanent").orElse("null"));
        System.out.println("short = " + cache.get("short").orElse("null"));
        System.out.println("medium = " + cache.get("medium").orElse("null"));
        
        System.out.println("\n等待 2 秒...");
        Thread.sleep(2000);
        
        System.out.println("\n2 秒后：");
        System.out.println("permanent = " + cache.get("permanent").orElse("null"));
        System.out.println("short = " + cache.get("short").orElse("已过期"));
        System.out.println("medium = " + cache.get("medium").orElse("null"));
    }
}
```

### 2. 缓存统计（CacheStats）

缓存会自动统计各种信息，方便你了解使用情况：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheStats;
import ltd.idcu.est.features.cache.memory.Caches;

public class StatsExample {
    public static void main(String[] args) {
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- 缓存统计示例 ---");
        
        for (int i = 1; i <= 10; i++) {
            cache.put("key:" + i, "value:" + i);
        }
        
        for (int i = 1; i <= 15; i++) {
            cache.get("key:" + i);
        }
        
        CacheStats stats = cache.getStats();
        System.out.println("命中次数：" + stats.getHitCount());
        System.out.println("未命中次数：" + stats.getMissCount());
        System.out.println("命中率：" + String.format("%.2f%%", stats.getHitRate() * 100));
        System.out.println("放入次数：" + stats.getPutCount());
        System.out.println("删除次数：" + stats.getRemoveCount());
        System.out.println("淘汰次数：" + stats.getEvictionCount());
    }
}
```

### 3. LRU 淘汰策略

当缓存满了的时候，会自动淘汰最久未使用的数据：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class LruExample {
    public static void main(String[] args) {
        System.out.println("--- LRU 淘汰策略示例 ---");
        
        Cache<String, String> cache = Caches.newMemoryCache(3);
        
        cache.put("A", "数据A");
        cache.put("B", "数据B");
        cache.put("C", "数据C");
        System.out.println("放入 A、B、C，缓存大小：" + cache.size());
        
        cache.get("A");
        System.out.println("访问了 A（现在 A 是最近使用的）");
        
        cache.put("D", "数据D");
        System.out.println("放入 D（缓存满了，会淘汰最久未用的 B）");
        
        System.out.println("\n检查各个 key：");
        System.out.println("A 存在？" + cache.containsKey("A"));
        System.out.println("B 存在？" + cache.containsKey("B"));
        System.out.println("C 存在？" + cache.containsKey("C"));
        System.out.println("D 存在？" + cache.containsKey("D"));
        
        System.out.println("\n缓存统计：");
        System.out.println("淘汰次数：" + cache.getStats().getEvictionCount());
    }
}
```

---

## 高级篇

### 1. 缓存监听器（CacheListener）

你可以监听缓存的各种事件：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheListener;
import ltd.idcu.est.features.cache.memory.Caches;
import ltd.idcu.est.features.cache.memory.MemoryCache;

public class ListenerExample {
    public static void main(String[] args) {
        System.out.println("--- 缓存监听器示例 ---");
        
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        
        cache.addListener(new CacheListener<String, String>() {
            @Override
            public void onPut(String key, String value) {
                System.out.println("[事件] 放入数据：" + key + " = " + value);
            }
            
            @Override
            public void onRemove(String key, String value) {
                System.out.println("[事件] 删除数据：" + key);
            }
            
            @Override
            public void onEvict(String key, String value) {
                System.out.println("[事件] 淘汰数据：" + key);
            }
            
            @Override
            public void onExpire(String key, String value) {
                System.out.println("[事件] 过期数据：" + key);
            }
            
            @Override
            public void onClear() {
                System.out.println("[事件] 清空缓存");
            }
        });
        
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.remove("key1");
        cache.clear();
    }
}
```

### 2. 缓存加载器（CacheLoader）

当缓存中没有数据时，可以自动从数据源加载：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheLoader;
import ltd.idcu.est.features.cache.memory.Caches;
import ltd.idcu.est.features.cache.memory.MemoryCache;

public class LoaderExample {
    public static void main(String[] args) {
        System.out.println("--- 缓存加载器示例 ---");
        
        MemoryCache<Long, String> cache = (MemoryCache<Long, String>) Caches.newMemoryCache();
        
        cache.setLoader(new CacheLoader<Long, String>() {
            @Override
            public String load(Long key) {
                System.out.println("[加载器] 从数据库加载 key：" + key);
                return "数据库中的数据:" + key;
            }
        });
        
        System.out.println("\n第一次获取 key=1（会触发加载）：");
        System.out.println(cache.get(1L).orElse("null"));
        
        System.out.println("\n第二次获取 key=1（从缓存获取）：");
        System.out.println(cache.get(1L).orElse("null"));
        
        System.out.println("\n获取 key=2（会触发加载）：");
        System.out.println(cache.get(2L).orElse("null"));
        
        System.out.println("\n缓存统计：");
        System.out.println("命中次数：" + cache.getStats().getHitCount());
        System.out.println("未命中次数：" + cache.getStats().getMissCount());
    }
}
```

---

## 与 est-collection 集成

EST Features Cache 和 est-collection 是绝配！让我们看看它们如何配合使用。

### 场景：商品信息缓存 + 数据处理

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;
import ltd.idcu.est.features.cache.memory.MemoryCache;

import java.util.ArrayList;
import java.util.List;

public class CollectionIntegration {
    public static void main(String[] args) {
        System.out.println("=== EST Cache + EST Collection 集成示例 ===\n");
        
        MemoryCache<String, Product> cache = (MemoryCache<String, Product>) Caches.newMemoryCache();
        
        Product p1 = new Product(1L, "iPhone 15", 5999.0, 100);
        Product p2 = new Product(2L, "iPhone 15 Pro", 8999.0, 50);
        Product p3 = new Product(3L, "MacBook Pro", 14999.0, 30);
        Product p4 = new Product(4L, "iPad", 3999.0, 200);
        Product p5 = new Product(5L, "AirPods Pro", 1999.0, 150);
        
        cache.put("product:1", p1);
        cache.put("product:2", p2);
        cache.put("product:3", p3);
        cache.put("product:4", p4);
        cache.put("product:5", p5);
        
        System.out.println("--- 1. 从缓存获取所有产品，转换为 Seq ---");
        List<Product> productList = new ArrayList<>(cache.values());
        Seq<Product> products = Seqs.from(productList);
        
        System.out.println("\n--- 2. 筛选价格小于 10000 的产品 ---");
        products
            .where(p -> p.getPrice() < 10000)
            .forEach(p -> System.out.println(p));
        
        System.out.println("\n--- 3. 计算所有产品的总库存价值 ---");
        double totalValue = products
            .mapToDouble(p -> p.getPrice() * p.getStock())
            .sum();
        System.out.println("库存总价值：" + totalValue + " 元");
        
        System.out.println("\n--- 4. 找出库存最多的 3 个产品 ---");
        products
            .sortBy(Product::getStock, true)
            .take(3)
            .forEach(p -> System.out.println(p));
        
        System.out.println("\n--- 5. 按价格区间分组统计 ---");
        long cheapCount = products.where(p -> p.getPrice() < 5000).count();
        long midCount = products.where(p -> p.getPrice() >= 5000 && p.getPrice() < 10000).count();
        long expensiveCount = products.where(p -> p.getPrice() >= 10000).count();
        
        System.out.println("价格 < 5000：" + cheapCount + " 个");
        System.out.println("5000-10000：" + midCount + " 个");
        System.out.println("价格 >= 10000：" + expensiveCount + " 个");
        
        System.out.println("\n--- 6. 缓存热门产品到新的缓存 ---");
        Cache<String, Product> hotCache = Caches.newMemoryCache();
        products
            .where(p -> p.getStock() > 100)
            .forEach(p -> hotCache.put("hot:" + p.getId(), p));
        
        System.out.println("热门产品缓存大小：" + hotCache.size());
    }
}

class Product {
    private Long id;
    private String name;
    private double price;
    private int stock;
    
    public Product(Long id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}
```

---

## 最佳实践

### 1. 合理设置缓存大小

```java
// ✅ 推荐：根据实际需求设置
Cache<String, String> cache = Caches.newMemoryCache(10000);

// ❌ 不推荐：设置太大浪费内存
Cache<String, String> badCache = Caches.newMemoryCache(10000000);
```

### 2. 善用 TTL

```java
// ✅ 推荐：为经常变化的数据设置较短的 TTL
cache.put("hot:products", hotProducts, 5, TimeUnit.MINUTES);

// ✅ 推荐：为基本不变的数据设置较长的 TTL 或不过期
cache.put("config:settings", settings);
```

### 3. 监控缓存命中率

```java
CacheStats stats = cache.getStats();
double hitRate = stats.getHitRate();

if (hitRate < 0.5) {
    System.out.println("缓存命中率太低，考虑调整策略！");
}
```

### 4. 使用 CacheLoader

```java
// ✅ 推荐：使用 CacheLoader 自动加载
cache.setLoader(key -> loadFromDatabase(key));
Optional<Data> data = cache.get(key);

// ❌ 不推荐：手动判断
Optional<Data> data = cache.get(key);
if (data.isEmpty()) {
    Data dbData = loadFromDatabase(key);
    cache.put(key, dbData);
}
```

## 常见问题

### Q: 内存缓存数据会持久化吗？

A: MemoryCache 是内存存储，程序重启后数据会丢失。如果需要持久化，请使用 Redis 或文件缓存实现。

### Q: 如何选择合适的缓存大小？

A: 根据你的数据量和可用内存来决定。建议先做性能测试，找到最佳平衡点。

### Q: 多线程环境下安全吗？

A: 是的！MemoryCache 使用 ConcurrentHashMap，是线程安全的。

## 下一步

- 学习 [est-collection](../../est-foundation/est-collection/README.md) 进行数据处理
- 查看 [est-features-data](../est-features-data/) 了解数据持久化
- 尝试使用 Redis 或文件缓存实现
