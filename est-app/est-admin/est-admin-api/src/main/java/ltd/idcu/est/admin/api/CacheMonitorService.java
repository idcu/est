package ltd.idcu.est.admin.api;

import java.util.Map;

public interface CacheMonitorService {
    
    Map<String, Object> getCacheStatistics();
    
    Map<String, Object> getCacheKeys();
    
    void clearCache(String cacheName);
    
    void clearAllCaches();
}
