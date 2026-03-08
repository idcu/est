package ltd.idcu.est.security.api;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface OAuth2Token extends Token {
    
    String getAccessToken();
    
    String getRefreshToken();
    
    String getTokenType();
    
    Set<String> getScopes();
    
    Date getAccessTokenExpiresAt();
    
    Date getRefreshTokenExpiresAt();
    
    boolean isAccessTokenExpired();
    
    boolean isRefreshTokenExpired();
}
