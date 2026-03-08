package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.CanaryReleaseConfig;
import ltd.idcu.est.gateway.api.CanaryReleaseManager;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.RateLimiter;
import ltd.idcu.est.gateway.api.WebSocketHandler;
import ltd.idcu.est.gateway.impl.middleware.CircuitBreakerMiddleware;
import ltd.idcu.est.gateway.impl.middleware.CorsMiddleware;
import ltd.idcu.est.gateway.impl.middleware.LoggingMiddleware;
import ltd.idcu.est.gateway.impl.middleware.RateLimitMiddleware;

import javax.net.ssl.SSLContext;

public class Gateway {
    private final DefaultApiGateway gateway;
    private SSLContext sslContext;
    private CanaryReleaseManager canaryReleaseManager;

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

    public Gateway withRateLimiting(long capacity, long refillRate) {
        RateLimiter rateLimiter = new TokenBucketRateLimiter(capacity, refillRate);
        gateway.addMiddleware(new RateLimitMiddleware(rateLimiter));
        return this;
    }

    public Gateway withRateLimiting(RateLimiter rateLimiter) {
        gateway.addMiddleware(new RateLimitMiddleware(rateLimiter));
        return this;
    }

    public Gateway withCircuitBreaker(String serviceName) {
        CircuitBreakerConfig config = CircuitBreakerConfig.builder().build();
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker(serviceName, config);
        gateway.addMiddleware(new CircuitBreakerMiddleware(circuitBreaker));
        return this;
    }

    public Gateway withCircuitBreaker(String serviceName, CircuitBreakerConfig config) {
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker(serviceName, config);
        gateway.addMiddleware(new CircuitBreakerMiddleware(circuitBreaker));
        return this;
    }

    public Gateway withCircuitBreaker(CircuitBreaker circuitBreaker) {
        gateway.addMiddleware(new CircuitBreakerMiddleware(circuitBreaker));
        return this;
    }

    public Gateway withSsl(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public Gateway withSsl(String keystorePath, String keystorePassword, String keyPassword) throws Exception {
        this.sslContext = SslContextFactory.createFromKeystore(keystorePath, keystorePassword, keyPassword);
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

    public Gateway registerService(String serviceId, String version, String baseUrl) {
        gateway.registerService(serviceId, version, baseUrl);
        return this;
    }

    public Gateway withCanaryRelease() {
        this.canaryReleaseManager = new DefaultCanaryReleaseManager();
        gateway.setCanaryReleaseManager(canaryReleaseManager);
        return this;
    }

    public Gateway withCanaryRelease(CanaryReleaseManager manager) {
        this.canaryReleaseManager = manager;
        gateway.setCanaryReleaseManager(manager);
        return this;
    }

    public Gateway addCanaryConfig(CanaryReleaseConfig config) {
        if (canaryReleaseManager == null) {
            withCanaryRelease();
        }
        canaryReleaseManager.addConfig(config);
        return this;
    }

    public Gateway addCanaryConfig(String serviceId, String primaryVersion, String canaryVersion, int canaryPercentage) {
        return addCanaryConfig(new CanaryReleaseConfig(serviceId, primaryVersion, canaryVersion, canaryPercentage));
    }

    public void start(int port) {
        if (sslContext != null) {
            gateway.start(port, sslContext);
        } else {
            gateway.start(port);
        }
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

    public Gateway webSocketRoute(String path, WebSocketHandler handler) {
        gateway.addWebSocketRoute(path, handler);
        return this;
    }

    public Gateway webSocketRoute(String path, String serviceId) {
        gateway.addWebSocketRoute(path, serviceId);
        return this;
    }
}
