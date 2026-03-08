package ltd.idcu.est.gateway.api;

import java.util.Map;

public interface Route {
    String getPath();

    String getServiceId();

    String getPathRewrite();

    Map<String, String> getFilters();

    Map<String, String> getRequestHeaders();

    Map<String, String> getResponseHeaders();

    Map<String, String> getRequiredHeaders();

    Map<String, String> getRequiredCookies();

    boolean matches(String requestPath, Map<String, String> requestHeaders, Map<String, String> requestCookies);

    boolean matches(String requestPath);

    String rewritePath(String requestPath);

    Map<String, String> rewriteHeaders(Map<String, String> headers);

    Map<String, String> rewriteResponseHeaders(Map<String, String> headers);
}
