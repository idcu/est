package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.OperationLog;

public class DefaultOperationLog implements OperationLog {
    
    private final String id;
    private final String userId;
    private final String username;
    private final String module;
    private final String operation;
    private final String method;
    private final String params;
    private final Long time;
    private final String ip;
    private final String userAgent;
    private final int status;
    private final String errorMsg;
    private final long createdAt;
    
    public DefaultOperationLog(String id, String userId, String username, String module, 
                           String operation, String method, String params, 
                           Long time, String ip, String userAgent, 
                           int status, String errorMsg) {
        this(id, userId, username, module, operation, method, params, time, ip, userAgent, status, errorMsg, System.currentTimeMillis());
    }
    
    public DefaultOperationLog(String id, String userId, String username, String module, 
                           String operation, String method, String params, 
                           Long time, String ip, String userAgent, 
                           int status, String errorMsg, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.module = module;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.time = time;
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
    public String getModule() {
        return module;
    }
    
    @Override
    public String getOperation() {
        return operation;
    }
    
    @Override
    public String getMethod() {
        return method;
    }
    
    @Override
    public String getParams() {
        return params;
    }
    
    @Override
    public Long getTime() {
        return time;
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
