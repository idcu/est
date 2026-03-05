# 功能教程 01: 缓存系统

在本教程中，我们将学习如何使用 EST 的缓存系统。缓存就像是一个"临时储物柜"，可以让你的应用运行得更快！

## 前置知识

在开始本教程之前，确保你已经：
- 完成了 [第一个应用教程](../beginner/01-first-app.md)
- 了解了 EST 的基本使用方法

## 什么是缓存？

想象一下，你在图书馆找一本书：
- **没有缓存**：每次都要去书架找（很慢）
- **有缓存**：把最近读的书放在桌子上（很快）

缓存就是把经常使用的数据放在容易拿到的地方，让应用运行得更快！

## 缓存的好处

1. **更快的响应速度**：不需要每次都去数据库查
2. **减轻数据库压力**：减少数据库的查询次数
3. **节省资源**：减少网络请求和计算

## 开始使用缓存

### 步骤 1: 添加缓存依赖

在 `pom.xml` 中添加缓存模块依赖：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-file</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 步骤 2: 使用内存缓存（最简单）

内存缓存是最快的缓存方式，数据存储在内存中。让我们创建一个简单的示例：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheStats;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

public class MemoryCacheDemo {
    public static void main(String[] args) {
        System.out.println("=== 内存缓存演示 ===\n");
        
        // 1. 创建一个内存缓存
        Cache<String, String> cache = MemoryCaches.create();
        
        // 2. 向缓存中存储数据（设置 1 小时过期）
        System.out.println("1. 存储数据到缓存...");
        cache.put("username", "张三", 3600);
        cache.put("email", "zhangsan@example.com", 3600);
        cache.put("city", "北京", 3600);
        System.out.println("   ✓ 数据已存储\n");
        
        // 3. 从缓存中获取数据
        System.out.println("2. 从缓存获取数据:");
        System.out.println("   username: " + cache.get("username"));
        System.out.println("   email: " + cache.get("email"));
        System.out.println("   city: " + cache.get("city"));
        System.out.println();
        
        // 4. 获取不存在的数据
        System.out.println("3. 获取不存在的数据:");
        System.out.println("   age: " + cache.get("age"));  // null
        System.out.println("   age (带默认值): " + cache.get("age", "18"));  // 18
        System.out.println();
        
        // 5. 删除缓存
        System.out.println("4. 删除缓存...");
        cache.remove("city");
        System.out.println("   city 删除后: " + cache.get("city"));
        System.out.println();
        
        // 6. 查看缓存统计信息
        System.out.println("5. 缓存统计:");
        CacheStats stats = cache.getStats();
        System.out.println("   命中次数: " + stats.getHits());
        System.out.println("   未命中次数: " + stats.getMisses());
        System.out.println("   命中率: " + stats.getHitRate());
        System.out.println("   当前缓存数量: " + stats.getSize());
        System.out.println();
        
        // 7. 清空所有缓存
        System.out.println("6. 清空所有缓存...");
        cache.clear();
        System.out.println("   清空后数量: " + cache.getStats().getSize());
    }
}
```

运行这个程序，你会看到缓存的基本使用方法！

## LRU 缓存策略（智能淘汰）

LRU（Least Recently Used，最近最少使用）是一种智能的缓存淘汰策略。当缓存满了，它会删除最久没使用的数据。

想象一下你的书桌：
- 你有 3 本书的位置
- 当你放第 4 本书时，最久没看的那本会被收走

让我们来看一个例子：

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheConfig;
import ltd.idcu.est.features.cache.memory.LruCacheStrategy;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

public class LruCacheDemo {
    public static void main(String[] args) {
        System.out.println("=== LRU 缓存演示 ===\n");
        
        // 1. 创建 LRU 缓存配置（最多缓存 3 个条目）
        CacheConfig config = CacheConfig.builder()
                .maxSize(3)
                .build();
        
        // 2. 使用 LRU 策略创建缓存
        Cache<String, String> cache = MemoryCaches.create(
                new LruCacheStrategy<>(config),
                config
        );
        
        // 3. 添加 4 个数据（超过限制）
        System.out.println("1. 添加 4 个数据（缓存限制 3 个）:");
        cache.put("book1", "Java 编程", 3600);
        cache.put("book2", "Python 入门", 3600);
        cache.put("book3", "算法导论", 3600);
        cache.put("book4", "设计模式", 3600);  // book1 会被淘汰
        
        System.out.println("   book1: " + cache.get("book1"));  // null（已被淘汰）
        System.out.println("   book2: " + cache.get("book2"));  // Python 入门
        System.out.println("   book3: " + cache.get("book3"));  // 算法导论
        System.out.println("   book4: " + cache.get("book4"));  // 设计模式
        System.out.println();
        
        // 4. 访问 book2，让它变成"最近使用"
        System.out.println("2. 访问 book2（让它变成最近使用）...");
        cache.get("book2");
        System.out.println("   ✓ book2 已被访问\n");
        
        // 5. 再添加一个数据
        System.out.println("3. 再添加 book5...");
        cache.put("book5", "数据库原理", 3600);  // book3 会被淘汰（因为 book2 刚被访问过）
        
        System.out.println("   book3: " + cache.get("book3"));  // null（已被淘汰）
        System.out.println("   book2: " + cache.get("book2"));  // Python 入门（还在）
        System.out.println("   book5: " + cache.get("book5"));  // 数据库原理
    }
}
```

