package ltd.idcu.est.gateway.middleware;

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
    public Function&lt;GatewayContext, GatewayContext&gt; process() {
        return context -&gt; {
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
