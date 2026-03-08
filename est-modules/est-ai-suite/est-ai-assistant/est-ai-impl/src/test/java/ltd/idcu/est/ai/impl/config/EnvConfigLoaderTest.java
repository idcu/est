package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

public class EnvConfigLoaderTest {
    
    @Test
    public void testSupportsEnvFormat() {
        EnvConfigLoader loader = new EnvConfigLoader();
        assertTrue(loader.supports("env"));
        assertTrue(loader.supports("ENV"));
    }
    
    @Test
    public void testDoesNotSupportOtherFormats() {
        EnvConfigLoader loader = new EnvConfigLoader();
        assertFalse(loader.supports("yaml"));
        assertFalse(loader.supports("yml"));
        assertFalse(loader.supports("json"));
    }
    
    @Test
    public void testLoadDefaultConfig() {
        EnvConfigLoader loader = new EnvConfigLoader();
        AiConfig config = loader.load();
        
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
        assertNotNull(config.getLlmProviders());
        assertNotNull(config.getLogLevel());
    }
    
    @Test
    public void testLoadWithPathParam() {
        EnvConfigLoader loader = new EnvConfigLoader();
        AiConfig config = loader.load("any-path");
        
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
    }
    
    @Test
    public void testDefaultProviderConfig() {
        EnvConfigLoader loader = new EnvConfigLoader();
        AiConfig config = loader.load();
        
        assertEquals("zhipuai", config.getDefaultLlmProvider());
        assertEquals("INFO", config.getLogLevel());
        assertFalse(config.isRequestLoggingEnabled());
    }
    
    @Test
    public void testDefaultLlmProvidersExist() {
        EnvConfigLoader loader = new EnvConfigLoader();
        AiConfig config = loader.load();
        
        assertNotNull(config.getLlmProvider("zhipuai"));
        assertNotNull(config.getLlmProvider("openai"));
    }
    
    @Test
    public void testFeatureConfigLoading() {
        EnvConfigLoader loader = new EnvConfigLoader();
        AiConfig config = loader.load();
        
        assertTrue(config.isFeatureEnabled("code-completion"));
        assertTrue(config.isFeatureEnabled("refactor-assistant"));
    }
    
    @Test
    public void testGetAllConfig() {
        EnvConfigLoader loader = new EnvConfigLoader();
        AiConfig config = loader.load();
        
        assertNotNull(config.getAllConfig());
        assertTrue(config.getAllConfig().containsKey("default-llm-provider"));
        assertTrue(config.getAllConfig().containsKey("llm-providers"));
        assertTrue(config.getAllConfig().containsKey("features"));
        assertTrue(config.getAllConfig().containsKey("log-level"));
        assertTrue(config.getAllConfig().containsKey("request-logging-enabled"));
    }
}
