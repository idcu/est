package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;

import java.util.*;
import java.util.regex.Pattern;

public class DefaultScoredChunk implements ScoredChunk {
    
    private final DocumentChunk chunk;
    private final double score;
    private final int originalRank;
    
    public DefaultScoredChunk(DocumentChunk chunk, double score, int originalRank) {
        this.chunk = chunk;
        this.score = score;
        this.originalRank = originalRank;
    }
    
    @Override
    public DocumentChunk getChunk() {
        return chunk;
    }
    
    @Override
    public double getScore() {
        return score;
    }
    
    @Override
    public int getOriginalRank() {
        return originalRank;
    }
}
