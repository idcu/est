package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public interface LlmClient {
    
    String generate(String prompt);
    
    String generate(String prompt, LlmOptions options);
    
    LlmResponse chat(List<LlmMessage> messages);
    
    LlmResponse chat(List<LlmMessage> messages, LlmOptions options);
    
    String getName();
    
    String getModel();
    
    void setApiKey(String apiKey);
    
    void setEndpoint(String endpoint);
    
    boolean isAvailable();
}
