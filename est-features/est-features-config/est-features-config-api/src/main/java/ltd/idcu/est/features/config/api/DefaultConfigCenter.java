package ltd.idcu.est.features.config.api;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultConfigCenter implements ConfigCenter {
    private final Map<String, Object> properties = new ConcurrentHashMap<>();
    private final List<ConfigChangeListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void setProperty(String key, Object value) {
        Objects.requireNonNull(key, "Key cannot be null");
        Object oldValue = properties.put(key, value);
        ConfigChangeEvent.ChangeType changeType = oldValue == null 
                ? ConfigChangeEvent.ChangeType.ADDED 
                : ConfigChangeEvent.ChangeType.MODIFIED;
        notifyListeners(new ConfigChangeEvent(key, oldValue, value, changeType));
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
        Object oldValue = properties.remove(key);
        if (oldValue != null) {
            notifyListeners(new ConfigChangeEvent(key, oldValue, null, ConfigChangeEvent.ChangeType.DELETED));
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
}
