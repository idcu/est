package ltd.idcu.est.features.performance.api;

import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assert.*;

public class PerformanceTest {

    @Test
    public void testGCMetrics() {
        GCTuner tuner = new GCTuner();
        GCMetrics metrics = tuner.collectMetrics();
        
        assertNotNull(metrics);
        assertTrue(metrics.getTotalGcCount() >= 0);
        assertTrue(metrics.getTotalGcTime() >= 0);
        assertTrue(metrics.getHeapUsed() >= 0);
        assertTrue(metrics.getHeapUsagePercent() >= 0);
        assertTrue(metrics.getHeapUsagePercent() <= 100);
    }

    @Test
    public void testGCRecommendation() {
        GCTuner tuner = new GCTuner();
        GCMetrics metrics = tuner.collectMetrics();
        GCRecommendation recommendation = tuner.getRecommendation(metrics);
        
        assertNotNull(recommendation);
        assertNotNull(recommendation.getPriority());
        assertNotNull(recommendation.getRecommendations());
        assertFalse(recommendation.getRecommendations().isEmpty());
    }

    @Test
    public void testJVMInfo() {
        GCTuner tuner = new GCTuner();
        String jvmInfo = tuner.getJVMInfo();
        
        assertNotNull(jvmInfo);
        assertTrue(jvmInfo.contains("Java Version"));
        assertTrue(jvmInfo.contains("Max Memory"));
    }

    @Test
    public void testHttpServerOptimizer() {
        HttpServerOptimizer optimizer = new HttpServerOptimizer();
        
        assertNotNull(optimizer);
        assertTrue(optimizer.getThreadPoolSize() > 0);
        assertTrue(optimizer.getConnectionTimeout() > 0);
        assertTrue(optimizer.getMaxRequestSize() > 0);
    }

    @Test
    public void testHttpServerOptimizerForProduction() {
        HttpServerOptimizer optimizer = HttpServerOptimizer.forProduction();
        
        assertNotNull(optimizer);
        assertEquals(100, optimizer.getBacklog());
        assertTrue(optimizer.isEnableCompression());
        assertTrue(optimizer.isEnableCaching());
    }

    @Test
    public void testHttpServerOptimizerForDevelopment() {
        HttpServerOptimizer optimizer = HttpServerOptimizer.forDevelopment();
        
        assertNotNull(optimizer);
        assertEquals(0, optimizer.getBacklog());
        assertFalse(optimizer.isEnableCompression());
        assertFalse(optimizer.isEnableCaching());
    }

    @Test
    public void testHttpServerOptimizerToMap() {
        HttpServerOptimizer optimizer = new HttpServerOptimizer();
        Map<String, Object> config = optimizer.toMap();
        
        assertNotNull(config);
        assertTrue(config.containsKey("backlog"));
        assertTrue(config.containsKey("threadPoolSize"));
        assertTrue(config.containsKey("useVirtualThreads"));
    }

    @Test
    public void testRequestMetrics() {
        RequestMetrics metrics = new RequestMetrics();
        
        metrics.recordRequest("/api/users", 200, 100);
        metrics.recordRequest("/api/users", 200, 150);
        metrics.recordRequest("/api/users", 500, 50);
        
        assertEquals(3, metrics.getTotalRequests());
        assertEquals(2, metrics.getSuccessfulRequests());
        assertEquals(1, metrics.getFailedRequests());
        assertEquals(100.0, metrics.getAverageResponseTime(), 0.1);
        assertEquals(150, metrics.getMaxResponseTime());
        assertEquals(50, metrics.getMinResponseTime());
        assertTrue(metrics.getSuccessRate() > 0);
        assertTrue(metrics.getUptimeMs() >= 0);
    }

    @Test
    public void testRequestMetricsReset() {
        RequestMetrics metrics = new RequestMetrics();
        
        metrics.recordRequest("/test", 200, 100);
        assertEquals(1, metrics.getTotalRequests());
        
        metrics.reset();
        assertEquals(0, metrics.getTotalRequests());
        assertEquals(0, metrics.getSuccessfulRequests());
        assertEquals(0, metrics.getFailedRequests());
    }

    @Test
    public void testRequestMetricsStatusCodes() {
        RequestMetrics metrics = new RequestMetrics();
        
        metrics.recordRequest("/a", 200, 10);
        metrics.recordRequest("/b", 404, 10);
        metrics.recordRequest("/c", 500, 10);
        metrics.recordRequest("/a", 200, 10);
        
        Map<Integer, AtomicLong> statusCodes = metrics.getStatusCodes();
        assertEquals(3, statusCodes.size());
        assertTrue(statusCodes.containsKey(200));
        assertTrue(statusCodes.containsKey(404));
        assertTrue(statusCodes.containsKey(500));
        assertEquals(2, statusCodes.get(200).get());
    }

    @Test
    public void testRequestMetricsPathCounts() {
        RequestMetrics metrics = new RequestMetrics();
        
        metrics.recordRequest("/api/users", 200, 10);
        metrics.recordRequest("/api/users", 200, 10);
        metrics.recordRequest("/api/orders", 200, 10);
        
        Map<String, AtomicLong> pathCounts = metrics.getPathCounts();
        assertEquals(2, pathCounts.size());
        assertTrue(pathCounts.containsKey("/api/users"));
        assertTrue(pathCounts.containsKey("/api/orders"));
        assertEquals(2, pathCounts.get("/api/users").get());
    }
}
