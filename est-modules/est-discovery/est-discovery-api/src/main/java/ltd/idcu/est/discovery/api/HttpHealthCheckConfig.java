package ltd.idcu.est.discovery.api;

public class HttpHealthCheckConfig {
    private final String path;
    private final int timeoutMs;
    private final int expectedStatusCode;

    public HttpHealthCheckConfig(String path, int timeoutMs, int expectedStatusCode) {
        this.path = path;
        this.timeoutMs = timeoutMs;
        this.expectedStatusCode = expectedStatusCode;
    }

    public HttpHealthCheckConfig(String path) {
        this(path, 5000, 200);
    }

    public String getPath() {
        return path;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public int getExpectedStatusCode() {
        return expectedStatusCode;
    }
}
