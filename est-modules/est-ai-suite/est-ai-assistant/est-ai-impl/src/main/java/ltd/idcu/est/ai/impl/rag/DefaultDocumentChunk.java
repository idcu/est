package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.Document;
import ltd.idcu.est.ai.api.rag.DocumentChunk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultDocumentChunk implements DocumentChunk {
    
    private final String id;
    private final String content;
    private final int startIndex;
    private final int endIndex;
    private final int chunkIndex;
    private final Map<String, Object> metadata;
    private final String sourceDocumentId;
    
    public DefaultDocumentChunk(String content, int startIndex, int endIndex, int chunkIndex, String sourceDocumentId) {
        this(UUID.randomUUID().toString(), content, startIndex, endIndex, chunkIndex, new HashMap<>(), sourceDocumentId);
    }
    
    public DefaultDocumentChunk(String id, String content, int startIndex, int endIndex, int chunkIndex, 
                                Map<String, Object> metadata, String sourceDocumentId) {
        this.id = id;
        this.content = content;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.chunkIndex = chunkIndex;
        this.metadata = metadata;
        this.sourceDocumentId = sourceDocumentId;
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
    public int getStartIndex() {
        return startIndex;
    }
    
    @Override
    public int getEndIndex() {
        return endIndex;
    }
    
    @Override
    public int getChunkIndex() {
        return chunkIndex;
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    @Override
    public String getSourceDocumentId() {
        return sourceDocumentId;
    }
}
