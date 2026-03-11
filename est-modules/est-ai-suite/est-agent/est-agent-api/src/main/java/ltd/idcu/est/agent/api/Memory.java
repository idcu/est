package ltd.idcu.est.agent.api;

import java.util.List;

public interface Memory {
    
    void add(MemoryItem item);
    
    void addAll(List<MemoryItem> items);
    
    List<MemoryItem> retrieve(String query, int topK);
    
    List<MemoryItem> getRecent(int count);
    
    List<MemoryItem> getAll();
    
    void clear();
    
    void remove(String id);
    
    int size();
}
