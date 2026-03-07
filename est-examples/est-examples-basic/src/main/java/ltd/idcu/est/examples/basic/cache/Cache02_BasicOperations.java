package ltd.idcu.est.examples.basic.cache;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.Caches;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Cache02_BasicOperations {
    public static void main(String[] args) {
        System.out.println("=== 基础篇：基本操作 ===\n");
        
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- 1. 放入数据 ---");
        cache.put("user:1", "张三");
        cache.put("user:2", "李四");
        System.out.println("放入 2 个用�?);
        
        System.out.println("\n--- 2. 获取数据 ---");
        Optional<String> user1 = cache.get("user:1");
        user1.ifPresent(name -> System.out.println("user:1 = " + name));
        
        String user3 = cache.get("user:3", "未知用户");
        System.out.println("user:3 = " + user3);
        
        System.out.println("\n--- 3. 检查是否存�?---");
        System.out.println("user:1 存在�? + cache.containsKey("user:1"));
        System.out.println("user:4 存在�? + cache.containsKey("user:4"));
        
        System.out.println("\n--- 4. 删除数据 ---");
        cache.remove("user:2");
        System.out.println("删除 user:2 后，缓存大小�? + cache.size());
        
        System.out.println("\n--- 5. 设置过期时间 ---");
        cache.put("temp:1", "临时数据", 10, TimeUnit.SECONDS);
        System.out.println("放入 10 秒过期的数据");
        
        System.out.println("\n--- 6. 清空缓存 ---");
        cache.clear();
        System.out.println("清空后，缓存大小�? + cache.size());
        System.out.println("缓存为空�? + cache.isEmpty());
    }
}
