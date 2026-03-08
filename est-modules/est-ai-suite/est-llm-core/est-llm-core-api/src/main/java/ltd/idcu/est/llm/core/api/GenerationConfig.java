package ltd.idcu.est.llm.core.api;

import java.util.List;

public class GenerationConfig {
    
    private float temperature = 0.7f;
    private float topP = 0.9f;
    private Integer topK = null;
    private int maxNewTokens = 100;
    private Integer minNewTokens = null;
    private List<String> stopSequences = null;
    private boolean doSample = true;
    private int numBeams = 1;
    private boolean earlyStopping = false;
    private float repetitionPenalty = 1.0f;
    private float lengthPenalty = 1.0f;
    private boolean useCache = true;
    private Integer padTokenId = null;
    private Integer bosTokenId = null;
    private Integer eosTokenId = null;
    
    public GenerationConfig() {
    }
    
    public static GenerationConfig defaultConfig() {
        return new GenerationConfig();
    }
    
    public static GenerationConfig greedyConfig() {
        GenerationConfig config = new GenerationConfig();
        config.setDoSample(false);
        config.setTemperature(0.0f);
        return config;
    }
    
    public static GenerationConfig creativeConfig() {
        GenerationConfig config = new GenerationConfig();
        config.setTemperature(0.9f);
        config.setTopP(0.95f);
        return config;
    }
    
    public float getTemperature() {
        return temperature;
    }
    
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    
    public float getTopP() {
        return topP;
    }
    
    public void setTopP(float topP) {
        this.topP = topP;
    }
    
    public Integer getTopK() {
        return topK;
    }
    
    public void setTopK(Integer topK) {
        this.topK = topK;
    }
    
    public int getMaxNewTokens() {
        return maxNewTokens;
    }
    
    public void setMaxNewTokens(int maxNewTokens) {
        this.maxNewTokens = maxNewTokens;
    }
    
    public Integer getMinNewTokens() {
        return minNewTokens;
    }
    
    public void setMinNewTokens(Integer minNewTokens) {
        this.minNewTokens = minNewTokens;
    }
    
    public List<String> getStopSequences() {
        return stopSequences;
    }
    
    public void setStopSequences(List<String> stopSequences) {
        this.stopSequences = stopSequences;
    }
    
    public boolean isDoSample() {
        return doSample;
    }
    
    public void setDoSample(boolean doSample) {
        this.doSample = doSample;
    }
    
    public int getNumBeams() {
        return numBeams;
    }
    
    public void setNumBeams(int numBeams) {
        this.numBeams = numBeams;
    }
    
    public boolean isEarlyStopping() {
        return earlyStopping;
    }
    
    public void setEarlyStopping(boolean earlyStopping) {
        this.earlyStopping = earlyStopping;
    }
    
    public float getRepetitionPenalty() {
        return repetitionPenalty;
    }
    
    public void setRepetitionPenalty(float repetitionPenalty) {
        this.repetitionPenalty = repetitionPenalty;
    }
    
    public float getLengthPenalty() {
        return lengthPenalty;
    }
    
    public void setLengthPenalty(float lengthPenalty) {
        this.lengthPenalty = lengthPenalty;
    }
    
    public boolean isUseCache() {
        return useCache;
    }
    
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }
    
    public Integer getPadTokenId() {
        return padTokenId;
    }
    
    public void setPadTokenId(Integer padTokenId) {
        this.padTokenId = padTokenId;
    }
    
    public Integer getBosTokenId() {
        return bosTokenId;
    }
    
    public void setBosTokenId(Integer bosTokenId) {
        this.bosTokenId = bosTokenId;
    }
    
    public Integer getEosTokenId() {
        return eosTokenId;
    }
    
    public void setEosTokenId(Integer eosTokenId) {
        this.eosTokenId = eosTokenId;
    }
}
