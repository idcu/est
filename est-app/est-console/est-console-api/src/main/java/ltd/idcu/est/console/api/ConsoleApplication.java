package ltd.idcu.est.console.api;

public interface ConsoleApplication {
    
    void run(String[] args);
    
    void stop();
    
    boolean isRunning();
    
    String getName();
    
    void setName(String name);
}