## 文件缓存（持久化存储）

文件缓存会把数据保存到磁盘上，即使重启应用，数据也不会丢失！

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.file.FileCaches;
import java.nio.file.Paths;

public class FileCacheDemo {
    public static void main(String[] args) {
        System.out.println("=== 文件缓存演示 ===\n");
        
        // 1. 创建文件缓存，存储在 ./cache 目录
        Cache<String, String> cache = FileCaches.create(
                Paths.get("./cache")
        );
        
        // 2. 存储用户数据
        System.out.println("1. 存储用户数据...");
        cache.put("user:1001", "{\"name\":\"张三\",\"age\":25,\"city\":\"北京\"}", 3600);
        cache.put("user:1002", "{\"name\":\"李四\",\"age\":30,\"city\":\"上海\"}", 3600);
        System.out.println("   ✓ 数据已存储到文件\n");
        
        // 3. 获取数据
        System.out.println("2. 获取用户数据:");
        System.out.println("   用户 1001: " + cache.get("user:1001"));
        System.out.println("   用户 1002: " + cache.get("user:1002"));
        System.out.println();
        
        // 4. 查看缓存数量
        System.out.println("3. 缓存统计:");
        System.out.println("   当前缓存数量: " + cache.getStats().getSize());
        System.out.println();
        
        // 5. 提示
        System.out.println("💡 文件缓存会持久化到磁盘！");
        System.out.println("💡 查看 ./cache 目录可以看到存储的文件");
        System.out.println("💡 重启应用后数据仍然可用！");
    }
}
```

运行后，你可以在项目目录下看到一个 `cache` 文件夹，里面存储着缓存的数据！

## 缓存加载器（自动加载）

缓存加载器可以在缓存中没有数据时，自动从数据源（比如数据库）加载数据。

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheLoader;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

public class CacheLoaderDemo {
    public static void main(String[] args) {
        System.out.println("=== 缓存加载器演示 ===\n");
        
        // 1. 创建带加载器的缓存
        Cache<String, String> cache = MemoryCaches.create(new CacheLoader<>() {
            @Override
            public String load(String key) {
                // 模拟从数据库或其他数据源加载
                System.out.println("   ⚡ 从数据库加载: " + key);
                return "数据_" + key + "_" + System.currentTimeMillis();
            }
        });
        
        // 2. 第一次获取，会触发加载
        System.out.println("1. 第一次获取 key1:");
        System.out.println("   结果: " + cache.get("key1"));
        System.out.println();
        
        // 3. 第二次获取，直接从缓存返回
        System.out.println("2. 第二次获取 key1:");
        System.out.println("   结果: " + cache.get("key1"));
        System.out.println("   (注意：没有从数据库加载！)");
        System.out.println();
        
        // 4. 获取另一个键
        System.out.println("3. 获取 key2:");
        System.out.println("   结果: " + cache.get("key2"));
    }
}
```

