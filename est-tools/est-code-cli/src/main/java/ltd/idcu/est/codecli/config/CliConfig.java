package ltd.idcu.est.codecli.config;

import ltd.idcu.est.utils.format.yaml.YamlUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CliConfig {
    
    private static final String DEFAULT_CONFIG_FILE = "est-code-cli.yml";
    private static final String USER_CONFIG_FILE = System.getProperty("user.home") + "/.est/est-code-cli.yml";
    
    private String nickname;
    private String workDir;
    private String chatModelApiUrl;
    private String chatModelApiKey;
    private String chatModelName;
    private boolean planningMode;
    private boolean hitlEnabled;
    
    public CliConfig() {
        this.nickname = "EST";
        this.workDir = System.getProperty("user.dir");
        this.planningMode = true;
        this.hitlEnabled = true;
    }
    
    public static CliConfig load() {
        CliConfig config = new CliConfig();
        
        Path userConfigPath = Paths.get(USER_CONFIG_FILE);
        if (Files.exists(userConfigPath)) {
            config.loadFromFile(userConfigPath);
        }
        
        Path localConfigPath = Paths.get(DEFAULT_CONFIG_FILE);
        if (Files.exists(localConfigPath)) {
            config.loadFromFile(localConfigPath);
        }
        
        return config;
    }
    
    private void loadFromFile(Path configPath) {
        try {
            String yamlContent = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
            Map<String, Object> data = YamlUtils.parse(yamlContent);
            
            Map<String, Object> solon = YamlUtils.getMap(data, "solon");
            if (solon != null) {
                Map<String, Object> code = YamlUtils.getMap(solon, "code");
                if (code != null) {
                    Map<String, Object> cli = YamlUtils.getMap(code, "cli");
                    if (cli != null) {
                        this.nickname = YamlUtils.getString(cli, "nickname", this.nickname);
                        this.workDir = YamlUtils.getString(cli, "workDir", this.workDir);
                        this.planningMode = YamlUtils.getBoolean(cli, "planningMode", this.planningMode);
                        this.hitlEnabled = YamlUtils.getBoolean(cli, "hitlEnabled", this.hitlEnabled);
                        
                        Map<String, Object> chatModel = YamlUtils.getMap(cli, "chatModel");
                        if (chatModel != null) {
                            this.chatModelApiUrl = YamlUtils.getString(chatModel, "apiUrl");
                            this.chatModelApiKey = YamlUtils.getString(chatModel, "apiKey");
                            this.chatModelName = YamlUtils.getString(chatModel, "model");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load config from " + configPath + ": " + e.getMessage());
        }
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getWorkDir() {
        return workDir;
    }
    
    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }
    
    public String getChatModelApiUrl() {
        return chatModelApiUrl;
    }
    
    public void setChatModelApiUrl(String chatModelApiUrl) {
        this.chatModelApiUrl = chatModelApiUrl;
    }
    
    public String getChatModelApiKey() {
        return chatModelApiKey;
    }
    
    public void setChatModelApiKey(String chatModelApiKey) {
        this.chatModelApiKey = chatModelApiKey;
    }
    
    public String getChatModelName() {
        return chatModelName;
    }
    
    public void setChatModelName(String chatModelName) {
        this.chatModelName = chatModelName;
    }
    
    public boolean isPlanningMode() {
        return planningMode;
    }
    
    public void setPlanningMode(boolean planningMode) {
        this.planningMode = planningMode;
    }
    
    public boolean isHitlEnabled() {
        return hitlEnabled;
    }
    
    public void setHitlEnabled(boolean hitlEnabled) {
        this.hitlEnabled = hitlEnabled;
    }
}
