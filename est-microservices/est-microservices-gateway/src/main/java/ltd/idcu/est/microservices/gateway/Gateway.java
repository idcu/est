package ltd.idcu.est.microservices.gateway;

import ltd.idcu.est.microservices.gateway.api.ApiGateway;
import ltd.idcu.est.microservices.gateway.api.GatewayMiddleware;
import ltd.idcu.est.microservices.gateway.api.GatewayRouter;
import ltd.idcu.est.microservices.gateway.middleware.CorsMiddleware;
import ltd.idcu.est.microservices.gateway.middleware.LoggingMiddleware;

public class Gateway {
    private final DefaultApiGateway gateway;

    private Gateway() {
        this.gateway = new DefaultApiGateway();
    }

    public static Gateway create() {
        return new Gateway();
    }

    public Gateway withLogging() {
        gateway.addMiddleware(new LoggingMiddleware());
        return this;
    }

    public Gateway withCors() {
        gateway.addMiddleware(new CorsMiddleware());
        return this;
    }

    public Gateway withCors(String allowedOrigins, String allowedMethods, 
                          String allowedHeaders, boolean allowCredentials) {
        gateway.addMiddleware(new CorsMiddleware(allowedOrigins, allowedMethods, 
            allowedHeaders, allowCredentials));
        return this;
    }

    public Gateway withMiddleware(GatewayMiddleware middleware) {
        gateway.addMiddleware(middleware);
        return this;
    }

    public Gateway route(String path, String serviceId) {
        gateway.getRouter().addRoute(path, serviceId);
        return this;
    }

    public Gateway route(String path, String serviceId, String pathRewrite) {
        gateway.getRouter().addRoute(path, serviceId, pathRewrite);
        return this;
    }

    public Gateway registerService(String serviceId, String baseUrl) {
        gateway.registerService(serviceId, baseUrl);
        return this;
    }

    public void start(int port) {
        gateway.start(port);
    }

    public void stop() {
        gateway.stop();
    }

    public ApiGateway getApiGateway() {
        return gateway;
    }

    public GatewayRouter getRouter() {
        return gateway.getRouter();
    }
}
