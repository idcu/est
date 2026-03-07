package ltd.idcu.est.config.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConfigCenter {
    void setProperty(String key, Object value);

    void setProperty(String key, Object value, String source);

    void setEncryptedProperty(String key, String plaintext);

    void setEncryptedProperty(String key, String plaintext, String source);

    Optional<Object> getProperty(String key);

    <T> Optional<T> getProperty(String key, Class<T> type);

    String getString(String key, String defaultValue);

    String getDecryptedString(String key, String defaultValue);

    int getInt(String key, int defaultValue);

    long getLong(String key, long defaultValue);

    double getDouble(String key, double defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    boolean containsProperty(String key);

    void removeProperty(String key);

    void removeProperty(String key, String source);

    Map<String, Object> getAllProperties();

    void addChangeListener(ConfigChangeListener listener);

    void removeChangeListener(ConfigChangeListener listener);

    List<ConfigChangeListener> getChangeListeners();

    void addAuditListener(ConfigAuditListener listener);

    void removeAuditListener(ConfigAuditListener listener);

    List<ConfigAuditListener> getAuditListeners();

    List<ConfigAuditEvent> getAuditEvents();

    List<ConfigAuditEvent> getAuditEvents(String key);

    void clear();

    void loadFromProperties(String path) throws IOException;

    void loadFromProperties(InputStream inputStream) throws IOException;

    void loadFromYaml(String path) throws IOException;

    void loadFromYaml(InputStream inputStream) throws IOException;

    void setEnvironment(String environment);

    String getEnvironment();

    void setEncryptor(ConfigEncryptor encryptor);

    ConfigEncryptor getEncryptor();

    void saveToProperties(String path) throws IOException;

    void saveToProperties(OutputStream outputStream) throws IOException;

    void saveToYaml(String path) throws IOException;

    void saveToYaml(OutputStream outputStream) throws IOException;

    void setAutoSave(boolean enabled);

    void setAutoSavePath(String path);

    boolean isAutoSaveEnabled();

    String getAutoSavePath();

    void setAuditEnabled(boolean enabled);

    boolean isAuditEnabled();

    void setAuditMaxSize(int maxSize);

    int getAuditMaxSize();
}
