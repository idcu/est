package ltd.idcu.est.web;

import ltd.idcu.est.web.api.AsyncHandler;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Route;
import ltd.idcu.est.web.api.RouteHandler;
import ltd.idcu.est.web.api.Router;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class OptimizedRouter implements Router {
    
    private static class TrieMatchResult {
        Route route;
        Map<String, String> pathVariables;
        
        TrieMatchResult(Route route, Map<String, String> pathVariables) {
            this.route = route;
            this.pathVariables = pathVariables;
        }
    }
    
    private final List<Route> routes;
    private final Map<String, Route> namedRoutes;
    private final Map<HttpMethod, RouteTrieNode> routeTries;
    private final Map<HttpMethod, Map<String, Route>> routeCache;
    private final Map<HttpMethod, List<Route>> routesByMethod;
    private String currentPrefix;
    private String currentName;
    private List<String> currentMiddleware;
    
    public OptimizedRouter() {
        this.routes = new CopyOnWriteArrayList<>();
        this.namedRoutes = new ConcurrentHashMap<>();
        this.routeTries = new ConcurrentHashMap<>();
        this.routeCache = new ConcurrentHashMap<>();
        this.routesByMethod = new ConcurrentHashMap<>();
        this.currentPrefix = "";
        this.currentName = "";
        this.currentMiddleware = new CopyOnWriteArrayList<>();
    }
    
    private void addRouteToTrie(Route route) {
        RouteTrieNode root = routeTries.computeIfAbsent(route.getMethod(), k -> new RouteTrieNode());
        String[] segments = route.getPath().split("/");
        RouteTrieNode current = root;
        
        for (String segment : segments) {
            if (segment.isEmpty()) {
                continue;
            }
            
            if (segment.equals("*")) {
                if (current.getWildcardChild() == null) {
                    current.setWildcardChild(new RouteTrieNode());
                }
                current = current.getWildcardChild();
            } else if (segment.startsWith("{") && segment.endsWith("}")) {
                String paramName = segment.substring(1, segment.length() - 1);
                if (current.getParamChild() == null) {
                    current.setParamChild(new RouteTrieNode());
                    current.setParamName(paramName);
                }
                current = current.getParamChild();
            } else {
                current = current.getChildren().computeIfAbsent(segment, k -> new RouteTrieNode());
            }
        }
        
        current.setRoute(route);
    }
    
    private TrieMatchResult matchRouteInTrie(String path, HttpMethod method) {
        RouteTrieNode root = routeTries.get(method);
        if (root == null) {
            return null;
        }
        
        String[] segments = path.split("/");
        Map<String, String> pathVariables = new HashMap<>();
        
        return matchSegment(root, segments, 0, pathVariables);
    }
    
    private TrieMatchResult matchSegment(RouteTrieNode node, String[] segments, int index, 
                                          Map<String, String> pathVariables) {
        if (index >= segments.length) {
            if (node.getRoute() != null) {
                return new TrieMatchResult(node.getRoute(), new HashMap<>(pathVariables));
            }
            return null;
        }
        
        String segment = segments[index];
        if (segment.isEmpty()) {
            return matchSegment(node, segments, index + 1, pathVariables);
        }
        
        RouteTrieNode exactChild = node.getChildren().get(segment);
        if (exactChild != null) {
            TrieMatchResult result = matchSegment(exactChild, segments, index + 1, pathVariables);
            if (result != null) {
                return result;
            }
        }
        
        if (node.getParamChild() != null) {
            Map<String, String> paramVars = new HashMap<>(pathVariables);
            paramVars.put(node.getParamName(), segment);
            TrieMatchResult result = matchSegment(node.getParamChild(), segments, index + 1, paramVars);
            if (result != null) {
                return result;
            }
        }
        
        if (node.getWildcardChild() != null && node.getWildcardChild().getRoute() != null) {
            return new TrieMatchResult(node.getWildcardChild().getRoute(), new HashMap<>(pathVariables));
        }
        
        return null;
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
        routesByMethod.computeIfAbsent(route.getMethod(), k -> new CopyOnWriteArrayList<>()).add(route);
        addRouteToTrie(route);
        
        Map<String, Route> methodCache = routeCache.get(route.getMethod());
        if (methodCache != null) {
            methodCache.clear();
        }
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
        if (cached != null) {
            return cached;
        }
        
        TrieMatchResult result = matchRouteInTrie(path, method);
        if (result != null) {
            methodCache.put(path, result.route);
            return result.route;
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
        List<Route> methodRoutes = routesByMethod.get(method);
        if (methodRoutes == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(methodRoutes);
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
        routesByMethod.computeIfAbsent(route.getMethod(), k -> new CopyOnWriteArrayList<>()).add(route);
        addRouteToTrie(route);
        
        Map<String, Route> methodCache = routeCache.get(route.getMethod());
        if (methodCache != null) {
            methodCache.clear();
        }
    }
    
    @Override
    public void removeRoute(String path, HttpMethod method) {
        routes.removeIf(route -> route.getPath().equals(path) && route.getMethod() == method);
        List<Route> methodRoutes = routesByMethod.get(method);
        if (methodRoutes != null) {
            methodRoutes.removeIf(route -> route.getPath().equals(path));
        }
        
        routeTries.remove(method);
        for (Route route : routes) {
            if (route.getMethod() == method) {
                addRouteToTrie(route);
            }
        }
        
        Map<String, Route> methodCache = routeCache.get(method);
        if (methodCache != null) {
            methodCache.clear();
        }
    }
    
    @Override
    public void clear() {
        routes.clear();
        namedRoutes.clear();
        routeTries.clear();
        routeCache.clear();
        routesByMethod.clear();
    }
    
    @Override
    public int size() {
        return routes.size();
    }
}
