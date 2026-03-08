package ltd.idcu.est.monitor.api;

import java.util.Map;

public enum HealthStatus {
    
    HEALTHY("healthy", "All systems operational"),
    DEGRADED("degraded", "System is running but with issues"),
    UNHEALTHY("unhealthy", "System is not operational"),
    UNKNOWN("unknown", "Health status could not be determined");
    
    public enum Name {
        HEALTHY,
        DEGRADED,
        UNHEALTHY,
        UNKNOWN
    }
    
    private final String name;
    private final String description;
    private String message;
    private Map<String, Object> details;
    private Throwable error;
    
    HealthStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public static HealthStatus healthy(String message) {
        HealthStatus status = HEALTHY;
        status.message = message;
        return status;
    }
    
    public static HealthStatus healthy(String message, Map<String, Object> details) {
        HealthStatus status = HEALTHY;
        status.message = message;
        status.details = details;
        return status;
    }
    
    public static HealthStatus degraded(String message) {
        HealthStatus status = DEGRADED;
        status.message = message;
        return status;
    }
    
    public static HealthStatus degraded(String message, Map<String, Object> details) {
        HealthStatus status = DEGRADED;
        status.message = message;
        status.details = details;
        return status;
    }
    
    public static HealthStatus unhealthy(String message) {
        HealthStatus status = UNHEALTHY;
        status.message = message;
        return status;
    }
    
    public static HealthStatus unhealthy(String message, Throwable error) {
        HealthStatus status = UNHEALTHY;
        status.message = message;
        status.error = error;
        return status;
    }
    
    public static HealthStatus unhealthy(String message, Map<String, Object> details) {
        HealthStatus status = UNHEALTHY;
        status.message = message;
        status.details = details;
        return status;
    }
    
    public static HealthStatus unhealthy(String message, Map<String, Object> details, Throwable error) {
        HealthStatus status = UNHEALTHY;
        status.message = message;
        status.details = details;
        status.error = error;
        return status;
    }
    
    public static HealthStatus unknown(String message) {
        HealthStatus status = UNKNOWN;
        status.message = message;
        return status;
    }
    
    public static HealthStatus unknown(String message, Map<String, Object> details) {
        HealthStatus status = UNKNOWN;
        status.message = message;
        status.details = details;
        return status;
    }
    
    public Name getName() {
        switch (this) {
            case HEALTHY: return Name.HEALTHY;
            case DEGRADED: return Name.DEGRADED;
            case UNHEALTHY: return Name.UNHEALTHY;
            case UNKNOWN: return Name.UNKNOWN;
            default: throw new IllegalStateException();
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getMessage() {
        return message != null ? message : description;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
    
    public Throwable getError() {
        return error;
    }
    
    public boolean isHealthy() {
        return this == HEALTHY;
    }
    
    public boolean isDegraded() {
        return this == DEGRADED;
    }
    
    public boolean isUnhealthy() {
        return this == UNHEALTHY;
    }
}
