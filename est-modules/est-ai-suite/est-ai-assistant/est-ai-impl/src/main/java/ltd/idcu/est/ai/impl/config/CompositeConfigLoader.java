package ltd.idcu.est.ai.impl.config;

import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.ConfigLoader;
import ltd.idcu.est.ai.api.config.LlmProviderConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeConfigLoader implements ConfigLoader {
    
    private List<ConfigLoader> loaders;
    
    public CompositeConfigLoader() {
        this.loaders = new ArrayList<>();
        this.loaders.add(new YamlConfigLoader());
        this.loaders.add(new EnvConfigLoader());
    }
    
    public CompositeConfigLoader(List<ConfigLoader> loaders) {
        this.loaders = new ArrayList<>(loaders);
    }
    
    public void addLoader(ConfigLoader loader) {
        this.loaders.add(loader);
    }
    
    public void addLoader(int index, ConfigLoader loader) {
        this.loaders.add(index, loader);
    }
    
    public void removeLoader(ConfigLoader loader) {
        this.loaders.remove(loader);
    }
    
    public List<ConfigLoader> getLoaders() {
        return new ArrayList<>(loaders);
    }
    
    @Override
    public AiConfig load() {
        DefaultAiConfig mergedConfig = new DefaultAiConfig();
        
        for (ConfigLoader loader : loaders) {
            try {
                AiConfig config = loader.load();
                mergeConfig(mergedConfig, config);
            } catch (Exception e) {
            }
        }
        
        return mergedConfig;
    }
    
    @Override
    public AiConfig load(String path) {
        DefaultAiConfig mergedConfig = new DefaultAiConfig();
        
        for (ConfigLoader loader : loaders) {
            try {
                AiConfig config = loader.load(path);
                mergeConfig(mergedConfig, config);
            } catch (Exception e) {
            }
        }
        
        return mergedConfig;
    }
    
    @Override
    public boolean supports(String format) {
        for (ConfigLoader loader : loaders) {
            if (loader.supports(format)) {
                return true;
            }
        }
        return false;
    }
    
    private void mergeConfig(DefaultAiConfig target, AiConfig source) {
        if (source.getDefaultLlmProvider() != null) {
            target.setDefaultLlmProvider(source.getDefaultLlmProvider());
        }
        
        Map<String, LlmProviderConfig> providers = source.getLlmProviders();
        if (providers != null) {
            for (Map.Entry<String, LlmProviderConfig> entry : providers.entrySet()) {
                target.addLlmProvider(entry.getKey(), entry.getValue());
            }
        }
        
        if (source.getLogLevel() != null) {
            target.setLogLevel(source.getLogLevel());
        }
        
        target.setRequestLoggingEnabled(source.isRequestLoggingEnabled());
        
        Map<String, Object> allConfig = source.getAllConfig();
        if (allConfig != null) {
            for (Map.Entry<String, Object> entry : allConfig.entrySet()) {
                target.getAllConfig().put(entry.getKey(), entry.getValue());
            }
        }
    }
}
