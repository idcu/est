package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRoute implements Route {

    private String path;
    private HttpMethod method;
    private String handler;
    private String name;
    private List<String> pathVariables = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
    private Pattern routePattern;

    public DefaultRoute(String path, HttpMethod method, String handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
        this.routePattern = compileRoutePattern(path);
        extractPathVariableNames(path);
    }

    private Pattern compileRoutePattern(String path) {
        String regex = path;
        regex = regex.replaceAll("\\{([^}]+)\\}", "([^/]+)");
        return Pattern.compile("^" + regex + "$");
    }

    private void extractPathVariableNames(String path) {
        Matcher matcher = Pattern.compile("\\{([^}]+)\\}").matcher(path);
        while (matcher.find()) {
            pathVariables.add(matcher.group(1));
        }
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
    public List<String> getPathVariables() {
        return pathVariables;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matchesPath(String path) {
        Matcher matcher = routePattern.matcher(path);
        return matcher.matches();
    }

    @Override
    public Map<String, String> extractPathVariables(String path) {
        Map<String, String> params = new HashMap<>();
        Matcher matcher = routePattern.matcher(path);
        if (matcher.matches()) {
            for (int i = 0; i < pathVariables.size(); i++) {
                if (i < matcher.groupCount()) {
                    params.put(pathVariables.get(i), matcher.group(i + 1));
                }
            }
        }
        return params;
    }
}
