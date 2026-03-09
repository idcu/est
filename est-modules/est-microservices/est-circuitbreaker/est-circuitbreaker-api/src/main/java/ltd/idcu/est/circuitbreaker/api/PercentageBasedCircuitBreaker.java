package ltd.idcu.est.circuitbreaker.api;

import java.util.concurrent.atomic.AtomicInteger;

public class PercentageBasedCircuitBreaker extends DefaultCircuitBreaker {
    
    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final double failureRateThreshold;
    private final int minimumRequestCount;
    private final int successThreshold;
    
    public PercentageBasedCircuitBreaker(String name, CircuitBreakerConfig config) {
        super(name, config);
        this.failureRateThreshold = config.getFailureRateThreshold();
        this.minimumRequestCount = config.getMinimumRequestCount();
        this.successThreshold = config.getSuccessThreshold();
    }
    
    @Override
    protected void onSuccess() {
        totalRequests.incrementAndGet();
        successCount.incrementAndGet();
        
        if (state.get() == CircuitState.HALF_OPEN && successCount.get() >= successThreshold) {
            transitionTo(CircuitState.CLOSED);
            resetCounters();
        }
    }
    
    @Override
    protected void onFailure() {
        totalRequests.incrementAndGet();
        failureCount.incrementAndGet();
        
        if (state.get() == CircuitState.CLOSED) {
            int total = totalRequests.get();
            if (total >= minimumRequestCount) {
                double failureRate = (double) failureCount.get() / total;
                if (failureRate >= failureRateThreshold) {
                    transitionTo(CircuitState.OPEN);
                    resetCounters();
                }
            }
        } else if (state.get() == CircuitState.HALF_OPEN) {
            transitionTo(CircuitState.OPEN);
            resetCounters();
        }
    }
    
    @Override
    protected void resetCounters() {
        totalRequests.set(0);
        failureCount.set(0);
        successCount.set(0);
    }
    
    public double getFailureRate() {
        int total = totalRequests.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) failureCount.get() / total;
    }
    
    @Override
    public CircuitBreakerMetrics getMetrics() {
        return new CircuitBreakerMetrics(
                successCount.get(),
                failureCount.get(),
                0,
                totalRequests.get(),
                0,
                0,
                0
        );
    }
}
