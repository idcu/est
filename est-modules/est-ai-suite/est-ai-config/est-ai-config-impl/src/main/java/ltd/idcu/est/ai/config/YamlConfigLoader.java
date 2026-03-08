package ltd.idcu.est.ai.config;

import ltd.idcu.est.ai.config.api.AiConfig;
import ltd.idcu.est.ai.config.api.ConfigLoader;
import ltd.idcu.est.ai.config.api.LlmProviderConfig;
import ltd.idcu.est.utils.format.yaml.YamlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlConfigLoader implements ConfigLoader {
    
    private static final String DEFAULT_CONFIG_PATH = "est-ai-config.yml";
    private static final String[] CONFIG_PATHS = {
        DEFAULT_CONFIG_PATH,
        "config/est-ai-config.yml",
        "src/main/resources/est-ai-config.yml"
    };
    
    @Override
    public AiConfig load() {
        for (String path : CONFIG_PATHS) {
            try {
                return loadFromFile(path);
            } catch (Exception e) {
            }
        }
        return loadFromResource(DEFAULT_CONFIG_PATH);
    }
    
    @Override
    public AiConfig load(String path) {
        try {
            return loadFromFile(path);
        } catch (Exception e) {
            return loadFromResource(path);
        }
    }
    
    @Override
    public boolean supports(String format) {
        return "yaml".equalsIgnoreCase(format) || "yml".equalsIgnoreCase(format);
    }
    
    private AiConfig loadFromFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            String yamlContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            return parseYaml(yamlContent);
        }
        throw new IOException("File not found: " + path);
    }
    
    private AiConfig loadFromResource(String path) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return parseYaml(sb.toString());
            }
        } catch (IOException e) {
        }
        return new DefaultAiConfig();
    }
    
    @SuppressWarnings("unchecked")
    private AiConfig parseYaml(String yamlContent) {
        DefaultAiConfig config = new DefaultAiConfig();
        Map<String, Object> yamlMap = YamlUtils.parse(yamlContent);
        
        if (yamlMap.containsKey("default-llm-provider")) {
            config.setDefaultLlmProvider(YamlUtils.getString(yamlMap, "default-llm-provider"));
        }
        
        if (yamlMap.containsKey("log-level")) {
            config.setLogLevel(YamlUtils.getString(yamlMap, "log-level"));
        }
        
        if (yamlMap.containsKey("request-logging-enabled")) {
            config.setRequestLoggingEnabled(YamlUtils.getBoolean(yamlMap, "request-logging-enabled", false));
        }
        
        if (yamlMap.containsKey("llm-providers")) {
            Map<String, Object> providersMap = YamlUtils.getMap(yamlMap, "llm-providers");
            if (providersMap != null) {
                for (Map.Entry<String, Object> entry : providersMap.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        LlmProviderConfig providerConfig = parseProviderConfig((Map<String, Object>) entry.getValue());
                        config.addLlmProvider(entry.getKey(), providerConfig);
                    }
                }
            }
        }
        
        if (yamlMap.containsKey("features")) {
            Map<String, Object> featuresMap = YamlUtils.getMap(yamlMap, "features");
            if (featuresMap != null) {
                for (Map.Entry<String, Object> entry : featuresMap.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        config.setFeatureConfig(entry.getKey(), (Map<String, Object>) entry.getValue());
                    }
                }
            }
        }
        
        Map<String, Object> allConfig = new HashMap<>(yamlMap);
        allConfig.remove("default-llm-provider");
        allConfig.remove("llm-providers");
        allConfig.remove("features");
        allConfig.remove("log-level");
        allConfig.remove("request-logging-enabled");
        config.setAllConfig(allConfig);
        
        return config;
    }
    
    private LlmProviderConfig parseProviderConfig(Map<String, Object> map) {
        LlmProviderConfig providerConfig = new LlmProviderConfig();
        
        if (map.containsKey("enabled")) {
            providerConfig.setEnabled(YamlUtils.getBoolean(map, "enabled", true));
        }
        
        if (map.containsKey("api-key")) {
            providerConfig.setApiKey(YamlUtils.getString(map, "api-key"));
        }
        
        if (map.containsKey("model")) {
            providerConfig.setModel(YamlUtils.getString(map, "model"));
        }
        
        if (map.containsKey("endpoint")) {
            providerConfig.setEndpoint(YamlUtils.getString(map, "endpoint"));
        }
        
        if (map.containsKey("timeout")) {
            providerConfig.setTimeout(YamlUtils.getInt(map, "timeout", 30));
        }
        
        if (map.containsKey("max-retries")) {
            providerConfig.setMaxRetries(YamlUtils.getInt(map, "max-retries", 3));
        }
        
        if (map.containsKey("extra")) {
            Map<String, Object> extra = YamlUtils.getMap(map, "extra");
            if (extra != null) {
                providerConfig.setExtra(extra);
            }
        }
        
        return providerConfig;
    }
}
