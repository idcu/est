package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.RagResponse;
import ltd.idcu.est.ai.api.rag.DocumentChunk;

import java.util.ArrayList;
import java.util.List;

public class DefaultRagResponse implements RagResponse {
    
    private final String answer;
    private final List<DocumentChunk> contextChunks;
    private final String originalQuery;
    private final boolean success;
    private final String errorMessage;
    
    public static DefaultRagResponse success(String answer, List<DocumentChunk> contextChunks, String originalQuery) {
        return new DefaultRagResponse(answer, contextChunks, originalQuery, true, null);
    }
    
    public static DefaultRagResponse error(String errorMessage, String originalQuery) {
        return new DefaultRagResponse(null, new ArrayList<>(), originalQuery, false, errorMessage);
    }
    
    private DefaultRagResponse(String answer, List<DocumentChunk> contextChunks, String originalQuery, 
                               boolean success, String errorMessage) {
        this.answer = answer;
        this.contextChunks = contextChunks;
        this.originalQuery = originalQuery;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String getAnswer() {
        return answer;
    }
    
    @Override
    public List<DocumentChunk> getContextChunks() {
        return contextChunks;
    }
    
    @Override
    public String getOriginalQuery() {
        return originalQuery;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
    
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
