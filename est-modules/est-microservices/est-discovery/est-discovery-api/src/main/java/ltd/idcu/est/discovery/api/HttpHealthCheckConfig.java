package ltd.idcu.est.discovery.api;

public class HttpHealthCheckConfig {
    private final String path;
    private final int timeoutMs;
    private final int expectedStatusCode;

    public HttpHealthCheckConfig() {
        this("/health", 5000, 200);
    }

    public HttpHealthCheckConfig(String path, int timeoutMs, int expectedStatusCode) {
        this.path = path;
        this.timeoutMs = timeoutMs;
        this.expectedStatusCode = expectedStatusCode;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String path = "/health";
        private int timeoutMs = 5000;
        private int expectedStatusCode = 200;

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder timeoutMs(int timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder expectedStatusCode(int expectedStatusCode) {
            this.expectedStatusCode = expectedStatusCode;
            return this;
        }

        public HttpHealthCheckConfig build() {
            return new HttpHealthCheckConfig(path, timeoutMs, expectedStatusCode);
        }
    }
}
