package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.LlmProviderConfig;

import java.util.HashMap;
import java.util.Map;

public class DefaultAiConfig implements AiConfig {
    
    private String defaultLlmProvider;
    private Map<String, LlmProviderConfig> llmProviders;
    private Map<String, Object> features;
    private String logLevel;
    private boolean requestLoggingEnabled;
    private Map<String, Object> allConfig;
    
    public DefaultAiConfig() {
        this.defaultLlmProvider = "zhipuai";
        this.llmProviders = new HashMap<>();
        this.features = new HashMap<>();
        this.logLevel = "INFO";
        this.requestLoggingEnabled = false;
        this.allConfig = new HashMap<>();
        
        initializeDefaults();
    }
    
    private void initializeDefaults() {
        LlmProviderConfig zhipuai = new LlmProviderConfig()
                .enabled(true)
                .model("glm-4")
                .endpoint("https://open.bigmodel.cn/api/paas/v4");
        llmProviders.put("zhipuai", zhipuai);
        
        LlmProviderConfig openai = new LlmProviderConfig()
                .enabled(false)
                .model("gpt-4")
                .endpoint("https://api.openai.com/v1");
        llmProviders.put("openai", openai);
        
        features.put("code-completion", Map.of("enabled", true, "use-llm", true));
        features.put("refactor-assistant", Map.of("enabled", true));
        features.put("architecture-advisor", Map.of("enabled", true));
    }
    
    @Override
    public String getDefaultLlmProvider() {
        return defaultLlmProvider;
    }
    
    public void setDefaultLlmProvider(String defaultLlmProvider) {
        this.defaultLlmProvider = defaultLlmProvider;
    }
    
    @Override
    public Map<String, LlmProviderConfig> getLlmProviders() {
        return new HashMap<>(llmProviders);
    }
    
    public void setLlmProviders(Map<String, LlmProviderConfig> llmProviders) {
        this.llmProviders = new HashMap<>(llmProviders);
    }
    
    @Override
    public LlmProviderConfig getLlmProvider(String name) {
        return llmProviders.get(name);
    }
    
    public void addLlmProvider(String name, LlmProviderConfig config) {
        llmProviders.put(name, config);
    }
    
    @Override
    public boolean isFeatureEnabled(String featureName) {
        Object featureConfig = features.get(featureName);
        if (featureConfig instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) featureConfig;
            Object enabled = map.get("enabled");
            return enabled instanceof Boolean && (Boolean) enabled;
        }
        return false;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getFeatureConfig(String featureName) {
        Object config = features.get(featureName);
        if (config instanceof Map) {
            return new HashMap<>((Map<String, Object>) config);
        }
        return new HashMap<>();
    }
    
    public void setFeatureConfig(String featureName, Map<String, Object> config) {
        features.put(featureName, new HashMap<>(config));
    }
    
    @Override
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    @Override
    public boolean isRequestLoggingEnabled() {
        return requestLoggingEnabled;
    }
    
    public void setRequestLoggingEnabled(boolean requestLoggingEnabled) {
        this.requestLoggingEnabled = requestLoggingEnabled;
    }
    
    @Override
    public Map<String, Object> getAllConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("default-llm-provider", defaultLlmProvider);
        config.put("llm-providers", llmProviders);
        config.put("features", features);
        config.put("log-level", logLevel);
        config.put("request-logging-enabled", requestLoggingEnabled);
        config.putAll(allConfig);
        return config;
    }
    
    public void setAllConfig(Map<String, Object> allConfig) {
        this.allConfig = new HashMap<>(allConfig);
    }
    
    public DefaultAiConfig defaultLlmProvider(String provider) {
        this.defaultLlmProvider = provider;
        return this;
    }
    
    public DefaultAiConfig logLevel(String level) {
        this.logLevel = level;
        return this;
    }
    
    public DefaultAiConfig requestLoggingEnabled(boolean enabled) {
        this.requestLoggingEnabled = enabled;
        return this;
    }
}
