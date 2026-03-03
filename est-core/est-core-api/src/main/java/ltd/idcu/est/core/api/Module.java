package ltd.idcu.est.core.api;

public interface Module {
    
    String getName();
    
    String getVersion();
    
    void initialize();
    
    void start();
    
    void stop();
    
    boolean isRunning();
}
