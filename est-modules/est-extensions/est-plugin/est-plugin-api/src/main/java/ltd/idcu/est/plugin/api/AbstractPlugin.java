package ltd.idcu.est.plugin.api;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlugin implements Plugin {
    
    protected PluginInfo info;
    protected PluginState state = PluginState.LOADED;
    protected PluginContext context;
    protected final Map<String, Object> attributes = new HashMap<>();
    
    protected AbstractPlugin(PluginInfo info) {
        this.info = info;
    }
    
    @Override
    public String getName() {
        return info.getName();
    }
    
    @Override
    public String getVersion() {
        return info.getVersion();
    }
    
    @Override
    public String getDescription() {
        return info.getDescription();
    }
    
    @Override
    public String getAuthor() {
        return info.getAuthor();
    }
    
    @Override
    public PluginState getState() {
        return state;
    }
    
    @Override
    public PluginInfo getInfo() {
        return info;
    }
    
    public PluginContext getContext() {
        return context;
    }
    
    public void setContext(PluginContext context) {
        this.context = context;
    }
    
    @Override
    public void onLoad() {
    }
    
    @Override
    public void initialize() {
        if (state != PluginState.LOADED) {
            throw PluginException.invalidState(getName(), state, PluginState.LOADED);
        }
        state = PluginState.INITIALIZED;
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void start() {
        if (!state.canStart()) {
            throw PluginException.invalidState(getName(), state, PluginState.INITIALIZED);
        }
        state = PluginState.STARTING;
        try {
            onEnable();
            state = PluginState.RUNNING;
        } catch (Exception e) {
            state = PluginState.ERROR;
            throw PluginException.startFailed(getName(), e);
        }
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void stop() {
        if (!state.canStop()) {
            throw PluginException.invalidState(getName(), state, PluginState.RUNNING);
        }
        state = PluginState.STOPPING;
        try {
            onDisable();
            state = PluginState.STOPPED;
        } catch (Exception e) {
            state = PluginState.ERROR;
            throw new PluginException(getName(), "Failed to stop plugin", e);
        }
    }
    
    @Override
    public void onUnload() {
    }
    
    @Override
    public boolean isRunning() {
        return state.isRunning();
    }
    
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }
    
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
    
    public void removeAttribute(String key) {
        attributes.remove(key);
    }
}
