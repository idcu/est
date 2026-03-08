package ltd.idcu.est.admin.api;

public interface Tenant {
    
    String getId();
    
    String getName();
    
    String getCode();
    
    String getDomain();
    
    TenantMode getMode();
    
    boolean isActive();
    
    long getCreatedAt();
    
    long getExpiresAt();
    
    enum TenantMode {
        COLUMN,
        SCHEMA,
        DATABASE
    }
}
