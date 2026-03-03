package ltd.idcu.est.web.api;

public abstract class WebException extends Exception {

    private final HttpStatus status;
    private final String errorCode;

    protected WebException(HttpStatus status, String message) {
        this(status, message, "WEB_" + status.getCode());
    }

    protected WebException(HttpStatus status, String message, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isClientError() {
        return status.is4xxClientError();
    }

    public boolean isServerError() {
        return status.is5xxServerError();
    }

    public String toJson() {
        return String.format(
            "{\"success\":false,\"code\":%d,\"message\":\"%s\",\"error\":\"%s\"}",
            getStatusCode(),
            escapeJson(getMessage()),
            escapeJson(errorCode)
        );
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

    public static WebException of(HttpStatus status, String message) {
        return new WebException(status, message) {};
    }

    public static WebException of(int statusCode, String message) {
        HttpStatus status = HttpStatus.fromCode(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return of(status, message);
    }

    public static WebException badRequest(String message) {
        return of(HttpStatus.BAD_REQUEST, message);
    }

    public static WebException unauthorized(String message) {
        return of(HttpStatus.UNAUTHORIZED, message);
    }

    public static WebException forbidden(String message) {
        return of(HttpStatus.FORBIDDEN, message);
    }

    public static WebException notFound(String message) {
        return of(HttpStatus.NOT_FOUND, message);
    }

    public static WebException methodNotAllowed(String message) {
        return of(HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    public static WebException internalServerError(String message) {
        return of(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static WebException notImplemented(String message) {
        return of(HttpStatus.NOT_IMPLEMENTED, message);
    }

    public static WebException serviceUnavailable(String message) {
        return of(HttpStatus.SERVICE_UNAVAILABLE, message);
    }
}
