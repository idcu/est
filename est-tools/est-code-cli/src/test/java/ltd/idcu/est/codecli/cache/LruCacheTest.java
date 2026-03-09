package ltd.idcu.est.codecli.cache;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import static ltd.idcu.est.test.Assertions.*;

public class LruCacheTest {
    
    private LruCache<String, String> cache;
    
    @BeforeEach
    void beforeEach() {
        cache = new LruCache<>(100);
    }
    
    @Test
    void testBasicPutAndGet() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }
    
    @Test
    void testGetNonExistent() {
        assertNull(cache.get("nonexistent"));
    }
    
    @Test
    void testContainsKey() {
        cache.put("key1", "value1");
        
        assertTrue(cache.containsKey("key1"));
        assertFalse(cache.containsKey("key2"));
    }
    
    @Test
    void testRemove() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        cache.remove("key1");
        
        assertNull(cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }
    
    @Test
    void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        
        cache.clear();
        
        assertTrue(cache.isEmpty());
        assertEquals(0, cache.size());
    }
    
    @Test
    void testSize() {
        assertEquals(0, cache.size());
        
        cache.put("key1", "value1");
        assertEquals(1, cache.size());
        
        cache.put("key2", "value2");
        assertEquals(2, cache.size());
        
        cache.remove("key1");
        assertEquals(1, cache.size());
    }
    
    @Test
    void testIsEmpty() {
        assertTrue(cache.isEmpty());
        
        cache.put("key1", "value1");
        assertFalse(cache.isEmpty());
        
        cache.clear();
        assertTrue(cache.isEmpty());
    }
    
    @Test
    void testEviction() {
        LruCache<Integer, String> smallCache = new LruCache<>(3);
        
        smallCache.put(1, "one");
        smallCache.put(2, "two");
        smallCache.put(3, "three");
        
        assertEquals(3, smallCache.size());
        
        smallCache.put(4, "four");
        
        assertEquals(3, smallCache.size());
        
        assertNotNull(smallCache.get(2));
        assertNotNull(smallCache.get(3));
        assertNotNull(smallCache.get(4));
    }
    
    @Test
    void testLruOrder() {
        LruCache<Integer, String> smallCache = new LruCache<>(3);
        
        smallCache.put(1, "one");
        smallCache.put(2, "two");
        smallCache.put(3, "three");
        
        smallCache.get(1);
        
        smallCache.put(4, "four");
        
        assertNotNull(smallCache.get(1));
        assertNotNull(smallCache.get(3));
        assertNotNull(smallCache.get(4));
    }
    
    @Test
    void testTtlExpiration() throws InterruptedException {
        LruCache<String, String> ttlCache = new LruCache<>(100, 100);
        
        ttlCache.put("key1", "value1");
        assertNotNull(ttlCache.get("key1"));
        
        Thread.sleep(150);
        
        assertNull(ttlCache.get("key1"));
    }
    
    @Test
    void testPerEntryTtl() throws InterruptedException {
        LruCache<String, String> cache = new LruCache<>(100);
        
        cache.put("key1", "value1", 50);
        cache.put("key2", "value2", 200);
        
        assertNotNull(cache.get("key1"));
        assertNotNull(cache.get("key2"));
        
        Thread.sleep(100);
        
        assertNull(cache.get("key1"));
        assertNotNull(cache.get("key2"));
    }
    
    @Test
    void testGetMaxSize() {
        LruCache<String, String> cache1 = new LruCache<>(50);
        LruCache<String, String> cache2 = new LruCache<>(200, 1000);
        
        assertEquals(50, cache1.getMaxSize());
        assertEquals(200, cache2.getMaxSize());
    }
    
    @Test
    void testGetDefaultTtl() {
        LruCache<String, String> cache1 = new LruCache<>(100);
        LruCache<String, String> cache2 = new LruCache<>(100, 5000);
        
        assertEquals(0, cache1.getDefaultTtl());
        assertEquals(5000, cache2.getDefaultTtl());
    }
    
    @Test
    void testUpdateExistingKey() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
        
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
        assertEquals(1, cache.size());
    }
    
    @Test
    void testNullKey() {
        assertThrows(NullPointerException.class, () -> {
            cache.put(null, "value");
        });
    }
    
    @Test
    void testNullValue() {
        cache.put("key1", null);
        assertNull(cache.get("key1"));
    }
}
