package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.rbac.api.Tenant;

public class DefaultTenantContext implements TenantContext {
    
    private Tenant currentTenant;
    
    public DefaultTenantContext() {
    }
    
    public DefaultTenantContext(Tenant tenant) {
        this.currentTenant = tenant;
    }
    
    @Override
    public Tenant getCurrentTenant() {
        return currentTenant;
    }
    
    @Override
    public void setCurrentTenant(Tenant tenant) {
        this.currentTenant = tenant;
    }
    
    @Override
    public void clearCurrentTenant() {
        this.currentTenant = null;
    }
    
    @Override
    public boolean hasCurrentTenant() {
        return currentTenant != null;
    }
    
    @Override
    public String getCurrentTenantId() {
        return currentTenant != null ? currentTenant.getId() : null;
    }
    
    @Override
    public String getCurrentTenantCode() {
        return currentTenant != null ? currentTenant.getCode() : null;
    }
    
    @Override
    public Tenant.TenantMode getCurrentTenantMode() {
        return currentTenant != null ? currentTenant.getMode() : null;
    }
}
