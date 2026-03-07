package ltd.idcu.est.circuitbreaker.api;

import java.util.ArrayList;
import java.util.List;

public class PrometheusCollector {
    private final List<CircuitBreaker> circuitBreakers = new ArrayList<>();

    public void register(CircuitBreaker circuitBreaker) {
        circuitBreakers.add(circuitBreaker);
    }

    public String collect() {
        StringBuilder sb = new StringBuilder();
        for (CircuitBreaker cb : circuitBreakers) {
            appendMetrics(sb, cb);
        }
        return sb.toString();
    }

    private void appendMetrics(StringBuilder sb, CircuitBreaker cb) {
        String name = cb.getName();
        CircuitBreakerMetrics metrics = cb.getMetrics();
        CircuitState state = cb.getState();

        sb.append("# HELP circuit_breaker_state Current state of the circuit breaker (0=CLOSED, 1=OPEN, 2=HALF_OPEN)\n");
        sb.append("# TYPE circuit_breaker_state gauge\n");
        sb.append("circuit_breaker_state{name=\"").append(name).append("\"} ").append(stateToNumber(state)).append("\n");

        sb.append("# HELP circuit_breaker_success_total Total number of successful calls\n");
        sb.append("# TYPE circuit_breaker_success_total counter\n");
        sb.append("circuit_breaker_success_total{name=\"").append(name).append("\"} ").append(metrics.getSuccessCount()).append("\n");

        sb.append("# HELP circuit_breaker_failure_total Total number of failed calls\n");
        sb.append("# TYPE circuit_breaker_failure_total counter\n");
        sb.append("circuit_breaker_failure_total{name=\"").append(name).append("\"} ").append(metrics.getFailureCount()).append("\n");

        sb.append("# HELP circuit_breaker_timeout_total Total number of timeout calls\n");
        sb.append("# TYPE circuit_breaker_timeout_total counter\n");
        sb.append("circuit_breaker_timeout_total{name=\"").append(name).append("\"} ").append(metrics.getTimeoutCount()).append("\n");

        sb.append("# HELP circuit_breaker_requests_total Total number of requests\n");
        sb.append("# TYPE circuit_breaker_requests_total counter\n");
        sb.append("circuit_breaker_requests_total{name=\"").append(name).append("\"} ").append(metrics.getTotalRequests()).append("\n");

        sb.append("# HELP circuit_breaker_rejected_total Total number of rejected requests\n");
        sb.append("# TYPE circuit_breaker_rejected_total counter\n");
        sb.append("circuit_breaker_rejected_total{name=\"").append(name).append("\"} ").append(metrics.getRejectedRequests()).append("\n");
    }

    private int stateToNumber(CircuitState state) {
        switch (state) {
            case CLOSED:
                return 0;
            case OPEN:
                return 1;
            case HALF_OPEN:
                return 2;
            default:
                return -1;
        }
    }
}
