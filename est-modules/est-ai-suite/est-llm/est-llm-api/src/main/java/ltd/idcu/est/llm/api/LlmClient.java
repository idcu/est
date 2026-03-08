package ltd.idcu.est.llm.api;

import java.util.List;
import java.util.Map;

public interface LlmClient {
    
    String generate(String prompt);
    
    String generate(String prompt, LlmOptions options);
    
    LlmResponse chat(List<LlmMessage> messages);
    
    LlmResponse chat(List<LlmMessage> messages, LlmOptions options);
    
    void chatStream(List<LlmMessage> messages, StreamCallback callback);
    
    void chatStream(List<LlmMessage> messages, LlmOptions options, StreamCallback callback);
    
    void setFunctionRegistry(FunctionRegistry registry);
    
    FunctionRegistry getFunctionRegistry();
    
    String getName();
    
    String getModel();
    
    void setModel(String model);
    
    void setApiKey(String apiKey);
    
    void setEndpoint(String endpoint);
    
    boolean isAvailable();
}
