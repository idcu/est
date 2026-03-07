package ltd.idcu.est.examples.features;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.Caches;

public class CacheExample {
    public static void main(String[] args) {
        // 创建内存缓存
        Cache<String, String> cache = Caches.newMemoryCache();
        
        // 存储数据
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        // 获取数据
        System.out.println("Value for key1: " + cache.get("key1"));
        System.out.println("Value for key2: " + cache.get("key2"));
        
        // 检查键是否存在
        System.out.println("Key1 exists: " + cache.containsKey("key1"));
        System.out.println("Key3 exists: " + cache.containsKey("key3"));
        
        // 移除数据
        cache.remove("key1");
        System.out.println("Value for key1 after removal: " + cache.get("key1"));
        
        // 清空缓存
        cache.clear();
        System.out.println("Cache size after clear: " + cache.size());
    }
}