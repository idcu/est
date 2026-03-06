package ltd.idcu.est.examples.basic.cache;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheStats;
import ltd.idcu.est.features.cache.memory.Caches;

public class Cache03_StatsExample {
    public static void main(String[] args) {
        System.out.println("=== 进阶篇：缓存统计 ===\n");
        
        Cache<String, String> cache = Caches.newMemoryCache();
        
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
