package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.*;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.rbac.api.TenantService;

public class TenantInterceptors {
    
    private final TenantContextHolder contextHolder;
    private final TenantService tenantService;
    private final TenantDataIsolationStrategy isolationStrategy;
    private final TenantDataSecurityFilter securityFilter;
    private final TenantAuditService auditService;
    
    public TenantInterceptors(TenantService tenantService) {
        this(ThreadLocalTenantContextHolder.getInstance(), tenantService,
                new DefaultTenantDataIsolationStrategy(),
                new DefaultTenantDataSecurityFilter(),
                new DefaultTenantAuditService());
    }
    
    public TenantInterceptors(TenantContextHolder contextHolder, TenantService tenantService,
                              TenantDataIsolationStrategy isolationStrategy,
                              TenantDataSecurityFilter securityFilter,
                              TenantAuditService auditService) {
        this.contextHolder = contextHolder;
        this.tenantService = tenantService;
        this.isolationStrategy = isolationStrategy;
        this.securityFilter = securityFilter;
        this.auditService = auditService;
    }
    
    public void setTenantById(String tenantId) {
        Tenant tenant = tenantService.getTenant(tenantId);
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant not found: " + tenantId);
        }
        setTenant(tenant);
    }
    
    public void setTenantByCode(String tenantCode) {
        Tenant tenant = tenantService.getTenantByCode(tenantCode);
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant not found: " + tenantCode);
        }
        setTenant(tenant);
    }
    
    public void setTenant(Tenant tenant) {
        TenantContext context = contextHolder.getContext();
        context.setCurrentTenant(tenant);
    }
    
    public void clearTenant() {
        contextHolder.clearContext();
    }
    
    public Tenant getCurrentTenant() {
        TenantContext context = contextHolder.getContext();
        return context.getCurrentTenant();
    }
    
    public String processSql(String sql) {
        TenantContext context = contextHolder.getContext();
        if (!context.hasCurrentTenant()) {
            return sql;
        }
        
        Tenant tenant = context.getCurrentTenant();
        Tenant.TenantMode mode = tenant.getMode();
        
        switch (mode) {
            case COLUMN:
                return isolationStrategy.applyTenantFilter(sql, tenant);
            case SCHEMA:
                return isolationStrategy.applyTenantSchema(sql, tenant);
            case DATABASE:
                return isolationStrategy.applyTenantDatabase(sql, tenant);
            default:
                return sql;
        }
    }
    
    public void beforeDataOperation(Object entity, String operation) {
        switch (operation.toUpperCase()) {
            case "QUERY":
                securityFilter.beforeQuery(entity);
                break;
            case "INSERT":
                securityFilter.beforeInsert(entity);
                break;
            case "UPDATE":
                securityFilter.beforeUpdate(entity);
                break;
            case "DELETE":
                securityFilter.beforeDelete(entity);
                break;
        }
    }
    
    public void afterDataOperation(Object entity, String operation) {
        if ("QUERY".equalsIgnoreCase(operation)) {
            securityFilter.afterQuery(entity);
        }
    }
    
    public void logDataOperation(String operation, String resourceType, String resourceId, String details, boolean success, String errorMessage) {
        if (success) {
            auditService.logSuccess(operation, resourceType, resourceId, details);
        } else {
            auditService.logFailure(operation, resourceType, resourceId, details, errorMessage);
        }
    }
    
    public TenantContextHolder getContextHolder() {
        return contextHolder;
    }
    
    public TenantDataIsolationStrategy getIsolationStrategy() {
        return isolationStrategy;
    }
    
    public TenantDataSecurityFilter getSecurityFilter() {
        return securityFilter;
    }
    
    public TenantAuditService getAuditService() {
        return auditService;
    }
}
