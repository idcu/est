package ltd.idcu.est.admin.api;

public interface LoginLog {
    
    String getId();
    
    String getUserId();
    
    String getUsername();
    
    String getIp();
    
    String getUserAgent();
    
    int getStatus();
    
    String getErrorMsg();
    
    long getCreatedAt();
}
