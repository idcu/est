package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.ConfigLoader;
import ltd.idcu.est.ai.api.config.LlmProviderConfig;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;

public class ConfigManagementTest {
    
    private ConfigLoader yamlConfigLoader;
    private ConfigLoader envConfigLoader;
    private ConfigLoader compositeConfigLoader;
    
    @Test
    public void setUp() {
        yamlConfigLoader = new YamlConfigLoader();
        envConfigLoader = new EnvConfigLoader();
        compositeConfigLoader = new CompositeConfigLoader();
    }
    
    @Test
    public void testYamlConfigLoaderSupportsYaml() {
        assertTrue(yamlConfigLoader.supports("yaml"));
        assertTrue(yamlConfigLoader.supports("yml"));
        assertFalse(yamlConfigLoader.supports("env"));
    }
    
    @Test
    public void testEnvConfigLoaderSupportsEnv() {
        assertTrue(envConfigLoader.supports("env"));
        assertFalse(envConfigLoader.supports("yaml"));
    }
    
    @Test
    public void testCompositeConfigLoaderSupportsAllFormats() {
        assertTrue(compositeConfigLoader.supports("yaml"));
        assertTrue(compositeConfigLoader.supports("yml"));
        assertTrue(compositeConfigLoader.supports("env"));
    }
    
    @Test
    public void testLoadDefaultConfig() {
        AiConfig config = yamlConfigLoader.load();
        
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
        assertNotNull(config.getLlmProviders());
        assertNotNull(config.getLogLevel());
    }
    
    @Test
    public void testDefaultAiConfigValues() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        assertEquals("zhipuai", config.getDefaultLlmProvider());
        assertEquals("INFO", config.getLogLevel());
        assertFalse(config.isRequestLoggingEnabled());
        assertNotNull(config.getLlmProvider("zhipuai"));
        assertNotNull(config.getLlmProvider("openai"));
    }
    
    @Test
    public void testLlmProviderConfig() {
        LlmProviderConfig provider = new LlmProviderConfig()
                .enabled(true)
                .model("test-model")
                .endpoint("https://test.endpoint")
                .apiKey("test-api-key")
                .timeout(60)
                .maxRetries(5);
        
        assertTrue(provider.isEnabled());
        assertEquals("test-model", provider.getModel());
        assertEquals("https://test.endpoint", provider.getEndpoint());
        assertEquals("test-api-key", provider.getApiKey());
        assertEquals(60, provider.getTimeout());
        assertEquals(5, provider.getMaxRetries());
    }
    
    @Test
    public void testFeatureConfig() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        assertTrue(config.isFeatureEnabled("code-completion"));
        assertTrue(config.isFeatureEnabled("refactor-assistant"));
        
        Map<String, Object> featureConfig = config.getFeatureConfig("code-completion");
        assertNotNull(featureConfig);
        assertTrue(featureConfig.containsKey("enabled"));
    }
    
    @Test
    public void testAiAssistantConfigIntegration() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        AiConfig config = assistant.getConfig();
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
        
        DefaultAiConfig newConfig = new DefaultAiConfig()
                .defaultLlmProvider("openai")
                .logLevel("DEBUG")
                .requestLoggingEnabled(true);
        
        assistant.setConfig(newConfig);
        
        assertEquals("openai", assistant.getConfig().getDefaultLlmProvider());
        assertEquals("DEBUG", assistant.getConfig().getLogLevel());
        assertTrue(assistant.getConfig().isRequestLoggingEnabled());
    }
    
    @Test
    public void testCompositeConfigLoaderMerge() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        loader.addLoader(new ConfigLoader() {
            @Override
            public AiConfig load() {
                DefaultAiConfig config = new DefaultAiConfig();
                config.setDefaultLlmProvider("test-provider");
                return config;
            }
            
            @Override
            public AiConfig load(String path) {
                return load();
            }
            
            @Override
            public boolean supports(String format) {
                return "test".equals(format);
            }
        });
        
        AiConfig config = loader.load();
        assertNotNull(config);
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
    public void testSetFeatureConfig() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        Map<String, Object> newFeatureConfig = Map.of(
                "enabled", true,
                "custom-setting", "value"
        );
        
        config.setFeatureConfig("new-feature", newFeatureConfig);
        
        assertTrue(config.isFeatureEnabled("new-feature"));
        Map<String, Object> retrievedConfig = config.getFeatureConfig("new-feature");
        assertEquals("value", retrievedConfig.get("custom-setting"));
    }
    
    @Test
    public void testAddLlmProvider() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        LlmProviderConfig newProvider = new LlmProviderConfig()
                .enabled(true)
                .model("custom-model");
        
        config.addLlmProvider("custom-provider", newProvider);
        
        LlmProviderConfig retrieved = config.getLlmProvider("custom-provider");
        assertNotNull(retrieved);
        assertTrue(retrieved.isEnabled());
        assertEquals("custom-model", retrieved.getModel());
    }
}
