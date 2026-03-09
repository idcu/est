package ltd.idcu.est.multitenancy.api;

public interface TenantAuditLog {
    
    String getId();
    
    String getTenantId();
    
    String getTenantCode();
    
    String getUserId();
    
    String getOperation();
    
    String getResourceType();
    
    String getResourceId();
    
    String getDetails();
    
    String getIpAddress();
    
    String getUserAgent();
    
    long getTimestamp();
    
    boolean isSuccess();
    
    String getErrorMessage();
}
