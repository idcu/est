package ltd.idcu.est.console.api;

public interface Command {
    
    String getName();
    
    String getDescription();
    
    void execute(String[] args);
    
    String getUsage();
}
