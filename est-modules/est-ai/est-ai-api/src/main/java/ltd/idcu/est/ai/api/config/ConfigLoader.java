package ltd.idcu.est.ai.api.config;

public interface ConfigLoader {
    
    AiConfig load();
    
    AiConfig load(String path);
    
    boolean supports(String format);
}
