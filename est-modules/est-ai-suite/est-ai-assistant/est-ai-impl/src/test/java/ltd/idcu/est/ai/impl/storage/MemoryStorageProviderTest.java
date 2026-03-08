package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;

public class MemoryStorageProviderTest {
    
    @Test
    public void testProviderName() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        assertEquals("memory", provider.getName());
    }
    
    @Test
    public void testSaveAndLoad() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        String key = "test-key";
        String value = "test-value";
        
        provider.save(key, value);
        String loaded = provider.load(key);
        
        assertEquals(value, loaded);
    }
    
    @Test
    public void testExists() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        String key = "test-exists";
        
        assertFalse(provider.exists(key));
        provider.save(key, "value");
        assertTrue(provider.exists(key));
    }
    
    @Test
    public void testDelete() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        String key = "test-delete";
        
        provider.save(key, "value");
        assertTrue(provider.exists(key));
        
        provider.delete(key);
        assertFalse(provider.exists(key));
    }
    
    @Test
    public void testLoadAll() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        
        provider.save("prefix-key1", "value1");
        provider.save("prefix-key2", "value2");
        provider.save("other-key3", "value3");
        
        Map<String, String> result = provider.loadAll("prefix-");
        
        assertEquals(2, result.size());
        assertTrue(result.containsKey("prefix-key1"));
        assertTrue(result.containsKey("prefix-key2"));
        assertFalse(result.containsKey("other-key3"));
    }
    
    @Test
    public void testSaveAll() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        Map<String, String> data = Map.of(
            "key1", "value1",
            "key2", "value2"
        );
        
        provider.saveAll(data);
        
        assertEquals("value1", provider.load("key1"));
        assertEquals("value2", provider.load("key2"));
    }
    
    @Test
    public void testDeleteAll() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        
        provider.save("test-key1", "value1");
        provider.save("test-key2", "value2");
        provider.save("other-key", "value3");
        
        provider.deleteAll("test-");
        
        assertFalse(provider.exists("test-key1"));
        assertFalse(provider.exists("test-key2"));
        assertTrue(provider.exists("other-key"));
    }
    
    @Test
    public void testClear() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        
        provider.save("key1", "value1");
        provider.save("key2", "value2");
        
        provider.clear();
        
        assertFalse(provider.exists("key1"));
        assertFalse(provider.exists("key2"));
    }
    
    @Test
    public void testLoadNonExistent() {
        MemoryStorageProvider provider = new MemoryStorageProvider();
        String loaded = provider.load("non-existent-key");
        assertNull(loaded);
    }
}
