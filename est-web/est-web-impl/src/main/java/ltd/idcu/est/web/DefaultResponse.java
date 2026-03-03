package ltd.idcu.est.web;

import com.sun.net.httpserver.HttpExchange;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.web.api.HttpStatus;
import ltd.idcu.est.web.api.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DefaultResponse implements Response {

    private final HttpExchange exchange;
    private HttpStatus status = HttpStatus.OK;
    private final Map<String, String> headers = new HashMap<>();
    private String characterEncoding = "UTF-8";
    private Object body;
    private byte[] bodyBytes;
    private boolean committed = false;
    private final Map<String, Cookie> cookies = new HashMap<>();

    public DefaultResponse(HttpExchange exchange) {
        this.exchange = exchange;
    }

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
        if (body instanceof String) {
            return (String) body;
        }
        if (bodyBytes != null) {
            return new String(bodyBytes, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
        this.bodyBytes = body != null ? body.getBytes(StandardCharsets.UTF_8) : null;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
        this.bodyBytes = body;
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
            this.body = bodyBytes;
        } catch (IOException e) {
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
        setBody(JsonUtils.toJson(object));
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
        committed = true;
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
        return exchange.getResponseBody();
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

    public void commit() throws IOException {
        if (committed) {
            return;
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            exchange.getResponseHeaders().set(entry.getKey(), entry.getValue());
        }

        for (Cookie cookie : cookies.values()) {
            exchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
        }

        byte[] responseBytes = bodyBytes;
        if (responseBytes == null && body != null) {
            responseBytes = body.toString().getBytes(StandardCharsets.UTF_8);
        }
        if (responseBytes == null) {
            responseBytes = new byte[0];
        }

        exchange.sendResponseHeaders(status.getCode(), responseBytes.length);
        if (responseBytes.length > 0) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }

        committed = true;
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

    public HttpExchange getExchange() {
        return exchange;
    }

    private static class Cookie {
        private final String name;
        private final String value;
        private final int maxAge;
        private final String path;
        private final String domain;
        private final boolean secure;
        private final boolean httpOnly;

        Cookie(String name, String value, int maxAge, String path, String domain, boolean secure, boolean httpOnly) {
            this.name = name;
            this.value = value;
            this.maxAge = maxAge;
            this.path = path;
            this.domain = domain;
            this.secure = secure;
            this.httpOnly = httpOnly;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append("=").append(value);
            if (maxAge >= 0) {
                sb.append("; Max-Age=").append(maxAge);
            }
            if (path != null) {
                sb.append("; Path=").append(path);
            }
            if (domain != null) {
                sb.append("; Domain=").append(domain);
            }
            if (secure) {
                sb.append("; Secure");
            }
            if (httpOnly) {
                sb.append("; HttpOnly");
            }
            return sb.toString();
        }
    }
}
