package ltd.idcu.est.features.security.oauth2;

import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.AuthenticationProvider;
import ltd.idcu.est.features.security.api.OAuth2Authentication;
import ltd.idcu.est.features.security.api.OAuth2Token;
import ltd.idcu.est.features.security.api.SecurityException;

public class OAuth2AuthenticationProvider implements AuthenticationProvider {
    
    private final OAuth2TokenValidator tokenValidator;
    
    public OAuth2AuthenticationProvider(OAuth2TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws SecurityException {
        if (!(authentication instanceof OAuth2Authentication)) {
            throw new SecurityException("Unsupported authentication type");
        }
        
        OAuth2Authentication oauthAuth = (OAuth2Authentication) authentication;
        OAuth2Token token = oauthAuth.getOAuth2Token();
        
        if (token == null) {
            throw new SecurityException("OAuth2 token is missing");
        }
        
        if (token.isExpired()) {
            throw new SecurityException("OAuth2 token has expired");
        }
        
        OAuth2Token validatedToken = tokenValidator.validate(token);
        
        DefaultOAuth2Authentication result = DefaultOAuth2Authentication.builder()
                .clientId(oauthAuth.getClientId())
                .scopes(oauthAuth.getScopes())
                .grantType(oauthAuth.getGrantType())
                .oAuth2Token(validatedToken)
                .user(oauthAuth.getUser())
                .credentials(oauthAuth.getCredentials())
                .details(oauthAuth.getDetails())
                .build();
        result.setAuthenticated(true);
        
        return result;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2Authentication.class.isAssignableFrom(authentication);
    }
    
    public interface OAuth2TokenValidator {
        OAuth2Token validate(OAuth2Token token) throws SecurityException;
    }
}
