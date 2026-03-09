package ltd.idcu.est.circuitbreaker.api;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TimeBasedCircuitBreaker extends DefaultCircuitBreaker {
    
    private final ConcurrentLinkedQueue<Long> failureTimestamps = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Long> successTimestamps = new ConcurrentLinkedQueue<>();
    private final long timeWindowMs;
    private final int failureThreshold;
    private final int successThreshold;
    
    public TimeBasedCircuitBreaker(String name, CircuitBreakerConfig config) {
        super(name, config);
        this.timeWindowMs = config.getTimeWindowMs();
        this.failureThreshold = config.getFailureThreshold();
        this.successThreshold = config.getSuccessThreshold();
    }
    
    @Override
    protected void onSuccess() {
        successTimestamps.add(System.currentTimeMillis());
        cleanupOldTimestamps();
        
        if (state.get() == CircuitState.HALF_OPEN) {
            int recentSuccesses = countRecent(successTimestamps);
            if (recentSuccesses >= successThreshold) {
                transitionTo(CircuitState.CLOSED);
                resetCounters();
            }
        }
    }
    
    @Override
    protected void onFailure() {
        failureTimestamps.add(System.currentTimeMillis());
        cleanupOldTimestamps();
        
        if (state.get() == CircuitState.CLOSED) {
            int recentFailures = countRecent(failureTimestamps);
            if (recentFailures >= failureThreshold) {
                transitionTo(CircuitState.OPEN);
                resetCounters();
            }
        } else if (state.get() == CircuitState.HALF_OPEN) {
            transitionTo(CircuitState.OPEN);
            resetCounters();
        }
    }
    
    @Override
    protected void resetCounters() {
        failureTimestamps.clear();
        successTimestamps.clear();
    }
    
    private void cleanupOldTimestamps() {
        long threshold = System.currentTimeMillis() - timeWindowMs;
        while (!failureTimestamps.isEmpty() && failureTimestamps.peek() < threshold) {
            failureTimestamps.poll();
        }
        while (!successTimestamps.isEmpty() && successTimestamps.peek() < threshold) {
            successTimestamps.poll();
        }
    }
    
    private int countRecent(ConcurrentLinkedQueue<Long> timestamps) {
        cleanupOldTimestamps();
        return timestamps.size();
    }
    
    @Override
    public CircuitBreakerMetrics getMetrics() {
        cleanupOldTimestamps();
        return new CircuitBreakerMetrics(
                successTimestamps.size(),
                failureTimestamps.size(),
                0,
                successTimestamps.size() + failureTimestamps.size(),
                0,
                0,
                0
        );
    }
}
