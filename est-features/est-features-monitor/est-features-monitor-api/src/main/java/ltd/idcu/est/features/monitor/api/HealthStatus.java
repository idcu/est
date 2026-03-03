package ltd.idcu.est.features.monitor.api;

public enum HealthStatus {
    
    HEALTHY("healthy", "All systems operational"),
    DEGRADED("degraded", "System is running but with issues"),
    UNHEALTHY("unhealthy", "System is not operational"),
    UNKNOWN("unknown", "Health status could not be determined");
    
    private final String name;
    private final String description;
    
    HealthStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
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
