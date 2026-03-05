package ltd.idcu.est.plugin.api;

public final class PluginConfig {
    
    private final String pluginDirectory;
    private final boolean autoStart;
    private final boolean hotReload;
    private final long scanInterval;
    private final int maxPlugins;
    private final boolean failFast;
    
    public PluginConfig(String pluginDirectory, boolean autoStart, boolean hotReload,
                        long scanInterval, int maxPlugins, boolean failFast) {
        this.pluginDirectory = pluginDirectory;
        this.autoStart = autoStart;
        this.hotReload = hotReload;
        this.scanInterval = scanInterval;
        this.maxPlugins = maxPlugins;
        this.failFast = failFast;
    }
    
    public String getPluginDirectory() {
        return pluginDirectory;
    }
    
    public boolean isAutoStart() {
        return autoStart;
    }
    
    public boolean isHotReload() {
        return hotReload;
    }
    
    public long getScanInterval() {
        return scanInterval;
    }
    
    public int getMaxPlugins() {
        return maxPlugins;
    }
    
    public boolean isFailFast() {
        return failFast;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static PluginConfig defaultConfig() {
        return builder().build();
    }
    
    public static class Builder {
        private String pluginDirectory = "plugins";
        private boolean autoStart = true;
        private boolean hotReload = false;
        private long scanInterval = 5000;
        private int maxPlugins = 100;
        private boolean failFast = false;
        
        public Builder pluginDirectory(String pluginDirectory) {
            this.pluginDirectory = pluginDirectory;
            return this;
        }
        
        public Builder autoStart(boolean autoStart) {
            this.autoStart = autoStart;
            return this;
        }
        
        public Builder hotReload(boolean hotReload) {
            this.hotReload = hotReload;
            return this;
        }
        
        public Builder scanInterval(long scanInterval) {
            this.scanInterval = scanInterval;
            return this;
        }
        
        public Builder maxPlugins(int maxPlugins) {
            this.maxPlugins = maxPlugins;
            return this;
        }
        
        public Builder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }
        
        public PluginConfig build() {
            return new PluginConfig(pluginDirectory, autoStart, hotReload,
                                    scanInterval, maxPlugins, failFast);
        }
    }
}
