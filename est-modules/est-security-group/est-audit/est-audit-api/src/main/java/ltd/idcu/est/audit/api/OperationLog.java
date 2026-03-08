package ltd.idcu.est.audit.api;

public interface OperationLog {
    
    String getId();
    
    String getUserId();
    
    String getUsername();
    
    String getModule();
    
    String getOperation();
    
    String getMethod();
    
    String getParams();
    
    Long getTime();
    
    String getIp();
    
    String getUserAgent();
    
    int getStatus();
    
    String getErrorMsg();
    
    long getCreatedAt();
}
