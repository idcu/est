package ltd.idcu.est.config.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConfigCenter {
    void setProperty(String key, Object value);

    Optional<Object> getProperty(String key);

    <T> Optional<T> getProperty(String key, Class<T> type);

    String getString(String key, String defaultValue);

    int getInt(String key, int defaultValue);

    long getLong(String key, long defaultValue);

    double getDouble(String key, double defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    boolean containsProperty(String key);

    void removeProperty(String key);

    Map<String, Object> getAllProperties();

    void addChangeListener(ConfigChangeListener listener);

    void removeChangeListener(ConfigChangeListener listener);

    List<ConfigChangeListener> getChangeListeners();

    void clear();
}
