package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.Route;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGatewayContext implements GatewayContext {
    private final String requestPath;
    private final String requestMethod;
    private final Map<String, String> requestHeaders;
    private final Map<String, String> requestCookies;
    private final byte[] requestBody;
    private Route matchedRoute;
    private String targetUrl;
    private Map<String, String> responseHeaders;
    private int responseStatus;
    private byte[] responseBody;
    private final Map<String, Object> attributes;

    public DefaultGatewayContext(String requestPath, String requestMethod) {
        this(requestPath, requestMethod, new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new byte[0]);
    }

    public DefaultGatewayContext(String requestPath, String requestMethod, 
                                 Map<String, String> requestHeaders, byte[] requestBody) {
        this(requestPath, requestMethod, requestHeaders, new ConcurrentHashMap<>(), requestBody);
    }

    public DefaultGatewayContext(String requestPath, String requestMethod, 
                                 Map<String, String> requestHeaders, Map<String, String> requestCookies, 
                                 byte[] requestBody) {
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
        this.requestHeaders = new ConcurrentHashMap<>(requestHeaders);
        this.requestCookies = new ConcurrentHashMap<>(requestCookies);
        this.requestBody = requestBody;
        this.responseHeaders = new ConcurrentHashMap<>();
        this.attributes = new ConcurrentHashMap<>();
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
    public Map<String, String> getRequestHeaders() {
        return Collections.unmodifiableMap(requestHeaders);
    }

    @Override
    public Map<String, String> getRequestCookies() {
        return Collections.unmodifiableMap(requestCookies);
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
    public Map<String, String> getResponseHeaders() {
        return Collections.unmodifiableMap(responseHeaders);
    }

    @Override
    public void setResponseHeaders(Map<String, String> headers) {
        this.responseHeaders = new ConcurrentHashMap<>(headers);
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
    public Map<String, Object> getAttributes() {
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

    public void addRequestCookie(String name, String value) {
        requestCookies.put(name, value);
    }
}
