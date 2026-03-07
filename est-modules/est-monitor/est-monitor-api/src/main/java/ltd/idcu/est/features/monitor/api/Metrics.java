package ltd.idcu.est.features.monitor.api;

public interface Metrics {
    
    Object getMetric(String name);
    
    java.util.Map<String, Object> getAllMetrics();
    
    void registerMetric(String name, Object value);
    
    void unregisterMetric(String name);
    
    boolean hasMetric(String name);
    
    void reset();

    void incrementCounter(String name);

    void decrementCounter(String name);

    void recordHistogram(String name, long value);

    void recordTimer(String name, long milliseconds);

    void recordGauge(String name, Number value);

    java.util.Map<String, Metric> getMetricDetails();

    long getCounter(String name);

    double getHistogramPercentile(String name, double percentile);
}
