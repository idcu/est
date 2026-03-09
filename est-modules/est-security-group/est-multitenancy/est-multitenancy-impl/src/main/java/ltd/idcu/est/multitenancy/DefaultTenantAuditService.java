package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantAuditLog;
import ltd.idcu.est.multitenancy.api.TenantAuditService;
import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.multitenancy.api.TenantContextHolder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultTenantAuditService implements TenantAuditService {
    
    private final List<TenantAuditLog> auditLogs;
    private final TenantContextHolder contextHolder;
    private final Map<String, List<TenantAuditLog>> tenantLogsIndex;
    private final Map<String, List<TenantAuditLog>> userLogsIndex;
    private final Map<String, List<TenantAuditLog>> resourceLogsIndex;
    
    public DefaultTenantAuditService() {
        this(ThreadLocalTenantContextHolder.getInstance());
    }
    
    public DefaultTenantAuditService(TenantContextHolder contextHolder) {
        this.auditLogs = new CopyOnWriteArrayList<>();
        this.contextHolder = contextHolder;
        this.tenantLogsIndex = new ConcurrentHashMap<>();
        this.userLogsIndex = new ConcurrentHashMap<>();
        this.resourceLogsIndex = new ConcurrentHashMap<>();
    }
    
    @Override
    public void logOperation(TenantAuditLog auditLog) {
        auditLogs.add(auditLog);
        indexLog(auditLog);
    }
    
    @Override
    public void logSuccess(String operation, String resourceType, String resourceId, String details) {
        TenantContext context = contextHolder.getContext();
        String tenantId = context.getCurrentTenantId();
        String tenantCode = context.getCurrentTenantCode();
        
        TenantAuditLog log = DefaultTenantAuditLog.builder()
                .id(UUID.randomUUID().toString())
                .tenantId(tenantId)
                .tenantCode(tenantCode)
                .operation(operation)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .details(details)
                .success(true)
                .build();
        
        logOperation(log);
    }
    
    @Override
    public void logFailure(String operation, String resourceType, String resourceId, String details, String errorMessage) {
        TenantContext context = contextHolder.getContext();
        String tenantId = context.getCurrentTenantId();
        String tenantCode = context.getCurrentTenantCode();
        
        TenantAuditLog log = DefaultTenantAuditLog.builder()
                .id(UUID.randomUUID().toString())
                .tenantId(tenantId)
                .tenantCode(tenantCode)
                .operation(operation)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .details(details)
                .success(false)
                .errorMessage(errorMessage)
                .build();
        
        logOperation(log);
    }
    
    @Override
    public List<TenantAuditLog> getAuditLogsByTenant(String tenantId, int limit) {
        List<TenantAuditLog> logs = tenantLogsIndex.getOrDefault(tenantId, Collections.emptyList());
        return logs.stream()
                .sorted(Comparator.comparingLong(TenantAuditLog::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TenantAuditLog> getAuditLogsByUser(String userId, int limit) {
        List<TenantAuditLog> logs = userLogsIndex.getOrDefault(userId, Collections.emptyList());
        return logs.stream()
                .sorted(Comparator.comparingLong(TenantAuditLog::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TenantAuditLog> getAuditLogsByResource(String resourceType, String resourceId, int limit) {
        String key = resourceType + ":" + resourceId;
        List<TenantAuditLog> logs = resourceLogsIndex.getOrDefault(key, Collections.emptyList());
        return logs.stream()
                .sorted(Comparator.comparingLong(TenantAuditLog::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TenantAuditLog> getRecentAuditLogs(int limit) {
        return auditLogs.stream()
                .sorted(Comparator.comparingLong(TenantAuditLog::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    private void indexLog(TenantAuditLog log) {
        if (log.getTenantId() != null) {
            tenantLogsIndex.computeIfAbsent(log.getTenantId(), k -> new CopyOnWriteArrayList<>()).add(log);
        }
        
        if (log.getUserId() != null) {
            userLogsIndex.computeIfAbsent(log.getUserId(), k -> new CopyOnWriteArrayList<>()).add(log);
        }
        
        if (log.getResourceType() != null && log.getResourceId() != null) {
            String key = log.getResourceType() + ":" + log.getResourceId();
            resourceLogsIndex.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(log);
        }
    }
}
