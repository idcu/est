package ltd.idcu.est.observability.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import ltd.idcu.est.observability.api.MetricsExporter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusMetricsExporter implements MetricsExporter {

    private final CollectorRegistry registry;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();
    private final Map<String, Gauge> gauges = new ConcurrentHashMap<>();
    private final Map<String, Histogram> histograms = new ConcurrentHashMap<>();
    private final Map<String, Summary> summaries = new ConcurrentHashMap<>();
    private HTTPServer httpServer;
    private boolean running = false;
    private final int port;

    public PrometheusMetricsExporter(int port) {
        this.port = port;
        this.registry = CollectorRegistry.defaultRegistry;
    }

    public PrometheusMetricsExporter() {
        this(9090);
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        try {
            DefaultExports.initialize();
            httpServer = new HTTPServer(port);
            running = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to start Prometheus HTTP server", e);
        }
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }
        if (httpServer != null) {
            httpServer.stop();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void registerCounter(String name, String help) {
        registerCounter(name, help, new String[0]);
    }

    @Override
    public void registerCounter(String name, String help, String... labelNames) {
        if (counters.containsKey(name)) {
            return;
        }
        Counter.Builder builder = Counter.build().name(name).help(help);
        if (labelNames.length > 0) {
            builder.labelNames(labelNames);
        }
        Counter counter = builder.register(registry);
        counters.put(name, counter);
    }

    @Override
    public void incrementCounter(String name) {
        Counter counter = counters.get(name);
        if (counter != null) {
            counter.inc();
        }
    }

    @Override
    public void incrementCounter(String name, long amount) {
        Counter counter = counters.get(name);
        if (counter != null) {
            counter.inc(amount);
        }
    }

    @Override
    public void incrementCounter(String name, Map<String, String> labels) {
        Counter counter = counters.get(name);
        if (counter != null) {
            counter.labels(labels.values().toArray(new String[0])).inc();
        }
    }

    @Override
    public void registerGauge(String name, String help) {
        registerGauge(name, help, new String[0]);
    }

    @Override
    public void registerGauge(String name, String help, String... labelNames) {
        if (gauges.containsKey(name)) {
            return;
        }
        Gauge.Builder builder = Gauge.build().name(name).help(help);
        if (labelNames.length > 0) {
            builder.labelNames(labelNames);
        }
        Gauge gauge = builder.register(registry);
        gauges.put(name, gauge);
    }

    @Override
    public void setGauge(String name, double value) {
        Gauge gauge = gauges.get(name);
        if (gauge != null) {
            gauge.set(value);
        }
    }

    @Override
    public void setGauge(String name, double value, Map<String, String> labels) {
        Gauge gauge = gauges.get(name);
        if (gauge != null) {
            gauge.labels(labels.values().toArray(new String[0])).set(value);
        }
    }

    @Override
    public void registerHistogram(String name, String help, double... buckets) {
        if (histograms.containsKey(name)) {
            return;
        }
        Histogram.Builder builder = Histogram.build().name(name).help(help);
        if (buckets.length > 0) {
            builder.buckets(buckets);
        }
        Histogram histogram = builder.register(registry);
        histograms.put(name, histogram);
    }

    @Override
    public void recordHistogram(String name, double value) {
        Histogram histogram = histograms.get(name);
        if (histogram != null) {
            histogram.observe(value);
        }
    }

    @Override
    public void recordHistogram(String name, double value, Map<String, String> labels) {
        Histogram histogram = histograms.get(name);
        if (histogram != null) {
            histogram.labels(labels.values().toArray(new String[0])).observe(value);
        }
    }

    @Override
    public void registerTimer(String name, String help) {
        if (summaries.containsKey(name)) {
            return;
        }
        Summary summary = Summary.build().name(name).help(help).register(registry);
        summaries.put(name, summary);
    }

    @Override
    public void recordTimer(String name, long milliseconds) {
        Summary summary = summaries.get(name);
        if (summary != null) {
            summary.observe(milliseconds);
        }
    }

    @Override
    public void recordTimer(String name, long milliseconds, Map<String, String> labels) {
        Summary summary = summaries.get(name);
        if (summary != null) {
            summary.labels(labels.values().toArray(new String[0])).observe(milliseconds);
        }
    }

    @Override
    public String scrape() {
        StringWriter writer = new StringWriter();
        try {
            registry.metricFamilySamples().asIterator().forEachRemaining(samples -> {
                writer.write(samples.toString());
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to scrape metrics", e);
        }
        return writer.toString();
    }

    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("counters", counters.keySet());
        metrics.put("gauges", gauges.keySet());
        metrics.put("histograms", histograms.keySet());
        metrics.put("summaries", summaries.keySet());
        metrics.put("port", port);
        return metrics;
    }
}
