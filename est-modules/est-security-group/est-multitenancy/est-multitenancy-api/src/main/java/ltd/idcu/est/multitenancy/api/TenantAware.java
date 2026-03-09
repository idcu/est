package ltd.idcu.est.multitenancy.api;

public interface TenantAware {
    
    String getTenantId();
    
    void setTenantId(String tenantId);
    
    String getTenantCode();
    
    void setTenantCode(String tenantCode);
}
