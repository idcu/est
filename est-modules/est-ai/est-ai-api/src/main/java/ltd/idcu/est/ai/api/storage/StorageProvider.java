package ltd.idcu.est.ai.api.storage;

import java.util.Map;

public interface StorageProvider {
    
    String getName();
    
    boolean exists(String key);
    
    String load(String key);
    
    void save(String key, String data);
    
    void delete(String key);
    
    Map<String, String> loadAll(String prefix);
    
    void saveAll(Map<String, String> data);
    
    void deleteAll(String prefix);
    
    void clear();
}
