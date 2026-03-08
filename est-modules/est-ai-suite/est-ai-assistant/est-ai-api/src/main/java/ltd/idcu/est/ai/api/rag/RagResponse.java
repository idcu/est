package ltd.idcu.est.ai.api.rag;

public interface RagResponse {
    
    String getAnswer();
    
    List<DocumentChunk> getContextChunks();
    
    String getOriginalQuery();
    
    boolean isSuccess();
    
    String getErrorMessage();
}
