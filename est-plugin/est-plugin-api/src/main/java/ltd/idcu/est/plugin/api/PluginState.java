package ltd.idcu.est.plugin.api;

public enum PluginState {
    
    LOADED("Loaded"),
    
    INITIALIZED("Initialized"),
    
    STARTING("Starting"),
    
    RUNNING("Running"),
    
    STOPPING("Stopping"),
    
    STOPPED("Stopped"),
    
    DISABLED("Disabled"),
    
    ERROR("Error");
    
    private final String displayName;
    
    PluginState(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isRunning() {
        return this == RUNNING;
    }
    
    public boolean isStopped() {
        return this == STOPPED || this == DISABLED;
    }
    
    public boolean isError() {
        return this == ERROR;
    }
    
    public boolean canStart() {
        return this == INITIALIZED || this == STOPPED || this == DISABLED;
    }
    
    public boolean canStop() {
        return this == RUNNING || this == STARTING;
    }
}
