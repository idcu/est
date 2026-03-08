package ltd.idcu.est.ai.config.api;

public interface ConfigLoader {
    
    AiConfig load();
    
    AiConfig load(String path);
    
    boolean supports(String format);
}
