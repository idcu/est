package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.storage.StorageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryStorageProvider implements StorageProvider {
    
    private final Map<String, String> storage = new HashMap<>();
    
    @Override
    public String getName() {
        return "memory";
    }
    
    @Override
    public boolean exists(String key) {
        return storage.containsKey(key);
    }
    
    @Override
    public String load(String key) {
        return storage.get(key);
    }
    
    @Override
    public void save(String key, String data) {
        storage.put(key, data);
    }
    
    @Override
    public void delete(String key) {
        storage.remove(key);
    }
    
    @Override
    public Map<String, String> loadAll(String prefix) {
        return storage.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    @Override
    public void saveAll(Map<String, String> data) {
        storage.putAll(data);
    }
    
    @Override
    public void deleteAll(String prefix) {
        storage.keySet().removeIf(key -> key.startsWith(prefix));
    }
    
    @Override
    public void clear() {
        storage.clear();
    }
}
