package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.LoginLog;
import ltd.idcu.est.admin.api.LoginLogService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultLoginLogService implements LoginLogService {
    
    private final Map<String, LoginLog> loginLogsById;
    private final List<LoginLog> loginLogs;
    
    public DefaultLoginLogService() {
        this.loginLogsById = new ConcurrentHashMap<>();
        this.loginLogs = Collections.synchronizedList(new ArrayList<>());
    }
    
    @Override
    public LoginLog createLoginLog(String userId, String username, String ip, 
                                   String userAgent, int status, String errorMsg) {
        String id = UUID.randomUUID().toString();
        long createdAt = System.currentTimeMillis();
        
        DefaultLoginLog log = new DefaultLoginLog(
            id, userId, username, ip, userAgent, status, errorMsg, createdAt
        );
        
        loginLogsById.put(id, log);
        loginLogs.add(log);
        
        return log;
    }
    
    @Override
    public LoginLog getLoginLog(String id) {
        return loginLogsById.get(id);
    }
    
    @Override
    public List<LoginLog> getAllLoginLogs() {
        return new ArrayList<>(loginLogs);
    }
    
    @Override
    public List<LoginLog> getLoginLogsByUserId(String userId) {
        return loginLogs.stream()
            .filter(log -> userId.equals(log.getUserId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteLoginLog(String id) {
        LoginLog log = loginLogsById.remove(id);
        if (log != null) {
            loginLogs.remove(log);
        }
    }
    
    @Override
    public List<LoginLog> getLoginLogsByStatus(int status) {
        return loginLogs.stream()
            .filter(log -> log.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public void clearLoginLogs() {
        loginLogsById.clear();
        loginLogs.clear();
    }
}
