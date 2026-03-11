package ltd.idcu.est.audit.api;

import java.util.List;
import java.util.Map;

public interface OperationLogService {
    
    OperationLog createOperationLog(String userId, String username, String module, 
                                     String operation, String method, String params, 
                                     Long time, String ip, String userAgent, 
                                     int status, String errorMsg);
    
    OperationLog createOperationLog(String userId, String username, String tenantId,
                                     String module, String operation, String resource,
                                     String resourceId, String description, String method,
                                     String params, String result, Long time, Long executionTime,
                                     String ip, String userAgent, String location, String requestId,
                                     int status, String errorMsg, String stackTrace);
    
    OperationLog getOperationLog(String id);
    
    List<OperationLog> getAllOperationLogs();
    
    List<OperationLog> getOperationLogsByUserId(String userId);
    
    List<OperationLog> getOperationLogsByUsername(String username);
    
    List<OperationLog> getOperationLogsByModule(String module);
    
    List<OperationLog> getOperationLogsByResource(String resource);
    
    List<OperationLog> getOperationLogsByResourceId(String resource, String resourceId);
    
    List<OperationLog> getOperationLogsByStatus(int status);
    
    List<OperationLog> getOperationLogsByTimeRange(long startTime, long endTime);
    
    List<OperationLog> queryOperationLogs(Map<String, Object> criteria);
    
    long countOperationLogs();
    
    long countOperationLogsByUserId(String userId);
    
    long countOperationLogsByModule(String module);
    
    Map<String, Long> getOperationStatsByModule();
    
    Map<String, Long> getOperationStatsByUser();
    
    void deleteOperationLog(String id);
    
    void deleteOperationLogsByTimeRange(long startTime, long endTime);
    
    void clearOperationLogs();
}
