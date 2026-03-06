package ltd.idcu.est.examples.basic.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class Cache04_LruExample {
    public static void main(String[] args) {
        System.out.println("=== 进阶篇：LRU 淘汰策略 ===\n");
        
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
