package ltd.idcu.est.web.api;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public interface Router {

    Router get(String path, String handler);

    Router post(String path, String handler);

    Router put(String path, String handler);

    Router delete(String path, String handler);

    Router patch(String path, String handler);

    Router head(String path, String handler);

    Router options(String path, String handler);

    Router route(String path, HttpMethod method, String handler);

    Router group(String prefix, BiConsumer<Router, Router> groupHandler);

    Router prefix(String prefix);

    Router name(String name);

    Router middleware(String... middleware);

    Router where(String param, String pattern);

    Router where(Map<String, String> patterns);

    List<Route> getRoutes();

    Route match(String path, HttpMethod method);

    default Route match(String path, String method) {
        return match(path, HttpMethod.fromString(method));
    }

    boolean hasRoute(String path, HttpMethod method);

    default boolean hasRoute(String path, String method) {
        return hasRoute(path, HttpMethod.fromString(method));
    }

    List<Route> getRoutesByPath(String path);

    List<Route> getRoutesByMethod(HttpMethod method);

    Route getRouteByName(String name);

    void addRoute(Route route);

    void removeRoute(String path, HttpMethod method);

    void clear();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    interface RouteGroup {
        Router getRouter();
        String getPrefix();
        List<String> getMiddleware();
        Map<String, String> getPatterns();
    }
}
