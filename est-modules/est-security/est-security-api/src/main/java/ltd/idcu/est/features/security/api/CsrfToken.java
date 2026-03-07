package ltd.idcu.est.features.security.api;

public interface CsrfToken {
    
    String getHeaderName();
    
    String getParameterName();
    
    String getToken();
}
