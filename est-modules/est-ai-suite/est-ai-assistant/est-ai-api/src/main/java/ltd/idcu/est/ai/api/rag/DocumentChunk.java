package ltd.idcu.est.ai.api.rag;

import java.util.Map;

public interface DocumentChunk {
    
    String getId();
    
    String getContent();
    
    int getStartIndex();
    
    int getEndIndex();
    
    int getChunkIndex();
    
    Map<String, Object> getMetadata();
    
    String getSourceDocumentId();
}
