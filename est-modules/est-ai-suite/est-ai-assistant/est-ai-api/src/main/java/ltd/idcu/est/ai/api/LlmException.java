package ltd.idcu.est.ai.api;

public class LlmException extends RuntimeException {
    
    private final ErrorType errorType;
    private final String provider;
    private final Integer statusCode;
    private final String requestId;
    
    public enum ErrorType {
        NETWORK_ERROR,
        TIMEOUT_ERROR,
        AUTHENTICATION_ERROR,
        RATE_LIMIT_ERROR,
        INVALID_REQUEST_ERROR,
        SERVER_ERROR,
        PARSE_ERROR,
        UNKNOWN_ERROR
    }
    
    public LlmException(String message) {
        this(ErrorType.UNKNOWN_ERROR, message, (Throwable) null, (String) null, (Integer) null, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message) {
        this(errorType, message, (Throwable) null, (String) null, (Integer) null, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message, Throwable cause) {
        this(errorType, message, cause, (String) null, (Integer) null, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message, String provider) {
        this(errorType, message, (Throwable) null, provider, (Integer) null, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message, Throwable cause, String provider) {
        this(errorType, message, cause, provider, (Integer) null, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message, String provider, Integer statusCode) {
        this(errorType, message, (Throwable) null, provider, statusCode, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message, Throwable cause, String provider, Integer statusCode) {
        this(errorType, message, cause, provider, statusCode, (String) null);
    }
    
    public LlmException(ErrorType errorType, String message, String provider, Integer statusCode, String requestId) {
        this(errorType, message, (Throwable) null, provider, statusCode, requestId);
    }
    
    public LlmException(ErrorType errorType, String message, Throwable cause, String provider, Integer statusCode, String requestId) {
        super(message, cause);
        this.errorType = errorType;
        this.provider = provider;
        this.statusCode = statusCode;
        this.requestId = requestId;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public boolean isRetryable() {
        switch (errorType) {
            case NETWORK_ERROR:
            case TIMEOUT_ERROR:
            case RATE_LIMIT_ERROR:
            case SERVER_ERROR:
                return true;
            default:
                return false;
        }
    }
    
    public static ErrorType determineErrorType(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) {
            return null;
        }
        switch (statusCode) {
            case 401:
            case 403:
                return ErrorType.AUTHENTICATION_ERROR;
            case 429:
                return ErrorType.RATE_LIMIT_ERROR;
            case 400:
            case 404:
                return ErrorType.INVALID_REQUEST_ERROR;
            case 500:
            case 502:
            case 503:
            case 504:
                return ErrorType.SERVER_ERROR;
            default:
                return ErrorType.UNKNOWN_ERROR;
        }
    }
}
