package ltd.idcu.est.llm.core.api;

import java.util.List;

public interface Embedding {
    
    Tensor forward(Tensor inputIds);
    
    Tensor getTokenEmbeddings();
    
    Tensor getPositionEmbeddings();
    
    int getVocabSize();
    
    int getEmbeddingDim();
    
    void setTokenEmbeddings(Tensor embeddings);
    
    void setPositionEmbeddings(Tensor embeddings);
    
    void initializeWeights();
    
    void loadWeights(String path);
    
    void saveWeights(String path);
    
    List<Tensor> getParameters();
    
    void setTraining(boolean training);
    
    boolean isTraining();
}
