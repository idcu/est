package ltd.idcu.est.examples.features;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.Caches;

public class CacheExample {
    public static void main(String[] args) {
        // Create memory cache
        Cache<String, String> cache = Caches.newMemoryCache();
        
        // Store data
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        // Get data
        System.out.println("Value for key1: " + cache.get("key1"));
        System.out.println("Value for key2: " + cache.get("key2"));
        
        // Check if key exists
        System.out.println("Key1 exists: " + cache.containsKey("key1"));
        System.out.println("Key3 exists: " + cache.containsKey("key3"));
        
        // Remove data
        cache.remove("key1");
        System.out.println("Value for key1 after removal: " + cache.get("key1"));
        
        // Clear cache
        cache.clear();
        System.out.println("Cache size after clear: " + cache.size());
    }
}
