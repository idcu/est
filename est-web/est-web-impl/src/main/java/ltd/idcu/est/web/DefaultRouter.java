package ltd.idcu.est.web;

import ltd.idcu.est.web.api.AsyncHandler;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Route;
import ltd.idcu.est.web.api.RouteHandler;
import ltd.idcu.est.web.api.Router;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class DefaultRouter implements Router {

    private final List<Route> routes;
    private final Map<String, Route> namedRoutes;
    private final Map<HttpMethod, Map<String, Route>> routeCache;
    private String currentPrefix;
    private String currentName;
    private List<String> currentMiddleware;

    public DefaultRouter() {
        this.routes = Collections.synchronizedList(new ArrayList<>());
        this.namedRoutes = new ConcurrentHashMap<>();
        this.routeCache = new ConcurrentHashMap<>();
        this.currentPrefix = "";
        this.currentName = "";
        this.currentMiddleware = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public Router get(String path, String handler) {
        return route(path, HttpMethod.GET, handler);
    }

    @Override
    public Router get(String path, RouteHandler handler) {
        return route(path, HttpMethod.GET, handler);
    }

    @Override
    public Router get(String path, AsyncHandler handler) {
        return route(path, HttpMethod.GET, handler);
    }

    @Override
    public Router post(String path, String handler) {
        return route(path, HttpMethod.POST, handler);
    }

    @Override
    public Router post(String path, RouteHandler handler) {
        return route(path, HttpMethod.POST, handler);
    }

    @Override
    public Router post(String path, AsyncHandler handler) {
        return route(path, HttpMethod.POST, handler);
    }

    @Override
    public Router put(String path, String handler) {
        return route(path, HttpMethod.PUT, handler);
    }

    @Override
    public Router put(String path, RouteHandler handler) {
        return route(path, HttpMethod.PUT, handler);
    }

    @Override
    public Router put(String path, AsyncHandler handler) {
        return route(path, HttpMethod.PUT, handler);
    }

    @Override
    public Router delete(String path, String handler) {
        return route(path, HttpMethod.DELETE, handler);
    }

    @Override
    public Router delete(String path, RouteHandler handler) {
        return route(path, HttpMethod.DELETE, handler);
    }

    @Override
    public Router delete(String path, AsyncHandler handler) {
        return route(path, HttpMethod.DELETE, handler);
    }

    @Override
    public Router patch(String path, String handler) {
        return route(path, HttpMethod.PATCH, handler);
    }

    @Override
    public Router patch(String path, RouteHandler handler) {
        return route(path, HttpMethod.PATCH, handler);
    }

    @Override
    public Router patch(String path, AsyncHandler handler) {
        return route(path, HttpMethod.PATCH, handler);
    }

    @Override
    public Router head(String path, String handler) {
        return route(path, HttpMethod.HEAD, handler);
    }

    @Override
    public Router head(String path, RouteHandler handler) {
        return route(path, HttpMethod.HEAD, handler);
    }

    @Override
    public Router head(String path, AsyncHandler handler) {
        return route(path, HttpMethod.HEAD, handler);
    }

    @Override
    public Router options(String path, String handler) {
        return route(path, HttpMethod.OPTIONS, handler);
    }

    @Override
    public Router options(String path, RouteHandler handler) {
        return route(path, HttpMethod.OPTIONS, handler);
    }

    @Override
    public Router options(String path, AsyncHandler handler) {
        return route(path, HttpMethod.OPTIONS, handler);
    }

    @Override
    public Router route(String path, HttpMethod method, String handler) {
        String fullPath = currentPrefix.isEmpty() ? path : currentPrefix + path;
        DefaultRoute route = new DefaultRoute(fullPath, method, handler);
        addRouteInternal(route);
        return this;
    }

    @Override
    public Router route(String path, HttpMethod method, RouteHandler handler) {
        String fullPath = currentPrefix.isEmpty() ? path : currentPrefix + path;
        DefaultRoute route = new DefaultRoute(fullPath, method, handler);
        addRouteInternal(route);
        return this;
    }

    @Override
    public Router route(String path, HttpMethod method, AsyncHandler handler) {
        String fullPath = currentPrefix.isEmpty() ? path : currentPrefix + path;
        DefaultRoute route = new DefaultRoute(fullPath, method, handler);
        addRouteInternal(route);
        return this;
    }

    private void addRouteInternal(DefaultRoute route) {
        if (!currentName.isEmpty()) {
            namedRoutes.put(currentName, route);
        }
        
        for (String middleware : currentMiddleware) {
            route.addMiddleware(middleware);
        }
        
        routes.add(route);
    }

    @Override
    public Router group(String prefix, BiConsumer<Router, Router> groupHandler) {
        String oldPrefix = this.currentPrefix;
        List<String> oldMiddleware = new ArrayList<>(this.currentMiddleware);
        
        this.currentPrefix = oldPrefix + prefix;
        groupHandler.accept(this, this);
        
        this.currentPrefix = oldPrefix;
        this.currentMiddleware = oldMiddleware;
        return this;
    }

    @Override
    public Router prefix(String prefix) {
        this.currentPrefix = prefix;
        return this;
    }

    @Override
    public Router name(String name) {
        this.currentName = name;
        return this;
    }

    @Override
    public Router middleware(String... middleware) {
        Collections.addAll(this.currentMiddleware, middleware);
        return this;
    }

    @Override
    public Router where(String param, String pattern) {
        return this;
    }

    @Override
    public Router where(Map<String, String> patterns) {
        return this;
    }

    @Override
    public List<Route> getRoutes() {
        return Collections.unmodifiableList(routes);
    }

    @Override
    public Route match(String path, HttpMethod method) {
        Map<String, Route> methodCache = routeCache.computeIfAbsent(method, k -> new ConcurrentHashMap<>());
        Route cached = methodCache.get(path);
        if (cached != null && cached.matches(path, method)) {
            return cached;
        }
        
        for (Route route : routes) {
            if (route.matches(path, method)) {
                methodCache.put(path, route);
                return route;
            }
        }
        return null;
    }

    @Override
    public boolean hasRoute(String path, HttpMethod method) {
        return match(path, method) != null;
    }

    @Override
    public List<Route> getRoutesByPath(String path) {
        List<Route> result = new ArrayList<>();
        for (Route route : routes) {
            if (route.matchesPath(path)) {
                result.add(route);
            }
        }
        return result;
    }

    @Override
    public List<Route> getRoutesByMethod(HttpMethod method) {
        List<Route> result = new ArrayList<>();
        for (Route route : routes) {
            if (route.getMethod() == method) {
                result.add(route);
            }
        }
        return result;
    }

    @Override
    public Route getRouteByName(String name) {
        return namedRoutes.get(name);
    }

    @Override
    public void addRoute(Route route) {
        routes.add(route);
        if (route.getName() != null && !route.getName().isEmpty()) {
            namedRoutes.put(route.getName(), route);
        }
    }

    @Override
    public void removeRoute(String path, HttpMethod method) {
        routes.removeIf(route -> route.getPath().equals(path) && route.getMethod() == method);
    }

    @Override
    public void clear() {
        routes.clear();
        namedRoutes.clear();
        routeCache.clear();
    }

    @Override
    public int size() {
        return routes.size();
    }
}
