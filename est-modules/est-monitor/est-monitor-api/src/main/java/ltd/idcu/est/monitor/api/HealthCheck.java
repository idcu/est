package ltd.idcu.est.monitor.api;

public interface HealthCheck {
    
    HealthStatus check();
    
    HealthStatus getStatus();
    
    String getName();
    
    String getDescription();
}
