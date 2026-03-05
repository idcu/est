package ltd.idcu.est.patterns.api.behavioral;

import java.util.List;

public interface CommandInvoker {
    
    void executeCommand(Command command);
    
    void undoLast();
    
    void redoLast();
    
    boolean canUndo();
    
    boolean canRedo();
    
    List<String> getHistory();
    
    void clearHistory();
}
