package ltd.idcu.est.ai.api.rag;

import java.util.List;
import java.util.Map;

public interface Reranker {
    
    String getName();
    
    List<DocumentChunk> rerank(String query, List<DocumentChunk> chunks, int topK);
    
    List<ScoredChunk> rerankWithScores(String query, List<DocumentChunk> chunks);
    
    void setRerankStrategy(String strategy);
    
    String getRerankStrategy();
    
    void setWeights(Map<String, Double> weights);
    
    Map<String, Double> getWeights();
    
    Map<String, Object> getRerankingStats();
}
