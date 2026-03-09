package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantAware;
import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.multitenancy.api.TenantContextHolder;
import ltd.idcu.est.multitenancy.api.TenantDataSecurityFilter;

public class DefaultTenantDataSecurityFilter implements TenantDataSecurityFilter {
    
    private final TenantContextHolder contextHolder;
    
    public DefaultTenantDataSecurityFilter() {
        this(ThreadLocalTenantContextHolder.getInstance());
    }
    
    public DefaultTenantDataSecurityFilter(TenantContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }
    
    @Override
    public void beforeQuery(Object entity) {
        if (entity instanceof TenantAware) {
            setTenantInfo((TenantAware) entity);
        }
    }
    
    @Override
    public void beforeInsert(Object entity) {
        if (entity instanceof TenantAware) {
            setTenantInfo((TenantAware) entity);
        }
    }
    
    @Override
    public void beforeUpdate(Object entity) {
        if (entity instanceof TenantAware) {
            validateTenantAccess(entity);
            setTenantInfo((TenantAware) entity);
        }
    }
    
    @Override
    public void beforeDelete(Object entity) {
        if (entity instanceof TenantAware) {
            validateTenantAccess(entity);
        }
    }
    
    @Override
    public void afterQuery(Object entity) {
        if (entity instanceof TenantAware) {
            validateTenantAccess(entity);
        }
    }
    
    @Override
    public void validateTenantAccess(Object entity) {
        if (!(entity instanceof TenantAware)) {
            return;
        }
        
        TenantAware tenantAware = (TenantAware) entity;
        TenantContext context = contextHolder.getContext();
        
        if (!context.hasCurrentTenant()) {
            throw new SecurityException("No current tenant context available");
        }
        
        String currentTenantId = context.getCurrentTenantId();
        String entityTenantId = tenantAware.getTenantId();
        
        if (entityTenantId != null && !entityTenantId.equals(currentTenantId)) {
            throw new SecurityException("Access denied to entity from different tenant");
        }
    }
    
    private void setTenantInfo(TenantAware tenantAware) {
        TenantContext context = contextHolder.getContext();
        if (context.hasCurrentTenant()) {
            tenantAware.setTenantId(context.getCurrentTenantId());
            tenantAware.setTenantCode(context.getCurrentTenantCode());
        }
    }
}
