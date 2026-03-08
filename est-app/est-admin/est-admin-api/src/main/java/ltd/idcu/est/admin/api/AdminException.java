package ltd.idcu.est.admin.api;

public class AdminException extends RuntimeException {
    
    public AdminException(String message) {
        super(message);
    }
    
    public AdminException(String message, Throwable cause) {
        super(message, cause);
    }
}
