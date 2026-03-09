package ltd.idcu.est.plugin.marketplace.api;

import ltd.idcu.est.plugin.api.PluginInfo;

import java.util.List;
import java.util.Optional;

public interface PluginRepository {
    
    String getId();
    
    String getName();
    
    String getUrl();
    
    boolean isEnabled();
    
    void setEnabled(boolean enabled);
    
    Optional<PluginInfo> getPlugin(String pluginId);
    
    Optional<PluginInfo> getPlugin(String pluginId, String version);
    
    List<PluginInfo> getAllPlugins();
    
    List<PluginInfo> searchPlugins(String query);
    
    List<PluginInfo> getPluginVersions(String pluginId);
    
    boolean downloadPlugin(String pluginId, String version, String targetPath);
    
    boolean uploadPlugin(PluginInfo pluginInfo, byte[] pluginData);
    
    boolean deletePlugin(String pluginId);
}
