package ltd.idcu.est.ai.api;

public interface StreamCallback {
    
    void onToken(String token);
    
    default void onToken(String token, int index) {
        onToken(token);
    }
    
    void onComplete(LlmResponse response);
    
    void onError(Throwable error);
    
    default void onProgress(int current, int total) {
    }
    
    default void onFinishReason(String finishReason) {
    }
    
    default boolean shouldCancel() {
        return false;
    }
}
