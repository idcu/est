package ltd.idcu.est.llm;

import ltd.idcu.est.llm.api.LlmClient;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class LlmClientFactory {

    private static final Map<String, LlmClientProvider> providers = new HashMap<>();
    private static String defaultProvider = "zhipuai";

    static {
        registerProvider("zhipuai", ZhipuAiLlmClient::new);
        registerProvider("openai", OpenAiLlmClient::new);
        registerProvider("qwen", QwenLlmClient::new);
        registerProvider("ernie", ErnieLlmClient::new);
        registerProvider("doubao", DoubaoLlmClient::new);
        registerProvider("kimi", KimiLlmClient::new);
        registerProvider("ollama", OllamaLlmClient::new);
        
        loadProvidersFromServiceLoader();
    }

    public static LlmClient create() {
        return create(defaultProvider);
    }

    public static LlmClient create(String providerName) {
        LlmClientProvider provider = providers.get(providerName.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unknown LLM provider: " + providerName);
        }
        return provider.create();
    }

    public static LlmClient create(String providerName, String apiKey) {
        LlmClient client = create(providerName);
        client.setApiKey(apiKey);
        return client;
    }

    public static void setDefaultProvider(String providerName) {
        if (!providers.containsKey(providerName.toLowerCase())) {
            throw new IllegalArgumentException("Unknown LLM provider: " + providerName);
        }
        defaultProvider = providerName.toLowerCase();
    }

    public static void registerProvider(String name, LlmClientProvider provider) {
        providers.put(name.toLowerCase(), provider);
    }

    public static Iterable<String> getAvailableProviders() {
        return providers.keySet();
    }

    private static void loadProvidersFromServiceLoader() {
        ServiceLoader<LlmClientProvider> loader = ServiceLoader.load(LlmClientProvider.class);
        for (LlmClientProvider provider : loader) {
            String name = provider.getClass().getSimpleName()
                .replace("LlmClientProvider", "")
                .toLowerCase();
            providers.put(name, provider);
        }
    }

    @FunctionalInterface
    public interface LlmClientProvider {
        LlmClient create();
    }
}
