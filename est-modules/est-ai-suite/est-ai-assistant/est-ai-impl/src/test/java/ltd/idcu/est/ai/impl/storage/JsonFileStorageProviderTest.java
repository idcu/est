package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class JsonFileStorageProviderTest {
    
    private static final String TEST_STORAGE_DIR = "test-ai-storage";
    
    @Test
    public void testProviderName() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        assertEquals("json-file", provider.getName());
        cleanUp();
    }
    
    @Test
    public void testSaveAndLoad() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        String key = "test-key";
        String value = "test-value";
        
        provider.save(key, value);
        String loaded = provider.load(key);
        
        assertEquals(value, loaded);
        cleanUp();
    }
    
    @Test
    public void testExists() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        String key = "test-exists";
        
        assertFalse(provider.exists(key));
        provider.save(key, "value");
        assertTrue(provider.exists(key));
        cleanUp();
    }
    
    @Test
    public void testDelete() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        String key = "test-delete";
        
        provider.save(key, "value");
        assertTrue(provider.exists(key));
        
        provider.delete(key);
        assertFalse(provider.exists(key));
        cleanUp();
    }
    
    @Test
    public void testLoadAll() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        
        provider.save("prefix-key1", "value1");
        provider.save("prefix-key2", "value2");
        provider.save("other-key3", "value3");
        
        Map<String, String> result = provider.loadAll("prefix-");
        
        assertEquals(2, result.size());
        assertTrue(result.containsKey("prefix-key1"));
        assertTrue(result.containsKey("prefix-key2"));
        assertFalse(result.containsKey("other-key3"));
        cleanUp();
    }
    
    @Test
    public void testSaveAll() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        Map<String, String> data = Map.of(
            "key1", "value1",
            "key2", "value2"
        );
        
        provider.saveAll(data);
        
        assertEquals("value1", provider.load("key1"));
        assertEquals("value2", provider.load("key2"));
        cleanUp();
    }
    
    @Test
    public void testDeleteAll() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        
        provider.save("test-key1", "value1");
        provider.save("test-key2", "value2");
        provider.save("other-key", "value3");
        
        provider.deleteAll("test-");
        
        assertFalse(provider.exists("test-key1"));
        assertFalse(provider.exists("test-key2"));
        assertTrue(provider.exists("other-key"));
        cleanUp();
    }
    
    @Test
    public void testClear() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        
        provider.save("key1", "value1");
        provider.save("key2", "value2");
        
        provider.clear();
        
        assertFalse(provider.exists("key1"));
        assertFalse(provider.exists("key2"));
        cleanUp();
    }
    
    @Test
    public void testLoadNonExistent() {
        JsonFileStorageProvider provider = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        String loaded = provider.load("non-existent-key");
        assertNull(loaded);
        cleanUp();
    }
    
    @Test
    public void testPersistenceAcrossInstances() {
        String key = "persistent-key";
        String value = "persistent-value";
        
        JsonFileStorageProvider provider1 = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        provider1.save(key, value);
        
        JsonFileStorageProvider provider2 = new JsonFileStorageProvider(TEST_STORAGE_DIR);
        String loaded = provider2.load(key);
        
        assertEquals(value, loaded);
        cleanUp();
    }
    
    private void cleanUp() {
        try {
            Path storagePath = Paths.get(TEST_STORAGE_DIR);
            if (Files.exists(storagePath)) {
                Files.list(storagePath)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(path -> {
                         try {
                             Files.deleteIfExists(path);
                         } catch (IOException e) {
                         }
                     });
                Files.deleteIfExists(storagePath);
            }
        } catch (IOException e) {
        }
    }
}
