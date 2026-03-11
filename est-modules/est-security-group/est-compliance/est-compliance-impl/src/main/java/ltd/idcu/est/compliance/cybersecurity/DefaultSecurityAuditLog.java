package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.SecurityAuditLog;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultSecurityAuditLog implements SecurityAuditLog {

    private final String id;
    private final LocalDateTime timestamp;
    private final String userId;
    private final String action;
    private final String resource;
    private final String result;
    private final String ipAddress;
    private final String userAgent;
    private final Map<String, Object> additionalInfo;
    private final boolean sensitive;

    public DefaultSecurityAuditLog(String userId, String action, String resource, String result, String ipAddress, String userAgent, Map<String, Object> additionalInfo, boolean sensitive) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.userId = userId;
        this.action = action;
        this.resource = resource;
        this.result = result;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.additionalInfo = additionalInfo != null ? new HashMap<>(additionalInfo) : new HashMap<>();
        this.sensitive = sensitive;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public String getResult() {
        return result;
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
    public Map<String, Object> getAdditionalInfo() {
        return new HashMap<>(additionalInfo);
    }

    @Override
    public boolean isSensitive() {
        return sensitive;
    }
}
