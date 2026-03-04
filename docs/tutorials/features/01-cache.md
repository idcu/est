# 功能教程 01: 缓存系统

在本教程中，你将学习如何使用 EST 的缓存系统。

## 步骤 1: 添加缓存依赖

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

## 步骤 2: 使用内存缓存

创建 `MemoryCacheExample.java`：

```java
package com.example.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheStats;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

public class MemoryCacheExample {
    public static void main(String[] args) {
        // 创建内存缓存
        Cache<String, String> cache = MemoryCaches.create();
        
        // 设置缓存（1小时过期）
        cache.put("key1", "value1", 3600);
        cache.put("key2", "value2", 3600);
        
        // 获取缓存
        System.out.println("key1: " + cache.get("key1"));
        System.out.println("key2: " + cache.get("key2"));
        
        // 获取不存在的键
        System.out.println("key3: " + cache.get("key3"));
        
        // 使用默认值
        System.out.println("key3 (default): " + cache.get("key3", "default-value"));
        
        // 删除缓存
        cache.remove("key1");
        System.out.println("After remove key1: " + cache.get("key1"));
        
        // 查看统计信息
        CacheStats stats = cache.getStats();
        System.out.println("\nCache Stats:");
        System.out.println("  Hits: " + stats.getHits());
        System.out.println("  Misses: " + stats.getMisses());
        System.out.println("  Hit Rate: " + stats.getHitRate());
        System.out.println("  Size: " + stats.getSize());
        
        // 清空缓存
        cache.clear();
        System.out.println("\nAfter clear, size: " + cache.getStats().getSize());
    }
}
```

## 步骤 3: 使用 LRU 缓存策略

创建 `LruCacheExample.java`：

```java
package com.example.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheConfig;
import ltd.idcu.est.features.cache.memory.LruCacheStrategy;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

public class LruCacheExample {
    public static void main(String[] args) {
        // 创建 LRU 缓存配置
        CacheConfig config = CacheConfig.builder()
                .maxSize(3) // 最多缓存 3 个条目
                .build();
        
        // 使用 LRU 策略创建缓存
        Cache<String, String> cache = MemoryCaches.create(
                new LruCacheStrategy<>(config),
                config
        );
        
        // 添加 4 个条目（超过 maxSize）
        cache.put("key1", "value1", 3600);
        cache.put("key2", "value2", 3600);
        cache.put("key3", "value3", 3600);
        cache.put("key4", "value4", 3600); // key1 将被淘汰
        
        // 检查缓存
        System.out.println("key1: " + cache.get("key1")); // null（已被淘汰）
        System.out.println("key2: " + cache.get("key2")); // value2
        System.out.println("key3: " + cache.get("key3")); // value3
        System.out.println("key4: " + cache.get("key4")); // value4
        
        // 访问 key2，使其变为最近使用
        cache.get("key2");
        
        // 再添加一个条目
        cache.put("key5", "value5", 3600); // key3 将被淘汰（因为 key2 刚被访问过）
        
        System.out.println("\nAfter adding key5:");
        System.out.println("key3: " + cache.get("key3")); // null
        System.out.println("key2: " + cache.get("key2")); // value2
    }
}
```

## 步骤 4: 使用文件缓存

创建 `FileCacheExample.java`：

```java
package com.example.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.file.FileCaches;

import java.nio.file.Paths;

public class FileCacheExample {
    public static void main(String[] args) {
        // 创建文件缓存，存储在 ./cache 目录
        Cache<String, String> cache = FileCaches.create(
                Paths.get("./cache")
        );
        
        // 设置缓存
        cache.put("user:1001", "{\"name\":\"Alice\",\"age\":30}", 3600);
        cache.put("user:1002", "{\"name\":\"Bob\",\"age\":25}", 3600);
        
        // 获取缓存
        System.out.println("User 1001: " + cache.get("user:1001"));
        System.out.println("User 1002: " + cache.get("user:1002"));
        
        // 查看统计
        System.out.println("\nCache size: " + cache.getStats().getSize());
        
        // 文件缓存会持久化到磁盘，重启应用后仍然可用
        System.out.println("\nFile cache is persistent!");
        System.out.println("Check ./cache directory for stored files.");
    }
}
```

## 步骤 5: 使用缓存加载器

创建 `CacheLoaderExample.java`：

```java
package com.example.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheLoader;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

public class CacheLoaderExample {
    public static void main(String[] args) {
        // 创建带加载器的缓存
        Cache<String, String> cache = MemoryCaches.create(new CacheLoader<>() {
            @Override
            public String load(String key) {
                // 模拟从数据库或其他数据源加载
                System.out.println("Loading data for key: " + key);
                return "Loaded value for " + key;
            }
        });
        
        // 第一次获取，会触发加载
        System.out.println("First get: " + cache.get("key1"));
        
        // 第二次获取，从缓存返回
        System.out.println("Second get: " + cache.get("key1"));
        
        // 获取另一个键
        System.out.println("Get key2: " + cache.get("key2"));
    }
}
```

## 运行示例

编译并运行示例：

```bash
# 内存缓存示例
mvn exec:java -Dexec.mainClass="com.example.cache.MemoryCacheExample"

# LRU 缓存示例
mvn exec:java -Dexec.mainClass="com.example.cache.LruCacheExample"

# 文件缓存示例
mvn exec:java -Dexec.mainClass="com.example.cache.FileCacheExample"

# 缓存加载器示例
mvn exec:java -Dexec.mainClass="com.example.cache.CacheLoaderExample"
```

## 下一步

在下一个教程中，我们将学习 [事件系统](./02-event.md)。
