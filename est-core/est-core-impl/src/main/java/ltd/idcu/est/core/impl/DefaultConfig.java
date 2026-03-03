package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Config;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultConfig implements Config {
    
    private final Map<String, Object> properties = new ConcurrentHashMap<>();
    
    public DefaultConfig() {
    }
    
    public DefaultConfig(Map<String, Object> initialProperties) {
        if (initialProperties != null) {
            properties.putAll(initialProperties);
        }
    }
    
    @Override
    public String getString(String key) {
        return getString(key, null);
    }
    
    @Override
    public String getString(String key, String defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }
    
    @Override
    public int getInt(String key) {
        return getInt(key, 0);
    }
    
    @Override
    public int getInt(String key, int defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public long getLong(String key) {
        return getLong(key, 0L);
    }
    
    @Override
    public long getLong(String key, long defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }
    
    @Override
    public double getDouble(String key, double defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(value.toString());
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = properties.get(key);
        if (value == null || type == null) {
            return Optional.empty();
        }
        if (type.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }
    
    @Override
    public boolean contains(String key) {
        return key != null && properties.containsKey(key);
    }
    
    @Override
    public Set<String> getKeys() {
        return Set.copyOf(properties.keySet());
    }
    
    @Override
    public void set(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            properties.remove(key);
        } else {
            properties.put(key, value);
        }
    }
    
    @Override
    public void remove(String key) {
        if (key != null) {
            properties.remove(key);
        }
    }
    
    @Override
    public void reload() {
    }
    
    @Override
    public void clear() {
        properties.clear();
    }
}
