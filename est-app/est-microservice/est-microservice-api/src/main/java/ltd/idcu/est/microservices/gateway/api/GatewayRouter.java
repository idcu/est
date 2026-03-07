package ltd.idcu.est.microservices.gateway.api;

import java.util.List;

public interface GatewayRouter {
    void addRoute(Route route);

    void addRoute(String path, String serviceId);

    void addRoute(String path, String serviceId, String pathRewrite);

    Route match(String requestPath);

    List<Route> getRoutes();

    void removeRoute(String path);

    void clear();
}
