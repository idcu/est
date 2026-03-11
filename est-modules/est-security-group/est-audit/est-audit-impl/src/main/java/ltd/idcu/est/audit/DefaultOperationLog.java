package ltd.idcu.est.audit;

import ltd.idcu.est.audit.api.OperationLog;

public class DefaultOperationLog implements OperationLog {
    
    private final String id;
    private final String userId;
    private final String username;
    private final String tenantId;
    private final String module;
    private final String operation;
    private final String resource;
    private final String resourceId;
    private final String description;
    private final String method;
    private final String params;
    private final String result;
    private final Long time;
    private final Long executionTime;
    private final String ip;
    private final String userAgent;
    private final String location;
    private final String requestId;
    private final int status;
    private final String errorMsg;
    private final String stackTrace;
    private final long createdAt;
    
    public DefaultOperationLog(String id, String userId, String username, String module, 
                           String operation, String method, String params, 
                           Long time, String ip, String userAgent, 
                           int status, String errorMsg) {
        this(id, userId, username, null, module, operation, null, null, null, 
             method, params, null, time, null, ip, userAgent, null, null, 
             status, errorMsg, null, System.currentTimeMillis());
    }
    
    public DefaultOperationLog(String id, String userId, String username, String tenantId,
                           String module, String operation, String resource,
                           String resourceId, String description, String method,
                           String params, String result, Long time, Long executionTime,
                           String ip, String userAgent, String location, String requestId,
                           int status, String errorMsg, String stackTrace) {
        this(id, userId, username, tenantId, module, operation, resource, resourceId, 
             description, method, params, result, time, executionTime, ip, userAgent, 
             location, requestId, status, errorMsg, stackTrace, System.currentTimeMillis());
    }
    
    public DefaultOperationLog(String id, String userId, String username, String tenantId,
                           String module, String operation, String resource,
                           String resourceId, String description, String method,
                           String params, String result, Long time, Long executionTime,
                           String ip, String userAgent, String location, String requestId,
                           int status, String errorMsg, String stackTrace, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
        this.module = module;
        this.operation = operation;
        this.resource = resource;
        this.resourceId = resourceId;
        this.description = description;
        this.method = method;
        this.params = params;
        this.result = result;
        this.time = time;
        this.executionTime = executionTime;
        this.ip = ip;
        this.userAgent = userAgent;
        this.location = location;
        this.requestId = requestId;
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
    public String getModule() {
        return module;
    }
    
    @Override
    public String getOperation() {
        return operation;
    }
    
    @Override
    public String getResource() {
        return resource;
    }
    
    @Override
    public String getResourceId() {
        return resourceId;
    }
    
    @Override
    public String getDescription() {
        return description;
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
    public String getResult() {
        return result;
    }
    
    @Override
    public Long getTime() {
        return time;
    }
    
    @Override
    public Long getExecutionTime() {
        return executionTime;
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
