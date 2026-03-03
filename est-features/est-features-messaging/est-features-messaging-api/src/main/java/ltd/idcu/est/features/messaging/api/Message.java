package ltd.idcu.est.features.messaging.api;

import java.util.Map;

public interface Message {
    
    String getId();
    
    String getQueue();
    
    String getTopic();
    
    Object getBody();
    
    Map<String, Object> getHeaders();
    
    long getTimestamp();
    
    int getPriority();
    
    long getExpiration();
    
    String getCorrelationId();
    
    String getReplyTo();
    
    default boolean isExpired() {
        long exp = getExpiration();
        return exp > 0 && System.currentTimeMillis() > getTimestamp() + exp;
    }
}
