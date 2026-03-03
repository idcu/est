package ltd.idcu.est.patterns.api.behavioral;

public interface Command {
    
    void execute();
    
    void undo();
    
    boolean canUndo();
    
    String getDescription();
}
