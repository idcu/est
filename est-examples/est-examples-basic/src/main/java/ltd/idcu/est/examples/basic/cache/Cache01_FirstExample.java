package ltd.idcu.est.examples.basic.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class Cache01_FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Cache 第一个示例 ===");
        System.out.println("这个示例将带你5分钟上手 EST Features Cache！\n");
        
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
