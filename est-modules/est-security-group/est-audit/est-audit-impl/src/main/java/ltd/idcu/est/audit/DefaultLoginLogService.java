package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultLoginLogService implements LoginLogService {
    
    private final Map<String, LoginLog> loginLogs;
    private final List<AuditEventListener> listeners;
    
    public DefaultLoginLogService() {
        this.loginLogs = new ConcurrentHashMap<>();
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
    public LoginLog createLoginLog(String userId, String username, String ip, 
                             String userAgent, int status, String errorMsg) {
        return createLoginLog(userId, username, null, null, ip, userAgent, null, null, null, 
                              status, errorMsg, null);
    }
    
    @Override
    public LoginLog createLoginLog(String userId, String username, String tenantId,
                            String loginType, String ip, String userAgent,
                            String location, String requestId, String sessionId,
                            int status, String errorMsg, String stackTrace) {
        String id = UUID.randomUUID().toString();
        DefaultLoginLog log = new DefaultLoginLog(
            id, userId, username, tenantId, loginType, ip, userAgent, 
            location, requestId, sessionId, status, errorMsg, stackTrace
        );
        loginLogs.put(id, log);
        
        fireEvent(new LoginLogEvent() {
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
                return "LOGIN_LOG_CREATED";
            }
            
            @Override
            public Object getSource() {
                return log;
            }
            
            @Override
            public LoginLog getLoginLog() {
                return log;
            }
        });
        
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
            .filter(log -> log.getUserId() != null && log.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByUsername(String username) {
        return loginLogs.values().stream()
            .filter(log -> log.getUsername() != null && log.getUsername().equals(username))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByTenantId(String tenantId) {
        return loginLogs.values().stream()
            .filter(log -> log.getTenantId() != null && log.getTenantId().equals(tenantId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByLoginType(String loginType) {
        return loginLogs.values().stream()
            .filter(log -> log.getLoginType() != null && log.getLoginType().equals(loginType))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByStatus(int status) {
        return loginLogs.values().stream()
            .filter(log -> log.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> getLoginLogsByTimeRange(long startTime, long endTime) {
        return loginLogs.values().stream()
            .filter(log -> log.getCreatedAt() >= startTime && log.getCreatedAt() <= endTime)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoginLog> queryLoginLogs(Map<String, Object> criteria) {
        return loginLogs.values().stream()
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
                        case "loginType":
                            if (!value.equals(log.getLoginType())) return false;
                            break;
                        case "status":
                            if (!value.equals(log.getStatus())) return false;
                            break;
                        case "ip":
                            if (!value.equals(log.getIp())) return false;
                            break;
                        case "sessionId":
                            if (!value.equals(log.getSessionId())) return false;
                            break;
                    }
                }
                return true;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public long countLoginLogs() {
        return loginLogs.size();
    }
    
    @Override
    public long countLoginLogsByUserId(String userId) {
        return loginLogs.values().stream()
            .filter(log -> log.getUserId() != null && log.getUserId().equals(userId))
            .count();
    }
    
    @Override
    public long countLoginLogsByStatus(int status) {
        return loginLogs.values().stream()
            .filter(log -> log.getStatus() == status)
            .count();
    }
    
    @Override
    public Map<String, Long> getLoginStatsByLoginType() {
        return loginLogs.values().stream()
            .filter(log -> log.getLoginType() != null)
            .collect(Collectors.groupingBy(LoginLog::getLoginType, Collectors.counting()));
    }
    
    @Override
    public Map<String, Long> getLoginStatsByUser() {
        return loginLogs.values().stream()
            .filter(log -> log.getUsername() != null)
            .collect(Collectors.groupingBy(LoginLog::getUsername, Collectors.counting()));
    }
    
    @Override
    public void deleteLoginLog(String id) {
        loginLogs.remove(id);
    }
    
    @Override
    public void deleteLoginLogsByTimeRange(long startTime, long endTime) {
        loginLogs.entrySet().removeIf(entry -> 
            entry.getValue().getCreatedAt() >= startTime && 
            entry.getValue().getCreatedAt() <= endTime
        );
    }
    
    @Override
    public void clearLoginLogs() {
        loginLogs.clear();
    }
}
