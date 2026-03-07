package ltd.idcu.est.features.security.api;

public class SecurityException extends RuntimeException {
    
    private final String errorCode;
    
    public SecurityException(String message) {
        super(message);
        this.errorCode = "SECURITY_ERROR";
    }
    
    public SecurityException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "SECURITY_ERROR";
    }
    
    public SecurityException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
