package ltd.idcu.est.features.monitor.api;

import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.Map;

import static ltd.idcu.est.test.Assert.*;

public class HealthCheckTest {

    static class SimpleHealthCheck implements HealthCheck {
        private final String name;
        private boolean healthy;

        public SimpleHealthCheck(String name, boolean healthy) {
            this.name = name;
            this.healthy = healthy;
        }

        @Override
        public HealthStatus check() {
            if (healthy) {
                return HealthStatus.healthy(name + " is healthy");
            } else {
                return HealthStatus.unhealthy(name + " is unhealthy");
            }
        }

        @Override
        public HealthStatus getStatus() {
            return check();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return "Simple health check for " + name;
        }
    }

    @Test
    public void testHealthCheckResultHealthy() {
        HealthCheckResult result = HealthCheckResult.healthy("database", "Connection successful");
        
        assertEquals(HealthStatus.Name.HEALTHY, result.getStatus().getName());
        assertEquals("database", result.getName());
        assertEquals("Connection successful", result.getMessage());
        assertTrue(result.isHealthy());
        assertFalse(result.isDegraded());
        assertFalse(result.isUnhealthy());
        assertNotNull(result.getTimestamp());
    }

    @Test
    public void testHealthCheckResultUnhealthy() {
        Exception error = new RuntimeException("Connection failed");
        HealthCheckResult result = HealthCheckResult.unhealthy("database", "Connection error", error);
        
        assertEquals(HealthStatus.Name.UNHEALTHY, result.getStatus().getName());
        assertEquals("database", result.getName());
        assertEquals("Connection error", result.getMessage());
        assertFalse(result.isHealthy());
        assertTrue(result.isUnhealthy());
        assertNotNull(result.getError());
        assertEquals("Connection failed", result.getError().getMessage());
    }

    @Test
    public void testHealthCheckResultDegraded() {
        Map<String, Object> details = new HashMap<>();
        details.put("responseTime", 5000);
        HealthCheckResult result = HealthCheckResult.degraded("cache", "Slow response", details);
        
        assertEquals(HealthStatus.Name.DEGRADED, result.getStatus().getName());
        assertEquals("cache", result.getName());
        assertEquals("Slow response", result.getMessage());
        assertTrue(result.isDegraded());
        assertNotNull(result.getDetails());
        assertEquals(5000, result.getDetails().get("responseTime"));
    }

    @Test
    public void testHealthCheckRegistry() {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        
        registry.register(new SimpleHealthCheck("database", true));
        registry.register(new SimpleHealthCheck("cache", true));
        
        assertEquals(2, registry.getNames().size());
        assertTrue(registry.getHealthCheck("database").isPresent());
        assertTrue(registry.getHealthCheck("cache").isPresent());
        
        Map<String, HealthCheckResult> results = registry.checkAll();
        assertEquals(2, results.size());
        assertTrue(results.get("database").isHealthy());
        assertTrue(results.get("cache").isHealthy());
        
        HealthStatus aggregateStatus = registry.getAggregateStatus();
        assertEquals(HealthStatus.Name.HEALTHY, aggregateStatus.getName());
    }

    @Test
    public void testHealthCheckRegistryAggregateUnhealthy() {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        
        registry.register(new SimpleHealthCheck("database", true));
        registry.register(new SimpleHealthCheck("cache", false));
        
        HealthStatus aggregateStatus = registry.getAggregateStatus();
        assertEquals(HealthStatus.Name.UNHEALTHY, aggregateStatus.getName());
    }

    @Test
    public void testHealthCheckRegistryToMap() {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        
        registry.register(new SimpleHealthCheck("database", true));
        
        Map<String, Object> map = registry.toMap();
        
        assertNotNull(map);
        assertTrue(map.containsKey("status"));
        assertTrue(map.containsKey("timestamp"));
        assertTrue(map.containsKey("checks"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> checks = (Map<String, Object>) map.get("checks");
        assertTrue(checks.containsKey("database"));
    }

    @Test
    public void testMemoryHealthCheck() {
        MemoryHealthCheck healthCheck = new MemoryHealthCheck();
        
        assertEquals("memory", healthCheck.getName());
        assertNotNull(healthCheck.getDescription());
        
        HealthStatus status = healthCheck.check();
        assertNotNull(status);
        assertTrue(status.isHealthy() || status.isDegraded());
        assertNotNull(status.getMessage());
        assertNotNull(status.getDetails());
        assertTrue(status.getDetails().containsKey("usagePercent"));
    }

    @Test
    public void testThreadHealthCheck() {
        ThreadHealthCheck healthCheck = new ThreadHealthCheck();
        
        assertEquals("threads", healthCheck.getName());
        assertNotNull(healthCheck.getDescription());
        
        HealthStatus status = healthCheck.check();
        assertNotNull(status);
        assertTrue(status.isHealthy() || status.isDegraded());
        assertNotNull(status.getMessage());
        assertNotNull(status.getDetails());
        assertTrue(status.getDetails().containsKey("threadCount"));
    }

    @Test
    public void testDiskSpaceHealthCheck() {
        DiskSpaceHealthCheck healthCheck = new DiskSpaceHealthCheck();
        
        assertEquals("disk", healthCheck.getName());
        assertNotNull(healthCheck.getDescription());
        
        HealthStatus status = healthCheck.check();
        assertNotNull(status);
        assertTrue(status.isHealthy() || status.isDegraded() || status.isUnhealthy());
        assertNotNull(status.getMessage());
        assertNotNull(status.getDetails());
        assertTrue(status.getDetails().containsKey("freeSpacePercent"));
    }

    @Test
    public void testHealthCheckRegistryWithBuiltInChecks() {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        
        registry.register(new MemoryHealthCheck());
        registry.register(new ThreadHealthCheck());
        registry.register(new DiskSpaceHealthCheck());
        
        assertEquals(3, registry.getNames().size());
        
        Map<String, HealthCheckResult> results = registry.checkAll();
        assertEquals(3, results.size());
        
        HealthStatus aggregateStatus = registry.getAggregateStatus();
        assertNotNull(aggregateStatus);
    }
}
