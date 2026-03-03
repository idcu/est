package ltd.idcu.est.core.api.lifecycle;

public interface Lifecycle {
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    void addListener(LifecycleListener listener);
    
    void removeListener(LifecycleListener listener);
}
