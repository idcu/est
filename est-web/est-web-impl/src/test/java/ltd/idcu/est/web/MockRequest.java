package ltd.idcu.est.web;

import ltd.idcu.est.web.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.UUID;

public class MockRequest implements Request {

    private HttpMethod method;
    private String path;
    private String queryString;
    private Map<String, List<String>> parameters = new HashMap<>();
    private Map<String, List<String>> headers = new HashMap<>();
    private String body;
    private byte[] bodyBytes;
    private Map<String, String> cookies = new HashMap<>();
    private Map<String, String> pathVariables = new HashMap<>();
    private Map<String, Object> attributes = new HashMap<>();
    private Session session;
    private String remoteAddress = "127.0.0.1";
    private String remoteHost = "localhost";
    private int remotePort = 12345;
    private String protocol = "HTTP/1.1";
    private String scheme = "http";
    private String host = "localhost";
    private int port = 80;
    private boolean secure = false;
    private boolean ajax = false;
    private boolean multipart = false;
    private FormData formData;

    public MockRequest() {
        this(HttpMethod.GET, "/");
    }

    public MockRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String getParameter(String name) {
        List<String> values = parameters.get(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public void setParameter(String name, String value) {
        parameters.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
    }

    @Override
    public List<String> getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return new HashMap<>(parameters);
    }

    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = new HashMap<>(parameters);
    }

    @Override
    public String getHeader(String name) {
        List<String> values = headers.get(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public void setHeader(String name, String value) {
        headers.put(name, Collections.singletonList(value));
    }

    @Override
    public List<String> getHeaders(String name) {
        return headers.get(name);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return new HashMap<>(headers);
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = new HashMap<>(headers);
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    public void setContentType(String contentType) {
        setHeader("Content-Type", contentType);
    }

    @Override
    public String getBody() {
        if (body != null) {
            return body;
        }
        if (bodyBytes != null) {
            return new String(bodyBytes);
        }
        return null;
    }

    public void setBody(String body) {
        this.body = body;
        this.bodyBytes = body != null ? body.getBytes() : null;
    }

    @Override
    public InputStream getBodyStream() {
        if (bodyBytes != null) {
            return new ByteArrayInputStream(bodyBytes);
        }
        if (body != null) {
            return new ByteArrayInputStream(body.getBytes());
        }
        return new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public byte[] getBodyBytes() {
        if (bodyBytes != null) {
            return bodyBytes;
        }
        if (body != null) {
            return body.getBytes();
        }
        return new byte[0];
    }

    public void setBodyBytes(byte[] bodyBytes) {
        this.bodyBytes = bodyBytes;
        this.body = bodyBytes != null ? new String(bodyBytes) : null;
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
        if (create) {
            session = new DefaultSession(UUID.randomUUID().toString());
        }
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public URI getURI() {
        try {
            return URI.create(getFullUrl());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getUrl() {
        return scheme + "://" + host + (port != 80 ? ":" + port : "") + path;
    }

    @Override
    public String getFullUrl() {
        if (queryString != null && !queryString.isEmpty()) {
            return getUrl() + "?" + queryString;
        }
        return getUrl();
    }

    @Override
    public List<String> getPathSegments() {
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

    public void setCookie(String name, String value) {
        cookies.put(name, value);
    }

    @Override
    public Map<String, String> getCookies() {
        return new HashMap<>(cookies);
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = new HashMap<>(cookies);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    @Override
    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    @Override
    public String getPathVariable(String name) {
        return pathVariables.get(name);
    }

    @Override
    public void addPathVariables(Map<String, String> variables) {
        if (variables != null) {
            this.pathVariables.putAll(variables);
        }
    }

    public void setPathVariable(String name, String value) {
        pathVariables.put(name, value);
    }

    @Override
    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    @Override
    public MultipartFile getFile(String name) {
        return formData != null ? formData.getFile(name) : null;
    }

    @Override
    public List<MultipartFile> getFiles(String name) {
        return formData != null ? formData.getFiles(name) : null;
    }

    @Override
    public AsyncContext startAsync() {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }
}
