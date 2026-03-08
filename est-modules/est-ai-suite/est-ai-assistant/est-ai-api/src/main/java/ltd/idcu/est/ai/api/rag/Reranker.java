package ltd.idcu.est.ai.api.rag;

import java.util.List;

public interface Reranker {
    
    String getName();
    
    List<DocumentChunk> rerank(String query, List<DocumentChunk> chunks, int topK);
    
    List<ScoredChunk> rerankWithScores(String query, List<DocumentChunk> chunks);
}
