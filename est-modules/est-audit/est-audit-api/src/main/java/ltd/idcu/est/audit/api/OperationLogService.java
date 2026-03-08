package ltd.idcu.est.audit.api;

import java.util.List;

public interface OperationLogService {
    
    OperationLog createOperationLog(String userId, String username, String module, 
                                     String operation, String method, String params, 
                                     Long time, String ip, String userAgent, 
                                     int status, String errorMsg);
    
    OperationLog getOperationLog(String id);
    
    List<OperationLog> getAllOperationLogs();
    
    List<OperationLog> getOperationLogsByUserId(String userId);
    
    List<OperationLog> getOperationLogsByModule(String module);
    
    void deleteOperationLog(String id);
    
    void clearOperationLogs();
}
