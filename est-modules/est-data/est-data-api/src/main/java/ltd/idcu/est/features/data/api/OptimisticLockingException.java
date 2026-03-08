package ltd.idcu.est.features.data.api;

public class OptimisticLockingException extends DataException {
    
    public OptimisticLockingException(String message) {
        super(message);
    }
    
    public OptimisticLockingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OptimisticLockingException(Throwable cause) {
        super(cause);
    }
}
