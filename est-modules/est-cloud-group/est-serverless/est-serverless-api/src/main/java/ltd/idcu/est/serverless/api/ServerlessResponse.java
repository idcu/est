package ltd.idcu.est.serverless.api;

import java.util.HashMap;
import java.util.Map;

public class ServerlessResponse {
    private int statusCode;
    private Map<String, String> headers;
    private String body;
    private boolean isBase64Encoded;

    public ServerlessResponse() {
        this.statusCode = 200;
        this.headers = new HashMap<>();
        this.isBase64Encoded = false;
    }

    public ServerlessResponse(int statusCode, String body) {
        this();
        this.statusCode = statusCode;
        this.body = body;
    }

    public static ServerlessResponse ok(String body) {
        return new ServerlessResponse(200, body);
    }

    public static ServerlessResponse error(int statusCode, String message) {
        return new ServerlessResponse(statusCode, message);
    }

    public static ServerlessResponse notFound() {
        return new ServerlessResponse(404, "Not Found");
    }

    public static ServerlessResponse badRequest(String message) {
        return new ServerlessResponse(400, message);
    }

    public static ServerlessResponse internalServerError(String message) {
        return new ServerlessResponse(500, message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isBase64Encoded() {
        return isBase64Encoded;
    }

    public void setBase64Encoded(boolean base64Encoded) {
        isBase64Encoded = base64Encoded;
    }
}
