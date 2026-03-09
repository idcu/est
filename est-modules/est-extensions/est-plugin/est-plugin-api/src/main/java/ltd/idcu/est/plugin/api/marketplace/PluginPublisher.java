package ltd.idcu.est.plugin.api.marketplace;

import java.io.File;
import java.io.InputStream;

public interface PluginPublisher {
    
    PluginPublishResult publishPlugin(PluginPublishRequest request);
    
    PluginPublishResult publishPlugin(File pluginFile, PluginMetadata metadata);
    
    PluginPublishResult publishPlugin(InputStream inputStream, PluginMetadata metadata);
    
    boolean validatePlugin(File pluginFile);
    
    boolean validatePluginMetadata(PluginMetadata metadata);
    
    PluginPublishResult updatePlugin(String pluginId, File updatedPluginFile, PluginMetadata metadata);
    
    boolean deprecatePlugin(String pluginId, String reason);
    
    boolean deletePlugin(String pluginId);
    
    PluginVersion getLatestVersion(String pluginId);
}
