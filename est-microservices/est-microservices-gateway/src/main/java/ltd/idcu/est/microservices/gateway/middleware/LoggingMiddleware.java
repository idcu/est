package ltd.idcu.est.microservices.gateway.middleware;

import ltd.idcu.est.microservices.gateway.api.GatewayContext;
import ltd.idcu.est.microservices.gateway.api.GatewayMiddleware;

import java.time.Instant;
import java.util.function.Function;

public class LoggingMiddleware implements GatewayMiddleware {
    private static final String NAME = "logging";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Function<GatewayContext, GatewayContext> process() {
        return context -> {
            long startTime = System.currentTimeMillis();
            String requestId = generateRequestId();
            
            context.setAttribute("requestId", requestId);
            context.setAttribute("startTime", startTime);
            
            System.out.printf("[%s] [%s] %s %s%n", 
                Instant.now(), 
                requestId, 
                context.getRequestMethod(), 
                context.getRequestPath());
            
            return context;
        };
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private String generateRequestId() {
        return Long.toHexString(System.currentTimeMillis()) + 
               "-" + 
               Integer.toHexString((int) (Math.random() * 0xFFFF));
    }
}
