package ltd.idcu.est.features.cache.api;

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
