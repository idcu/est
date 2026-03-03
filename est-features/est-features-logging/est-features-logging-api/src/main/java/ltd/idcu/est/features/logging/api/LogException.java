package ltd.idcu.est.features.logging.api;

public class LogException extends RuntimeException {
    
    public LogException(String message) {
        super(message);
    }
    
    public LogException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LogException(Throwable cause) {
        super(cause);
    }
}
