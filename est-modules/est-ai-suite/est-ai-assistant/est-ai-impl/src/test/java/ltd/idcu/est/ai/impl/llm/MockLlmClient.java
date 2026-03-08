package ltd.idcu.est.ai.impl.llm;

import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.LlmOptions;
import ltd.idcu.est.ai.api.LlmResponse;
import ltd.idcu.est.ai.api.StreamCallback;
import ltd.idcu.est.ai.api.FunctionRegistry;
import ltd.idcu.est.ai.api.FunctionTool;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MockLlmClient implements LlmClient {
    
    private String apiKey;
    private String model;
    private String endpoint;
    private LlmResponse mockResponse;
    private boolean available;
    private List<List<LlmMessage>> receivedMessages;
    private FunctionRegistry functionRegistry;
    private List<String> streamedTokens;
    private long streamDelayMs;
    
    public MockLlmClient() {
        this.available = true;
        this.receivedMessages = new ArrayList<>();
        this.mockResponse = LlmResponse.success("Mock response");
        this.streamedTokens = new ArrayList<>();
        this.streamDelayMs = 50;
    }
    
    public void setMockResponse(LlmResponse response) {
        this.mockResponse = response;
    }
    
    public void setMockResponse(String content) {
        this.mockResponse = LlmResponse.success(content);
    }
    
    public void setErrorResponse(String errorMessage) {
        this.mockResponse = LlmResponse.error(errorMessage);
    }
    
    public void setStreamedTokens(List<String> tokens) {
        this.streamedTokens = new ArrayList<>(tokens);
    }
    
    public void setStreamDelayMs(long delayMs) {
        this.streamDelayMs = delayMs;
    }
    
    public List<List<LlmMessage>> getReceivedMessages() {
        return receivedMessages;
    }
    
    public void clearReceivedMessages() {
        receivedMessages.clear();
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public String getApiKey() {
        return apiKey;
    }
    
    @Override
    public String generate(String prompt) {
        return mockResponse.getContent();
    }
    
    @Override
    public String generate(String prompt, LlmOptions options) {
        return generate(prompt);
    }
    
    @Override
    public String getName() {
        return "mock";
    }
    
    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    @Override
    public String getModel() {
        return model;
    }
    
    @Override
    public void setModel(String model) {
        this.model = model;
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public LlmResponse chat(List<LlmMessage> messages) {
        receivedMessages.add(new ArrayList<>(messages));
        return mockResponse;
    }
    
    @Override
    public LlmResponse chat(List<LlmMessage> messages, LlmOptions options) {
        receivedMessages.add(new ArrayList<>(messages));
        return mockResponse;
    }
    
    @Override
    public void chatStream(List<LlmMessage> messages, StreamCallback callback) {
        chatStream(messages, new LlmOptions(), callback);
    }
    
    @Override
    public void chatStream(List<LlmMessage> messages, LlmOptions options, StreamCallback callback) {
        receivedMessages.add(new ArrayList<>(messages));
        
        new Thread(() -> {
            try {
                if (streamedTokens.isEmpty()) {
                    String content = mockResponse.getContent();
                    for (char c : content.toCharArray()) {
                        callback.onToken(String.valueOf(c));
                        Thread.sleep(streamDelayMs);
                    }
                } else {
                    for (String token : streamedTokens) {
                        callback.onToken(token);
                        Thread.sleep(streamDelayMs);
                    }
                }
                callback.onComplete(mockResponse);
            } catch (InterruptedException e) {
                callback.onError(e);
            }
        }).start();
    }
    
    @Override
    public void setFunctionRegistry(FunctionRegistry registry) {
        this.functionRegistry = registry;
    }
    
    @Override
    public FunctionRegistry getFunctionRegistry() {
        return this.functionRegistry;
    }
}
