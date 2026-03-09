package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.marketplace.api.PluginRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LocalPluginRepository implements PluginRepository {
    
    private final String id;
    private final String name;
    private final String url;
    private boolean enabled = true;
    private final Map<String, List<PluginInfo>> plugins = new ConcurrentHashMap<>();
    
    public LocalPluginRepository(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getUrl() {
        return url;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public Optional<PluginInfo> getPlugin(String pluginId) {
        List<PluginInfo> versions = plugins.get(pluginId);
        if (versions == null || versions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(versions.get(0));
    }
    
    @Override
    public Optional<PluginInfo> getPlugin(String pluginId, String version) {
        List<PluginInfo> versions = plugins.get(pluginId);
        if (versions == null) {
            return Optional.empty();
        }
        return versions.stream()
            .filter(p -> version.equals(p.getVersion()))
            .findFirst();
    }
    
    @Override
    public List<PluginInfo> getAllPlugins() {
        return plugins.values().stream()
            .filter(list -> !list.isEmpty())
            .map(list -> list.get(0))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> searchPlugins(String query) {
        String lowerQuery = query.toLowerCase();
        return getAllPlugins().stream()
            .filter(p -> p.getName().toLowerCase().contains(lowerQuery) ||
                        p.getDescription().toLowerCase().contains(lowerQuery) ||
                        Arrays.stream(p.getTags()).anyMatch(t -> t.toLowerCase().contains(lowerQuery)))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getPluginVersions(String pluginId) {
        List<PluginInfo> versions = plugins.get(pluginId);
        return versions != null ? new ArrayList<>(versions) : Collections.emptyList();
    }
    
    @Override
    public boolean downloadPlugin(String pluginId, String version, String targetPath) {
        return getPlugin(pluginId, version).isPresent();
    }
    
    @Override
    public boolean uploadPlugin(PluginInfo pluginInfo, byte[] pluginData) {
        plugins.computeIfAbsent(pluginInfo.getName(), k -> new ArrayList<>());
        List<PluginInfo> versions = plugins.get(pluginInfo.getName());
        versions.add(0, pluginInfo);
        return true;
    }
    
    @Override
    public boolean deletePlugin(String pluginId) {
        return plugins.remove(pluginId) != null;
    }
    
    public void addPlugin(PluginInfo pluginInfo) {
        uploadPlugin(pluginInfo, new byte[0]);
    }
}
