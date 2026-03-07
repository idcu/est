package ltd.idcu.est.gateway;

import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.Route;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGatewayContext implements GatewayContext {
    private final String requestPath;
    private final String requestMethod;
    private final Map&lt;String, String&gt; requestHeaders;
    private final byte[] requestBody;
    private Route matchedRoute;
    private String targetUrl;
    private Map&lt;String, String&gt; responseHeaders;
    private int responseStatus;
    private byte[] responseBody;
    private final Map&lt;String, Object&gt; attributes;

    public DefaultGatewayContext(String requestPath, String requestMethod) {
        this(requestPath, requestMethod, new ConcurrentHashMap&lt;&gt;(), new byte[0]);
    }

    public DefaultGatewayContext(String requestPath, String requestMethod, 
                                 Map&lt;String, String&gt; requestHeaders, byte[] requestBody) {
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
        this.requestHeaders = new ConcurrentHashMap&lt;&gt;(requestHeaders);
        this.requestBody = requestBody;
        this.responseHeaders = new ConcurrentHashMap&lt;&gt;();
        this.attributes = new ConcurrentHashMap&lt;&gt;();
        this.responseStatus = 200;
        this.responseBody = new byte[0];
    }

    @Override
    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public Map&lt;String, String&gt; getRequestHeaders() {
        return Collections.unmodifiableMap(requestHeaders);
    }

    @Override
    public byte[] getRequestBody() {
        return requestBody.clone();
    }

    @Override
    public Route getMatchedRoute() {
        return matchedRoute;
    }

    @Override
    public void setMatchedRoute(Route route) {
        this.matchedRoute = route;
    }

    @Override
    public String getTargetUrl() {
        return targetUrl;
    }

    @Override
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public Map&lt;String, String&gt; getResponseHeaders() {
        return Collections.unmodifiableMap(responseHeaders);
    }

    @Override
    public void setResponseHeaders(Map&lt;String, String&gt; headers) {
        this.responseHeaders = new ConcurrentHashMap&lt;&gt;(headers);
    }

    @Override
    public int getResponseStatus() {
        return responseStatus;
    }

    @Override
    public void setResponseStatus(int status) {
        this.responseStatus = status;
    }

    @Override
    public byte[] getResponseBody() {
        return responseBody.clone();
    }

    @Override
    public void setResponseBody(byte[] body) {
        this.responseBody = body.clone();
    }

    @Override
    public Map&lt;String, Object&gt; getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    public void addResponseHeader(String name, String value) {
        responseHeaders.put(name, value);
    }
}
