package ltd.idcu.est.rag.api;

public interface EmbeddingModel {
    
    Embedding embed(String text);
    
    List<Embedding> embed(List<String> texts);
    
    float[] embedToVector(String text);
    
    List<float[]> embedToVectors(List<String> texts);
    
    int getDimension();
    
    String getModelName();
}
