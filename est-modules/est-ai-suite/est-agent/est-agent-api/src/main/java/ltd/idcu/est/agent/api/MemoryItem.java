package ltd.idcu.est.agent.api;

import java.time.LocalDateTime;
import java.util.Map;

public class MemoryItem {
    
    private String id;
    private String content;
    private MemoryType type;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
    
    public MemoryItem() {
        this.timestamp = LocalDateTime.now();
    }
    
    public MemoryItem(String id, String content, MemoryType type) {
        this();
        this.id = id;
        this.content = content;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public MemoryType getType() {
        return type;
    }
    
    public void setType(MemoryType type) {
        this.type = type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public enum MemoryType {
        OBSERVATION,
        THOUGHT,
        ACTION,
        REFLECTION,
        TASK,
        CONVERSATION,
        CUSTOM
    }
}
