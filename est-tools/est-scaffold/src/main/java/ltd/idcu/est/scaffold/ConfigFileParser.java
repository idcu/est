package ltd.idcu.est.scaffold;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileParser {

    public static final String CONFIG_FILE_NAME = ".est-scaffold.yml";

    public static Map<String, String> loadConfig() throws IOException {
        return loadConfig(Paths.get(System.getProperty("user.home"), CONFIG_FILE_NAME));
    }

    public static Map<String, String> loadConfig(Path configPath) throws IOException {
        Map<String, String> config = new HashMap<>();
        
        if (!Files.exists(configPath)) {
            return config;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            String line;
            String currentSection = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                if (line.endsWith(":")) {
                    currentSection = line.substring(0, line.length() - 1).trim();
                    continue;
                }
                
                if (currentSection != null && line.startsWith("- ")) {
                    continue;
                }
                
                if (line.contains(":")) {
                    int colonIndex = line.indexOf(":");
                    String key = line.substring(0, colonIndex).trim();
                    String value = line.substring(colonIndex + 1).trim();
                    
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    } else if (value.startsWith("'") && value.endsWith("'")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    String fullKey = currentSection != null ? currentSection + "." + key : key;
                    config.put(fullKey, value);
                }
            }
        }
        
        return config;
    }

    public static Map<String, String> getDefaultConfig(Map<String, String> config) {
        Map<String, String> defaults = new HashMap<>();
        
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("default.")) {
                String subKey = key.substring("default.".length());
                defaults.put(subKey, entry.getValue());
            }
        }
        
        return defaults;
    }

    public static Map<String, String> getPreset(Map<String, String> config, String presetName) {
        Map<String, String> preset = new HashMap<>();
        String prefix = "presets." + presetName + ".";
        
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(prefix)) {
                String subKey = key.substring(prefix.length());
                preset.put(subKey, entry.getValue());
            }
        }
        
        return preset;
    }
}
