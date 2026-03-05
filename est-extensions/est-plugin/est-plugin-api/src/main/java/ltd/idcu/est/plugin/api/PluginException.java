package ltd.idcu.est.plugin.api;

public class PluginException extends RuntimeException {
    
    private final String pluginName;
    private final PluginState state;
    
    public PluginException(String message) {
        super(message);
        this.pluginName = null;
        this.state = null;
    }
    
    public PluginException(String message, Throwable cause) {
        super(message, cause);
        this.pluginName = null;
        this.state = null;
    }
    
    public PluginException(String pluginName, String message) {
        super("Plugin [" + pluginName + "]: " + message);
        this.pluginName = pluginName;
        this.state = null;
    }
    
    public PluginException(String pluginName, String message, Throwable cause) {
        super("Plugin [" + pluginName + "]: " + message, cause);
        this.pluginName = pluginName;
        this.state = null;
    }
    
    public PluginException(String pluginName, PluginState state, String message) {
        super("Plugin [" + pluginName + "] in state [" + state + "]: " + message);
        this.pluginName = pluginName;
        this.state = state;
    }
    
    public PluginException(String pluginName, PluginState state, String message, Throwable cause) {
        super("Plugin [" + pluginName + "] in state [" + state + "]: " + message, cause);
        this.pluginName = pluginName;
        this.state = state;
    }
    
    public String getPluginName() {
        return pluginName;
    }
    
    public PluginState getState() {
        return state;
    }
    
    public static PluginException notFound(String pluginName) {
        return new PluginException(pluginName, "Plugin not found");
    }
    
    public static PluginException alreadyLoaded(String pluginName) {
        return new PluginException(pluginName, "Plugin already loaded");
    }
    
    public static PluginException invalidState(String pluginName, PluginState current, 
                                                PluginState expected) {
        return new PluginException(pluginName, current, 
            "Invalid state, expected " + expected + " but was " + current);
    }
    
    public static PluginException loadFailed(String pluginName, Throwable cause) {
        return new PluginException(pluginName, "Failed to load plugin", cause);
    }
    
    public static PluginException startFailed(String pluginName, Throwable cause) {
        return new PluginException(pluginName, "Failed to start plugin", cause);
    }
    
    public static PluginException dependencyNotFound(String pluginName, String dependency) {
        return new PluginException(pluginName, 
            "Required dependency not found: " + dependency);
    }
    
    public static PluginException circularDependency(String pluginName) {
        return new PluginException(pluginName, "Circular dependency detected");
    }
}
