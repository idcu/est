package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.ConfigLoader;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class CompositeConfigLoaderTest {
    
    @Test
    public void testSupportsAllFormats() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        assertTrue(loader.supports("yaml"));
        assertTrue(loader.supports("yml"));
        assertTrue(loader.supports("env"));
    }
    
    @Test
    public void testDefaultLoadersAdded() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        List<ConfigLoader> loaders = loader.getLoaders();
        
        assertNotNull(loaders);
        assertEquals(2, loaders.size());
    }
    
    @Test
    public void testAddLoader() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        int initialSize = loader.getLoaders().size();
        
        ConfigLoader testLoader = new ConfigLoader() {
            @Override
            public AiConfig load() {
                return new DefaultAiConfig();
            }
            
            @Override
            public AiConfig load(String path) {
                return load();
            }
            
            @Override
            public boolean supports(String format) {
                return "test".equals(format);
            }
        };
        
        loader.addLoader(testLoader);
        
        assertEquals(initialSize + 1, loader.getLoaders().size());
        assertTrue(loader.supports("test"));
    }
    
    @Test
    public void testAddLoaderAtIndex() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        ConfigLoader testLoader = new ConfigLoader() {
            @Override
            public AiConfig load() {
                return new DefaultAiConfig();
            }
            
            @Override
            public AiConfig load(String path) {
                return load();
            }
            
            @Override
            public boolean supports(String format) {
                return "test".equals(format);
            }
        };
        
        loader.addLoader(0, testLoader);
        
        List<ConfigLoader> loaders = loader.getLoaders();
        assertEquals(testLoader, loaders.get(0));
    }
    
    @Test
    public void testRemoveLoader() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        int initialSize = loader.getLoaders().size();
        ConfigLoader firstLoader = loader.getLoaders().get(0);
        
        loader.removeLoader(firstLoader);
        
        assertEquals(initialSize - 1, loader.getLoaders().size());
    }
    
    @Test
    public void testGetLoadersReturnsCopy() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        List<ConfigLoader> loaders1 = loader.getLoaders();
        List<ConfigLoader> loaders2 = loader.getLoaders();
        
        assertNotSame(loaders1, loaders2);
    }
    
    @Test
    public void testLoadConfig() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        AiConfig config = loader.load();
        
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
        assertNotNull(config.getLlmProviders());
    }
    
    @Test
    public void testLoadConfigWithPath() {
        CompositeConfigLoader loader = new CompositeConfigLoader();
        AiConfig config = loader.load("any-path");
        
        assertNotNull(config);
        assertNotNull(config.getDefaultLlmProvider());
    }
    
    @Test
    public void testConstructorWithLoaders() {
        ConfigLoader customLoader = new ConfigLoader() {
            @Override
            public AiConfig load() {
                return new DefaultAiConfig().defaultLlmProvider("custom");
            }
            
            @Override
            public AiConfig load(String path) {
                return load();
            }
            
            @Override
            public boolean supports(String format) {
                return "custom".equals(format);
            }
        };
        
        CompositeConfigLoader loader = new CompositeConfigLoader(List.of(customLoader));
        
        assertEquals(1, loader.getLoaders().size());
        assertTrue(loader.supports("custom"));
    }
}
