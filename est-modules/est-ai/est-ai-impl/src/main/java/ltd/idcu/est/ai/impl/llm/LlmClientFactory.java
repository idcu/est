package ltd.idcu.est.ai.impl.llm;

import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.LlmProviderConfig;

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
        registerProvider("anthropic", AnthropicLlmClient::new);
        registerProvider("claude", AnthropicLlmClient::new);
        registerProvider("gemini", GeminiLlmClient::new);
        registerProvider("google", GeminiLlmClient::new);
        registerProvider("mistral", MistralLlmClient::new);
        
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
    
    public static LlmClient create(AiConfig config) {
        String providerName = config.getDefaultLlmProvider();
        LlmProviderConfig providerConfig = config.getLlmProvider(providerName);
        LlmClient client = create(providerName);
        
        if (providerConfig != null) {
            if (providerConfig.getApiKey() != null) {
                client.setApiKey(providerConfig.getApiKey());
            }
            if (providerConfig.getEndpoint() != null) {
                client.setEndpoint(providerConfig.getEndpoint());
            }
            if (providerConfig.getModel() != null) {
                client.setModel(providerConfig.getModel());
            }
        }
        
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
