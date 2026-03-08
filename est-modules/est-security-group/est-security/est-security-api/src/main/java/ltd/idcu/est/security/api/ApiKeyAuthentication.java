package ltd.idcu.est.security.api;

public interface ApiKeyAuthentication extends Authentication {
    
    String getApiKey();
    
    String getKeyId();
    
    String getClientId();
}
