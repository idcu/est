package ltd.idcu.est.core.api;

import java.util.Optional;
import java.util.Set;

public interface Config {
    
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
    
    <T> Optional<T> get(String key, Class<T> type);
    
    boolean contains(String key);
    
    Set<String> getKeys();
    
    void set(String key, Object value);
    
    void remove(String key);
    
    void reload();
    
    void clear();
}
