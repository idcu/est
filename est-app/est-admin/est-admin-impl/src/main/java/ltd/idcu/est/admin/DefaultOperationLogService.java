package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.OperationLog;
import ltd.idcu.est.admin.api.OperationLogService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultOperationLogService implements OperationLogService {
    
    private final Map<String, OperationLog> operationLogsById;
    private final List<OperationLog> operationLogs;
    
    public DefaultOperationLogService() {
        this.operationLogsById = new ConcurrentHashMap<>();
        this.operationLogs = Collections.synchronizedList(new ArrayList<>());
    }
    
    @Override
    public OperationLog createOperationLog(String userId, String username, String module, 
                                           String operation, String method, String params, 
                                           Long time, String ip, String userAgent, 
                                           int status, String errorMsg) {
        String id = UUID.randomUUID().toString();
        long createdAt = System.currentTimeMillis();
        
        DefaultOperationLog log = new DefaultOperationLog(
            id, userId, username, module, operation, method, params, 
            time, ip, userAgent, status, errorMsg, createdAt
        );
        
        operationLogsById.put(id, log);
        operationLogs.add(log);
        
        return log;
    }
    
    @Override
    public OperationLog getOperationLog(String id) {
        return operationLogsById.get(id);
    }
    
    @Override
    public List<OperationLog> getAllOperationLogs() {
        return new ArrayList<>(operationLogs);
    }
    
    @Override
    public List<OperationLog> getOperationLogsByUserId(String userId) {
        return operationLogs.stream()
            .filter(log -> userId.equals(log.getUserId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OperationLog> getOperationLogsByModule(String module) {
        return operationLogs.stream()
            .filter(log -> module.equals(log.getModule()))
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteOperationLog(String id) {
        OperationLog log = operationLogsById.remove(id);
        if (log != null) {
            operationLogs.remove(log);
        }
    }
    
    @Override
    public void clearOperationLogs() {
        operationLogsById.clear();
        operationLogs.clear();
    }
}
