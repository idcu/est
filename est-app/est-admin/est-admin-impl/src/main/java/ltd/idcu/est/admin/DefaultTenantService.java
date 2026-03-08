package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Tenant;
import ltd.idcu.est.admin.api.TenantService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTenantService implements TenantService {
    
    private final Map<String, Tenant> tenantsById;
    private final Map<String, Tenant> tenantsByCode;
    private final Map<String, Tenant> tenantsByDomain;
    private final ThreadLocal<Tenant> currentTenant;
    
    public DefaultTenantService() {
        this.tenantsById = new ConcurrentHashMap<>();
        this.tenantsByCode = new ConcurrentHashMap<>();
        this.tenantsByDomain = new ConcurrentHashMap<>();
        this.currentTenant = new ThreadLocal<>();
        initializeDefaultTenant();
    }
    
    private void initializeDefaultTenant() {
        DefaultTenant defaultTenant = new DefaultTenant(
            UUID.randomUUID().toString(),
            "默认租户",
            "default",
            "localhost",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        tenantsById.put(defaultTenant.getId(), defaultTenant);
        tenantsByCode.put(defaultTenant.getCode(), defaultTenant);
        tenantsByDomain.put(defaultTenant.getDomain(), defaultTenant);
    }
    
    @Override
    public Tenant createTenant(String name, String code, String domain, Tenant.TenantMode mode, 
                               long expiresAt) {
        if (tenantsByCode.containsKey(code)) {
            throw new AdminException("Tenant code already exists: " + code);
        }
        if (domain != null && tenantsByDomain.containsKey(domain)) {
            throw new AdminException("Tenant domain already exists: " + domain);
        }
        
        String id = UUID.randomUUID().toString();
        DefaultTenant tenant = new DefaultTenant(
            id,
            name,
            code,
            domain,
            mode,
            true,
            System.currentTimeMillis(),
            expiresAt
        );
        
        tenantsById.put(id, tenant);
        tenantsByCode.put(code, tenant);
        if (domain != null) {
            tenantsByDomain.put(domain, tenant);
        }
        
        return tenant;
    }
    
    @Override
    public Tenant getTenant(String id) {
        return tenantsById.get(id);
    }
    
    @Override
    public Tenant getTenantByCode(String code) {
        return tenantsByCode.get(code);
    }
    
    @Override
    public Tenant getTenantByDomain(String domain) {
        return tenantsByDomain.get(domain);
    }
    
    @Override
    public List<Tenant> getAllTenants() {
        return new ArrayList<>(tenantsById.values());
    }
    
    @Override
    public Tenant updateTenant(String id, String name, String domain, boolean active, long expiresAt) {
        Tenant existingTenant = tenantsById.get(id);
        if (existingTenant == null) {
            throw new AdminException("Tenant not found: " + id);
        }
        
        if (domain != null && !domain.equals(existingTenant.getDomain())) {
            if (existingTenant.getDomain() != null) {
                tenantsByDomain.remove(existingTenant.getDomain());
            }
            if (tenantsByDomain.containsKey(domain)) {
                throw new AdminException("Tenant domain already exists: " + domain);
            }
            tenantsByDomain.put(domain, existingTenant);
        }
        
        DefaultTenant updatedTenant = new DefaultTenant(
            id,
            name != null ? name : existingTenant.getName(),
            existingTenant.getCode(),
            domain != null ? domain : existingTenant.getDomain(),
            existingTenant.getMode(),
            active,
            existingTenant.getCreatedAt(),
            expiresAt
        );
        
        tenantsById.put(id, updatedTenant);
        tenantsByCode.put(updatedTenant.getCode(), updatedTenant);
        
        return updatedTenant;
    }
    
    @Override
    public void deleteTenant(String id) {
        Tenant tenant = tenantsById.remove(id);
        if (tenant != null) {
            tenantsByCode.remove(tenant.getCode());
            if (tenant.getDomain() != null) {
                tenantsByDomain.remove(tenant.getDomain());
            }
        }
    }
    
    @Override
    public void setCurrentTenant(String tenantId) {
        Tenant tenant = tenantsById.get(tenantId);
        if (tenant == null) {
            throw new AdminException("Tenant not found: " + tenantId);
        }
        if (!tenant.isActive()) {
            throw new AdminException("Tenant is not active: " + tenantId);
        }
        currentTenant.set(tenant);
    }
    
    @Override
    public Tenant getCurrentTenant() {
        return currentTenant.get();
    }
    
    @Override
    public void clearCurrentTenant() {
        currentTenant.remove();
    }
}
