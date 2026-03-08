package ltd.idcu.est.admin.api;

import java.util.Map;

public interface MonitorService {
    
    Map<String, Object> getJvmMetrics();
    
    Map<String, Object> getSystemMetrics();
    
    Map<String, Object> getHealthChecks();
    
    Map<String, Object> getAllMetrics();
}
