package ltd.idcu.est.gateway.impl.middleware;

import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.GatewayMiddleware;

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
            
            logRequest(requestId, context);
            
            return context;
        };
    }

    private void logRequest(String requestId, GatewayContext context) {
        System.out.printf("[%s] [%s] REQ %s %s%n", 
            Instant.now(), 
            requestId, 
            context.getRequestMethod(), 
            context.getRequestPath());
    }

    public static void logResponse(String requestId, long startTime, GatewayContext context) {
        long duration = System.currentTimeMillis() - startTime;
        int status = context.getResponseStatus();
        System.out.printf("[%s] [%s] RES %s %d (%dms)%n", 
            Instant.now(), 
            requestId, 
            context.getRequestPath(), 
            status,
            duration);
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
