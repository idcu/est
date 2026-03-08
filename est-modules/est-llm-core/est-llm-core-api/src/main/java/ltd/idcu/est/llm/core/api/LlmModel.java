package ltd.idcu.est.llm.core.api;

import java.util.List;
import java.util.Map;

public interface LlmModel {
    
    Tensor forward(Tensor inputIds);
    
    Tensor forward(Tensor inputIds, Tensor attentionMask);
    
    Tensor generate(Tensor inputIds, int maxNewTokens);
    
    Tensor generate(Tensor inputIds, int maxNewTokens, GenerationConfig config);
    
    String generateText(String prompt);
    
    String generateText(String prompt, int maxNewTokens);
    
    String generateText(String prompt, GenerationConfig config);
    
    Embedding getEmbedding();
    
    List<TransformerBlock> getLayers();
    
    Tensor getFinalLayerNormWeights();
    
    Tensor getFinalLayerNormBias();
    
    Tensor getLmHeadWeights();
    
    Tensor getLmHeadBias();
    
    void setFinalLayerNormWeights(Tensor weights);
    
    void setFinalLayerNormBias(Tensor bias);
    
    void setLmHeadWeights(Tensor weights);
    
    void setLmHeadBias(Tensor bias);
    
    Tokenizer getTokenizer();
    
    void setTokenizer(Tokenizer tokenizer);
    
    int getVocabSize();
    
    int getHiddenSize();
    
    int getNumLayers();
    
    int getNumHeads();
    
    int getMaxPositionEmbeddings();
    
    float getLayerNormEpsilon();
    
    void setLayerNormEpsilon(float epsilon);
    
    void initializeWeights();
    
    void loadWeights(String path);
    
    void saveWeights(String path);
    
    void loadCheckpoint(String path);
    
    void saveCheckpoint(String path);
    
    List<Tensor> getParameters();
    
    void setTraining(boolean training);
    
    boolean isTraining();
    
    void train(Dataset dataset, TrainingConfig config);
    
    void evaluate(Dataset dataset);
    
    Map<String, Object> getModelConfig();
    
    void setModelConfig(Map<String, Object> config);
}
