package ltd.idcu.est.circuitbreaker.api;

import java.util.concurrent.atomic.AtomicInteger;

public class CountBasedCircuitBreaker extends DefaultCircuitBreaker {
    
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final int failureThreshold;
    private final int successThreshold;
    
    public CountBasedCircuitBreaker(String name, CircuitBreakerConfig config) {
        super(name, config);
        this.failureThreshold = config.getFailureThreshold();
        this.successThreshold = config.getSuccessThreshold();
    }
    
    @Override
    protected void onSuccess() {
        successCount.incrementAndGet();
        if (state.get() == CircuitState.HALF_OPEN && successCount.get() >= successThreshold) {
            transitionTo(CircuitState.CLOSED);
            resetCounters();
        }
    }
    
    @Override
    protected void onFailure() {
        failureCount.incrementAndGet();
        if (state.get() == CircuitState.CLOSED && failureCount.get() >= failureThreshold) {
            transitionTo(CircuitState.OPEN);
            resetCounters();
        } else if (state.get() == CircuitState.HALF_OPEN) {
            transitionTo(CircuitState.OPEN);
            resetCounters();
        }
    }
    
    @Override
    protected void resetCounters() {
        successCount.set(0);
        failureCount.set(0);
    }
    
    @Override
    public CircuitBreakerMetrics getMetrics() {
        return new CircuitBreakerMetrics(
                successCount.get(),
                failureCount.get(),
                0,
                successCount.get() + failureCount.get(),
                0,
                0,
                0
        );
    }
}
