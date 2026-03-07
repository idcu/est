package ltd.idcu.est.config.impl;

import ltd.idcu.est.config.api.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultConfigCenter implements ConfigCenter {
    private final Map<String, Object> properties = new ConcurrentHashMap<>();
    private final List<ConfigChangeListener> listeners = new CopyOnWriteArrayList<>();
    private final List<ConfigAuditListener> auditListeners = new CopyOnWriteArrayList<>();
    private final Queue<ConfigAuditEvent> auditEvents = new ConcurrentLinkedQueue<>();
    private String environment = "default";
    private ConfigEncryptor encryptor;
    private boolean autoSaveEnabled = false;
    private String autoSavePath = null;
    private boolean auditEnabled = true;
    private int auditMaxSize = 1000;

    @Override
    public void setProperty(String key, Object value) {
        setProperty(key, value, "unknown");
    }

    @Override
    public void setProperty(String key, Object value, String source) {
        Objects.requireNonNull(key, "Key cannot be null");
        Object oldValue = properties.put(key, value);
        ConfigChangeEvent.ChangeType changeType = oldValue == null 
                ? ConfigChangeEvent.ChangeType.ADDED 
                : ConfigChangeEvent.ChangeType.MODIFIED;
        notifyListeners(new ConfigChangeEvent(key, oldValue, value, changeType));
        
        if (auditEnabled) {
            ConfigAuditEvent.ChangeType auditType = oldValue == null 
                    ? ConfigAuditEvent.ChangeType.ADDED 
                    : ConfigAuditEvent.ChangeType.MODIFIED;
            recordAuditEvent(new ConfigAuditEvent(key, oldValue, value, auditType, source));
        }
        
        autoSaveIfEnabled();
    }

    @Override
    public Optional<Object> getProperty(String key) {
        return Optional.ofNullable(properties.get(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getProperty(String key, Class<T> type) {
        Object value = properties.get(key);
        if (value != null && type.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getProperty(key, String.class).orElse(defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        Optional<Object> value = getProperty(key);
        if (value.isPresent()) {
            Object v = value.get();
            if (v instanceof Number) {
                return ((Number) v).intValue();
            } else if (v instanceof String) {
                try {
                    return Integer.parseInt((String) v);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        Optional<Object> value = getProperty(key);
        if (value.isPresent()) {
            Object v = value.get();
            if (v instanceof Number) {
                return ((Number) v).longValue();
            } else if (v instanceof String) {
                try {
                    return Long.parseLong((String) v);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        Optional<Object> value = getProperty(key);
        if (value.isPresent()) {
            Object v = value.get();
            if (v instanceof Number) {
                return ((Number) v).doubleValue();
            } else if (v instanceof String) {
                try {
                    return Double.parseDouble((String) v);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        Optional<Object> value = getProperty(key);
        if (value.isPresent()) {
            Object v = value.get();
            if (v instanceof Boolean) {
                return (Boolean) v;
            } else if (v instanceof String) {
                return Boolean.parseBoolean((String) v);
            }
        }
        return defaultValue;
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public void removeProperty(String key) {
        removeProperty(key, "unknown");
    }

    @Override
    public void removeProperty(String key, String source) {
        Object oldValue = properties.remove(key);
        if (oldValue != null) {
            notifyListeners(new ConfigChangeEvent(key, oldValue, null, ConfigChangeEvent.ChangeType.DELETED));
            
            if (auditEnabled) {
                recordAuditEvent(new ConfigAuditEvent(key, oldValue, null, ConfigAuditEvent.ChangeType.DELETED, source));
            }
        }
    }

    @Override
    public Map<String, Object> getAllProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public void addChangeListener(ConfigChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeChangeListener(ConfigChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public List<ConfigChangeListener> getChangeListeners() {
        return new ArrayList<>(listeners);
    }

    @Override
    public void clear() {
        properties.clear();
        listeners.clear();
    }

    private void notifyListeners(ConfigChangeEvent event) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onConfigChange(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadFromProperties(String path) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("Properties file not found: " + path);
            }
            loadFromProperties(is);
        }
    }

    @Override
    public void loadFromProperties(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            setProperty(key, parseValue(value));
        }
    }

    @Override
    public void loadFromYaml(String path) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("YAML file not found: " + path);
            }
            loadFromYaml(is);
        }
    }

    @Override
    public void loadFromYaml(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            String currentPrefix = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.contains(":")) {
                    int colonIndex = line.indexOf(':');
                    String key = line.substring(0, colonIndex).trim();
                    String value = line.substring(colonIndex + 1).trim();
                    
                    if (value.isEmpty()) {
                        currentPrefix = key + ".";
                    } else {
                        String fullKey = currentPrefix + key;
                        setProperty(fullKey, parseValue(value));
                    }
                }
            }
        }
    }

    private Object parseValue(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        value = value.trim();
        
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
        }
        
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
        }
        
        return value;
    }

    @Override
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String getEnvironment() {
        return environment;
    }

    @Override
    public String getDecryptedString(String key, String defaultValue) {
        String encrypted = getString(key, null);
        if (encrypted == null) {
            return defaultValue;
        }
        if (encryptor == null) {
            throw new IllegalStateException("Encryptor not set. Use setEncryptor() first.");
        }
        return encryptor.decrypt(encrypted);
    }

    @Override
    public void setEncryptor(ConfigEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public ConfigEncryptor getEncryptor() {
        return encryptor;
    }

    @Override
    public void saveToProperties(String path) throws IOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            saveToProperties(fos);
        }
    }

    @Override
    public void saveToProperties(OutputStream outputStream) throws IOException {
        Properties props = new Properties();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                props.setProperty(key, valueToString(value));
            }
        }
        props.store(outputStream, "EST Config Center - Saved properties");
    }

    @Override
    public void saveToYaml(String path) throws IOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            saveToYaml(fos);
        }
    }

    @Override
    public void saveToYaml(OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"))) {
            writer.write("# EST Config Center - Saved YAML");
            writer.newLine();
            
            Map<String, Object> nestedMap = buildNestedMap();
            writeYamlMap(writer, nestedMap, "");
        }
    }

    private Map<String, Object> buildNestedMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            addToNestedMap(result, key, value);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void addToNestedMap(Map<String, Object> map, String key, Object value) {
        int dotIndex = key.indexOf('.');
        if (dotIndex == -1) {
            map.put(key, value);
        } else {
            String prefix = key.substring(0, dotIndex);
            String suffix = key.substring(dotIndex + 1);
            Map<String, Object> childMap = (Map<String, Object>) map.computeIfAbsent(
                prefix, k -> new LinkedHashMap<>());
            addToNestedMap(childMap, suffix, value);
        }
    }

    @SuppressWarnings("unchecked")
    private void writeYamlMap(BufferedWriter writer, Map<String, Object> map, String indent) throws IOException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            writer.write(indent);
            writer.write(key);
            writer.write(":");
            
            if (value instanceof Map) {
                writer.newLine();
                writeYamlMap(writer, (Map<String, Object>) value, indent + "  ");
            } else {
                writer.write(" ");
                writer.write(valueToString(value));
                writer.newLine();
            }
        }
    }

    private String valueToString(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.contains(":") || str.contains("#") || str.trim().isEmpty()) {
                return "\"" + str.replace("\"", "\\\"") + "\"";
            }
            return str;
        }
        return value.toString();
    }

    @Override
    public void setAutoSave(boolean enabled) {
        this.autoSaveEnabled = enabled;
    }

    @Override
    public void setAutoSavePath(String path) {
        this.autoSavePath = path;
    }

    @Override
    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }

    @Override
    public String getAutoSavePath() {
        return autoSavePath;
    }

    @Override
    public void addAuditListener(ConfigAuditListener listener) {
        if (listener != null) {
            auditListeners.add(listener);
        }
    }

    @Override
    public void removeAuditListener(ConfigAuditListener listener) {
        auditListeners.remove(listener);
    }

    @Override
    public List<ConfigAuditListener> getAuditListeners() {
        return new ArrayList<>(auditListeners);
    }

    @Override
    public List<ConfigAuditEvent> getAuditEvents() {
        return new ArrayList<>(auditEvents);
    }

    @Override
    public List<ConfigAuditEvent> getAuditEvents(String key) {
        List<ConfigAuditEvent> result = new ArrayList<>();
        for (ConfigAuditEvent event : auditEvents) {
            if (event.getKey().equals(key)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public void setAuditEnabled(boolean enabled) {
        this.auditEnabled = enabled;
    }

    @Override
    public boolean isAuditEnabled() {
        return auditEnabled;
    }

    @Override
    public void setAuditMaxSize(int maxSize) {
        this.auditMaxSize = maxSize;
        trimAuditEvents();
    }

    @Override
    public int getAuditMaxSize() {
        return auditMaxSize;
    }

    private void recordAuditEvent(ConfigAuditEvent event) {
        auditEvents.add(event);
        trimAuditEvents();
        
        for (ConfigAuditListener listener : auditListeners) {
            try {
                listener.onAuditEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void trimAuditEvents() {
        while (auditEvents.size() > auditMaxSize) {
            auditEvents.poll();
        }
    }

    @Override
    public void setEncryptedProperty(String key, String plaintext) {
        setEncryptedProperty(key, plaintext, "unknown");
    }

    @Override
    public void setEncryptedProperty(String key, String plaintext, String source) {
        if (encryptor == null) {
            throw new IllegalStateException("Encryptor not set. Use setEncryptor() first.");
        }
        String encrypted = encryptor.encrypt(plaintext);
        setProperty(key, encrypted, source);
    }

    private void autoSaveIfEnabled() {
        if (autoSaveEnabled && autoSavePath != null && !autoSavePath.isEmpty()) {
            try {
                if (autoSavePath.toLowerCase().endsWith(".yaml") || 
                    autoSavePath.toLowerCase().endsWith(".yml")) {
                    saveToYaml(autoSavePath);
                } else {
                    saveToProperties(autoSavePath);
                }
            } catch (IOException e) {
                System.err.println("Failed to auto-save config: " + e.getMessage());
            }
        }
    }
}
