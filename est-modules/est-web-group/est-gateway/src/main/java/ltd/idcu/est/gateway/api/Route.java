package ltd.idcu.est.gateway.api;

import java.util.Map;

public interface Route {
    String getPath();

    String getServiceId();

    String getPathRewrite();

    Map<String, String> getFilters();

    boolean matches(String requestPath);

    String rewritePath(String requestPath);
}
