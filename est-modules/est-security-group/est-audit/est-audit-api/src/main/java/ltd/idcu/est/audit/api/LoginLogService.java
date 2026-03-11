package ltd.idcu.est.audit.api;

import java.util.List;
import java.util.Map;

public interface LoginLogService {
    
    LoginLog createLoginLog(String userId, String username, String ip, 
                             String userAgent, int status, String errorMsg);
    
    LoginLog createLoginLog(String userId, String username, String tenantId,
                            String loginType, String ip, String userAgent,
                            String location, String requestId, String sessionId,
                            int status, String errorMsg, String stackTrace);
    
    LoginLog getLoginLog(String id);
    
    List<LoginLog> getAllLoginLogs();
    
    List<LoginLog> getLoginLogsByUserId(String userId);
    
    List<LoginLog> getLoginLogsByUsername(String username);
    
    List<LoginLog> getLoginLogsByTenantId(String tenantId);
    
    List<LoginLog> getLoginLogsByLoginType(String loginType);
    
    List<LoginLog> getLoginLogsByStatus(int status);
    
    List<LoginLog> getLoginLogsByTimeRange(long startTime, long endTime);
    
    List<LoginLog> queryLoginLogs(Map<String, Object> criteria);
    
    long countLoginLogs();
    
    long countLoginLogsByUserId(String userId);
    
    long countLoginLogsByStatus(int status);
    
    Map<String, Long> getLoginStatsByLoginType();
    
    Map<String, Long> getLoginStatsByUser();
    
    void deleteLoginLog(String id);
    
    void deleteLoginLogsByTimeRange(long startTime, long endTime);
    
    void clearLoginLogs();
}
