package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultDocument implements Document {
    
    private final String id;
    private final String content;
    private final Map<String, Object> metadata;
    private final String source;
    
    public DefaultDocument(String content) {
        this(UUID.randomUUID().toString(), content, new HashMap<>(), null);
    }
    
    public DefaultDocument(String content, String source) {
        this(UUID.randomUUID().toString(), content, new HashMap<>(), source);
    }
    
    public DefaultDocument(String id, String content, Map<String, Object> metadata, String source) {
        this.id = id;
        this.content = content;
        this.metadata = metadata;
        this.source = source;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    @Override
    public String getSource() {
        return source;
    }
}
