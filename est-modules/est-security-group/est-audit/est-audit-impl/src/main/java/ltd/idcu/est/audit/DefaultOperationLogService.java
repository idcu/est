package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultOperationLogService implements OperationLogService {
    
    private final Map<String, OperationLog> operationLogs;
    private final List<AuditEventListener> listeners;
    
    public DefaultOperationLogService() {
        this.operationLogs = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
    }
    
    public void addListener(AuditEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(AuditEventListener listener) {
        listeners.remove(listener);
    }
    
    private void fireEvent(AuditEvent event) {
        for (AuditEventListener listener : listeners) {
            if (listener.supports(event.getEventType())) {
                try {
                    listener.onAuditEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public OperationLog createOperationLog(String userId, String username, String module, 
                                     String operation, String method, String params, 
                                     Long time, String ip, String userAgent, 
                                     int status, String errorMsg) {
        return createOperationLog(userId, username, null, module, operation, null, null, null, 
                                  method, params, null, time, null, ip, userAgent, null, null, 
                                  status, errorMsg, null);
    }
    
    @Override
    public OperationLog createOperationLog(String userId, String username, String tenantId,
                                     String module, String operation, String resource,
                                     String resourceId, String description, String method,
                                     String params, String result, Long time, Long executionTime,
                                     String ip, String userAgent, String location, String requestId,
                                     int status, String errorMsg, String stackTrace) {
        String id = UUID.randomUUID().toString();
        DefaultOperationLog log = new DefaultOperationLog(
            id, userId, username, tenantId, module, operation, resource, resourceId, description, 
            method, params, result, time, executionTime, ip, userAgent, location, requestId, 
            status, errorMsg, stackTrace
        );
        operationLogs.put(id, log);
        
        fireEvent(new OperationLogEvent() {
            @Override
            public String getId() {
                return UUID.randomUUID().toString();
            }
            
            @Override
            public long getTimestamp() {
                return System.currentTimeMillis();
            }
            
            @Override
            public String getEventType() {
                return "OPERATION_LOG_CREATED";
            }
            
            @Override
            public Object getSource() {
                return log;
            }
            
            @Override
            public OperationLog getOperationLog() {
                return log;
            }
        });
        
        return log;
    }
    
    @Override
    public OperationLog getOperationLog(String id) {
        return operationLogs.get(id);
    }
    
    @Override
    public List<OperationLog> getAllOperationLogs() {
        return new ArrayList<>(operationLogs.values());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByUserId(String userId) {
        return operationLogs.values().stream()
            .filter(log -> log.getUserId() != null && log.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByUsername(String username) {
        return operationLogs.values().stream()
            .filter(log -> log.getUsername() != null && log.getUsername().equals(username))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByModule(String module) {
        return operationLogs.values().stream()
            .filter(log -> log.getModule() != null && log.getModule().equals(module))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByResource(String resource) {
        return operationLogs.values().stream()
            .filter(log -> log.getResource() != null && log.getResource().equals(resource))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByResourceId(String resource, String resourceId) {
        return operationLogs.values().stream()
            .filter(log -> log.getResource() != null && log.getResource().equals(resource))
            .filter(log -> log.getResourceId() != null && log.getResourceId().equals(resourceId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByStatus(int status) {
        return operationLogs.values().stream()
            .filter(log -> log.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByTimeRange(long startTime, long endTime) {
        return operationLogs.values().stream()
            .filter(log -> log.getCreatedAt() >= startTime && log.getCreatedAt() <= endTime)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> queryOperationLogs(Map<String, Object> criteria) {
        return operationLogs.values().stream()
            .filter(log -> {
                for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value == null) continue;
                    
                    switch (key) {
                        case "userId":
                            if (!value.equals(log.getUserId())) return false;
                            break;
                        case "username":
                            if (!value.equals(log.getUsername())) return false;
                            break;
                        case "tenantId":
                            if (!value.equals(log.getTenantId())) return false;
                            break;
                        case "module":
                            if (!value.equals(log.getModule())) return false;
                            break;
                        case "operation":
                            if (!value.equals(log.getOperation())) return false;
                            break;
                        case "resource":
                            if (!value.equals(log.getResource())) return false;
                            break;
                        case "resourceId":
                            if (!value.equals(log.getResourceId())) return false;
                            break;
                        case "status":
                            if (!value.equals(log.getStatus())) return false;
                            break;
                        case "ip":
                            if (!value.equals(log.getIp())) return false;
                            break;
                    }
                }
                return true;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public long countOperationLogs() {
        return operationLogs.size();
    }
    
    @Override
    public long countOperationLogsByUserId(String userId) {
        return operationLogs.values().stream()
            .filter(log -> log.getUserId() != null && log.getUserId().equals(userId))
            .count();
    }
    
    @Override
    public long countOperationLogsByModule(String module) {
        return operationLogs.values().stream()
            .filter(log -> log.getModule() != null && log.getModule().equals(module))
            .count();
    }
    
    @Override
    public Map<String, Long> getOperationStatsByModule() {
        return operationLogs.values().stream()
            .filter(log -> log.getModule() != null)
            .collect(Collectors.groupingBy(OperationLog::getModule, Collectors.counting()));
    }
    
    @Override
    public Map<String, Long> getOperationStatsByUser() {
        return operationLogs.values().stream()
            .filter(log -> log.getUsername() != null)
            .collect(Collectors.groupingBy(OperationLog::getUsername, Collectors.counting()));
    }
    
    @Override
    public void deleteOperationLog(String id) {
        operationLogs.remove(id);
    }
    
    @Override
    public void deleteOperationLogsByTimeRange(long startTime, long endTime) {
        operationLogs.entrySet().removeIf(entry -> 
            entry.getValue().getCreatedAt() >= startTime && 
            entry.getValue().getCreatedAt() <= endTime
        );
    }
    
    @Override
    public void clearOperationLogs() {
        operationLogs.clear();
    }
}
