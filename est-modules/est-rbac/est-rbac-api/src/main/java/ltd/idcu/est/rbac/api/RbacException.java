package ltd.idcu.est.rbac.api;

public class RbacException extends RuntimeException {
    
    public RbacException(String message) {
        super(message);
    }
    
    public RbacException(String message, Throwable cause) {
        super(message, cause);
    }
}
