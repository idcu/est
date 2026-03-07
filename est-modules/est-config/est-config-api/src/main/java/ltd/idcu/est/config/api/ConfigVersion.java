package ltd.idcu.est.config.api;

import java.time.Instant;
import java.util.Map;

public class ConfigVersion {
    private final String versionId;
    private final Instant createdAt;
    private final Map<String, Object> properties;
    private final String comment;

    public ConfigVersion(String versionId, Map<String, Object> properties, String comment) {
        this.versionId = versionId;
        this.createdAt = Instant.now();
        this.properties = Map.copyOf(properties);
        this.comment = comment;
    }

    public String getVersionId() {
        return versionId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "ConfigVersion{" +
                "versionId='" + versionId + '\'' +
                ", createdAt=" + createdAt +
                ", propertyCount=" + properties.size() +
                ", comment='" + comment + '\'' +
                '}';
    }
}