## 在 Web 应用中使用缓存

让我们把缓存应用到 Web 应用中，提升性能：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCaches;
import java.util.Map;

public class WebCacheApp {
    
    // 创建一个缓存来存储用户数据
    private static final Cache<String, Map<String, Object>> userCache = MemoryCaches.create();
    
    public static void main(String[] args) {
        WebApplication app = Web.create("缓存 Web 应用", "1.0.0");
        
        // 获取用户信息（使用缓存）
        app.get("/users/:id", (req, res) -> {
            String userId = req.getPathVariable("id");
            String cacheKey = "user:" + userId;
            
            // 先尝试从缓存获取
            Map<String, Object> user = userCache.get(cacheKey);
            
            if (user == null) {
                // 缓存中没有，模拟从数据库查询
                System.out.println("⚡ 从数据库查询用户: " + userId);
                user = Map.of(
                    "id", userId,
                    "name", "用户" + userId,
                    "email", "user" + userId + "@example.com",
                    "cachedAt", "刚刚从数据库加载"
                );
                
                // 存入缓存（1小时过期）
                userCache.put(cacheKey, user, 3600);
            } else {
                // 缓存命中
                user = new java.util.HashMap<>(user);
                user.put("cachedAt", "从缓存加载");
            }
            
            res.json(user);
        });
        
        // 清除缓存
        app.post("/users/:id/clear-cache", (req, res) -> {
            String userId = req.getPathVariable("id");
            String cacheKey = "user:" + userId;
            userCache.remove(cacheKey);
            res.json(Map.of(
                "success", true,
                "message", "缓存已清除"
            ));
        });
        
        // 查看缓存统计
        app.get("/cache/stats", (req, res) -> {
            res.json(Map.of(
                "size", userCache.getStats().getSize(),
                "hits", userCache.getStats().getHits(),
                "misses", userCache.getStats().getMisses(),
                "hitRate", userCache.getStats().getHitRate()
            ));
        });
        
        app.run(8080);
        System.out.println("应用已启动！");
        System.out.println("测试地址:");
        System.out.println("  - http://localhost:8080/users/1");
        System.out.println("  - http://localhost:8080/cache/stats");
    }
}
```

## 测试这个 Web 应用

1. 启动应用
2. 访问 `http://localhost:8080/users/1`，第一次会看到"刚刚从数据库加载"
3. 刷新页面，第二次会看到"从缓存加载"
4. 访问 `http://localhost:8080/cache/stats` 查看缓存统计
5. 访问 `http://localhost:8080/users/1/clear-cache` 清除缓存

## 最佳实践

1. **选择合适的缓存类型**
   - 内存缓存：速度最快，但重启后数据丢失
   - 文件缓存：持久化存储，适合需要保存的数据
   - 根据需求选择

2. **设置合理的过期时间**
   - 不要让缓存永不过期
   - 根据数据更新频率设置 TTL（生存时间）

3. **缓存 Key 设计**
   - 使用有意义的 Key，如 `user:123`、`product:456`
   - 避免 Key 冲突

4. **监控缓存**
   - 定期查看缓存命中率
   - 命中率低时考虑调整策略

5. **处理缓存雪崩**
   - 避免大量缓存同时过期
   - 可以在过期时间上加上随机值

## 小练习

1. 创建一个商品缓存系统，使用 LRU 策略，限制 10 个商品
2. 实现一个文件缓存来存储文章内容
3. 在 Web 应用中添加缓存，并实现缓存预热（启动时加载热门数据）

## 下一步

恭喜你完成了缓存系统的学习！现在你知道如何让你的应用运行得更快了！

接下来，你可以：
- 学习 [事件系统](./02-event.md)
- 探索 EST 的其他功能模块
- 在你的项目中应用缓存来提升性能！
