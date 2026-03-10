package ltd.idcu.est.ai.api.vector;

public class VectorStoreRuntimeException extends RuntimeException implements VectorStoreException {
    private final ErrorType errorType;
    private final String storeName;
    private final Throwable cause;
    
    public VectorStoreRuntimeException(ErrorType errorType, String message, String storeName, Throwable cause) {
        super(message);
        this.errorType = errorType;
        this.storeName = storeName;
        this.cause = cause;
    }
    
    public VectorStoreRuntimeException(ErrorType errorType, String message, String storeName) {
        this(errorType, message, storeName, null);
    }
    
    @Override
    public ErrorType getErrorType() {
        return errorType;
    }
    
    @Override
    public String getStoreName() {
        return storeName;
    }
    
    @Override
    public Throwable getCause() {
        return cause;
    }
}
