package ltd.idcu.est.web.api;

import java.util.List;
import java.util.Map;

public interface Route {

    String getPath();

    HttpMethod getMethod();

    String getHandler();

    List<String> getPathVariables();

    Map<String, String> getMetadata();

    String getName();

    default boolean hasPathVariables() {
        List<String> vars = getPathVariables();
        return vars != null && !vars.isEmpty();
    }

    default boolean matches(String path, HttpMethod method) {
        if (getMethod() != method) {
            return false;
        }
        return matchesPath(path);
    }

    boolean matchesPath(String path);

    Map<String, String> extractPathVariables(String path);
}
