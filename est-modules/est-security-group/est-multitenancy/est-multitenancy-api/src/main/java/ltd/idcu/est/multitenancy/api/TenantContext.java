package ltd.idcu.est.multitenancy.api;

import ltd.idcu.est.rbac.api.Tenant;

public interface TenantContext {
    
    Tenant getCurrentTenant();
    
    void setCurrentTenant(Tenant tenant);
    
    void clearCurrentTenant();
    
    boolean hasCurrentTenant();
    
    String getCurrentTenantId();
    
    String getCurrentTenantCode();
    
    Tenant.TenantMode getCurrentTenantMode();
}
