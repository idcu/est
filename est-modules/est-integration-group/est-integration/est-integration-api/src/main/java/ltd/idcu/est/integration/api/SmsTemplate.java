package ltd.idcu.est.integration.api;

public interface SmsTemplate {
    
    String getCode();
    
    String getName();
    
    String getContent();
    
    String getProvider();
    
    long getCreatedAt();
    
    long getUpdatedAt();
}
