package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Module;

public abstract class AbstractModule implements Module {
    
    private final String name;
    private final String version;
    private volatile boolean running = false;
    private volatile boolean initialized = false;
    
    protected AbstractModule(String name, String version) {
        this.name = name;
        this.version = version;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getVersion() {
        return version;
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            return;
        }
        doInitialize();
        initialized = true;
    }
    
    @Override
    public void start() {
        if (!initialized) {
            initialize();
        }
        if (running) {
            return;
        }
        doStart();
        running = true;
    }
    
    @Override
    public void stop() {
        if (!running) {
            return;
        }
        doStop();
        running = false;
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    protected void doInitialize() {
    }
    
    protected void doStart() {
    }
    
    protected void doStop() {
    }
}
