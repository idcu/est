package ltd.idcu.est.observability.api;

public interface Observability {
    
    MetricsExporter getMetricsExporter();
    
    LogsExporter getLogsExporter();
    
    TracesExporter getTracesExporter();
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    String getServiceName();
    
    String getServiceVersion();
}
