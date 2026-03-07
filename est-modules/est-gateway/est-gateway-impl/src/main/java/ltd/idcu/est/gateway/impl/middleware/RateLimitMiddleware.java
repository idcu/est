package ltd.idcu.est.gateway.impl.middleware;

import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.RateLimiter;

import java.util.function.Function;

public class RateLimitMiddleware implements GatewayMiddleware {
    private final RateLimiter rateLimiter;

    public RateLimitMiddleware(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public String getName() {
        return "rate-limit";
    }

    @Override
    public Function<GatewayContext, GatewayContext> process() {
        return context -> {
            if (!rateLimiter.tryAcquire()) {
                context.setResponseStatus(429);
                context.setResponseBody("Too Many Requests".getBytes());
                context.setAttribute("shouldAbort", true);
            }
            return context;
        };
    }
}
