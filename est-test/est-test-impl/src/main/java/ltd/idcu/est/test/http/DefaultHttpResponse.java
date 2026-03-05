package ltd.idcu.est.test.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultHttpResponse implements HttpResponse {

    private final int statusCode;
    private final String statusMessage;
    private final byte[] body;
    private final Map<String, List<String>> headers;

    public DefaultHttpResponse(int statusCode, String statusMessage, byte[] body, Map<String, List<String>> headers) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.body = body != null ? body : new byte[0];
        this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public String getBody() {
        return new String(body);
    }

    @Override
    public byte[] getBodyAsBytes() {
        return body.clone();
    }

    @Override
    public String getHeader(String name) {
        List<String> values = headers.get(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    @Override
    public List<String> getHeaders(String name) {
        List<String> values = headers.get(name);
        return values != null ? new ArrayList<>(values) : new ArrayList<>();
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        Map<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    @Override
    public long getContentLength() {
        String length = getHeader("Content-Length");
        if (length != null) {
            try {
                return Long.parseLong(length);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return body.length;
    }

    @Override
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public boolean isRedirect() {
        return statusCode >= 300 && statusCode < 400;
    }

    @Override
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    @Override
    public boolean isServerError() {
        return statusCode >= 500 && statusCode < 600;
    }
}
