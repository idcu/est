package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.CacheMonitorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCacheMonitorService implements CacheMonitorService {
    
    private final Map<String, Map<String, Object>> cacheData = new ConcurrentHashMap<>();
    
    public DefaultCacheMonitorService() {
        initializeMockCacheData();
    }
    
    private void initializeMockCacheData() {
        Map<String, Object> userCache = new HashMap<>();
        userCache.put("name", "userCache");
        userCache.put("size", 156);
        userCache.put("hitCount", 12345);
        userCache.put("missCount", 456);
        userCache.put("hitRate", 0.964);
        cacheData.put("userCache", userCache);
        
        Map<String, Object> menuCache = new HashMap<>();
        menuCache.put("name", "menuCache");
        menuCache.put("size", 42);
        menuCache.put("hitCount", 8923);
        menuCache.put("missCount", 128);
        menuCache.put("hitRate", 0.986);
        cacheData.put("menuCache", menuCache);
        
        Map<String, Object> roleCache = new HashMap<>();
        roleCache.put("name", "roleCache");
        roleCache.put("size", 28);
        roleCache.put("hitCount", 5678);
        roleCache.put("missCount", 89);
        roleCache.put("hitRate", 0.984);
        cacheData.put("roleCache", roleCache);
    }
    
    @Override
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("caches", cacheData.values());
        result.put("totalSize", cacheData.values().stream().mapToInt(c -> (int) c.get("size")).sum());
        result.put("totalHitCount", cacheData.values().stream().mapToLong(c -> (long) c.get("hitCount")).sum());
        result.put("totalMissCount", cacheData.values().stream().mapToLong(c -> (long) c.get("missCount")).sum());
        long totalHits = result.get("totalHitCount") != null ? (long) result.get("totalHitCount") : 0;
        long totalMisses = result.get("totalMissCount") != null ? (long) result.get("totalMissCount") : 0;
        double totalHitRate = totalHits + totalMisses > 0 ? (double) totalHits / (totalHits + totalMisses) : 0;
        result.put("totalHitRate", totalHitRate);
        return result;
    }
    
    @Override
    public Map<String, Object> getCacheKeys() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : cacheData.entrySet()) {
            result.put(entry.getKey(), List.of("key1", "key2", "key3"));
        }
        return result;
    }
    
    @Override
    public void clearCache(String cacheName) {
        Map<String, Object> cache = cacheData.get(cacheName);
        if (cache != null) {
            cache.put("size", 0);
            cache.put("hitCount", 0L);
            cache.put("missCount", 0L);
            cache.put("hitRate", 0.0);
        }
    }
    
    @Override
    public void clearAllCaches() {
        for (String cacheName : cacheData.keySet()) {
            clearCache(cacheName);
        }
    }
}
