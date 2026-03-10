package ltd.idcu.est.ai.api.rag;

import java.util.List;

public interface RagResponse {
    
    String getAnswer();
    
    List<DocumentChunk> getContextChunks();
    
    String getOriginalQuery();
    
    boolean isSuccess();
    
    String getErrorMessage();
}
