package ltd.idcu.est.codecli.plugin;

import java.nio.file.Path;
import java.util.Map;

public interface PluginContext {
    
    Path getPluginDataDir();
    
    Path getConfigDir();
    
    Path getWorkDir();
    
    Object getService(String serviceName);
    
    <T> T getService(Class<T> serviceClass);
    
    void registerService(String serviceName, Object service);
    
    void logInfo(String message);
    
    void logWarn(String message);
    
    void logError(String message);
    
    void logError(String message, Throwable throwable);
    
    Map<String, Object> getConfig();
    
    Object getConfig(String key);
    
    void setConfig(String key, Object value);
}
