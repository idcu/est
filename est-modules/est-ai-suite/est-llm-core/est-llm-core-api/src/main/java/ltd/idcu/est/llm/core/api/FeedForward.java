package ltd.idcu.est.llm.core.api;

import java.util.List;

public interface FeedForward {
    
    Tensor forward(Tensor hiddenStates);
    
    int getHiddenSize();
    
    int getIntermediateSize();
    
    Tensor getFirstLayerWeights();
    
    Tensor getFirstLayerBias();
    
    Tensor getSecondLayerWeights();
    
    Tensor getSecondLayerBias();
    
    void setFirstLayerWeights(Tensor weights);
    
    void setFirstLayerBias(Tensor bias);
    
    void setSecondLayerWeights(Tensor weights);
    
    void setSecondLayerBias(Tensor bias);
    
    String getActivationFunction();
    
    void setActivationFunction(String activation);
    
    void initializeWeights();
    
    void loadWeights(String path);
    
    void saveWeights(String path);
    
    List<Tensor> getParameters();
    
    void setTraining(boolean training);
    
    boolean isTraining();
}
