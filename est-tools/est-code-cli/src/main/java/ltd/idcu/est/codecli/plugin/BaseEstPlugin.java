package ltd.idcu.est.codecli.plugin;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseEstPlugin implements EstPlugin {
    
    protected PluginContext context;
    protected final Map<String, Object> capabilities;
    
    public BaseEstPlugin() {
        this.capabilities = new HashMap<>();
    }
    
    @Override
    public void initialize(PluginContext context) throws PluginException {
        this.context = context;
        context.logInfo("初始化插件: " + getName());
        onInitialize();
    }
    
    protected void onInitialize() throws PluginException {
    }
    
    @Override
    public void shutdown() throws PluginException {
        if (context != null) {
            context.logInfo("关闭插件: " + getName());
        }
        onShutdown();
    }
    
    protected void onShutdown() throws PluginException {
    }
    
    @Override
    public Map<String, Object> getCapabilities() {
        return new HashMap<>(capabilities);
    }
    
    protected void addCapability(String key, Object value) {
        capabilities.put(key, value);
    }
    
    protected void logInfo(String message) {
        if (context != null) {
            context.logInfo(message);
        }
    }
    
    protected void logWarn(String message) {
        if (context != null) {
            context.logWarn(message);
        }
    }
    
    protected void logError(String message) {
        if (context != null) {
            context.logError(message);
        }
    }
    
    protected void logError(String message, Throwable throwable) {
        if (context != null) {
            context.logError(message, throwable);
        }
    }
    
    @Override
    public String toString() {
        return getName() + " v" + getVersion() + " (" + getId() + ")";
    }
}
