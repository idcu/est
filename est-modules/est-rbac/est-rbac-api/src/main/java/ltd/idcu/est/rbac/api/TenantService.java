package ltd.idcu.est.rbac.api;

import java.util.List;

public interface TenantService {
    
    Tenant createTenant(String name, String code, String domain, Tenant.TenantMode mode, 
                        long expiresAt);
    
    Tenant getTenant(String id);
    
    Tenant getTenantByCode(String code);
    
    Tenant getTenantByDomain(String domain);
    
    List<Tenant> getAllTenants();
    
    Tenant updateTenant(String id, String name, String domain, boolean active, long expiresAt);
    
    void deleteTenant(String id);
    
    void setCurrentTenant(String tenantId);
    
    Tenant getCurrentTenant();
    
    void clearCurrentTenant();
}
