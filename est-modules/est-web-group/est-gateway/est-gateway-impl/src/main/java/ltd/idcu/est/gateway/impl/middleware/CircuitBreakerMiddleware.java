package ltd.idcu.est.gateway.impl.middleware;

import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.GatewayMiddleware;

import java.util.function.Function;

public class CircuitBreakerMiddleware implements GatewayMiddleware {
    private final CircuitBreaker circuitBreaker;

    public CircuitBreakerMiddleware(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public String getName() {
        return "circuit-breaker";
    }

    @Override
    public Function<GatewayContext, GatewayContext> process() {
        return context -> {
            try {
                circuitBreaker.execute(() -> null);
            } catch (Exception e) {
                context.setResponseStatus(503);
                context.setResponseBody("Service Unavailable".getBytes());
                context.setAttribute("shouldAbort", true);
            }
            return context;
        };
    }
}
