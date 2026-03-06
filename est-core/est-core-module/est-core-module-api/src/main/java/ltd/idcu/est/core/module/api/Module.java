package ltd.idcu.est.core.module.api;

public interface Module {
    
    String getName();
    
    String getVersion();
    
    void initialize();
    
    void start();
    
    void stop();
    
    boolean isRunning();
}
