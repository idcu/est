package ltd.idcu.est.web;

import com.sun.net.httpserver.HttpExchange;
import ltd.idcu.est.utils.io.IOUtils;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Session;
import ltd.idcu.est.web.api.SessionManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DefaultRequest implements Request {

    private final HttpExchange exchange;
    private final Map<String, List<String>> parameters;
    private final Map<String, String> cookies;
    private final Map<String, String> pathVariables;
    private SessionManager sessionManager;
    private Session session;
    private byte[] bodyBytes;

    public DefaultRequest(HttpExchange exchange) {
        this.exchange = exchange;
        this.parameters = new HashMap<>();
        this.cookies = new HashMap<>();
        this.pathVariables = new HashMap<>();
        parseQueryParameters();
        parseCookies();
    }

    private void parseQueryParameters() {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    parameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
                } else {
                    String key = URLDecoder.decode(pair, StandardCharsets.UTF_8);
                    parameters.computeIfAbsent(key, k -> new ArrayList<>()).add("");
                }
            }
        }
    }

    private void parseCookies() {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] cookiePairs = cookieHeader.split(";");
            for (String pair : cookiePairs) {
                String trimmed = pair.trim();
                int idx = trimmed.indexOf('=');
                if (idx > 0) {
                    String key = trimmed.substring(0, idx);
                    String value = trimmed.substring(idx + 1);
                    cookies.put(key, value);
                }
            }
        }
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.fromString(exchange.getRequestMethod());
    }

    @Override
    public String getPath() {
        return exchange.getRequestURI().getPath();
    }

    @Override
    public String getQueryString() {
        return exchange.getRequestURI().getQuery();
    }

    @Override
    public String getParameter(String name) {
        List<String> values = parameters.get(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    @Override
    public List<String> getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return new HashMap<>(parameters);
    }

    @Override
    public String getHeader(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }

    @Override
    public List<String> getHeaders(String name) {
        return exchange.getRequestHeaders().get(name);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return new HashMap<>(exchange.getRequestHeaders());
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    @Override
    public String getBody() {
        try {
            return IOUtils.toString(getBodyStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public InputStream getBodyStream() {
        return exchange.getRequestBody();
    }

    @Override
    public byte[] getBodyBytes() {
        if (bodyBytes == null) {
            try {
                bodyBytes = IOUtils.toByteArray(getBodyStream());
            } catch (IOException e) {
                bodyBytes = new byte[0];
            }
        }
        return bodyBytes;
    }

    @Override
    public Session getSession() {
        return getSession(true);
    }

    @Override
    public Session getSession(boolean create) {
        if (session != null && session.isValid()) {
            return session;
        }
        if (sessionManager == null) {
            return null;
        }
        String sessionId = getCookie("JSESSIONID");
        if (sessionId != null) {
            session = sessionManager.getSession(sessionId, create);
        } else if (create) {
            session = sessionManager.createSession();
        }
        return session;
    }

    @Override
    public String getRemoteAddress() {
        return exchange.getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public String getRemoteHost() {
        return exchange.getRemoteAddress().getHostName();
    }

    @Override
    public int getRemotePort() {
        return exchange.getRemoteAddress().getPort();
    }

    @Override
    public String getProtocol() {
        return exchange.getProtocol();
    }

    @Override
    public String getScheme() {
        return "http";
    }

    @Override
    public String getHost() {
        return getHeader("Host");
    }

    @Override
    public int getPort() {
        String host = getHost();
        if (host != null && host.contains(":")) {
            return Integer.parseInt(host.substring(host.lastIndexOf(":") + 1));
        }
        return 80;
    }

    @Override
    public URI getURI() {
        return exchange.getRequestURI();
    }

    @Override
    public String getUrl() {
        return getScheme() + "://" + getHost() + getPath();
    }

    @Override
    public String getFullUrl() {
        String query = getQueryString();
        if (query != null && !query.isEmpty()) {
            return getUrl() + "?" + query;
        }
        return getUrl();
    }

    @Override
    public List<String> getPathSegments() {
        String path = getPath();
        if (path == null || path.isEmpty() || path.equals("/")) {
            return Collections.emptyList();
        }
        String[] segments = path.split("/");
        List<String> result = new ArrayList<>();
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                result.add(segment);
            }
        }
        return result;
    }

    @Override
    public String getPathSegment(int index) {
        List<String> segments = getPathSegments();
        if (index >= 0 && index < segments.size()) {
            return segments.get(index);
        }
        return null;
    }

    @Override
    public String getCookie(String name) {
        return cookies.get(name);
    }

    @Override
    public Map<String, String> getCookies() {
        return new HashMap<>(cookies);
    }

    @Override
    public boolean isSecure() {
        return "https".equalsIgnoreCase(getScheme());
    }

    @Override
    public boolean isAjax() {
        String requestedWith = getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    @Override
    public boolean isMultipart() {
        String contentType = getContentType();
        return contentType != null && contentType.startsWith("multipart/form-data");
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void addPathVariables(Map<String, String> variables) {
        if (variables != null) {
            this.pathVariables.putAll(variables);
        }
    }

    public String getPathVariable(String name) {
        return pathVariables.get(name);
    }

    public HttpExchange getExchange() {
        return exchange;
    }
}
