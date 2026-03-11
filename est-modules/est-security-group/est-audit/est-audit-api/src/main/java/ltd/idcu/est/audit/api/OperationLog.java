package ltd.idcu.est.audit.api;

public interface OperationLog {
    
    String getId();
    
    String getUserId();
    
    String getUsername();
    
    String getTenantId();
    
    String getModule();
    
    String getOperation();
    
    String getResource();
    
    String getResourceId();
    
    String getDescription();
    
    String getMethod();
    
    String getParams();
    
    String getResult();
    
    Long getTime();
    
    Long getExecutionTime();
    
    String getIp();
    
    String getUserAgent();
    
    String getLocation();
    
    String getRequestId();
    
    int getStatus();
    
    String getErrorMsg();
    
    String getStackTrace();
    
    long getCreatedAt();
}
