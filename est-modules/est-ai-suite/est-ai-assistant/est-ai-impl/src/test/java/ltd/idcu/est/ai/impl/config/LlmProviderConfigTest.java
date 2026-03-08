package ltd.idcu.est.ai.api.config;

import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;

public class LlmProviderConfigTest {
    
    @Test
    public void testDefaultValues() {
        LlmProviderConfig config = new LlmProviderConfig();
        
        assertTrue(config.isEnabled());
        assertEquals(30000, config.getTimeout());
        assertEquals(3, config.getMaxRetries());
    }
    
    @Test
    public void testSetEnabled() {
        LlmProviderConfig config = new LlmProviderConfig();
        config.setEnabled(false);
        
        assertFalse(config.isEnabled());
    }
    
    @Test
    public void testFluentApiEnabled() {
        LlmProviderConfig config = new LlmProviderConfig()
                .enabled(false);
        
        assertFalse(config.isEnabled());
    }
    
    @Test
    public void testSetApiKey() {
        LlmProviderConfig config = new LlmProviderConfig();
        config.setApiKey("test-api-key");
        
        assertEquals("test-api-key", config.getApiKey());
    }
    
    @Test
    public void testFluentApiApiKey() {
        LlmProviderConfig config = new LlmProviderConfig()
                .apiKey("fluent-api-key");
        
        assertEquals("fluent-api-key", config.getApiKey());
    }
    
    @Test
    public void testSetModel() {
        LlmProviderConfig config = new LlmProviderConfig();
        config.setModel("gpt-4");
        
        assertEquals("gpt-4", config.getModel());
    }
    
    @Test
    public void testFluentApiModel() {
        LlmProviderConfig config = new LlmProviderConfig()
                .model("llama-2");
        
        assertEquals("llama-2", config.getModel());
    }
    
    @Test
    public void testSetEndpoint() {
        LlmProviderConfig config = new LlmProviderConfig();
        config.setEndpoint("https://api.example.com");
        
        assertEquals("https://api.example.com", config.getEndpoint());
    }
    
    @Test
    public void testFluentApiEndpoint() {
        LlmProviderConfig config = new LlmProviderConfig()
                .endpoint("https://test.endpoint");
        
        assertEquals("https://test.endpoint", config.getEndpoint());
    }
    
    @Test
    public void testSetTimeout() {
        LlmProviderConfig config = new LlmProviderConfig();
        config.setTimeout(60);
        
        assertEquals(60, config.getTimeout());
    }
    
    @Test
    public void testFluentApiTimeout() {
        LlmProviderConfig config = new LlmProviderConfig()
                .timeout(120);
        
        assertEquals(120, config.getTimeout());
    }
    
    @Test
    public void testSetMaxRetries() {
        LlmProviderConfig config = new LlmProviderConfig();
        config.setMaxRetries(5);
        
        assertEquals(5, config.getMaxRetries());
    }
    
    @Test
    public void testFluentApiMaxRetries() {
        LlmProviderConfig config = new LlmProviderConfig()
                .maxRetries(10);
        
        assertEquals(10, config.getMaxRetries());
    }
    
    @Test
    public void testSetExtra() {
        LlmProviderConfig config = new LlmProviderConfig();
        Map<String, Object> extra = Map.of(
                "param1", "value1",
                "param2", 123
        );
        
        config.setExtra(extra);
        
        assertNotNull(config.getExtra());
        assertEquals("value1", config.getExtra().get("param1"));
        assertEquals(123, config.getExtra().get("param2"));
    }
    
    @Test
    public void testFluentApiExtra() {
        LlmProviderConfig config = new LlmProviderConfig()
                .extra(Map.of("custom", "value"));
        
        assertNotNull(config.getExtra());
        assertEquals("value", config.getExtra().get("custom"));
    }
    
    @Test
    public void testFullFluentApi() {
        LlmProviderConfig config = new LlmProviderConfig()
                .enabled(true)
                .apiKey("full-api-key")
                .model("full-model")
                .endpoint("https://full.endpoint")
                .timeout(90)
                .maxRetries(7)
                .extra(Map.of("key", "val"));
        
        assertTrue(config.isEnabled());
        assertEquals("full-api-key", config.getApiKey());
        assertEquals("full-model", config.getModel());
        assertEquals("https://full.endpoint", config.getEndpoint());
        assertEquals(90, config.getTimeout());
        assertEquals(7, config.getMaxRetries());
        assertNotNull(config.getExtra());
    }
    
    @Test
    public void testNullValues() {
        LlmProviderConfig config = new LlmProviderConfig();
        
        assertNull(config.getApiKey());
        assertNull(config.getModel());
        assertNull(config.getEndpoint());
        assertNull(config.getExtra());
    }
}
