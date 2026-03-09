package ltd.idcu.est.circuitbreaker.api;

public enum CircuitBreakerStrategy {
    
    COUNT_BASED,
    TIME_BASED,
    PERCENTAGE_BASED,
    ADAPTIVE,
    MANUAL
}
