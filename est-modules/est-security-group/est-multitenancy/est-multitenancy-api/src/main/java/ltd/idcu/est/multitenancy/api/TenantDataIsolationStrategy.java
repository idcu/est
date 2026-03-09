package ltd.idcu.est.multitenancy.api;

import ltd.idcu.est.rbac.api.Tenant;

public interface TenantDataIsolationStrategy {
    
    String getTenantColumnName();
    
    String getTenantColumnValue(Tenant tenant);
    
    String applyTenantFilter(String sql, Tenant tenant);
    
    String applyTenantSchema(String sql, Tenant tenant);
    
    String applyTenantDatabase(String sql, Tenant tenant);
    
    boolean supports(Tenant.TenantMode mode);
}
