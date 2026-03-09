package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantAuditLog;

public class DefaultTenantAuditLog implements TenantAuditLog {
    
    private final String id;
    private final String tenantId;
    private final String tenantCode;
    private final String userId;
    private final String operation;
    private final String resourceType;
    private final String resourceId;
    private final String details;
    private final String ipAddress;
    private final String userAgent;
    private final long timestamp;
    private final boolean success;
    private final String errorMessage;
    
    public DefaultTenantAuditLog(String id, String tenantId, String tenantCode, String userId,
                                  String operation, String resourceType, String resourceId,
                                  String details, String ipAddress, String userAgent,
                                  long timestamp, boolean success, String errorMessage) {
        this.id = id;
        this.tenantId = tenantId;
        this.tenantCode = tenantCode;
        this.userId = userId;
        this.operation = operation;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.details = details;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.timestamp = timestamp;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getTenantId() {
        return tenantId;
    }
    
    @Override
    public String getTenantCode() {
        return tenantCode;
    }
    
    @Override
    public String getUserId() {
        return userId;
    }
    
    @Override
    public String getOperation() {
        return operation;
    }
    
    @Override
    public String getResourceType() {
        return resourceType;
    }
    
    @Override
    public String getResourceId() {
        return resourceId;
    }
    
    @Override
    public String getDetails() {
        return details;
    }
    
    @Override
    public String getIpAddress() {
        return ipAddress;
    }
    
    @Override
    public String getUserAgent() {
        return userAgent;
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
    
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String tenantId;
        private String tenantCode;
        private String userId;
        private String operation;
        private String resourceType;
        private String resourceId;
        private String details;
        private String ipAddress;
        private String userAgent;
        private long timestamp = System.currentTimeMillis();
        private boolean success = true;
        private String errorMessage;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }
        
        public Builder tenantCode(String tenantCode) {
            this.tenantCode = tenantCode;
            return this;
        }
        
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder operation(String operation) {
            this.operation = operation;
            return this;
        }
        
        public Builder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }
        
        public Builder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }
        
        public Builder details(String details) {
            this.details = details;
            return this;
        }
        
        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        
        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }
        
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public DefaultTenantAuditLog build() {
            return new DefaultTenantAuditLog(id, tenantId, tenantCode, userId, operation,
                    resourceType, resourceId, details, ipAddress, userAgent, timestamp,
                    success, errorMessage);
        }
    }
}
