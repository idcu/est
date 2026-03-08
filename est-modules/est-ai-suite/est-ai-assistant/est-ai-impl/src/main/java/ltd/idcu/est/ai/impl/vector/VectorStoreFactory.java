package ltd.idcu.est.ai.impl.vector;

import ltd.idcu.est.ai.api.vector.VectorStore;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class VectorStoreFactory {
    
    private static final Map<String, VectorStoreProvider> providers = new HashMap<>();
    private static String defaultProvider = "inmemory";
    
    static {
        registerProvider("inmemory", InMemoryVectorStore::new);
        registerProvider("memory", InMemoryVectorStore::new);
        registerProvider("pinecone", PineconeVectorStore::new);
        registerProvider("milvus", MilvusVectorStore::new);
        registerProvider("chroma", ChromaVectorStore::new);
        
        loadProvidersFromServiceLoader();
    }
    
    public static VectorStore create() {
        return create(defaultProvider);
    }
    
    public static VectorStore create(String providerName) {
        VectorStoreProvider provider = providers.get(providerName.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unknown VectorStore provider: " + providerName);
        }
        return provider.create();
    }
    
    public static void setDefaultProvider(String providerName) {
        if (!providers.containsKey(providerName.toLowerCase())) {
            throw new IllegalArgumentException("Unknown VectorStore provider: " + providerName);
        }
        defaultProvider = providerName.toLowerCase();
    }
    
    public static void registerProvider(String name, VectorStoreProvider provider) {
        providers.put(name.toLowerCase(), provider);
    }
    
    public static Iterable<String> getAvailableProviders() {
        return providers.keySet();
    }
    
    private static void loadProvidersFromServiceLoader() {
        ServiceLoader<VectorStoreProvider> loader = ServiceLoader.load(VectorStoreProvider.class);
        for (VectorStoreProvider provider : loader) {
            String name = provider.getClass().getSimpleName()
                .replace("VectorStoreProvider", "")
                .toLowerCase();
            providers.put(name, provider);
        }
    }
    
    @FunctionalInterface
    public interface VectorStoreProvider {
        VectorStore create();
    }
}
