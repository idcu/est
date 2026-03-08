package ltd.idcu.est.ai.api.vector;

public interface EmbeddingModel {
    
    String getName();
    
    float[] embed(String text);
    
    float[][] embedBatch(String[] texts);
    
    int getDimension();
}
