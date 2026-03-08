package ltd.idcu.est.ai.api.vector;

import java.util.Map;

public interface VectorStoreException {
    
    enum ErrorType {
        CONNECTION_ERROR,
        COLLECTION_ERROR,
        VECTOR_ERROR,
        SEARCH_ERROR,
        PERMISSION_ERROR,
        UNKNOWN_ERROR
    }
    
    ErrorType getErrorType();
    
    String getMessage();
    
    String getStoreName();
    
    Throwable getCause();
}
