package ltd.idcu.est.security.api;

public interface CsrfToken {
    
    String getHeaderName();
    
    String getParameterName();
    
    String getToken();
}
