package ltd.idcu.est.admin.api;

import java.util.List;

public interface LoginLogService {
    
    LoginLog createLoginLog(String userId, String username, String ip, 
                             String userAgent, int status, String errorMsg);
    
    LoginLog getLoginLog(String id);
    
    List<LoginLog> getAllLoginLogs();
    
    List<LoginLog> getLoginLogsByUserId(String userId);
    
    List<LoginLog> getLoginLogsByStatus(int status);
    
    void deleteLoginLog(String id);
    
    void clearLoginLogs();
}
