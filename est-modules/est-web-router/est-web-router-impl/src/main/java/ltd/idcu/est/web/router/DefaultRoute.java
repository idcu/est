package ltd.idcu.est.web.router;

import ltd.idcu.est.web.router.api.AsyncHandler;
import ltd.idcu.est.web.router.api.HttpMethod;
import ltd.idcu.est.web.router.api.Route;
import ltd.idcu.est.web.router.api.RouteHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRoute implements Route {

    private final String path;
    private final HttpMethod method;
    private final String handler;
    private final RouteHandler routeHandler;
    private final AsyncHandler asyncHandler;
    private final String name;
    private final List<String> pathVariables;
    private final Map<String, String> metadata;
    private final Pattern pathPattern;
    private final List<String> middleware;

    public DefaultRoute(String path, HttpMethod method, String handler) {
        this(path, method, handler, null, null, new ArrayList<>(), new HashMap<>());
    }

    public DefaultRoute(String path, HttpMethod method, RouteHandler routeHandler) {
        this(path, method, null, routeHandler, null, new ArrayList<>(), new HashMap<>());
    }

    public DefaultRoute(String path, HttpMethod method, AsyncHandler asyncHandler) {
        this(path, method, null, null, asyncHandler, new ArrayList<>(), new HashMap<>());
    }

    public DefaultRoute(String path, HttpMethod method, String handler, RouteHandler routeHandler, AsyncHandler asyncHandler,
                        List<String> pathVariables, Map<String, String> metadata) {
        this.path = path;
        this.method = method;
        this.handler = handler;
        this.routeHandler = routeHandler;
        this.asyncHandler = asyncHandler;
        this.name = null;
        this.pathVariables = new ArrayList<>(pathVariables);
        this.metadata = new HashMap<>(metadata);
        this.middleware = new ArrayList<>();
        this.pathPattern = compilePathPattern(path, this.pathVariables);
    }

    private Pattern compilePathPattern(String path, List<String> pathVariables) {
        StringBuilder pattern = new StringBuilder("^");
        String[] segments = path.split("/");
        for (String segment : segments) {
            if (segment.isEmpty()) {
                continue;
            }
            pattern.append("/");
            if (segment.startsWith("{") && segment.endsWith("}")) {
                String varName = segment.substring(1, segment.length() - 1);
                pathVariables.add(varName);
                pattern.append("([^/]+)");
            } else if (segment.equals("*")) {
                pattern.append(".*");
            } else {
                pattern.append(Pattern.quote(segment));
            }
        }
        pattern.append("/?$");
        return Pattern.compile(pattern.toString());
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getHandler() {
        return handler;
    }

    @Override
    public RouteHandler getRouteHandler() {
        return routeHandler;
    }

    @Override
    public AsyncHandler getAsyncHandler() {
        return asyncHandler;
    }

    @Override
    public List<String> getPathVariables() {
        return Collections.unmodifiableList(pathVariables);
    }

    @Override
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matchesPath(String requestPath) {
        return pathPattern.matcher(requestPath).matches();
    }

    @Override
    public Map<String, String> extractPathVariables(String requestPath) {
        Map<String, String> variables = new HashMap<>();
        if (pathVariables.isEmpty()) {
            return variables;
        }
        Matcher matcher = pathPattern.matcher(requestPath);
        if (matcher.matches()) {
            for (int i = 0; i < pathVariables.size(); i++) {
                if (i + 1 <= matcher.groupCount()) {
                    variables.put(pathVariables.get(i), matcher.group(i + 1));
                }
            }
        }
        return variables;
    }

    public List<String> getMiddleware() {
        return Collections.unmodifiableList(middleware);
    }

    public void addMiddleware(String middlewareName) {
        middleware.add(middlewareName);
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    @Override
    public String toString() {
        return String.format("Route{path='%s', method=%s, handler='%s'}", path, method, handler);
    }
}
