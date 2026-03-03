package ltd.idcu.est.plugin.api;

import java.util.List;
import java.util.Optional;

public interface PluginManager {
    
    Plugin loadPlugin(String pluginPath);
    
    Plugin loadPluginFromClass(Class<? extends Plugin> pluginClass);
    
    void unloadPlugin(String pluginName);
    
    void unloadPlugin(Plugin plugin);
    
    void startPlugin(String pluginName);
    
    void stopPlugin(String pluginName);
    
    void startAllPlugins();
    
    void stopAllPlugins();
    
    Optional<Plugin> getPlugin(String pluginName);
    
    List<Plugin> getPlugins();
    
    List<Plugin> getPluginsByState(PluginState state);
    
    boolean hasPlugin(String pluginName);
    
    int getPluginCount();
    
    void addListener(PluginListener listener);
    
    void removeListener(PluginListener listener);
    
    PluginStats getStats();
    
    interface Builder {
        Builder config(PluginConfig config);
        Builder addLoader(PluginLoader loader);
        Builder addListener(PluginListener listener);
        PluginManager build();
    }
}
