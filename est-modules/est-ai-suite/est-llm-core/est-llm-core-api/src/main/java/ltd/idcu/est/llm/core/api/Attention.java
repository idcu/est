package ltd.idcu.est.llm.core.api;

import java.util.List;

public interface Attention {
    
    Tensor forward(Tensor hiddenStates);
    
    Tensor forward(Tensor hiddenStates, Tensor attentionMask);
    
    Tensor forward(Tensor hiddenStates, Tensor attentionMask, Tensor keyValueStates);
    
    int getHiddenSize();
    
    int getNumHeads();
    
    int getHeadDim();
    
    boolean isCausal();
    
    void setCausal(boolean causal);
    
    Tensor getQueryWeights();
    
    Tensor getKeyWeights();
    
    Tensor getValueWeights();
    
    Tensor getOutputWeights();
    
    Tensor getQueryBias();
    
    Tensor getKeyBias();
    
    Tensor getValueBias();
    
    Tensor getOutputBias();
    
    void setQueryWeights(Tensor weights);
    
    void setKeyWeights(Tensor weights);
    
    void setValueWeights(Tensor weights);
    
    void setOutputWeights(Tensor weights);
    
    void setQueryBias(Tensor bias);
    
    void setKeyBias(Tensor bias);
    
    void setValueBias(Tensor bias);
    
    void setOutputBias(Tensor bias);
    
    void initializeWeights();
    
    void loadWeights(String path);
    
    void saveWeights(String path);
    
    List<Tensor> getParameters();
    
    void setTraining(boolean training);
    
    boolean isTraining();
}
