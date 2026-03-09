package ltd.idcu.est.config.impl;

import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.api.ConfigVersion;
import ltd.idcu.est.config.api.ConfigVersionManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultConfigVersionManager implements ConfigVersionManager {
    private final ConfigCenter configCenter;
    private final List<ConfigVersion> versions = new CopyOnWriteArrayList<>();
    private final Map<String, ConfigVersion> versionMap = new ConcurrentHashMap<>();
    private ConfigVersion currentVersion;
    private int versionCounter = 0;

    public DefaultConfigVersionManager() {
        this.configCenter = null;
    }

    public DefaultConfigVersionManager(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    @Override
    public String createVersion(Map<String, Object> properties, String comment) {
        String versionId = "v" + (++versionCounter);
        ConfigVersion version = new ConfigVersion(versionId, properties, comment);
        versions.add(version);
        versionMap.put(versionId, version);
        currentVersion = version;
        return versionId;
    }

    @Override
    public Optional<ConfigVersion> getVersion(String versionId) {
        return Optional.ofNullable(versionMap.get(versionId));
    }

    @Override
    public List<ConfigVersion> listVersions() {
        return new ArrayList<>(versions);
    }

    @Override
    public List<ConfigVersion> listVersions(int limit) {
        int size = versions.size();
        int start = Math.max(0, size - limit);
        return versions.subList(start, size).stream()
                .sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void rollbackTo(String versionId) {
        if (configCenter == null) {
            throw new UnsupportedOperationException("ConfigCenter not initialized");
        }
        
        ConfigVersion version = versionMap.get(versionId);
        if (version == null) {
            throw new IllegalArgumentException("Version not found: " + versionId);
        }
        
        configCenter.clear();
        for (Map.Entry<String, Object> entry : version.getProperties().entrySet()) {
            configCenter.setProperty(entry.getKey(), entry.getValue());
        }
        
        currentVersion = version;
    }

    @Override
    public Optional<ConfigVersion> getCurrentVersion() {
        return Optional.ofNullable(currentVersion);
    }

    @Override
    public void deleteVersion(String versionId) {
        ConfigVersion version = versionMap.remove(versionId);
        if (version != null) {
            versions.remove(version);
            if (currentVersion != null && currentVersion.getVersionId().equals(versionId)) {
                currentVersion = versions.isEmpty() ? null : versions.get(versions.size() - 1);
            }
        }
    }

    @Override
    public void clearVersions() {
        versions.clear();
        versionMap.clear();
        currentVersion = null;
    }

    public String snapshot(String comment) {
        if (configCenter == null) {
            throw new UnsupportedOperationException("ConfigCenter not initialized");
        }
        return createVersion(configCenter.getAllProperties(), comment);
    }
}
