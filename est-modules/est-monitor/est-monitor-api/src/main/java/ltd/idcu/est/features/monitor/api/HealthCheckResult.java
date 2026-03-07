package ltd.idcu.est.features.monitor.api;

import java.time.Instant;
import java.util.Map;

public class HealthCheckResult {
    
    private final HealthStatus status;
    private final String name;
    private final String message;
    private final Instant timestamp;
    private final Map<String, Object> details;
    private final Throwable error;
    
    public HealthCheckResult(HealthStatus status, String name, String message) {
        this(status, name, message, null, null);
    }
    
    public HealthCheckResult(HealthStatus status, String name, String message, 
                            Map<String, Object> details) {
        this(status, name, message, details, null);
    }
    
    public HealthCheckResult(HealthStatus status, String name, String message, 
                            Map<String, Object> details, Throwable error) {
        this.status = status;
        this.name = name;
        this.message = message;
        this.timestamp = Instant.now();
        this.details = details;
        this.error = error;
    }
    
    public static HealthCheckResult healthy(String name, String message) {
        return new HealthCheckResult(HealthStatus.HEALTHY, name, message);
    }
    
    public static HealthCheckResult healthy(String name, String message, 
                                           Map<String, Object> details) {
        return new HealthCheckResult(HealthStatus.HEALTHY, name, message, details);
    }
    
    public static HealthCheckResult degraded(String name, String message) {
        return new HealthCheckResult(HealthStatus.DEGRADED, name, message);
    }
    
    public static HealthCheckResult degraded(String name, String message, 
                                            Map<String, Object> details) {
        return new HealthCheckResult(HealthStatus.DEGRADED, name, message, details);
    }
    
    public static HealthCheckResult unhealthy(String name, String message) {
        return new HealthCheckResult(HealthStatus.UNHEALTHY, name, message);
    }
    
    public static HealthCheckResult unhealthy(String name, String message, 
                                             Map<String, Object> details) {
        return new HealthCheckResult(HealthStatus.UNHEALTHY, name, message, details);
    }
    
    public static HealthCheckResult unhealthy(String name, String message, 
                                             Throwable error) {
        return new HealthCheckResult(HealthStatus.UNHEALTHY, name, message, null, error);
    }
    
    public static HealthCheckResult unknown(String name, String message) {
        return new HealthCheckResult(HealthStatus.UNKNOWN, name, message);
    }
    
    public HealthStatus getStatus() {
        return status;
    }
    
    public String getName() {
        return name;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
    
    public Throwable getError() {
        return error;
    }
    
    public boolean isHealthy() {
        return status.isHealthy();
    }
    
    public boolean isDegraded() {
        return status.isDegraded();
    }
    
    public boolean isUnhealthy() {
        return status.isUnhealthy();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HealthCheckResult{status=").append(status.getName());
        sb.append(", name='").append(name).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", timestamp=").append(timestamp);
        if (details != null && !details.isEmpty()) {
            sb.append(", details=").append(details);
        }
        if (error != null) {
            sb.append(", error=").append(error.getMessage());
        }
        sb.append('}');
        return sb.toString();
    }
}
