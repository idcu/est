package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.LoginLog;

public class DefaultLoginLog implements LoginLog {
    
    private final String id;
    private final String userId;
    private final String username;
    private final String tenantId;
    private final String loginType;
    private final String ip;
    private final String userAgent;
    private final String location;
    private final String requestId;
    private final String sessionId;
    private final int status;
    private final String errorMsg;
    private final String stackTrace;
    private final long createdAt;
    
    public DefaultLoginLog(String id, String userId, String username, String ip, 
                        String userAgent, int status, String errorMsg) {
        this(id, userId, username, null, null, ip, userAgent, null, null, null, 
             status, errorMsg, null, System.currentTimeMillis());
    }
    
    public DefaultLoginLog(String id, String userId, String username, String tenantId,
                        String loginType, String ip, String userAgent,
                        String location, String requestId, String sessionId,
                        int status, String errorMsg, String stackTrace) {
        this(id, userId, username, tenantId, loginType, ip, userAgent, location, 
             requestId, sessionId, status, errorMsg, stackTrace, System.currentTimeMillis());
    }
    
    public DefaultLoginLog(String id, String userId, String username, String tenantId,
                        String loginType, String ip, String userAgent,
                        String location, String requestId, String sessionId,
                        int status, String errorMsg, String stackTrace, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
        this.loginType = loginType;
        this.ip = ip;
        this.userAgent = userAgent;
        this.location = location;
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.status = status;
        this.errorMsg = errorMsg;
        this.stackTrace = stackTrace;
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
    public String getTenantId() {
        return tenantId;
    }
    
    @Override
    public String getLoginType() {
        return loginType;
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
    public String getLocation() {
        return location;
    }
    
    @Override
    public String getRequestId() {
        return requestId;
    }
    
    @Override
    public String getSessionId() {
        return sessionId;
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
    public String getStackTrace() {
        return stackTrace;
    }
    
    @Override
    public long getCreatedAt() {
        return createdAt;
    }
}
