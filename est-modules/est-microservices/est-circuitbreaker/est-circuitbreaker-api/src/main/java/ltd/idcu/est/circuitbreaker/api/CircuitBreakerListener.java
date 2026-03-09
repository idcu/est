package ltd.idcu.est.circuitbreaker.api;

public interface CircuitBreakerListener {
    void onStateChange(String circuitBreakerName, CircuitState oldState, CircuitState newState);

    void onSuccess(String circuitBreakerName, long durationMs);

    void onFailure(String circuitBreakerName, Throwable throwable, long durationMs);

    void onReject(String circuitBreakerName);
}
