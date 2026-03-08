package ltd.idcu.est.cache.api;

public class CacheException extends RuntimeException {
    
    public CacheException(String message) {
        super(message);
    }
    
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CacheException(Throwable cause) {
        super(cause);
    }
}
