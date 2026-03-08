package ltd.idcu.est.web.router.api;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static HttpMethod fromString(String method) {
        if (method == null) {
            return null;
        }
        String upperMethod = method.toUpperCase();
        for (HttpMethod httpMethod : values()) {
            if (httpMethod.method.equals(upperMethod)) {
                return httpMethod;
            }
        }
        return null;
    }

    public boolean allowsBody() {
        return this == POST || this == PUT || this == PATCH;
    }

    public boolean isSafe() {
        return this == GET || this == HEAD || this == OPTIONS || this == TRACE;
    }

    public boolean isIdempotent() {
        return this == GET || this == HEAD || this == OPTIONS || this == TRACE || this == PUT || this == DELETE;
    }
}
