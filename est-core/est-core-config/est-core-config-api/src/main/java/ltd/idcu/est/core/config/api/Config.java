package ltd.idcu.est.core.config.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Config {
    
    void set(String key, Object value);
    
    <T> T get(String key);
    
    <T> T get(String key, T defaultValue);
    
    String getString(String key);
    
    String getString(String key, String defaultValue);
    
    int getInt(String key);
    
    int getInt(String key, int defaultValue);
    
    long getLong(String key);
    
    long getLong(String key, long defaultValue);
    
    double getDouble(String key);
    
    double getDouble(String key, double defaultValue);
    
    boolean getBoolean(String key);
    
    boolean getBoolean(String key, boolean defaultValue);
    
    <T> List<T> getList(String key);
    
    <T> List<T> getList(String key, List<T> defaultValue);
    
    <K, V> Map<K, V> getMap(String key);
    
    <K, V> Map<K, V> getMap(String key, Map<K, V> defaultValue);
    
    <T> Optional<T> get(String key, Class<T> type);
    
    boolean contains(String key);
    
    Set<String> getKeys();
    
    void remove(String key);
    
    void load(String resourcePath);
    
    void load(java.nio.file.Path filePath);
    
    Map<String, Object> getAll();
    
    void reload();
    
    void clear();
}
