package ltd.idcu.est.web;

import ltd.idcu.est.features.monitor.api.Metrics;

import java.util.HashMap;
import java.util.Map;

public class MockMetrics implements Metrics {

    private Map<String, Object> metrics = new HashMap<>();
    private Map<String, Long> counters = new HashMap<>();

    @Override
    public Object getMetric(String name) {
        return metrics.get(name);
    }

    @Override
    public Map<String, Object> getAllMetrics() {
        return new HashMap<>(metrics);
    }

    @Override
    public void registerMetric(String name, Object value) {
        metrics.put(name, value);
    }

    @Override
    public void unregisterMetric(String name) {
        metrics.remove(name);
    }

    @Override
    public boolean hasMetric(String name) {
        return metrics.containsKey(name);
    }

    @Override
    public void reset() {
        metrics.clear();
        counters.clear();
    }

    @Override
    public void incrementCounter(String name) {
        counters.put(name, counters.getOrDefault(name, 0L) + 1);
    }

    @Override
    public void decrementCounter(String name) {
        counters.put(name, counters.getOrDefault(name, 0L) - 1);
    }

    @Override
    public void recordHistogram(String name, long value) {
        metrics.put(name, value);
    }

    @Override
    public void recordTimer(String name, long milliseconds) {
        metrics.put(name, milliseconds);
    }

    @Override
    public void recordGauge(String name, Number value) {
        metrics.put(name, value);
    }

    @Override
    public Map<String, ltd.idcu.est.features.monitor.api.Metric> getMetricDetails() {
        return new HashMap<>();
    }

    @Override
    public long getCounter(String name) {
        return counters.getOrDefault(name, 0L);
    }

    @Override
    public double getHistogramPercentile(String name, double percentile) {
        return 0.0;
    }
}
