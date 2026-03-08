package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.MonitorService;
import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.system.SystemMonitor;

import java.util.HashMap;
import java.util.Map;

public class DefaultMonitorService implements MonitorService {
    
    private final JvmMonitor jvmMonitor;
    private final SystemMonitor systemMonitor;
    
    public DefaultMonitorService() {
        this.jvmMonitor = JvmMonitor.getInstance();
        this.systemMonitor = SystemMonitor.getInstance();
    }
    
    @Override
    public Map<String, Object> getJvmMetrics() {
        Map<String, Object> result = new HashMap<>();
        result.put("metrics", jvmMonitor.getJvmMetrics().getAllMetrics());
        result.put("uptime", jvmMonitor.getUptime());
        result.put("jvmInfo", jvmMonitor.getJvmInfo());
        result.put("health", jvmMonitor.checkHealth());
        return result;
    }
    
    @Override
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> result = new HashMap<>();
        result.put("metrics", systemMonitor.getSystemMetrics().getAllMetrics());
        result.put("osInfo", systemMonitor.getOsInfo());
        result.put("health", systemMonitor.checkHealth());
        return result;
    }
    
    @Override
    public Map<String, Object> getHealthChecks() {
        Map<String, Object> result = new HashMap<>();
        result.put("jvm", jvmMonitor.checkAllHealth());
        result.put("system", systemMonitor.checkAllHealth());
        return result;
    }
    
    @Override
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> result = new HashMap<>();
        result.put("jvm", getJvmMetrics());
        result.put("system", getSystemMetrics());
        result.put("health", getHealthChecks());
        return result;
    }
}
