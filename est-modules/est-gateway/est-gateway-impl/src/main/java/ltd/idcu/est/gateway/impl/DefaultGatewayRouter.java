package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.Route;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultGatewayRouter implements GatewayRouter {
    private final List<Route> routes;
    private final Map<String, Route> routeByPath;

    public DefaultGatewayRouter() {
        this.routes = new CopyOnWriteArrayList<>();
        this.routeByPath = new ConcurrentHashMap<>();
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
        return match(requestPath, Collections.emptyMap(), Collections.emptyMap());
    }

    @Override
    public Route match(String requestPath, Map<String, String> requestHeaders, Map<String, String> requestCookies) {
        List<Route> matchingRoutes = routes.stream()
                .filter(route -> route.matches(requestPath, requestHeaders, requestCookies))
                .sorted(Comparator.comparingInt((Route r) -> r.getPath().length()).reversed())
                .collect(Collectors.toList());
        
        return matchingRoutes.isEmpty() ? null : matchingRoutes.get(0);
    }

    @Override
    public List<Route> getRoutes() {
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
