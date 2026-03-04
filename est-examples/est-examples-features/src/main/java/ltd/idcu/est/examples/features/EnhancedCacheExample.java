package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheConfig;
import ltd.idcu.est.features.cache.file.FileCaches;
import ltd.idcu.est.features.cache.memory.Caches;

import java.util.concurrent.TimeUnit;

public class EnhancedCacheExample {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== Enhanced Cache Examples ===");
        
        lruCacheExample();
        fileCacheExample();
        tempFileCacheExample();
        userFileCacheExample();
        cacheWithTTLExample();
    }
    
    private static void lruCacheExample() {
        System.out.println("\n--- LRU Cache Example ---");
        
        Cache<String, String> cache = Caches.newLruCache(3);
        System.out.println("Created LRU cache with max size: 3");
        
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        System.out.println("Added keys: a, b, c");
        
        cache.get("a");
        System.out.println("Accessed key: a (now most recently used)");
        
        cache.put("d", "4");
        System.out.println("Added key: d (should evict least recently used)");
        
        System.out.println("Key a exists: " + cache.containsKey("a"));
        System.out.println("Key b exists: " + cache.containsKey("b"));
        System.out.println("Key c exists: " + cache.containsKey("c"));
        System.out.println("Key d exists: " + cache.containsKey("d"));
        
        System.out.println("Cache size: " + cache.size());
    }
    
    private static void fileCacheExample() {
        System.out.println("\n--- File Cache Example ---");
        
        String cacheDir = System.getProperty("java.io.tmpdir") + "/est-file-cache-example";
        Cache<String, String> cache = FileCaches.newFileCache(cacheDir);
        System.out.println("Created file cache in: " + cacheDir);
        
        cache.put("key1", "File cached value 1");
        cache.put("key2", "File cached value 2");
        System.out.println("Stored 2 values in file cache");
        
        System.out.println("key1: " + cache.get("key1"));
        System.out.println("key2: " + cache.get("key2"));
        
        cache.remove("key1");
        System.out.println("Removed key1");
        System.out.println("key1 after removal: " + cache.get("key1"));
        
        cache.clear();
        System.out.println("Cache cleared");
        System.out.println("Cache size: " + cache.size());
    }
    
    private static void tempFileCacheExample() {
        System.out.println("\n--- Temporary File Cache Example ---");
        
        Cache<String, String> cache = FileCaches.newTempFileCache();
        System.out.println("Created temporary file cache");
        
        cache.put("temp-key", "Temporary cached data");
        System.out.println("Stored temporary value");
        
        System.out.println("Retrieved: " + cache.get("temp-key"));
    }
    
    private static void userFileCacheExample() {
        System.out.println("\n--- User File Cache Example ---");
        
        Cache<String, String> cache = FileCaches.newUserFileCache("my-app");
        System.out.println("Created user file cache in ~/.est-cache/my-app");
        
        cache.put("user-pref", "dark-mode=true");
        System.out.println("Stored user preference");
        
        System.out.println("User preference: " + cache.get("user-pref"));
    }
    
    private static void cacheWithTTLExample() throws InterruptedException {
        System.out.println("\n--- Cache with TTL Example ---");
        
        CacheConfig config = new CacheConfig()
                .setMaxSize(100)
                .setDefaultTtl(2000);
        
        Cache<String, String> cache = Caches.newMemoryCache(config);
        System.out.println("Created cache with 2 second TTL");
        
        cache.put("ttl-key", "This will expire");
        System.out.println("Stored value with TTL");
        
        System.out.println("Value before expiration: " + cache.get("ttl-key"));
        
        System.out.println("Waiting 2.5 seconds...");
        Thread.sleep(2500);
        
        System.out.println("Value after expiration: " + cache.get("ttl-key"));
        System.out.println("Key exists after expiration: " + cache.containsKey("ttl-key"));
    }
}
