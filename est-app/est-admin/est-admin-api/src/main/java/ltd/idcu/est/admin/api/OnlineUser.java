package ltd.idcu.est.admin.api;

public interface OnlineUser {
    
    String getSessionId();
    
    String getUserId();
    
    String getUsername();
    
    String getIp();
    
    String getBrowser();
    
    long getLoginTime();
    
    long getLastActivityTime();
}
