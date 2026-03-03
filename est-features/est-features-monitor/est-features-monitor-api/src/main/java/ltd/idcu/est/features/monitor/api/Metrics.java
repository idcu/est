package ltd.idcu.est.features.monitor.api;

public interface Metrics {
    
    Object getMetric(String name);
    
    java.util.Map<String, Object> getAllMetrics();
    
    void registerMetric(String name, Object value);
    
    void unregisterMetric(String name);
    
    boolean hasMetric(String name);
    
    void reset();
}
