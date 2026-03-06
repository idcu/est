package ltd.idcu.est.core.config.impl;

import ltd.idcu.est.core.config.api.Config;
import ltd.idcu.est.utils.common.StringUtils;
import ltd.idcu.est.utils.io.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultConfig implements Config {
    
    private final Map<String, Object> properties = new ConcurrentHashMap<>();
    
    private final List<String> loadedResources = new ArrayList<>();
    
    public DefaultConfig() {
        loadEnvironmentVariables();
    }
    
    public DefaultConfig(Map<String, Object> initialProperties) {
        this();
        if (initialProperties != null) {
            for (Entry<String, Object> entry : initialProperties.entrySet()) {
                setNestedProperty(entry.getKey(), entry.getValue());
            }
        }
    }
    
    private void loadEnvironmentVariables() {
        Map<String, String> env = System.getenv();
        for (Entry<String, String> entry : env.entrySet()) {
            String key = convertEnvKey(entry.getKey());
            set(key, entry.getValue());
        }
        
        Properties systemProps = System.getProperties();
        for (Entry<Object, Object> entry : systemProps.entrySet()) {
            set((String) entry.getKey(), entry.getValue());
        }
    }
    
    private String convertEnvKey(String envKey) {
        return envKey.toLowerCase().replace('_', '.');
    }
    
    @Override
    public void set(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            remove(key);
        } else {
            setNestedProperty(key, value);
        }
    }
    
    private void setNestedProperty(String key, Object value) {
        if (!key.contains(".")) {
            properties.put(key, value);
            return;
        }
        
        String[] parts = key.split("\\.", 2);
        String firstPart = parts[0];
        String remaining = parts[1];
        
        Object current = properties.get(firstPart);
        if (current instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> nestedMap = (Map<String, Object>) current;
            setNestedProperty(nestedMap, remaining, value);
        } else {
            Map<String, Object> newMap = new HashMap<>();
            setNestedProperty(newMap, remaining, value);
            properties.put(firstPart, newMap);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setNestedProperty(Map<String, Object> map, String key, Object value) {
        if (!key.contains(".")) {
            map.put(key, value);
            return;
        }
        
        String[] parts = key.split("\\.", 2);
        String firstPart = parts[0];
        String remaining = parts[1];
        
        Object current = map.get(firstPart);
        if (current instanceof Map) {
            setNestedProperty((Map<String, Object>) current, remaining, value);
        } else {
            Map<String, Object> newMap = new HashMap<>();
            setNestedProperty(newMap, remaining, value);
            map.put(firstPart, newMap);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object value = getNestedProperty(key);
        return (T) value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object value = getNestedProperty(key);
        return value != null ? (T) value : defaultValue;
    }
    
    private Object getNestedProperty(String key) {
        if (!key.contains(".")) {
            return properties.get(key);
        }
        
        String[] parts = key.split("\\.", 2);
        String firstPart = parts[0];
        String remaining = parts[1];
        
        Object current = properties.get(firstPart);
        if (current instanceof Map) {
            return getNestedProperty((Map<?, ?>) current, remaining);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private Object getNestedProperty(Map<?, ?> map, String key) {
        if (!key.contains(".")) {
            return map.get(key);
        }
        
        String[] parts = key.split("\\.", 2);
        String firstPart = parts[0];
        String remaining = parts[1];
        
        Object current = map.get(firstPart);
        if (current instanceof Map) {
            return getNestedProperty((Map<?, ?>) current, remaining);
        }
        return null;
    }
    
    @Override
    public String getString(String key) {
        return getString(key, null);
    }
    
    @Override
    public String getString(String key, String defaultValue) {
        Object value = getNestedProperty(key);
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
        Object value = getNestedProperty(key);
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
        Object value = getNestedProperty(key);
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
        Object value = getNestedProperty(key);
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
        Object value = getNestedProperty(key);
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
    public <T> List<T> getList(String key) {
        return getList(key, null);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, List<T> defaultValue) {
        Object value = getNestedProperty(key);
        if (value instanceof List) {
            return (List<T>) value;
        }
        return defaultValue;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(String key) {
        return getMap(key, null);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(String key, Map<K, V> defaultValue) {
        Object value = getNestedProperty(key);
        if (value instanceof Map) {
            return (Map<K, V>) value;
        }
        return defaultValue;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = getNestedProperty(key);
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
        return key != null && getNestedProperty(key) != null;
    }
    
    @Override
    public Set<String> getKeys() {
        return Collections.unmodifiableSet(properties.keySet());
    }
    
    @Override
    public void remove(String key) {
        if (key != null) {
            if (!key.contains(".")) {
                properties.remove(key);
            } else {
                String[] parts = key.split("\\.", 2);
                String firstPart = parts[0];
                String remaining = parts[1];
                
                Object current = properties.get(firstPart);
                if (current instanceof Map) {
                    removeNestedProperty((Map<String, Object>) current, remaining);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void removeNestedProperty(Map<String, Object> map, String key) {
        if (!key.contains(".")) {
            map.remove(key);
            return;
        }
        
        String[] parts = key.split("\\.", 2);
        String firstPart = parts[0];
        String remaining = parts[1];
        
        Object current = map.get(firstPart);
        if (current instanceof Map) {
            removeNestedProperty((Map<String, Object>) current, remaining);
        }
    }
    
    @Override
    public void load(String resourcePath) {
        if (StringUtils.isBlank(resourcePath)) {
            return;
        }
        
        try {
            InputStream inputStream = ResourceUtils.getInputStream(resourcePath);
            loadFromInputStream(inputStream, resourcePath);
            loadedResources.add(resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourcePath, e);
        }
    }
    
    @Override
    public void load(Path filePath) {
        if (filePath == null) {
            return;
        }
        
        String path = filePath.toString();
        try {
            InputStream inputStream = ResourceUtils.getInputStream(path);
            loadFromInputStream(inputStream, path);
            loadedResources.add(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + filePath, e);
        }
    }
    
    private void loadFromInputStream(InputStream inputStream, String resourceName) throws IOException {
        if (resourceName.endsWith(".properties")) {
            loadProperties(inputStream);
        } else {
            loadProperties(inputStream);
        }
    }
    
    private void loadProperties(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        
        for (Entry<Object, Object> entry : props.entrySet()) {
            set((String) entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(properties));
    }
    
    @Override
    public void reload() {
        properties.clear();
        loadEnvironmentVariables();
        
        List<String> resources = new ArrayList<>(loadedResources);
        loadedResources.clear();
        
        for (String resource : resources) {
            try {
                load(resource);
            } catch (Exception e) {
                throw new RuntimeException("Failed to reload resource: " + resource, e);
            }
        }
    }
    
    @Override
    public void clear() {
        properties.clear();
        loadedResources.clear();
    }
}
