package ltd.idcu.est.rag.api;

import java.util.Map;

public class DocumentChunk {
    
    private String id;
    private String documentId;
    private String content;
    private int chunkIndex;
    private int startPosition;
    private int endPosition;
    private Map<String, Object> metadata;
    
    public DocumentChunk() {
    }
    
    public DocumentChunk(String id, String documentId, String content, int chunkIndex) {
        this.id = id;
        this.documentId = documentId;
        this.content = content;
        this.chunkIndex = chunkIndex;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getChunkIndex() {
        return chunkIndex;
    }
    
    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }
    
    public int getStartPosition() {
        return startPosition;
    }
    
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
    
    public int getEndPosition() {
        return endPosition;
    }
    
    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
