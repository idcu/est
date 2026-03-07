package ltd.idcu.est.gateway.api;

import javax.net.ssl.SSLContext;

public interface ApiGateway {
    void start(int port);

    void start(int port, SSLContext sslContext);

    void stop();

    GatewayRouter getRouter();

    void addMiddleware(GatewayMiddleware middleware);

    void removeMiddleware(String name);

    void registerService(String serviceId, String baseUrl);

    void registerService(String serviceId, String version, String baseUrl);

    void addWebSocketRoute(String path, WebSocketHandler handler);

    void addWebSocketRoute(String path, String serviceId);

    void removeWebSocketRoute(String path);

    void setCanaryReleaseManager(CanaryReleaseManager manager);

    CanaryReleaseManager getCanaryReleaseManager();
}
