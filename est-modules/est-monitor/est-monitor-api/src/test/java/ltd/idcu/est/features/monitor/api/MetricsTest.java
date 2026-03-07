package ltd.idcu.est.features.monitor.api;

import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assert.*;

public class MetricsTest {

    @Test
    public void testCounter() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.incrementCounter("requests");
        metrics.incrementCounter("requests");
        metrics.incrementCounter("requests");
        
        assertEquals(3, metrics.getCounter("requests"));
        
        metrics.decrementCounter("requests");
        assertEquals(2, metrics.getCounter("requests"));
    }

    @Test
    public void testGauge() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.recordGauge("memory_used", 1024);
        metrics.recordGauge("memory_used", 2048);
        
        assertEquals(2048, metrics.getMetric("memory_used"));
    }

    @Test
    public void testHistogram() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.recordHistogram("response_time", 100);
        metrics.recordHistogram("response_time", 200);
        metrics.recordHistogram("response_time", 300);
        metrics.recordHistogram("response_time", 400);
        metrics.recordHistogram("response_time", 500);
        
        assertEquals(100, metrics.getHistogramPercentile("response_time", 0.0));
        assertEquals(300, metrics.getHistogramPercentile("response_time", 0.5));
        assertEquals(500, metrics.getHistogramPercentile("response_time", 1.0));
    }

    @Test
    public void testTimer() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.recordTimer("api_call", 150);
        metrics.recordTimer("api_call", 250);
        metrics.recordTimer("api_call", 350);
        
        assertTrue(metrics.hasMetric("api_call"));
    }

    @Test
    public void testRegisterMetric() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.registerMetric("version", "1.0.0");
        metrics.registerMetric("build_time", 123456789L);
        
        assertEquals("1.0.0", metrics.getMetric("version"));
        assertEquals(123456789L, metrics.getMetric("build_time"));
    }

    @Test
    public void testReset() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.incrementCounter("requests");
        metrics.recordGauge("memory", 1024);
        metrics.recordHistogram("latency", 100);
        
        metrics.reset();
        
        assertEquals(0, metrics.getCounter("requests"));
        assertFalse(metrics.hasMetric("memory"));
    }

    @Test
    public void testToMap() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.incrementCounter("requests");
        metrics.incrementCounter("requests");
        metrics.recordGauge("active_connections", 10);
        metrics.recordHistogram("response_time", 100);
        metrics.recordHistogram("response_time", 200);
        metrics.recordTimer("api_call", 150);
        
        Map<String, Object> map = ((DefaultMetrics) metrics).toMap();
        
        assertNotNull(map);
        assertTrue(map.containsKey("counters"));
        assertTrue(map.containsKey("gauges"));
        assertTrue(map.containsKey("histograms"));
        assertTrue(map.containsKey("timers"));
    }

    @Test
    public void testMetricDetails() {
        Metrics metrics = new DefaultMetrics();
        
        metrics.incrementCounter("test_counter");
        metrics.recordGauge("test_gauge", 42);
        
        Map<String, Metric> details = metrics.getMetricDetails();
        
        assertTrue(details.containsKey("test_counter"));
        assertTrue(details.containsKey("test_gauge"));
        
        assertEquals(Metric.Type.COUNTER, details.get("test_counter").getType());
        assertEquals(Metric.Type.GAUGE, details.get("test_gauge").getType());
    }
}
