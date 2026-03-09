package ltd.idcu.est.config.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConfigVersion {
    private final String versionId;
    private final String configId;
    private final Map<String, Object> properties;
    private final String comment;
    private final LocalDateTime createdAt;

    public ConfigVersion(String versionId, Map<String, Object> properties, String comment) {
        this(versionId, null, properties, comment);
    }

    public ConfigVersion(String versionId, String configId, Map<String, Object> properties, String comment) {
        this.versionId = versionId;
        this.configId = configId;
        this.properties = new HashMap<>(properties);
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public String getVersionId() {
        return versionId;
    }

    public String getConfigId() {
        return configId;
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    public Map<String, Object> getConfig() {
        return getProperties();
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
