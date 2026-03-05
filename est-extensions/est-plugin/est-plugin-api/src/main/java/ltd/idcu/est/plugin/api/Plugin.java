package ltd.idcu.est.plugin.api;

import ltd.idcu.est.core.api.Module;

public interface Plugin extends Module {
    
    String getDescription();
    
    String getAuthor();
    
    PluginState getState();
    
    void onLoad();
    
    void onEnable();
    
    void onDisable();
    
    void onUnload();
    
    PluginInfo getInfo();
}
