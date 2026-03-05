package ltd.idcu.est.web;

import ltd.idcu.est.web.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MockResponse implements Response {

    private HttpStatus status = HttpStatus.OK;
    private Map<String, String> headers = new HashMap<>();
    private String characterEncoding = "UTF-8";
    private String body;
    private byte[] bodyBytes;
    private boolean committed = false;
    private Map<String, Cookie> cookies = new HashMap<>();
    private View.ViewResolver viewResolver;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(HttpStatus status) {
        if (committed) {
            throw new IllegalStateException("Response already committed");
        }
        this.status = status;
    }

    @Override
    public void setStatus(int code) {
        setStatus(HttpStatus.fromCode(code));
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void removeHeader(String name) {
        headers.remove(name);
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    @Override
    public void setContentType(String contentType) {
        setHeader("Content-Type", contentType);
    }

    @Override
    public long getContentLength() {
        String length = getHeader("Content-Length");
        return length != null ? Long.parseLong(length) : -1;
    }

    @Override
    public void setContentLength(long length) {
        setHeader("Content-Length", String.valueOf(length));
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

    @Override
    public void setBody(String body) {
        this.body = body;
        this.bodyBytes = body != null ? body.getBytes() : null;
    }

    @Override
    public void setBody(byte[] body) {
        this.bodyBytes = body;
        this.body = body != null ? new String(body) : null;
    }

    @Override
    public void setBody(InputStream inputStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            this.bodyBytes = baos.toByteArray();
            this.body = new String(bodyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read input stream", e);
        }
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public void setCharacterEncoding(String encoding) {
        this.characterEncoding = encoding;
    }

    @Override
    public void json(Object object) {
        setContentType("application/json; charset=" + characterEncoding);
        setBody(object != null ? object.toString() : "null");
    }

    @Override
    public void json(String json) {
        setContentType("application/json; charset=" + characterEncoding);
        setBody(json);
    }

    @Override
    public void xml(String xml) {
        setContentType("application/xml; charset=" + characterEncoding);
        setBody(xml);
    }

    @Override
    public void html(String html) {
        setContentType("text/html; charset=" + characterEncoding);
        setBody(html);
    }

    @Override
    public void text(String text) {
        setContentType("text/plain; charset=" + characterEncoding);
        setBody(text);
    }

    @Override
    public void redirect(String url) {
        redirect(url, HttpStatus.FOUND);
    }

    @Override
    public void redirect(String url, HttpStatus status) {
        setStatus(status);
        setHeader("Location", url);
    }

    @Override
    public void sendError(HttpStatus status) {
        sendError(status, status.getReasonPhrase());
    }

    @Override
    public void sendError(HttpStatus status, String message) {
        setStatus(status);
        setContentType("application/json; charset=" + characterEncoding);
        setBody(String.format("{\"error\":\"%s\",\"message\":\"%s\",\"status\":%d}",
                escapeJson(status.getReasonPhrase()),
                escapeJson(message),
                status.getCode()));
    }

    @Override
    public void sendError(int code) {
        sendError(HttpStatus.fromCode(code));
    }

    @Override
    public void sendError(int code, String message) {
        sendError(HttpStatus.fromCode(code), message);
    }

    @Override
    public OutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }

    @Override
    public void flush() {
        committed = true;
    }

    @Override
    public void reset() {
        if (committed) {
            throw new IllegalStateException("Response already committed");
        }
        status = HttpStatus.OK;
        headers.clear();
        body = null;
        bodyBytes = null;
    }

    @Override
    public void setCookie(String name, String value) {
        setCookie(name, value, -1, "/", null, false, true);
    }

    @Override
    public void setCookie(String name, String value, int maxAge) {
        setCookie(name, value, maxAge, "/", null, false, true);
    }

    @Override
    public void setCookie(String name, String value, int maxAge, String path, String domain, boolean secure, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value, maxAge, path, domain, secure, httpOnly);
        cookies.put(name, cookie);
    }

    @Override
    public void removeCookie(String name) {
        Cookie cookie = new Cookie(name, "", 0, "/", null, false, true);
        cookies.put(name, cookie);
    }

    public Map<String, Cookie> getCookies() {
        return new HashMap<>(cookies);
    }

    public boolean hasCookie(String name) {
        return cookies.containsKey(name);
    }

    public Cookie getCookie(String name) {
        return cookies.get(name);
    }

    public void setViewResolver(View.ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    @Override
    public void render(View view) {
        String rendered = view.render();
        setContentType(view.getContentType());
        setBody(rendered);
    }

    @Override
    public void render(String viewName) {
        render(viewName, new HashMap<>());
    }

    @Override
    public void render(String viewName, Map<String, Object> model) {
        if (viewResolver != null) {
            View view = viewResolver.resolve(viewName);
            view.setModel(model);
            render(view);
        } else {
            DefaultView view = new DefaultView(viewName);
            view.setViewEngine(new StringTemplateEngine());
            view.setModel(model);
            render(view);
        }
    }

    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static class Cookie {
        private final String name;
        private final String value;
        private final int maxAge;
        private final String path;
        private final String domain;
        private final boolean secure;
        private final boolean httpOnly;

        public Cookie(String name, String value, int maxAge, String path, String domain, boolean secure, boolean httpOnly) {
            this.name = name;
            this.value = value;
            this.maxAge = maxAge;
            this.path = path;
            this.domain = domain;
            this.secure = secure;
            this.httpOnly = httpOnly;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public int getMaxAge() {
            return maxAge;
        }

        public String getPath() {
            return path;
        }

        public String getDomain() {
            return domain;
        }

        public boolean isSecure() {
            return secure;
        }

        public boolean isHttpOnly() {
            return httpOnly;
        }
    }
}
