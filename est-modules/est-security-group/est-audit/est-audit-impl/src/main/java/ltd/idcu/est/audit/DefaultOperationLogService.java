package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.AuditException;
import ltd.idcu.est.audit.api.OperationLog;
import ltd.idcu.est.audit.api.OperationLogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultOperationLogService implements OperationLogService {
    
    private final Map<String, OperationLog> operationLogs;
    
    public DefaultOperationLogService() {
        this.operationLogs = new ConcurrentHashMap<>();
    }
    
    @Override
    public OperationLog createOperationLog(String userId, String username, String module, 
                                     String operation, String method, String params, 
                                     Long time, String ip, String userAgent, 
                                     int status, String errorMsg) {
        String id = UUID.randomUUID().toString();
        DefaultOperationLog log = new DefaultOperationLog(
            id, userId, username, module, operation, method, params, time, ip, userAgent, status, errorMsg
        );
        operationLogs.put(id, log);
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
            .filter(log -> log.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByModule(String module) {
        return operationLogs.values().stream()
            .filter(log -> log.getModule().equals(module))
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteOperationLog(String id) {
        operationLogs.remove(id);
    }
    
    @Override
    public void clearOperationLogs() {
        operationLogs.clear();
    }
}
