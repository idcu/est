package ltd.idcu.est.codecli.ux;

import java.util.*;

public class CommandHistory {
    
    private final List<String> history;
    private final int maxSize;
    private int currentIndex;
    
    public CommandHistory(int maxSize) {
        this.history = new LinkedList<>();
        this.maxSize = maxSize;
        this.currentIndex = -1;
    }
    
    public CommandHistory() {
        this(100);
    }
    
    public void add(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }
        
        String trimmed = command.trim();
        
        if (!history.isEmpty() && history.get(history.size() - 1).equals(trimmed)) {
            return;
        }
        
        history.add(trimmed);
        
        if (history.size() > maxSize) {
            history.remove(0);
        }
        
        currentIndex = history.size();
    }
    
    public String getPrevious() {
        if (history.isEmpty()) {
            return null;
        }
        
        if (currentIndex > 0) {
            currentIndex--;
            return history.get(currentIndex);
        }
        
        return null;
    }
    
    public String getNext() {
        if (history.isEmpty()) {
            return null;
        }
        
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        }
        
        currentIndex = history.size();
        return null;
    }
    
    public List<String> getAll() {
        return new ArrayList<>(history);
    }
    
    public void clear() {
        history.clear();
        currentIndex = -1;
    }
    
    public int size() {
        return history.size();
    }
    
    public List<String> search(String prefix) {
        List<String> results = new ArrayList<>();
        String lowerPrefix = prefix.toLowerCase();
        
        for (String cmd : history) {
            if (cmd.toLowerCase().startsWith(lowerPrefix)) {
                results.add(cmd);
            }
        }
        
        return results;
    }
}
