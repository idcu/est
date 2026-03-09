package ltd.idcu.est.observability.api;

public class DefaultObservability implements Observability {

    private final MetricsExporter metricsExporter;
    private final LogsExporter logsExporter;
    private final TracesExporter tracesExporter;
    private final String serviceName;
    private final String serviceVersion;
    private boolean running = false;

    public DefaultObservability(String serviceName, String serviceVersion, 
                                 MetricsExporter metricsExporter, 
                                 LogsExporter logsExporter, 
                                 TracesExporter tracesExporter) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.metricsExporter = metricsExporter;
        this.logsExporter = logsExporter;
        this.tracesExporter = tracesExporter;
    }

    @Override
    public MetricsExporter getMetricsExporter() {
        return metricsExporter;
    }

    @Override
    public LogsExporter getLogsExporter() {
        return logsExporter;
    }

    @Override
    public TracesExporter getTracesExporter() {
        return tracesExporter;
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        if (metricsExporter != null) {
            metricsExporter.start();
        }
        if (logsExporter != null) {
            logsExporter.start();
        }
        if (tracesExporter != null) {
            tracesExporter.start();
        }
        running = true;
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }
        if (metricsExporter != null) {
            metricsExporter.stop();
        }
        if (logsExporter != null) {
            logsExporter.stop();
        }
        if (tracesExporter != null) {
            tracesExporter.stop();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getServiceVersion() {
        return serviceVersion;
    }
}
