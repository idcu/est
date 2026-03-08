package ltd.idcu.est.gateway;

import ltd.idcu.est.gateway.api.Route;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRoute implements Route {
    private final String path;
    private final String serviceId;
    private final String pathRewrite;
    private final Map&lt;String, String&gt; filters;

    public DefaultRoute(String path, String serviceId) {
        this(path, serviceId, null, new ConcurrentHashMap&lt;&gt;());
    }

    public DefaultRoute(String path, String serviceId, String pathRewrite) {
        this(path, serviceId, pathRewrite, new ConcurrentHashMap&lt;&gt;());
    }

    public DefaultRoute(String path, String serviceId, String pathRewrite, Map&lt;String, String&gt; filters) {
        this.path = path;
        this.serviceId = serviceId;
        this.pathRewrite = pathRewrite;
        this.filters = new ConcurrentHashMap&lt;&gt;(filters);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getPathRewrite() {
        return pathRewrite;
    }

    @Override
    public Map&lt;String, String&gt; getFilters() {
        return Collections.unmodifiableMap(filters);
    }

    @Override
    public boolean matches(String requestPath) {
        if (requestPath == null) {
            return false;
        }
        if (path.equals(requestPath)) {
            return true;
        }
        if (path.endsWith("*")) {
            String prefix = path.substring(0, path.length() - 1);
            return requestPath.startsWith(prefix);
        }
        if (path.endsWith("**")) {
            String prefix = path.substring(0, path.length() - 2);
            return requestPath.startsWith(prefix);
        }
        return false;
    }

    @Override
    public String rewritePath(String requestPath) {
        if (pathRewrite == null || pathRewrite.isEmpty()) {
            if (path.endsWith("*")) {
                String prefix = path.substring(0, path.length() - 1);
                return requestPath.substring(prefix.length());
            }
            if (path.endsWith("**")) {
                String prefix = path.substring(0, path.length() - 2);
                return requestPath.substring(prefix.length());
            }
            return requestPath;
        }
        if (pathRewrite.contains("$")) {
            String prefix = path.endsWith("**") ? path.substring(0, path.length() - 2) : 
                           path.endsWith("*") ? path.substring(0, path.length() - 1) : path;
            String remainingPath = requestPath.startsWith(prefix) ? requestPath.substring(prefix.length()) : requestPath;
            return pathRewrite.replace("$1", remainingPath);
        }
        return pathRewrite;
    }

    public void addFilter(String key, String value) {
        filters.put(key, value);
    }
}
