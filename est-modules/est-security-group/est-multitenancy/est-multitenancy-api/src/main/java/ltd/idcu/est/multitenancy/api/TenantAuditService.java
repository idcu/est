package ltd.idcu.est.multitenancy.api;

import java.util.List;

public interface TenantAuditService {
    
    void logOperation(TenantAuditLog auditLog);
    
    void logSuccess(String operation, String resourceType, String resourceId, String details);
    
    void logFailure(String operation, String resourceType, String resourceId, String details, String errorMessage);
    
    List<TenantAuditLog> getAuditLogsByTenant(String tenantId, int limit);
    
    List<TenantAuditLog> getAuditLogsByUser(String userId, int limit);
    
    List<TenantAuditLog> getAuditLogsByResource(String resourceType, String resourceId, int limit);
    
    List<TenantAuditLog> getRecentAuditLogs(int limit);
}
