package ltd.idcu.est.ai.config;

import ltd.idcu.est.ai.config.api.AiConfig;
import ltd.idcu.est.ai.config.api.ConfigLoader;
import ltd.idcu.est.ai.config.api.LlmProviderConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvConfigLoader implements ConfigLoader {
    
    private static final String ENV_PREFIX = "EST_AI_";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");
    
    @Override
    public AiConfig load() {
        return loadFromEnv();
    }
    
    @Override
    public AiConfig load(String path) {
        return loadFromEnv();
    }
    
    @Override
    public boolean supports(String format) {
        return "env".equalsIgnoreCase(format);
    }
    
    private AiConfig loadFromEnv() {
        DefaultAiConfig config = new DefaultAiConfig();
        
        Map<String, String> env = System.getenv();
        
        String defaultProvider = env.get(ENV_PREFIX + "DEFAULT_LLM_PROVIDER");
        if (defaultProvider != null) {
            config.setDefaultLlmProvider(defaultProvider);
        }
        
        String logLevel = env.get(ENV_PREFIX + "LOG_LEVEL");
        if (logLevel != null) {
            config.setLogLevel(logLevel);
        }
        
        String requestLogging = env.get(ENV_PREFIX + "REQUEST_LOGGING_ENABLED");
        if (requestLogging != null) {
            config.setRequestLoggingEnabled(Boolean.parseBoolean(requestLogging));
        }
        
        loadLlmProvidersFromEnv(config, env);
        
        return config;
    }
    
    private void loadLlmProvidersFromEnv(DefaultAiConfig config, Map<String, String> env) {
        String providersStr = env.get(ENV_PREFIX + "LLM_PROVIDERS");
        if (providersStr != null) {
            String[] providers = providersStr.split(",");
            for (String provider : providers) {
                provider = provider.trim();
                if (!provider.isEmpty()) {
                    loadLlmProviderFromEnv(config, provider, env);
                }
            }
        }
    }
    
    private void loadLlmProviderFromEnv(DefaultAiConfig config, String provider, Map<String, String> env) {
        LlmProviderConfig providerConfig = new LlmProviderConfig();
        
        String prefix = ENV_PREFIX + "LLM_" + provider.toUpperCase() + "_";
        
        String enabled = env.get(prefix + "ENABLED");
        if (enabled != null) {
            providerConfig.setEnabled(Boolean.parseBoolean(enabled));
        }
        
        String apiKey = env.get(prefix + "API_KEY");
        if (apiKey != null) {
            providerConfig.setApiKey(resolveEnvVar(apiKey));
        }
        
        String model = env.get(prefix + "MODEL");
        if (model != null) {
            providerConfig.setModel(model);
        }
        
        String endpoint = env.get(prefix + "ENDPOINT");
        if (endpoint != null) {
            providerConfig.setEndpoint(endpoint);
        }
        
        String timeout = env.get(prefix + "TIMEOUT");
        if (timeout != null) {
            providerConfig.setTimeout(Integer.parseInt(timeout));
        }
        
        String maxRetries = env.get(prefix + "MAX_RETRIES");
        if (maxRetries != null) {
            providerConfig.setMaxRetries(Integer.parseInt(maxRetries));
        }
        
        config.addLlmProvider(provider, providerConfig);
    }
    
    private String resolveEnvVar(String value) {
        if (value == null) {
            return null;
        }
        
        StringBuffer result = new StringBuffer();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
        
        while (matcher.find()) {
            String envVarName = matcher.group(1);
            String envVarValue = System.getenv(envVarName);
            matcher.appendReplacement(result, envVarValue != null ? envVarValue : "");
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
}
