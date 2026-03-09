package ltd.idcu.est.observability.api;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Map;
import java.util.function.Supplier;

public class DefaultObservabilityTest {

    @Test
    public void testConstructorAndGetters() {
        NoOpMetricsExporter metrics = new NoOpMetricsExporter();
        NoOpLogsExporter logs = new NoOpLogsExporter();
        NoOpTracesExporter traces = new NoOpTracesExporter();

        DefaultObservability observability = new DefaultObservability(
                "test-service", "1.0.0", metrics, logs, traces);

        Assertions.assertEquals("test-service", observability.getServiceName());
        Assertions.assertEquals("1.0.0", observability.getServiceVersion());
        Assertions.assertEquals(metrics, observability.getMetricsExporter());
        Assertions.assertEquals(logs, observability.getLogsExporter());
        Assertions.assertEquals(traces, observability.getTracesExporter());
        Assertions.assertFalse(observability.isRunning());
    }

    @Test
    public void testStartStop() {
        NoOpMetricsExporter metrics = new NoOpMetricsExporter();
        NoOpLogsExporter logs = new NoOpLogsExporter();
        NoOpTracesExporter traces = new NoOpTracesExporter();

        DefaultObservability observability = new DefaultObservability(
                "test-service", "1.0.0", metrics, logs, traces);

        observability.start();
        Assertions.assertTrue(observability.isRunning());
        Assertions.assertTrue(metrics.started);
        Assertions.assertTrue(logs.started);
        Assertions.assertTrue(traces.started);

        observability.stop();
        Assertions.assertFalse(observability.isRunning());
        Assertions.assertFalse(metrics.started);
        Assertions.assertFalse(logs.started);
        Assertions.assertFalse(traces.started);
    }

    @Test
    public void testStartTwice() {
        NoOpMetricsExporter metrics = new NoOpMetricsExporter();
        NoOpLogsExporter logs = new NoOpLogsExporter();
        NoOpTracesExporter traces = new NoOpTracesExporter();

        DefaultObservability observability = new DefaultObservability(
                "test-service", "1.0.0", metrics, logs, traces);

        observability.start();
        metrics.started = false;
        logs.started = false;
        traces.started = false;

        observability.start();
        Assertions.assertTrue(observability.isRunning());
        Assertions.assertFalse(metrics.started);
        Assertions.assertFalse(logs.started);
        Assertions.assertFalse(traces.started);
    }

    @Test
    public void testStopTwice() {
        NoOpMetricsExporter metrics = new NoOpMetricsExporter();
        NoOpLogsExporter logs = new NoOpLogsExporter();
        NoOpTracesExporter traces = new NoOpTracesExporter();

        DefaultObservability observability = new DefaultObservability(
                "test-service", "1.0.0", metrics, logs, traces);

        observability.start();
        observability.stop();
        metrics.started = true;
        logs.started = true;
        traces.started = true;

        observability.stop();
        Assertions.assertFalse(observability.isRunning());
        Assertions.assertTrue(metrics.started);
        Assertions.assertTrue(logs.started);
        Assertions.assertTrue(traces.started);
    }

    @Test
    public void testWithNullExporters() {
        DefaultObservability observability = new DefaultObservability(
                "test-service", "1.0.0", null, null, null);

        Assertions.assertNull(observability.getMetricsExporter());
        Assertions.assertNull(observability.getLogsExporter());
        Assertions.assertNull(observability.getTracesExporter());

        observability.start();
        Assertions.assertTrue(observability.isRunning());

        observability.stop();
        Assertions.assertFalse(observability.isRunning());
    }

    private static class NoOpMetricsExporter implements MetricsExporter {
        boolean started = false;

        @Override
        public void start() {
            started = true;
        }

        @Override
        public void stop() {
            started = false;
        }

        @Override
        public boolean isRunning() {
            return started;
        }

        @Override
        public void registerCounter(String name, String help) {}

        @Override
        public void registerCounter(String name, String help, String... labelNames) {}

        @Override
        public void incrementCounter(String name) {}

        @Override
        public void incrementCounter(String name, long amount) {}

