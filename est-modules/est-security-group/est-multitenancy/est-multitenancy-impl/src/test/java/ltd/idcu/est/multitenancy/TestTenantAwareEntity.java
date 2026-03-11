package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantAware;

public class TestTenantAwareEntity implements TenantAware {
    
    private String id;
    private String name;
    private String tenantId;
    private String tenantCode;
    
    public TestTenantAwareEntity() {
    }
    
    public TestTenantAwareEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getTenantId() {
        return tenantId;
    }
    
    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getTenantCode() {
        return tenantCode;
    }
    
    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
