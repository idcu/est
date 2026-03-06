package ltd.idcu.est.core.lifecycle.api;

public interface Lifecycle {
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    void addListener(LifecycleListener listener);
    
    void removeListener(LifecycleListener listener);
}
