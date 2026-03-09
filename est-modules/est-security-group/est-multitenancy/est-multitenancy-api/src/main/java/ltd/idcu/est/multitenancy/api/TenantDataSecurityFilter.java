package ltd.idcu.est.multitenancy.api;

public interface TenantDataSecurityFilter {
    
    void beforeQuery(Object entity);
    
    void beforeInsert(Object entity);
    
    void beforeUpdate(Object entity);
    
    void beforeDelete(Object entity);
    
    void afterQuery(Object entity);
    
    void validateTenantAccess(Object entity);
}
