package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public class LlmOptions {
    
    private String model;
    private Double temperature;
    private Integer maxTokens;
    private Double topP;
    private Integer topK;
    private Double frequencyPenalty;
    private Double presencePenalty;
    private List<String> stop;
    private Integer numChoices;
    private Boolean stream;
    private Map<String, Object> extraParams;
    
    public LlmOptions() {
        this.temperature = 0.7;
        this.maxTokens = 2000;
        this.topP = 1.0;
        this.numChoices = 1;
        this.stream = false;
    }
    
    public static LlmOptions defaults() {
        return new LlmOptions();
    }
    
    public static LlmOptions creative() {
        LlmOptions options = new LlmOptions();
        options.temperature = 1.0;
        options.maxTokens = 4000;
        return options;
    }
    
    public static LlmOptions precise() {
        LlmOptions options = new LlmOptions();
        options.temperature = 0.1;
        options.maxTokens = 1000;
        return options;
    }
    
    public LlmOptions model(String model) {
        this.model = model;
        return this;
    }
    
    public LlmOptions temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }
    
    public LlmOptions maxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
        return this;
    }
    
    public LlmOptions topP(Double topP) {
        this.topP = topP;
        return this;
    }
    
    public LlmOptions topK(Integer topK) {
        this.topK = topK;
        return this;
    }
    
    public LlmOptions frequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
        return this;
    }
    
    public LlmOptions presencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
        return this;
    }
    
    public LlmOptions stop(List<String> stop) {
        this.stop = stop;
        return this;
    }
    
    public LlmOptions numChoices(Integer numChoices) {
        this.numChoices = numChoices;
        return this;
    }
    
    public LlmOptions stream(Boolean stream) {
        this.stream = stream;
        return this;
    }
    
    public LlmOptions extraParam(String key, Object value) {
        if (this.extraParams == null) {
            this.extraParams = new java.util.HashMap<>();
        }
        this.extraParams.put(key, value);
        return this;
    }
    
    public String getModel() {
        return model;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public Integer getMaxTokens() {
        return maxTokens;
    }
    
    public Double getTopP() {
        return topP;
    }
    
    public Integer getTopK() {
        return topK;
    }
    
    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }
    
    public Double getPresencePenalty() {
        return presencePenalty;
    }
    
    public List<String> getStop() {
        return stop;
    }
    
    public Integer getNumChoices() {
        return numChoices;
    }
    
    public Boolean getStream() {
        return stream;
    }
    
    public Map<String, Object> getExtraParams() {
        return extraParams;
    }
}
