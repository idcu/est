package ltd.idcu.est.discovery.api;

public interface HealthChecker {
    boolean checkHealth(ServiceInstance instance);
}
