package ltd.idcu.est.ai.config.api;

import java.util.Map;

public class LlmProviderConfig {
    
    private boolean enabled;
    private String apiKey;
    private String model;
    private String endpoint;
    private Integer timeout;
    private Integer maxRetries;
    private Map<String, Object> extra;
    
    public LlmProviderConfig() {
        this.enabled = true;
        this.timeout = 30000;
        this.maxRetries = 3;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public Integer getTimeout() {
        return timeout;
    }
    
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    
    public Integer getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public Map<String, Object> getExtra() {
        return extra;
    }
    
    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
    
    public LlmProviderConfig enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public LlmProviderConfig apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }
    
    public LlmProviderConfig model(String model) {
        this.model = model;
        return this;
    }
    
    public LlmProviderConfig endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }
    
    public LlmProviderConfig timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }
    
    public LlmProviderConfig maxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }
    
    public LlmProviderConfig extra(Map<String, Object> extra) {
        this.extra = extra;
        return this;
    }
}
