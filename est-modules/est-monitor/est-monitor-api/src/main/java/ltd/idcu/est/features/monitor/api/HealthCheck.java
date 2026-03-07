package ltd.idcu.est.features.monitor.api;

public interface HealthCheck {
    
    HealthStatus check();
    
    HealthStatus getStatus();
    
    String getName();
    
    String getDescription();
}
