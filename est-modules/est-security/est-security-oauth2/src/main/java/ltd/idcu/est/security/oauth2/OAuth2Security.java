package ltd.idcu.est.security.oauth2;

import ltd.idcu.est.security.api.OAuth2Token;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class OAuth2Security {
    
    private OAuth2Security() {
    }
    
    public static OAuth2Token generateToken(String subject, Set<String> scopes,
                                             long accessTokenExpirationMillis, long refreshTokenExpirationMillis) {
        return generateToken(subject, scopes, accessTokenExpirationMillis, 
                            refreshTokenExpirationMillis, Map.of(), Set.of(), Set.of());
    }
    
    public static OAuth2Token generateToken(String subject, Set<String> scopes,
                                             long accessTokenExpirationMillis, long refreshTokenExpirationMillis,
                                             Map<String, Object> claims, Set<String> roles, Set<String> permissions) {
        Date now = new Date();
        Date accessTokenExpiresAt = new Date(now.getTime() + accessTokenExpirationMillis);
        Date refreshTokenExpiresAt = new Date(now.getTime() + refreshTokenExpirationMillis);
        
        return DefaultOAuth2Token.builder()
                .accessToken(generateRandomToken())
                .refreshToken(generateRandomToken())
                .tokenType("Bearer")
                .scopes(scopes)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .subject(subject)
                .issuedAt(now)
                .claims(claims)
                .roles(roles)
                .permissions(permissions)
                .build();
    }
    
    private static String generateRandomToken() {
        return UUID.randomUUID().toString().replace("-", "") +
               UUID.randomUUID().toString().replace("-", "");
    }
    
    public static OAuth2AuthenticationProvider createProvider(
            OAuth2AuthenticationProvider.OAuth2TokenValidator validator) {
        return new OAuth2AuthenticationProvider(validator);
    }
}
