package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.LlmProviderConfig;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;

public class YamlConfigLoaderTest {
    
    @Test
    public void testSupportsYamlFormat() {
        YamlConfigLoader loader = new YamlConfigLoader();
        assertTrue(loader.supports("yaml"));
        assertTrue(loader.supports("yml"));
        assertTrue(loader.supports("YAML"));
        assertTrue(loader.supports("YML"));
    }
    
    @Test
    public void testDoesNotSupportOtherFormats() {
        YamlConfigLoader loader = new YamlConfigLoader();
        assertFalse(loader.supports("env"));
        assertFalse(loader.supports("json"));
        assertFalse(loader.supports("xml"));
        assertFalse(loader.supports("properties"));
    }
    
    @Test
    public void testLoadDefaultConfig() {
        YamlConfigLoader loader = new YamlConfigLoader();
        AiConfig config = loader.load();
        
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
        assertNotNull(config.getLlmProviders());
        assertNotNull(config.getLogLevel());
    }
    
    @Test
    public void testDefaultProviderConfig() {
        YamlConfigLoader loader = new YamlConfigLoader();
        AiConfig config = loader.load();
        
        LlmProviderConfig zhipuai = config.getLlmProvider("zhipuai");
        assertNotNull(zhipuai);
        assertTrue(zhipuai.isEnabled());
    }
    
    @Test
    public void testFeatureConfigLoading() {
        YamlConfigLoader loader = new YamlConfigLoader();
        AiConfig config = loader.load();
        
        assertTrue(config.isFeatureEnabled("code-completion"));
        
        Map<String, Object> codeCompletionConfig = config.getFeatureConfig("code-completion");
        assertNotNull(codeCompletionConfig);
        assertTrue(codeCompletionConfig.containsKey("enabled"));
    }
    
    @Test
    public void testMultipleLlmProvidersLoaded() {
        YamlConfigLoader loader = new YamlConfigLoader();
        AiConfig config = loader.load();
        
        Map<String, LlmProviderConfig> providers = config.getLlmProviders();
        assertNotNull(providers);
        assertFalse(providers.isEmpty());
        
        assertNotNull(providers.get("zhipuai"));
        assertNotNull(providers.get("openai"));
    }
    
    @Test
    public void testGetAllConfig() {
        YamlConfigLoader loader = new YamlConfigLoader();
        AiConfig config = loader.load();
        
        Map<String, Object> allConfig = config.getAllConfig();
        assertNotNull(allConfig);
        assertTrue(allConfig.containsKey("default-llm-provider"));
        assertTrue(allConfig.containsKey("llm-providers"));
        assertTrue(allConfig.containsKey("features"));
        assertTrue(allConfig.containsKey("log-level"));
        assertTrue(allConfig.containsKey("request-logging-enabled"));
    }
}
