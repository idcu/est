package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.config.LlmProviderConfig;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;

public class DefaultAiConfigTest {
    
    @Test
    public void testDefaultValues() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        assertEquals("zhipuai", config.getDefaultLlmProvider());
        assertEquals("INFO", config.getLogLevel());
        assertFalse(config.isRequestLoggingEnabled());
    }
    
    @Test
    public void testSetDefaultLlmProvider() {
        DefaultAiConfig config = new DefaultAiConfig();
        config.setDefaultLlmProvider("openai");
        
        assertEquals("openai", config.getDefaultLlmProvider());
    }
    
    @Test
    public void testFluentApiDefaultLlmProvider() {
        DefaultAiConfig config = new DefaultAiConfig()
                .defaultLlmProvider("qwen");
        
        assertEquals("qwen", config.getDefaultLlmProvider());
    }
    
    @Test
    public void testSetLogLevel() {
        DefaultAiConfig config = new DefaultAiConfig();
        config.setLogLevel("DEBUG");
        
        assertEquals("DEBUG", config.getLogLevel());
    }
    
    @Test
    public void testFluentApiLogLevel() {
        DefaultAiConfig config = new DefaultAiConfig()
                .logLevel("WARN");
        
        assertEquals("WARN", config.getLogLevel());
    }
    
    @Test
    public void testSetRequestLoggingEnabled() {
        DefaultAiConfig config = new DefaultAiConfig();
        config.setRequestLoggingEnabled(true);
        
        assertTrue(config.isRequestLoggingEnabled());
    }
    
    @Test
    public void testFluentApiRequestLoggingEnabled() {
        DefaultAiConfig config = new DefaultAiConfig()
                .requestLoggingEnabled(true);
        
        assertTrue(config.isRequestLoggingEnabled());
    }
    
    @Test
    public void testDefaultLlmProviders() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        Map<String, LlmProviderConfig> providers = config.getLlmProviders();
        assertNotNull(providers);
        assertFalse(providers.isEmpty());
        
        assertNotNull(config.getLlmProvider("zhipuai"));
        assertNotNull(config.getLlmProvider("openai"));
    }
    
    @Test
    public void testSetLlmProviders() {
        DefaultAiConfig config = new DefaultAiConfig();
        Map<String, LlmProviderConfig> newProviders = Map.of(
                "test", new LlmProviderConfig().enabled(true)
        );
        
        config.setLlmProviders(newProviders);
        
        Map<String, LlmProviderConfig> providers = config.getLlmProviders();
        assertEquals(1, providers.size());
        assertNotNull(providers.get("test"));
    }
    
    @Test
    public void testAddLlmProvider() {
        DefaultAiConfig config = new DefaultAiConfig();
        LlmProviderConfig newProvider = new LlmProviderConfig()
                .enabled(true)
                .model("test-model");
        
        config.addLlmProvider("new-provider", newProvider);
        
        LlmProviderConfig retrieved = config.getLlmProvider("new-provider");
        assertNotNull(retrieved);
        assertTrue(retrieved.isEnabled());
        assertEquals("test-model", retrieved.getModel());
    }
    
    @Test
    public void testGetLlmProvidersReturnsCopy() {
        DefaultAiConfig config = new DefaultAiConfig();
        Map<String, LlmProviderConfig> providers1 = config.getLlmProviders();
        Map<String, LlmProviderConfig> providers2 = config.getLlmProviders();
        
        assertNotSame(providers1, providers2);
    }
    
    @Test
    public void testDefaultFeaturesEnabled() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        assertTrue(config.isFeatureEnabled("code-completion"));
        assertTrue(config.isFeatureEnabled("refactor-assistant"));
        assertTrue(config.isFeatureEnabled("architecture-advisor"));
    }
    
    @Test
    public void testFeatureDisabledByDefault() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        assertFalse(config.isFeatureEnabled("non-existent-feature"));
    }
    
    @Test
    public void testGetFeatureConfig() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        Map<String, Object> featureConfig = config.getFeatureConfig("code-completion");
        assertNotNull(featureConfig);
        assertFalse(featureConfig.isEmpty());
    }
    
    @Test
    public void testGetFeatureConfigReturnsCopy() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        Map<String, Object> config1 = config.getFeatureConfig("code-completion");
        Map<String, Object> config2 = config.getFeatureConfig("code-completion");
        
        assertNotSame(config1, config2);
    }
    
    @Test
    public void testSetFeatureConfig() {
        DefaultAiConfig config = new DefaultAiConfig();
        Map<String, Object> newConfig = Map.of(
                "enabled", true,
                "custom-option", "value"
        );
        
        config.setFeatureConfig("new-feature", newConfig);
        
        assertTrue(config.isFeatureEnabled("new-feature"));
        Map<String, Object> retrieved = config.getFeatureConfig("new-feature");
        assertEquals("value", retrieved.get("custom-option"));
    }
    
    @Test
    public void testGetAllConfig() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        Map<String, Object> allConfig = config.getAllConfig();
        assertNotNull(allConfig);
        assertTrue(allConfig.containsKey("default-llm-provider"));
        assertTrue(allConfig.containsKey("llm-providers"));
        assertTrue(allConfig.containsKey("features"));
        assertTrue(allConfig.containsKey("log-level"));
        assertTrue(allConfig.containsKey("request-logging-enabled"));
    }
    
    @Test
    public void testSetAllConfig() {
        DefaultAiConfig config = new DefaultAiConfig();
        Map<String, Object> extraConfig = Map.of(
                "custom-key", "custom-value"
        );
        
        config.setAllConfig(extraConfig);
        
        Map<String, Object> allConfig = config.getAllConfig();
        assertTrue(allConfig.containsKey("custom-key"));
        assertEquals("custom-value", allConfig.get("custom-key"));
    }
}
