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
    
    LlmResponse chatWithImage(List<LlmMessage> messages, String imageBase64, String imageType);
    
    LlmResponse chatWithImage(List<LlmMessage> messages, String imageBase64, String imageType, LlmOptions options);
    
    byte[] textToSpeech(String text);
    
    byte[] textToSpeech(String text, String voice);
    
    String speechToText(byte[] audioBytes);
    
    String speechToText(byte[] audioBytes, String language);
    
    byte[] generateImage(String prompt);
    
    byte[] generateImage(String prompt, int width, int height);
}