        @Override
        public void incrementCounter(String name, Map<String, String> labels) {}

        @Override
        public void registerGauge(String name, String help) {}

        @Override
        public void registerGauge(String name, String help, String... labelNames) {}

        @Override
        public void setGauge(String name, double value) {}

        @Override
        public void setGauge(String name, double value, Map<String, String> labels) {}

        @Override
        public void registerHistogram(String name, String help, double... buckets) {}

        @Override
        public void recordHistogram(String name, double value) {}

        @Override
        public void recordHistogram(String name, double value, Map<String, String> labels) {}

        @Override
        public void registerTimer(String name, String help) {}

        @Override
        public void recordTimer(String name, long milliseconds) {}

        @Override
        public void recordTimer(String name, long milliseconds, Map<String, String> labels) {}

        @Override
        public String scrape() {
            return "";
        }

        @Override
        public Map<String, Object> getMetrics() {
            return Map.of();
        }
    }

    private static class NoOpLogsExporter implements LogsExporter {
        boolean started = false;

        @Override
        public void start() {
            started = true;
        }

        @Override
        public void stop() {
            started = false;
        }

        @Override
        public boolean isRunning() {
            return started;
        }

        @Override
        public void log(LogLevel level, String message) {}

        @Override
        public void log(LogLevel level, String message, Map<String, Object> context) {}

        @Override
        public void log(LogLevel level, String message, Throwable throwable) {}

        @Override
        public void log(LogLevel level, String message, Throwable throwable, Map<String, Object> context) {}

        @Override
        public void debug(String message) {}

        @Override
        public void debug(String message, Map<String, Object> context) {}

        @Override
        public void info(String message) {}

        @Override
        public void info(String message, Map<String, Object> context) {}

        @Override
        public void warn(String message) {}

        @Override
        public void warn(String message, Map<String, Object> context) {}

        @Override
        public void error(String message) {}

        @Override
        public void error(String message, Map<String, Object> context) {}

        @Override
        public void error(String message, Throwable throwable) {}

        @Override
        public void error(String message, Throwable throwable, Map<String, Object> context) {}
    }

    private static class NoOpTracesExporter implements TracesExporter {
        boolean started = false;

        @Override
        public void start() {
            started = true;
        }

        @Override
        public void stop() {
            started = false;
        }

        @Override
        public boolean isRunning() {
            return started;
        }

        @Override
        public TraceScope startSpan(String spanName) {
            String traceId = SimpleTraceContext.generateTraceId();
            String spanId = SimpleTraceContext.generateSpanId();
            return new SimpleTraceScope(spanName, new SimpleTraceContext(traceId, spanId, true), this);
        }

        @Override
        public TraceScope startSpan(String spanName, TraceContext parent) {
            String traceId = parent.getTraceId();
            String spanId = SimpleTraceContext.generateSpanId();
            String parentSpanId = parent.getSpanId();
            return new SimpleTraceScope(spanName, new SimpleTraceContext(traceId, spanId, parentSpanId, parent.isSampled()), this);
        }

        @Override
        public void endSpan(TraceScope scope) {}

        @Override
        public void endSpan(TraceScope scope, boolean success) {}

        @Override
        public void addTag(TraceScope scope, String key, String value) {}

        @Override
        public void addTag(TraceScope scope, String key, long value) {}

        @Override
        public void addTag(TraceScope scope, String key, boolean value) {}

        @Override
        public void addTag(TraceScope scope, String key, double value) {}

        @Override
        public void addEvent(TraceScope scope, String eventName) {}

        @Override
        public void addEvent(TraceScope scope, String eventName, Map<String, Object> attributes) {}

        @Override
        public <T> T trace(String spanName, Supplier<T> supplier) {
            return supplier.get();
        }

        @Override
        public void trace(String spanName, Runnable runnable) {
            runnable.run();
        }

        @Override
        public TraceContext getCurrentContext() {
            return null;
        }

        @Override
        public void setCurrentContext(TraceContext context) {}

        @Override
        public void clearCurrentContext() {}

        @Override
        public String generateTraceId() {
            return "";
        }

        @Override
        public String generateSpanId() {
            return "";
        }
    }
}
