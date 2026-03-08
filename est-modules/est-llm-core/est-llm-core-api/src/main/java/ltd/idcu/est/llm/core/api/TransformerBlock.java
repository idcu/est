package ltd.idcu.est.llm.core.api;

import java.util.List;

public interface TransformerBlock {
    
    Tensor forward(Tensor hiddenStates);
    
    Tensor forward(Tensor hiddenStates, Tensor attentionMask);
    
    Attention getAttention();
    
    FeedForward getFeedForward();
    
    Tensor getInputLayerNormWeights();
    
    Tensor getInputLayerNormBias();
    
    Tensor getPostAttentionLayerNormWeights();
    
    Tensor getPostAttentionLayerNormBias();
    
    void setInputLayerNormWeights(Tensor weights);
    
    void setInputLayerNormBias(Tensor bias);
    
    void setPostAttentionLayerNormWeights(Tensor weights);
    
    void setPostAttentionLayerNormBias(Tensor bias);
    
    float getLayerNormEpsilon();
    
    void setLayerNormEpsilon(float epsilon);
    
    int getHiddenSize();
    
    void initializeWeights();
    
    void loadWeights(String path);
    
    void saveWeights(String path);
    
    List<Tensor> getParameters();
    
    void setTraining(boolean training);
    
    boolean isTraining();
}
