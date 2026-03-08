package ltd.idcu.est.gateway.api;

import java.util.List;
import java.util.Map;

public interface GatewayRouter {
    void addRoute(Route route);

    void addRoute(String path, String serviceId);

    void addRoute(String path, String serviceId, String pathRewrite);

    Route match(String requestPath);

    Route match(String requestPath, Map<String, String> requestHeaders, Map<String, String> requestCookies);

    List<Route> getRoutes();

    void removeRoute(String path);

    void clear();
}
