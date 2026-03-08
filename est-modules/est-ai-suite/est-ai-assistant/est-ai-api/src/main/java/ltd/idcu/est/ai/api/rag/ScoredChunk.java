package ltd.idcu.est.ai.api.rag;

public interface ScoredChunk {
    
    DocumentChunk getChunk();
    
    double getScore();
    
    int getOriginalRank();
}
