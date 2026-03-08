package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.LoginLog;

public class DefaultLoginLog implements LoginLog {
    
    private final String id;
    private final String userId;
    private final String username;
    private final String ip;
    private final String userAgent;
    private final int status;
    private final String errorMsg;
    private final long createdAt;
    
    public DefaultLoginLog(String id, String userId, String username, String ip, 
                        String userAgent, int status, String errorMsg) {
        this(id, userId, username, ip, userAgent, status, errorMsg, System.currentTimeMillis());
    }
    
    public DefaultLoginLog(String id, String userId, String username, String ip, 
                        String userAgent, int status, String errorMsg, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.ip = ip;
        this.userAgent = userAgent;
        this.status = status;
        this.errorMsg = errorMsg;
        this.createdAt = createdAt;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getUserId() {
        return userId;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public String getIp() {
        return ip;
    }
    
    @Override
    public String getUserAgent() {
        return userAgent;
    }
    
    @Override
    public int getStatus() {
        return status;
    }
    
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
    
    @Override
    public long getCreatedAt() {
        return createdAt;
    }
}
