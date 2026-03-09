package ltd.idcu.est.observability.api;

import java.util.Map;

public interface MetricsExporter {
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    void registerCounter(String name, String help);
    
    void registerCounter(String name, String help, String... labelNames);
    
    void incrementCounter(String name);
    
    void incrementCounter(String name, long amount);
    
    void incrementCounter(String name, Map<String, String> labels);
    
    void registerGauge(String name, String help);
    
    void registerGauge(String name, String help, String... labelNames);
    
    void setGauge(String name, double value);
    
    void setGauge(String name, double value, Map<String, String> labels);
    
    void registerHistogram(String name, String help, double... buckets);
    
    void recordHistogram(String name, double value);
    
    void recordHistogram(String name, double value, Map<String, String> labels);
    
    void registerTimer(String name, String help);
    
    void recordTimer(String name, long milliseconds);
    
    void recordTimer(String name, long milliseconds, Map<String, String> labels);
    
    String scrape();
    
    Map<String, Object> getMetrics();
}
