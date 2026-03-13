package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.impl.config.CompositeConfigLoader;
import ltd.idcu.est.ai.impl.config.DefaultAiConfig;
import ltd.idcu.est.ai.impl.config.EnvConfigLoader;
import ltd.idcu.est.ai.impl.config.YamlConfigLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST AI Configuration Management Example");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("This example demonstrates EST AI's configuration management system:");
        System.out.println("  - YAML configuration file");
        System.out.println("  - Environment variable configuration");
        System.out.println("  - Composite configuration loading");
        System.out.println("  - LLM provider configuration");
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("Part 1: YAML Configuration");
        System.out.println("=".repeat(60));

        yamlConfigExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 2: Environment Variable Configuration");
        System.out.println("=".repeat(60));

        envConfigExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 3: Composite Configuration");
        System.out.println("=".repeat(60));

        compositeConfigExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 4: Complete AI Configuration");
        System.out.println("=".repeat(60));

        aiConfigExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Configuration management example run complete");
        System.out.println("=".repeat(60));
    }

    public static void run() {
        main(new String[]{});
    }

    private static void yamlConfigExample() {
        System.out.println("\n--- YAML Configuration Example ---");
        System.out.println("Supports loading configuration from YAML file\n");

        System.out.println("[Configuration File Example]");
        System.out.println("""
                est:
                  ai:
                    llm:
                      default: zhipuai
                      providers:
                        zhipuai:
                          enabled: true
                          api-key: your-api-key
                          model: glm-4
                          endpoint: https://open.bigmodel.cn/api/paas/v4
                        openai:
                          enabled: false
                          api-key: ${OPENAI_API_KEY}
                          model: gpt-4
                    features:
                      code-completion:
                        enabled: true
                        use-llm: true
                      refactor-assistant:
                        enabled: true
                    logging:
                      level: INFO
                      enable-request-logging: false
                """);

        System.out.println("\n[Loading YAML Configuration]");
        Path configPath = Paths.get("est-ai-config.yaml");
        YamlConfigLoader yamlLoader = new YamlConfigLoader(configPath);

        System.out.println("1. Check configuration file:");
        System.out.println("   - Configuration file path: " + configPath.toAbsolutePath());
        System.out.println("   - Configuration file exists: " + yamlLoader.exists());

        System.out.println("\n2. Get configuration values:");
        String defaultProvider = yamlLoader.getString("est.ai.llm.default", "zhipuai");
        System.out.println("   - Default LLM provider: " + defaultProvider);

        boolean codeCompletionEnabled = yamlLoader.getBoolean("est.ai.features.code-completion.enabled", true);
        System.out.println("   - Code completion enabled: " + codeCompletionEnabled);

        String logLevel = yamlLoader.getString("est.ai.logging.level", "INFO");
        System.out.println("   - Log level: " + logLevel);

        System.out.println("\n3. Nested configuration:");
        Map<String, Object> zhipuaiConfig = yamlLoader.getMap("est.ai.llm.providers.zhipuai");
        System.out.println("   - Zhipu AI configuration: " + zhipuaiConfig);

        System.out.println("\n[X] YAML configuration example complete\n");
    }

    private static void envConfigExample() {
        System.out.println("\n--- Environment Variable Configuration Example ---");
        System.out.println("Supports loading configuration from environment variables\n");

        EnvConfigLoader envLoader = new EnvConfigLoader();

        System.out.println("[Environment Variable Naming Rules]");
        System.out.println("   - Use underscore separators: EST_AI_LLM_DEFAULT");
        System.out.println("   - Corresponding configuration path: est.ai.llm.default");
        System.out.println("   - Auto case conversion");

        System.out.println("\n[Reading Environment Variables]");
        System.out.println("1. Check environment variables:");
        
        String zhipuaiKey = envLoader.getString("EST_AI_LLM_ZHIPUAI_API_KEY", "not-set");
        System.out.println("   - ZHIPUAI_API_KEY: " + (zhipuaiKey.equals("not-set") ? "Not set" : "Set"));

        String openaiKey = envLoader.getString("EST_AI_LLM_OPENAI_API_KEY", "not-set");
        System.out.println("   - OPENAI_API_KEY: " + (openaiKey.equals("not-set") ? "Not set" : "Set"));

        System.out.println("\n2. Set example environment variables (for demo):");
        System.out.println("   Windows: set EST_AI_LLM_DEFAULT=zhipuai");
        System.out.println("   Linux/Mac: export EST_AI_LLM_DEFAULT=zhipuai");

        System.out.println("\n3. Common environment variable list:");
        System.out.println("   - EST_AI_LLM_DEFAULT: Default LLM provider");
        System.out.println("   - EST_AI_LLM_ZHIPUAI_API_KEY: Zhipu AI API Key");
        System.out.println("   - EST_AI_LLM_OPENAI_API_KEY: OpenAI API Key");
        System.out.println("   - EST_AI_LLM_QWEN_API_KEY: Qwen API Key");
        System.out.println("   - EST_AI_LLM_ERNIE_API_KEY: Ernie API Key");
        System.out.println("   - EST_AI_LLM_DOUBAO_API_KEY: Doubao API Key");
        System.out.println("   - EST_AI_LLM_KIMI_API_KEY: Kimi API Key");
        System.out.println("   - EST_AI_LOGGING_LEVEL: Log level");

        System.out.println("\n[X] Environment variable configuration example complete\n");
    }

    private static void compositeConfigExample() {
        System.out.println("\n--- Composite Configuration Example ---");
        System.out.println("Supports composite loading of multiple configuration sources\n");

        System.out.println("[Configuration Priority]");
        System.out.println("   1. Environment variables (highest priority)");
        System.out.println("   2. YAML configuration file");
        System.out.println("   3. Default configuration (lowest priority)");

        System.out.println("\n[Using Composite Configuration Loader]");
        Path configPath = Paths.get("est-ai-config.yaml");
        CompositeConfigLoader compositeLoader = new CompositeConfigLoader(
                new YamlConfigLoader(configPath),
                new EnvConfigLoader()
        );

        System.out.println("1. Get configuration by priority:");
        String defaultProvider = compositeLoader.getString("est.ai.llm.default", "zhipuai");
        System.out.println("   - Default LLM provider: " + defaultProvider);

        boolean codeCompletionEnabled = compositeLoader.getBoolean(
                "est.ai.features.code-completion.enabled", true);
        System.out.println("   - Code completion enabled: " + codeCompletionEnabled);

        System.out.println("\n2. Configuration source explanation:");
        System.out.println("   - If environment variable is set, prioritize using environment variable");
        System.out.println("   - Otherwise use YAML configuration file");
        System.out.println("   - If neither exists, use default value");

        System.out.println("\n3. Actual usage scenarios:");
        System.out.println("   - Development environment: Use YAML configuration file");
        System.out.println("   - Production environment: Use environment variables (more secure)");
        System.out.println("   - CI/CD: Inject configuration through environment variables");

        System.out.println("\n[X] Composite configuration example complete\n");
    }

    private static void aiConfigExample() {
        System.out.println("\n--- Complete AI Configuration Example ---");
        System.out.println("Use DefaultAiConfig for complete configuration\n");

        DefaultAiConfig aiConfig = DefaultAiConfig.getDefault();

        System.out.println("[Default Configuration]");
        System.out.println("1. LLM configuration:");
        System.out.println("   - Default provider: " + aiConfig.getDefaultLlmProvider());
        System.out.println("   - Available providers: " + aiConfig.getAvailableLlmProviders());

        System.out.println("\n2. Feature switches:");
        System.out.println("   - Code completion: " + aiConfig.isCodeCompletionEnabled());
        System.out.println("   - Refactor assistant: " + aiConfig.isRefactorAssistantEnabled());
        System.out.println("   - Architecture advisor: " + aiConfig.isArchitectureAdvisorEnabled());

        System.out.println("\n3. Logging configuration:");
        System.out.println("   - Log level: " + aiConfig.getLogLevel());
        System.out.println("   - Request logging: " + aiConfig.isRequestLoggingEnabled());

        System.out.println("\n[Custom Configuration]");
        System.out.println("1. Load from file:");
        Path configPath = Paths.get("est-ai-config.yaml");
        DefaultAiConfig customConfig = DefaultAiConfig.loadFromFile(configPath);
        System.out.println("   - Configuration loaded from file");

        System.out.println("\n2. Load from environment variables:");
        DefaultAiConfig envConfig = DefaultAiConfig.loadFromEnv();
        System.out.println("   - Configuration loaded from environment variables");

        System.out.println("\n3. Composite loading:");
        DefaultAiConfig combinedConfig = DefaultAiConfig.load();
        System.out.println("   - Configuration composite loaded (file + environment variables)");

        System.out.println("\n[Getting LLM Provider Configuration]");
        System.out.println("1. Zhipu AI configuration:");
        var zhipuaiConfig = aiConfig.getLlmProviderConfig("zhipuai");
        System.out.println("   - Enabled: " + zhipuaiConfig.isEnabled());
        System.out.println("   - Model: " + zhipuaiConfig.getModel());
        System.out.println("   - Endpoint: " + zhipuaiConfig.getEndpoint());

        System.out.println("\n2. OpenAI configuration:");
        var openaiConfig = aiConfig.getLlmProviderConfig("openai");
        System.out.println("   - Enabled: " + openaiConfig.isEnabled());
        System.out.println("   - Model: " + openaiConfig.getModel());

        System.out.println("\n[Configuration File Template]");
        System.out.println("""
                # est-ai-config.yaml
                est:
                  ai:
                    llm:
                      default: zhipuai
                      providers:
                        zhipuai:
                          enabled: true
                          api-key: ${ZHIPUAI_API_KEY}
                          model: glm-4
                          endpoint: https://open.bigmodel.cn/api/paas/v4
                        openai:
                          enabled: false
                          api-key: ${OPENAI_API_KEY}
                          model: gpt-4
                        qwen:
                          enabled: false
                          api-key: ${QWEN_API_KEY}
                          model: qwen-max
                        ernie:
                          enabled: false
                          api-key: ${ERNIE_API_KEY}
                          model: ernie-4.0
                        doubao:
                          enabled: false
                          api-key: ${DOUBAO_API_KEY}
                          model: doubao-pro
                        kimi:
                          enabled: false
                          api-key: ${KIMI_API_KEY}
                          model: moonshot-v1
                        ollama:
                          enabled: false
                          endpoint: http://localhost:11434
                          model: llama2
                    features:
                      code-completion:
                        enabled: true
                        use-llm: true
                      refactor-assistant:
                        enabled: true
                      architecture-advisor:
                        enabled: true
                      requirement-parser:
                        enabled: true
                      architecture-designer:
                        enabled: true
                      test-deploy-manager:
                        enabled: true
                    storage:
                      type: file
                      directory: ./ai-storage
                    logging:
                      level: INFO
                      enable-request-logging: false
                      enable-token-logging: false
                """);

        System.out.println("\n[X] Complete AI configuration example complete\n");
    }
}
