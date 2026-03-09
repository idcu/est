package ltd.idcu.est.ai.api.rag;

import java.util.List;
import java.util.Map;

public interface DocumentChunker {
    
    List<DocumentChunk> chunk(Document document);
    
    List<DocumentChunk> chunk(String content);
    
    List<DocumentChunk> chunk(String content, String documentId);
    
    List<DocumentChunk> chunkSmart(Document document);
    
    List<DocumentChunk> chunkByParagraph(Document document);
    
    List<DocumentChunk> chunkByHeading(Document document);
    
    void setChunkSize(int chunkSize);
    
    void setOverlapSize(int overlapSize);
    
    int getChunkSize();
    
    int getOverlapSize();
    
    void setChunkerStrategy(String strategy);
    
    String getChunkerStrategy();
    
    Map<String, Object> getChunkingStats();
}
