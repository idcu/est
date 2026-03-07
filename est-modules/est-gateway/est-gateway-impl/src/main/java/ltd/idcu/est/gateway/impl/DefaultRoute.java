package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.Route;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRoute implements Route {
    private final String path;
    private final String serviceId;
    private final String pathRewrite;
    private final Map<String, String> filters;
    private final Map<String, String> requestHeaders;
    private final Map<String, String> responseHeaders;
    private final Map<String, String> requiredHeaders;
    private final Map<String, String> requiredCookies;

    public DefaultRoute(String path, String serviceId) {
        this(path, serviceId, null, new ConcurrentHashMap<>(), 
             new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), 
             new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }

    public DefaultRoute(String path, String serviceId, String pathRewrite) {
        this(path, serviceId, pathRewrite, new ConcurrentHashMap<>(), 
             new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), 
             new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }

    public DefaultRoute(String path, String serviceId, String pathRewrite, Map<String, String> filters) {
        this(path, serviceId, pathRewrite, filters, 
             new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), 
             new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }

    public DefaultRoute(String path, String serviceId, String pathRewrite, Map<String, String> filters,
                        Map<String, String> requestHeaders, Map<String, String> responseHeaders,
                        Map<String, String> requiredHeaders, Map<String, String> requiredCookies) {
        this.path = path;
        this.serviceId = serviceId;
        this.pathRewrite = pathRewrite;
        this.filters = new ConcurrentHashMap<>(filters);
        this.requestHeaders = new ConcurrentHashMap<>(requestHeaders);
        this.responseHeaders = new ConcurrentHashMap<>(responseHeaders);
        this.requiredHeaders = new ConcurrentHashMap<>(requiredHeaders);
        this.requiredCookies = new ConcurrentHashMap<>(requiredCookies);
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
    public Map<String, String> getFilters() {
        return Collections.unmodifiableMap(filters);
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return Collections.unmodifiableMap(requestHeaders);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return Collections.unmodifiableMap(responseHeaders);
    }

    @Override
    public Map<String, String> getRequiredHeaders() {
        return Collections.unmodifiableMap(requiredHeaders);
    }

    @Override
    public Map<String, String> getRequiredCookies() {
        return Collections.unmodifiableMap(requiredCookies);
    }

    @Override
    public boolean matches(String requestPath) {
        return matches(requestPath, Collections.emptyMap(), Collections.emptyMap());
    }

    @Override
    public boolean matches(String requestPath, Map<String, String> requestHeaders, Map<String, String> requestCookies) {
        if (requestPath == null) {
            return false;
        }

        if (!matchesPath(requestPath)) {
            return false;
        }

        if (!matchesRequiredHeaders(requestHeaders)) {
            return false;
        }

        if (!matchesRequiredCookies(requestCookies)) {
            return false;
        }

        return true;
    }

    private boolean matchesPath(String requestPath) {
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

    private boolean matchesRequiredHeaders(Map<String, String> requestHeaders) {
        for (Map.Entry<String, String> entry : requiredHeaders.entrySet()) {
            String actualValue = requestHeaders.get(entry.getKey());
            if (actualValue == null) {
                return false;
            }
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                if (!actualValue.matches(entry.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean matchesRequiredCookies(Map<String, String> requestCookies) {
        for (Map.Entry<String, String> entry : requiredCookies.entrySet()) {
            String actualValue = requestCookies.get(entry.getKey());
            if (actualValue == null) {
                return false;
            }
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                if (!actualValue.matches(entry.getValue())) {
                    return false;
                }
            }
        }
        return true;
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

    @Override
    public Map<String, String> rewriteHeaders(Map<String, String> headers) {
        Map<String, String> result = new ConcurrentHashMap<>(headers);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                result.remove(entry.getKey());
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public Map<String, String> rewriteResponseHeaders(Map<String, String> headers) {
        Map<String, String> result = new ConcurrentHashMap<>(headers);
        for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                result.remove(entry.getKey());
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public void addFilter(String key, String value) {
        filters.put(key, value);
    }

    public void addRequestHeader(String key, String value) {
        requestHeaders.put(key, value);
    }

    public void addResponseHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    public void addRequiredHeader(String key, String pattern) {
        requiredHeaders.put(key, pattern);
    }

    public void addRequiredCookie(String key, String pattern) {
        requiredCookies.put(key, pattern);
    }
}
