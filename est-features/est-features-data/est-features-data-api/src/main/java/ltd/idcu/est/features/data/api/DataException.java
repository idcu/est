package ltd.idcu.est.features.data.api;

public class DataException extends RuntimeException {
    
    public DataException(String message) {
        super(message);
    }
    
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DataException(Throwable cause) {
        super(cause);
    }
}
