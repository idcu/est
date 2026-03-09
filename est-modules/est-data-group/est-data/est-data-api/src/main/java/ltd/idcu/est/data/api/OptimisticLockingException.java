package ltd.idcu.est.data.api;

public class OptimisticLockingException extends DataException {
    
    public OptimisticLockingException(String message) {
        super(message);
    }
    
    public OptimisticLockingException(String message, Throwable cause) {
        super(message, cause);
    }
}
