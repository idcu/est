package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.LoginLog;
import ltd.idcu.est.audit.api.LoginLogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultLoginLogService implements LoginLogService {
    
    private final Map<String, LoginLog> loginLogs;
    
    public DefaultLoginLogService() {
        this.loginLogs = new ConcurrentHashMap<>();
    }
    
    @Override
    public LoginLog createLoginLog(String userId, String username, String ip, 
                             String userAgent, int status, String errorMsg) {
        String id = UUID.randomUUID().toString();
        DefaultLoginLog log = new DefaultLoginLog(id, userId, username, ip, userAgent, status, errorMsg);
        loginLogs.put(id, log);
        return log;
    }
    
    @Override
    public LoginLog getLoginLog(String id) {
        return loginLogs.get(id);
    }
    
    @Override
    public List<LoginLog> getAllLoginLogs() {
        return new ArrayList<>(loginLogs.values());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByUserId(String userId) {
        return loginLogs.values().stream()
            .filter(log -> log.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByStatus(int status) {
        return loginLogs.values().stream()
            .filter(log -> log.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteLoginLog(String id) {
        loginLogs.remove(id);
    }
    
    @Override
    public void clearLoginLogs() {
        loginLogs.clear();
    }
}
