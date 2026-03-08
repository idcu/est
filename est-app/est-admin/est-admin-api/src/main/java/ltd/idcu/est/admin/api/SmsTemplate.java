package ltd.idcu.est.admin.api;

public interface SmsTemplate {
    
    String getCode();
    
    String getName();
    
    String getContent();
    
    String getProvider();
    
    long getCreatedAt();
    
    long getUpdatedAt();
}
