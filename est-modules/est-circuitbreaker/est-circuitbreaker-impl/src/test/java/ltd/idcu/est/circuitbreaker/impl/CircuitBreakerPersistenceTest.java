package ltd.idcu.est.circuitbreaker.impl;

import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;
import ltd.idcu.est.circuitbreaker.api.CircuitState;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class CircuitBreakerPersistenceTest {

    @Test
    public void testCircuitBreakerJsonSerialization() {
        DefaultCircuitBreaker circuitBreaker = new DefaultCircuitBreaker(
            "test-service", 
            new CircuitBreakerConfig(5, 30000, 3, 5000)
        );
        
        for (int i = 0; i < 3; i++) {
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Test failure");
                });
            } catch (Exception e) {
            }
        }
        
        String json = circuitBreaker.toJson();
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("test-service"));
        Assertions.assertTrue(json.contains("failureThreshold"));
        Assertions.assertTrue(json.contains("CLOSED"));
        
        DefaultCircuitBreaker restored = DefaultCircuitBreaker.fromJson(json);
        Assertions.assertEquals("test-service", restored.getName());
        Assertions.assertEquals(5, ((CircuitBreakerConfig) restored.getConfig()).getFailureThreshold());
        Assertions.assertEquals(30000, ((CircuitBreakerConfig) restored.getConfig()).getWaitDurationMs());
    }

    @Test
    public void testCircuitBreakerRegistrySaveAndLoad() throws IOException {
        CircuitBreakerRegistry registry1 = new DefaultCircuitBreakerRegistry();
        
        CircuitBreaker cb1 = registry1.create("service-a");
        CircuitBreaker cb2 = registry1.create("service-b", new CircuitBreakerConfig(10, 60000, 5, 10000));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((DefaultCircuitBreakerRegistry) registry1).saveToJson(outputStream);
        
        CircuitBreakerRegistry registry2 = new DefaultCircuitBreakerRegistry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ((DefaultCircuitBreakerRegistry) registry2).loadFromJson(inputStream);
        
        Map<String, CircuitBreaker> allBreakers = registry2.getAll();
        Assertions.assertEquals(2, allBreakers.size());
        
        Optional<CircuitBreaker> cb1Restored = registry2.get("service-a");
        Assertions.assertTrue(cb1Restored.isPresent());
        
        Optional<CircuitBreaker> cb2Restored = registry2.get("service-b");
        Assertions.assertTrue(cb2Restored.isPresent());
    }

    @Test
    public void testCircuitBreakerRegistryAutoSave() throws IOException, InterruptedException {
        DefaultCircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry();
        String tempPath = System.getProperty("java.io.tmpdir") + "/test-circuit-breaker-registry.json";
        
        registry.setAutoSavePath(tempPath);
        registry.setAutoSave(true);
        
        registry.create("auto-save-test");
        
        Thread.sleep(100);
        
        DefaultCircuitBreakerRegistry registry2 = new DefaultCircuitBreakerRegistry();
        registry2.loadFromJson(tempPath);
        
        Optional<CircuitBreaker> restored = registry2.get("auto-save-test");
        Assertions.assertTrue(restored.isPresent());
        
        java.io.File tempFile = new java.io.File(tempPath);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}
