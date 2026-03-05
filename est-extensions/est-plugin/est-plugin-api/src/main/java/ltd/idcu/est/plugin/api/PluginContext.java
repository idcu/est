package ltd.idcu.est.plugin.api;

import java.util.Optional;

public final class PluginContext {
    
    private final Plugin plugin;
    private final PluginManager manager;
    private final PluginConfig config;
    private final ClassLoader classLoader;
    
    public PluginContext(Plugin plugin, PluginManager manager, 
                         PluginConfig config, ClassLoader classLoader) {
        this.plugin = plugin;
        this.manager = manager;
        this.config = config;
        this.classLoader = classLoader;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    public PluginManager getManager() {
        return manager;
    }
    
    public PluginConfig getConfig() {
        return config;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    public String getPluginName() {
        return plugin.getName();
    }
    
    public String getPluginVersion() {
        return plugin.getVersion();
    }
    
    public Optional<Plugin> getDependency(String name) {
        return manager.getPlugin(name);
    }
    
    public boolean hasDependency(String name) {
        return manager.hasPlugin(name);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Plugin plugin;
        private PluginManager manager;
        private PluginConfig config;
        private ClassLoader classLoader;
        
        public Builder plugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }
        
        public Builder manager(PluginManager manager) {
            this.manager = manager;
            return this;
        }
        
        public Builder config(PluginConfig config) {
            this.config = config;
            return this;
        }
        
        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }
        
        public PluginContext build() {
            return new PluginContext(plugin, manager, config, classLoader);
        }
    }
}
