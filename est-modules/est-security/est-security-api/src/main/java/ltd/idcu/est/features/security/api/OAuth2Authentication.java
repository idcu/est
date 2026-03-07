package ltd.idcu.est.features.security.api;

import java.util.Set;

public interface OAuth2Authentication extends Authentication {
    
    String getClientId();
    
    Set<String> getScopes();
    
    String getGrantType();
    
    OAuth2Token getOAuth2Token();
}
