package ltd.idcu.est.plugin.api;

import java.util.Optional;

public interface PluginLoader {
    
    Plugin load(String pluginPath) throws PluginException;
    
    void unload(Plugin plugin) throws PluginException;
    
    Optional<PluginInfo> loadInfo(String pluginPath);
    
    boolean isValidPlugin(String pluginPath);
    
    String[] scanDirectory(String directory);
}
