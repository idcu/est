package ltd.idcu.est.monitor.api;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class DefaultMetrics implements Metrics {
    private final Map<String, Object> metrics = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> histograms = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> timers = new ConcurrentHashMap<>();
    private final Map<String, Number> gauges = new ConcurrentHashMap<>();
    private final Map<String, Metric> metricDetails = new ConcurrentHashMap<>();

    @Override
    public Object getMetric(String name) {
        return metrics.get(name);
    }

    @Override
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> all = new LinkedHashMap<>();
        all.putAll(metrics);
        counters.forEach((k, v) -> all.put(k, v.get()));
        gauges.forEach((k, v) -> all.put(k, v));
        return Collections.unmodifiableMap(all);
    }

    @Override
    public void registerMetric(String name, Object value) {
        metrics.put(name, value);
        metricDetails.put(name, new Metric(name, Metric.Type.GAUGE, value));
    }

    @Override
    public void unregisterMetric(String name) {
        metrics.remove(name);
        counters.remove(name);
        histograms.remove(name);
        timers.remove(name);
        gauges.remove(name);
        metricDetails.remove(name);
    }

    @Override
    public boolean hasMetric(String name) {
        return metrics.containsKey(name) || 
               counters.containsKey(name) || 
               histograms.containsKey(name) || 
               timers.containsKey(name) || 
               gauges.containsKey(name);
    }

    @Override
    public void reset() {
        metrics.clear();
        counters.values().forEach(c -> c.set(0));
        histograms.clear();
        timers.clear();
        metricDetails.clear();
    }

    @Override
    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong(0)).incrementAndGet();
        metricDetails.put(name, new Metric(name, Metric.Type.COUNTER, counters.get(name).get()));
    }

    @Override
    public void decrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong(0)).decrementAndGet();
        metricDetails.put(name, new Metric(name, Metric.Type.COUNTER, counters.get(name).get()));
    }

    @Override
    public void recordHistogram(String name, long value) {
        histograms.computeIfAbsent(name, k -> Collections.synchronizedList(new ArrayList<>())).add(value);
        metricDetails.put(name, new Metric(name, Metric.Type.HISTOGRAM, summarizeHistogram(name)));
    }

    @Override
    public void recordTimer(String name, long milliseconds) {
        timers.computeIfAbsent(name, k -> Collections.synchronizedList(new ArrayList<>())).add(milliseconds);
        metricDetails.put(name, new Metric(name, Metric.Type.TIMER, summarizeTimer(name)));
    }

    @Override
    public void recordGauge(String name, Number value) {
        gauges.put(name, value);
        metricDetails.put(name, new Metric(name, Metric.Type.GAUGE, value));
    }

    @Override
    public Map<String, Metric> getMetricDetails() {
        return Collections.unmodifiableMap(metricDetails);
    }

    @Override
    public long getCounter(String name) {
        AtomicLong counter = counters.get(name);
        return counter != null ? counter.get() : 0;
    }

    @Override
    public double getHistogramPercentile(String name, double percentile) {
        List<Long> values = histograms.get(name);
        if (values == null || values.isEmpty()) {
            return 0;
        }
        
        List<Long> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        
        int index = (int) Math.ceil(percentile * sorted.size()) - 1;
        return sorted.get(Math.max(0, Math.min(index, sorted.size() - 1)));
    }

    private Map<String, Object> summarizeHistogram(String name) {
        List<Long> values = histograms.get(name);
        if (values == null || values.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("count", values.size());
        summary.put("min", Collections.min(values));
        summary.put("max", Collections.max(values));
        summary.put("mean", values.stream().mapToLong(Long::longValue).average().orElse(0));
        summary.put("p50", getHistogramPercentile(name, 0.5));
        summary.put("p75", getHistogramPercentile(name, 0.75));
        summary.put("p95", getHistogramPercentile(name, 0.95));
        summary.put("p99", getHistogramPercentile(name, 0.99));
        return summary;
    }

    private Map<String, Object> summarizeTimer(String name) {
        List<Long> values = timers.get(name);
        if (values == null || values.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("count", values.size());
        summary.put("min", Collections.min(values));
        summary.put("max", Collections.max(values));
        summary.put("mean", values.stream().mapToLong(Long::longValue).average().orElse(0));
        return summary;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        
        Map<String, Object> countersMap = new LinkedHashMap<>();
        counters.forEach((k, v) -> countersMap.put(k, v.get()));
        if (!countersMap.isEmpty()) {
            map.put("counters", countersMap);
        }

        Map<String, Object> gaugesMap = new LinkedHashMap<>();
        gauges.forEach((k, v) -> gaugesMap.put(k, v));
        if (!gaugesMap.isEmpty()) {
            map.put("gauges", gaugesMap);
        }

        Map<String, Object> histogramsMap = new LinkedHashMap<>();
        histograms.keySet().forEach(k -> histogramsMap.put(k, summarizeHistogram(k)));
        if (!histogramsMap.isEmpty()) {
            map.put("histograms", histogramsMap);
        }

        Map<String, Object> timersMap = new LinkedHashMap<>();
        timers.keySet().forEach(k -> timersMap.put(k, summarizeTimer(k)));
        if (!timersMap.isEmpty()) {
            map.put("timers", timersMap);
        }

        Map<String, Object> customMap = new LinkedHashMap<>(metrics);
        if (!customMap.isEmpty()) {
            map.put("custom", customMap);
        }

        return map;
    }
}
