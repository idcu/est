package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Router;
import ltd.idcu.est.web.api.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class DefaultRouter implements Router {

    private List<Route> routes = new ArrayList<>();
    private String currentPrefix = "";
    private String currentName;
    private List<String> currentMiddleware = new ArrayList<>();
    private Map<String, String> currentPatterns = new HashMap<>();

    @Override
    public Router get(String path, String handler) {
        return route(path, HttpMethod.GET, handler);
    }

    @Override
    public Router post(String path, String handler) {
        return route(path, HttpMethod.POST, handler);
    }

    @Override
    public Router put(String path, String handler) {
        return route(path, HttpMethod.PUT, handler);
    }

    @Override
    public Router delete(String path, String handler) {
        return route(path, HttpMethod.DELETE, handler);
    }

    @Override
    public Router patch(String path, String handler) {
        return route(path, HttpMethod.PATCH, handler);
    }

    @Override
    public Router head(String path, String handler) {
        return route(path, HttpMethod.HEAD, handler);
    }

    @Override
    public Router options(String path, String handler) {
        return route(path, HttpMethod.OPTIONS, handler);
    }

    @Override
    public Router route(String path, HttpMethod method, String handler) {
        String fullPath = currentPrefix + path;
        Route route = new DefaultRoute(fullPath, method, handler);
        addRoute(route);
        return this;
    }

    @Override
    public Router group(String prefix, BiConsumer<Router, Router> groupHandler) {
        String oldPrefix = currentPrefix;
        List<String> oldMiddleware = new ArrayList<>(currentMiddleware);
        Map<String, String> oldPatterns = new HashMap<>(currentPatterns);
        
        currentPrefix = oldPrefix + prefix;
        groupHandler.accept(this, this);
        
        currentPrefix = oldPrefix;
        currentMiddleware = oldMiddleware;
        currentPatterns = oldPatterns;
        
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
        for (String m : middleware) {
            this.currentMiddleware.add(m);
        }
        return this;
    }

    @Override
    public Router where(String param, String pattern) {
        this.currentPatterns.put(param, pattern);
        return this;
    }

    @Override
    public Router where(Map<String, String> patterns) {
        this.currentPatterns.putAll(patterns);
        return this;
    }

    @Override
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public Route match(String path, HttpMethod method) {
        for (Route route : routes) {
            if (route.matches(path, method)) {
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
            if (route.getPath().equals(path)) {
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
        for (Route route : routes) {
            if (name.equals(route.getName())) {
                return route;
            }
        }
        return null;
    }

    @Override
    public void addRoute(Route route) {
        routes.add(route);
    }

    @Override
    public void removeRoute(String path, HttpMethod method) {
        routes.removeIf(route -> route.getPath().equals(path) && route.getMethod() == method);
    }

    @Override
    public void clear() {
        routes.clear();
    }

    @Override
    public int size() {
        return routes.size();
    }
}
