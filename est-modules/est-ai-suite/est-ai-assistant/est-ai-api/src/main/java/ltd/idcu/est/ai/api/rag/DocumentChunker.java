package ltd.idcu.est.ai.api.rag;

import java.util.List;

public interface DocumentChunker {
    
    List<DocumentChunk> chunk(Document document);
    
    List<DocumentChunk> chunk(String content);
    
    List<DocumentChunk> chunk(String content, String documentId);
    
    void setChunkSize(int chunkSize);
    
    void setOverlapSize(int overlapSize);
    
    int getChunkSize();
    
    int getOverlapSize();
}
