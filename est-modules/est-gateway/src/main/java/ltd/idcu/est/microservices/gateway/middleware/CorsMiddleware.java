package ltd.idcu.est.microservices.gateway.middleware;

import ltd.idcu.est.microservices.gateway.DefaultGatewayContext;
import ltd.idcu.est.microservices.gateway.api.GatewayContext;
import ltd.idcu.est.microservices.gateway.api.GatewayMiddleware;

import java.util.function.Function;

public class CorsMiddleware implements GatewayMiddleware {
    private static final String NAME = "cors";
    private final String allowedOrigins;
    private final String allowedMethods;
    private final String allowedHeaders;
    private final boolean allowCredentials;

    public CorsMiddleware() {
        this("*", "GET,POST,PUT,DELETE,PATCH,OPTIONS", "*", true);
    }

    public CorsMiddleware(String allowedOrigins, String allowedMethods, 
                         String allowedHeaders, boolean allowCredentials) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.allowCredentials = allowCredentials;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Function<GatewayContext, GatewayContext> process() {
        return context -> {
            if (context instanceof DefaultGatewayContext) {
                DefaultGatewayContext ctx = (DefaultGatewayContext) context;
                ctx.addResponseHeader("Access-Control-Allow-Origin", allowedOrigins);
                ctx.addResponseHeader("Access-Control-Allow-Methods", allowedMethods);
                ctx.addResponseHeader("Access-Control-Allow-Headers", allowedHeaders);
                ctx.addResponseHeader("Access-Control-Allow-Credentials", 
                    String.valueOf(allowCredentials));
            }
            return context;
        };
    }

    @Override
    public int getOrder() {
        return -50;
    }
}
