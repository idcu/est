package ltd.idcu.est.cache.memory;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.api.CacheConfig;
import ltd.idcu.est.cache.api.CacheListener;
import ltd.idcu.est.cache.api.CacheLoader;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryCacheTest {

    @Test
    public void testPutAndGet() {
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        
        Optional<String> result = cache.get("key");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("value", result.get());
    }

    @Test
    public void testGetWithDefault() {
        Cache<String, String> cache = Caches.newMemoryCache();
        String result = cache.get("key", "default");
        Assertions.assertEquals("default", result);
    }

    @Test
    public void testRemove() {
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        Assertions.assertTrue(cache.containsKey("key"));
        
        cache.remove("key");
        Assertions.assertFalse(cache.containsKey("key"));
    }

    @Test
    public void testClear() {
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        Assertions.assertEquals(2, cache.size());
        
        cache.clear();
        Assertions.assertTrue(cache.isEmpty());
    }

    @Test
    public void testSize() {
        Cache<String, String> cache = Caches.newMemoryCache();
        Assertions.assertEquals(0, cache.size());
        
        cache.put("key", "value");
        Assertions.assertEquals(1, cache.size());
    }

    @Test
    public void testIsEmpty() {
        Cache<String, String> cache = Caches.newMemoryCache();
        Assertions.assertTrue(cache.isEmpty());
        
        cache.put("key", "value");
        Assertions.assertFalse(cache.isEmpty());
    }

    @Test
    public void testContainsKey() {
        Cache<String, String> cache = Caches.newMemoryCache();
        Assertions.assertFalse(cache.containsKey("key"));
        
        cache.put("key", "value");
        Assertions.assertTrue(cache.containsKey("key"));
    }

    @Test
    public void testPutNullKeyThrowsException() {
        Cache<String, String> cache = Caches.newMemoryCache();
        Assertions.assertThrows(NullPointerException.class, () -> {
            cache.put(null, "value");
        });
    }

    @Test
    public void testGetNullKeyReturnsEmpty() {
        Cache<String, String> cache = Caches.newMemoryCache();
        Optional<String> result = cache.get(null);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testTtl() throws InterruptedException {
        CacheConfig config = CacheConfig.defaultConfig().setDefaultTtl(100);
        Cache<String, String> cache = Caches.newMemoryCache(config);
        
        cache.put("key", "value");
        Assertions.assertTrue(cache.containsKey("key"));
        
        Thread.sleep(200);
        Assertions.assertFalse(cache.containsKey("key"));
    }

    @Test
    public void testEviction() {
        Cache<String, String> cache = Caches.newMemoryCache(3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        Assertions.assertEquals(3, cache.size());
        
        cache.put("key4", "value4");
        Assertions.assertEquals(3, cache.size());
    }

    @Test
    public void testCacheLoader() {
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        AtomicInteger loadCount = new AtomicInteger(0);
        
        cache.setLoader(new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                loadCount.incrementAndGet();
                return "loaded:" + key;
            }
        });
        
        Optional<String> result = cache.get("key");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("loaded:key", result.get());
        Assertions.assertEquals(1, loadCount.get());
    }

    @Test
    public void testCacheListener() {
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        AtomicInteger putCount = new AtomicInteger(0);
        AtomicInteger removeCount = new AtomicInteger(0);
        
        cache.addListener(new CacheListener<String, String>() {
            @Override
            public void onPut(String key, String value) {
                putCount.incrementAndGet();
            }

            @Override
            public void onRemove(String key, String value) {
                removeCount.incrementAndGet();
            }

            @Override
            public void onEvict(String key, String value) {}

            @Override
            public void onExpire(String key, String value) {}

            @Override
            public void onClear() {}
        });
        
        cache.put("key", "value");
        Assertions.assertEquals(1, putCount.get());
        
        cache.remove("key");
        Assertions.assertEquals(1, removeCount.get());
    }

    @Test
    public void testBuilder() {
        Cache<String, String> cache = Caches.<String, String>builder()
                .maxSize(500)
                .defaultTtl(60000)
                .build();
        
        cache.put("key", "value");
        Optional<String> result = cache.get("key");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("value", result.get());
    }

    @Test
    public void testKeySet() {
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        Assertions.assertTrue(cache.keySet().contains("key1"));
        Assertions.assertTrue(cache.keySet().contains("key2"));
        Assertions.assertEquals(2, cache.keySet().size());
    }

    @Test
    public void testValues() {
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        Assertions.assertTrue(cache.values().contains("value1"));
        Assertions.assertTrue(cache.values().contains("value2"));
        Assertions.assertEquals(2, cache.values().size());
    }

    @Test
    public void testEntrySet() {
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        Assertions.assertEquals(2, cache.entrySet().size());
    }

    @Test
    public void testStats() {
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        cache.get("key");
        cache.get("nonexistent");
        
        Assertions.assertEquals(1, cache.getStats().getPutCount());
        Assertions.assertEquals(1, cache.getStats().getHitCount());
        Assertions.assertEquals(1, cache.getStats().getMissCount());
    }
}
