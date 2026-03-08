package ltd.idcu.est.rbac;

import ltd.idcu.est.rbac.api.Tenant;

public class DefaultTenant implements Tenant {
    
    private final String id;
    private final String name;
    private final String code;
    private final String domain;
    private final TenantMode mode;
    private final boolean active;
    private final long createdAt;
    private final long expiresAt;
    
    public DefaultTenant(String id, String name, String code, String domain, TenantMode mode, 
                        boolean active, long createdAt, long expiresAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.domain = domain;
        this.mode = mode;
        this.active = active;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getDomain() {
        return domain;
    }
    
    @Override
    public TenantMode getMode() {
        return mode;
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public long getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public long getExpiresAt() {
        return expiresAt;
    }
}
