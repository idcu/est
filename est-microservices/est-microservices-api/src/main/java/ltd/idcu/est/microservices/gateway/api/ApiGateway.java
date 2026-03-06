package ltd.idcu.est.microservices.gateway.api;

public interface ApiGateway {
    void start(int port);

    void stop();

    GatewayRouter getRouter();

    void addMiddleware(GatewayMiddleware middleware);

    void removeMiddleware(String name);
}
