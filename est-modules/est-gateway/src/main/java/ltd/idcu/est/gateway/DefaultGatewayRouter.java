package ltd.idcu.est.gateway;

import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.Route;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultGatewayRouter implements GatewayRouter {
    private final List&lt;Route&gt; routes;
    private final Map&lt;String, Route&gt; routeByPath;

    public DefaultGatewayRouter() {
        this.routes = new CopyOnWriteArrayList&lt;&gt;();
        this.routeByPath = new ConcurrentHashMap&lt;&gt;();
    }

    @Override
    public void addRoute(Route route) {
        routes.add(route);
        routeByPath.put(route.getPath(), route);
    }

    @Override
    public void addRoute(String path, String serviceId) {
        addRoute(new DefaultRoute(path, serviceId));
    }

    @Override
    public void addRoute(String path, String serviceId, String pathRewrite) {
        addRoute(new DefaultRoute(path, serviceId, pathRewrite));
    }

    @Override
    public Route match(String requestPath) {
        List&lt;Route&gt; matchingRoutes = routes.stream()
                .filter(route -&gt; route.matches(requestPath))
                .sorted(Comparator.comparingInt((Route r) -&gt; r.getPath().length()).reversed())
                .collect(Collectors.toList());
        
        return matchingRoutes.isEmpty() ? null : matchingRoutes.get(0);
    }

    @Override
    public List&lt;Route&gt; getRoutes() {
        return Collections.unmodifiableList(routes);
    }

    @Override
    public void removeRoute(String path) {
        Route route = routeByPath.remove(path);
        if (route != null) {
            routes.remove(route);
        }
    }

    @Override
    public void clear() {
        routes.clear();
        routeByPath.clear();
    }
}
