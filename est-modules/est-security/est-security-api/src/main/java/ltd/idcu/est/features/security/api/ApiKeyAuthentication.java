package ltd.idcu.est.features.security.api;

public interface ApiKeyAuthentication extends Authentication {
    
    String getApiKey();
    
    String getKeyId();
    
    String getClientId();
}
