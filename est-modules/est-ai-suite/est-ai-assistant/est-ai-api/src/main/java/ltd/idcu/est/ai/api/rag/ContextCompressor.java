package ltd.idcu.est.ai.api.rag;

import java.util.List;

public interface ContextCompressor {
    
    String getName();
    
    List<DocumentChunk> compress(String query, List<DocumentChunk> chunks, int maxTokens);
    
    String compressToString(String query, List<DocumentChunk> chunks, int maxTokens);
}
