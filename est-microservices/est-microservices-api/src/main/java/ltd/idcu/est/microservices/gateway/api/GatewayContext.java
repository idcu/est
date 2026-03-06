package ltd.idcu.est.microservices.gateway.api;

import java.util.Map;

public interface GatewayContext {
    String getRequestPath();

    String getRequestMethod();

    Map<String, String> getRequestHeaders();

    byte[] getRequestBody();

    Route getMatchedRoute();

    void setMatchedRoute(Route route);

    String getTargetUrl();

    void setTargetUrl(String targetUrl);

    Map<String, String> getResponseHeaders();

    void setResponseHeaders(Map<String, String> headers);

    int getResponseStatus();

    void setResponseStatus(int status);

    byte[] getResponseBody();

    void setResponseBody(byte[] body);

    Map<String, Object> getAttributes();

    void setAttribute(String key, Object value);

    Object getAttribute(String key);
}
