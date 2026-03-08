package ltd.idcu.est.ai.impl.embedding;

import ltd.idcu.est.ai.api.vector.EmbeddingModel;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class EmbeddingModelFactory {
    
    private static final Map<String, EmbeddingModelProvider> providers = new HashMap<>();
    private static String defaultProvider = "openai";
    
    static {
        registerProvider("openai", OpenAiEmbeddingModel::new);
        registerProvider("zhipuai", ZhipuAiEmbeddingModel::new);
        registerProvider("glm", ZhipuAiEmbeddingModel::new);
        registerProvider("cohere", CohereEmbeddingModel::new);
        
        loadProvidersFromServiceLoader();
    }
    
    public static EmbeddingModel create() {
        return create(defaultProvider);
    }
    
    public static EmbeddingModel create(String providerName) {
        EmbeddingModelProvider provider = providers.get(providerName.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unknown EmbeddingModel provider: " + providerName);
        }
        return provider.create();
    }
    
    public static EmbeddingModel create(String providerName, String apiKey) {
        EmbeddingModel model = create(providerName);
        if (model instanceof AbstractEmbeddingModel) {
            ((AbstractEmbeddingModel) model).setApiKey(apiKey);
        }
        return model;
    }
    
    public static void setDefaultProvider(String providerName) {
        if (!providers.containsKey(providerName.toLowerCase())) {
            throw new IllegalArgumentException("Unknown EmbeddingModel provider: " + providerName);
        }
        defaultProvider = providerName.toLowerCase();
    }
    
    public static void registerProvider(String name, EmbeddingModelProvider provider) {
        providers.put(name.toLowerCase(), provider);
    }
    
    public static Iterable<String> getAvailableProviders() {
        return providers.keySet();
    }
    
    private static void loadProvidersFromServiceLoader() {
        ServiceLoader<EmbeddingModelProvider> loader = ServiceLoader.load(EmbeddingModelProvider.class);
        for (EmbeddingModelProvider provider : loader) {
            String name = provider.getClass().getSimpleName()
                .replace("EmbeddingModelProvider", "")
                .toLowerCase();
            providers.put(name, provider);
        }
    }
    
    @FunctionalInterface
    public interface EmbeddingModelProvider {
        EmbeddingModel create();
    }
}
