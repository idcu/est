package ltd.idcu.est.plugin.api;

public interface PluginListener {
    
    default void onPluginLoaded(Plugin plugin) {}
    
    default void onPluginInitialized(Plugin plugin) {}
    
    default void onPluginStarted(Plugin plugin) {}
    
    default void onPluginStopped(Plugin plugin) {}
    
    default void onPluginUnloaded(Plugin plugin) {}
    
    default void onPluginError(Plugin plugin, Throwable error) {}
}
