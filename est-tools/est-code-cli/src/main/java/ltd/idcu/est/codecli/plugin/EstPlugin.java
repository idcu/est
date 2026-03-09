package ltd.idcu.est.codecli.plugin;

import java.util.Map;

public interface EstPlugin {
    
    String getId();
    
    String getName();
    
    String getVersion();
    
    String getDescription();
    
    String getAuthor();
    
    void initialize(PluginContext context) throws PluginException;
    
    void shutdown() throws PluginException;
    
    Map<String, Object> getCapabilities();
    
    default boolean isEnabled() {
        return true;
    }
}
