package ltd.idcu.est.features.circuitbreaker.api;

import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assert.*;

public class CircuitBreakerTest {

    @Test
    public void testCircuitBreakerClosedState() {
        CircuitBreakerConfig config = new CircuitBreakerConfig(3, 10000, 2, 5000);
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test", config);
        
        assertEquals(CircuitState.CLOSED, circuitBreaker.getState());
        
        String result = circuitBreaker.execute(() -> "success");
        assertEquals("success", result);
        
        CircuitBreakerMetrics metrics = circuitBreaker.getMetrics();
        assertEquals(1, metrics.getSuccessCount());
        assertEquals(0, metrics.getFailureCount());
    }

    @Test
    public void testCircuitBreakerOpenOnFailures() {
        CircuitBreakerConfig config = new CircuitBreakerConfig(3, 10000, 2, 5000);
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test", config);
        
        for (int i = 0; i < 3; i++) {
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Failure");
                });
            } catch (Exception e) {
            }
        }
        
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());
        
        boolean exceptionThrown = false;
        try {
            circuitBreaker.execute(() -> "should not execute");
        } catch (DefaultCircuitBreaker.CircuitBreakerOpenException e) {
            exceptionThrown = true;
        } catch (Exception e) {
        }
        
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCircuitBreakerReset() {
        CircuitBreakerConfig config = new CircuitBreakerConfig(3, 10000, 2, 5000);
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test", config);
        
        for (int i = 0; i < 3; i++) {
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Failure");
                });
            } catch (Exception e) {
            }
        }
        
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());
        
        circuitBreaker.reset();
        assertEquals(CircuitState.CLOSED, circuitBreaker.getState());
        
        CircuitBreakerMetrics metrics = circuitBreaker.getMetrics();
        assertEquals(0, metrics.getTotalRequests());
    }

    @Test
    public void testCircuitBreakerMetrics() {
        CircuitBreakerConfig config = new CircuitBreakerConfig(5, 10000, 2, 5000);
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test", config);
        
        for (int i = 0; i < 5; i++) {
            try {
                if (i < 3) {
                    circuitBreaker.execute(() -> "success");
                } else {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                }
            } catch (Exception e) {
            }
        }
        
        CircuitBreakerMetrics metrics = circuitBreaker.getMetrics();
        assertEquals(3, metrics.getSuccessCount());
        assertEquals(2, metrics.getFailureCount());
        assertEquals(5, metrics.getTotalRequests());
        assertEquals(0.4, metrics.getFailureRate(), 0.01);
    }

    @Test
    public void testCircuitBreakerRegistry() {
        CircuitBreakerRegistry registry = new CircuitBreakerRegistry();
        
        CircuitBreaker cb1 = registry.create("service-a");
        CircuitBreaker cb2 = registry.create("service-b");
        
        assertNotNull(cb1);
        assertNotNull(cb2);
        assertEquals("service-a", cb1.getName());
        assertEquals("service-b", cb2.getName());
        
        assertEquals(2, registry.getAll().size());
        
        registry.remove("service-a");
        assertEquals(1, registry.getAll().size());
    }
}
