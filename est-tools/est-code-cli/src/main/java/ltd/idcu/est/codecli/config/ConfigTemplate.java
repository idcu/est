package ltd.idcu.est.codecli.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigTemplate {

    private final String name;
    private final String description;
    private final Map<String, Object> defaultValues;

    public ConfigTemplate(String name, String description) {
        this.name = name;
        this.description = description;
        this.defaultValues = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ConfigTemplate setDefault(String key, Object value) {
        defaultValues.put(key, value);
        return this;
    }

    public Map<String, Object> getDefaultValues() {
        return new HashMap<>(defaultValues);
    }

    public void applyTo(CliConfig config) {
        if (defaultValues.containsKey("nickname")) {
            config.setNickname((String) defaultValues.get("nickname"));
        }
        if (defaultValues.containsKey("workDir")) {
            config.setWorkDir((String) defaultValues.get("workDir"));
        }
        if (defaultValues.containsKey("planningMode")) {
            config.setPlanningMode((Boolean) defaultValues.get("planningMode"));
        }
        if (defaultValues.containsKey("hitlEnabled")) {
            config.setHitlEnabled((Boolean) defaultValues.get("hitlEnabled"));
        }
        if (defaultValues.containsKey("chatModelApiUrl")) {
            config.setChatModelApiUrl((String) defaultValues.get("chatModelApiUrl"));
        }
        if (defaultValues.containsKey("chatModelApiKey")) {
            config.setChatModelApiKey((String) defaultValues.get("chatModelApiKey"));
        }
        if (defaultValues.containsKey("chatModelName")) {
            config.setChatModelName((String) defaultValues.get("chatModelName"));
        }
    }

    public static ConfigTemplate createDefault() {
        return new ConfigTemplate("default", "Default configuration")
            .setDefault("nickname", "EST")
            .setDefault("planningMode", true)
            .setDefault("hitlEnabled", true);
    }

    public static ConfigTemplate createMinimal() {
        return new ConfigTemplate("minimal", "Minimal configuration with all security disabled")
            .setDefault("nickname", "EST")
            .setDefault("planningMode", false)
            .setDefault("hitlEnabled", false);
    }

    public static ConfigTemplate createDeveloper() {
        return new ConfigTemplate("developer", "Developer-friendly configuration")
            .setDefault("nickname", "DevBot")
            .setDefault("planningMode", true)
            .setDefault("hitlEnabled", false);
    }

    public static ConfigTemplate createSecure() {
        return new ConfigTemplate("secure", "High-security configuration")
            .setDefault("nickname", "SecureBot")
            .setDefault("planningMode", true)
            .setDefault("hitlEnabled", true);
    }

    public static List<ConfigTemplate> getAllTemplates() {
        List<ConfigTemplate> templates = new ArrayList<>();
        templates.add(createDefault());
        templates.add(createMinimal());
        templates.add(createDeveloper());
        templates.add(createSecure());
        return templates;
    }

    public static ConfigTemplate getTemplate(String name) {
        for (ConfigTemplate template : getAllTemplates()) {
            if (template.getName().equalsIgnoreCase(name)) {
                return template;
            }
        }
        return null;
    }
}
