package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.SecurityEvent;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityEventType;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityEventSeverity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultSecurityEvent implements SecurityEvent {

    private final String id;
    private final SecurityEventType type;
    private final SecurityEventSeverity severity;
    private final String source;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, Object> details;
    private final String userId;
    private final String ipAddress;

    public DefaultSecurityEvent(SecurityEventType type, SecurityEventSeverity severity, String source, String message, Map<String, Object> details, String userId, String ipAddress) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.severity = severity;
        this.source = source;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = details != null ? new HashMap<>(details) : new HashMap<>();
        this.userId = userId;
        this.ipAddress = ipAddress;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public SecurityEventType getType() {
        return type;
    }

    @Override
    public SecurityEventSeverity getSeverity() {
        return severity;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, Object> getDetails() {
        return new HashMap<>(details);
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }
}
