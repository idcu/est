package ltd.idcu.est.ai.api.rag;

import java.util.List;

public interface HybridRetriever extends RagRetriever {
    
    List<DocumentChunk> hybridSearch(String query, int topK, double bm25Weight, double vectorWeight);
    
    List<DocumentChunk> bm25Search(String query, int topK);
}
