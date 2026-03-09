package ltd.idcu.est.multitenancy.api;

public interface TenantContextHolder {
    
    TenantContext getContext();
    
    void setContext(TenantContext context);
    
    void clearContext();
    
    TenantContext createEmptyContext();
}
