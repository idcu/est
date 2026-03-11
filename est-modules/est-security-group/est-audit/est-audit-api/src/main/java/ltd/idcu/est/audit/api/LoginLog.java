package ltd.idcu.est.audit.api;

public interface LoginLog {
    
    String getId();
    
    String getUserId();
    
    String getUsername();
    
    String getTenantId();
    
    String getLoginType();
    
    String getIp();
    
    String getUserAgent();
    
    String getLocation();
    
    String getRequestId();
    
    String getSessionId();
    
    int getStatus();
    
    String getErrorMsg();
    
    String getStackTrace();
    
    long getCreatedAt();
}
