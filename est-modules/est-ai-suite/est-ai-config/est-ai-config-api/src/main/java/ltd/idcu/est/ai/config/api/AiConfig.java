package ltd.idcu.est.ai.config.api;

import java.util.Map;

public interface AiConfig {
    
    String getDefaultLlmProvider();
    
    Map<String, LlmProviderConfig> getLlmProviders();
    
    LlmProviderConfig getLlmProvider(String name);
    
    boolean isFeatureEnabled(String featureName);
    
    Map<String, Object> getFeatureConfig(String featureName);
    
    String getLogLevel();
    
    boolean isRequestLoggingEnabled();
    
    Map<String, Object> getAllConfig();
}
