package ltd.idcu.est.llm.api;

public interface StreamCallback {
    
    void onToken(String token);
    
    void onComplete(LlmResponse response);
    
    void onError(Throwable error);
}
