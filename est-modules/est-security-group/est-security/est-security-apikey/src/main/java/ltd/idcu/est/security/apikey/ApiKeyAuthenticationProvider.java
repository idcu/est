package ltd.idcu.est.security.apikey;

import ltd.idcu.est.security.api.ApiKeyAuthentication;
import ltd.idcu.est.security.api.Authentication;
import ltd.idcu.est.security.api.AuthenticationProvider;
import ltd.idcu.est.security.api.SecurityException;

public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    
    private final ApiKeyValidator apiKeyValidator;
    
    public ApiKeyAuthenticationProvider(ApiKeyValidator apiKeyValidator) {
        this.apiKeyValidator = apiKeyValidator;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws SecurityException {
        if (!(authentication instanceof ApiKeyAuthentication)) {
            throw new SecurityException("Unsupported authentication type");
        }
        
        ApiKeyAuthentication apiKeyAuth = (ApiKeyAuthentication) authentication;
        String apiKey = apiKeyAuth.getApiKey();
        
        if (apiKey == null || apiKey.isBlank()) {
            throw new SecurityException("API Key is missing");
        }
        
        ApiKeyValidationResult result = apiKeyValidator.validate(apiKey);
        
        if (!result.isValid()) {
            throw new SecurityException("Invalid API Key");
        }
        
        DefaultApiKeyAuthentication authenticatedAuth = DefaultApiKeyAuthentication.builder()
                .apiKey(apiKey)
                .keyId(apiKeyAuth.getKeyId())
                .clientId(result.getClientId())
                .user(result.getUser())
                .credentials(apiKeyAuth.getCredentials())
                .details(apiKeyAuth.getDetails())
                .build();
        authenticatedAuth.setAuthenticated(true);
        
        return authenticatedAuth;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentication.class.isAssignableFrom(authentication);
    }
    
    public interface ApiKeyValidator {
        ApiKeyValidationResult validate(String apiKey) throws SecurityException;
    }
    
    public interface ApiKeyValidationResult {
        boolean isValid();
        String getClientId();
        ltd.idcu.est.security.api.User getUser();
    }
    
    public static class DefaultApiKeyValidationResult implements ApiKeyValidationResult {
        private final boolean valid;
        private final String clientId;
        private final ltd.idcu.est.security.api.User user;
        
        public DefaultApiKeyValidationResult(boolean valid, String clientId, 
                                               ltd.idcu.est.security.api.User user) {
            this.valid = valid;
            this.clientId = clientId;
            this.user = user;
        }
        
        @Override
        public boolean isValid() {
            return valid;
        }
        
        @Override
        public String getClientId() {
            return clientId;
        }
        
        @Override
        public ltd.idcu.est.security.api.User getUser() {
            return user;
        }
        
        public static ApiKeyValidationResult valid(String clientId, 
                                                     ltd.idcu.est.security.api.User user) {
            return new DefaultApiKeyValidationResult(true, clientId, user);
        }
        
        public static ApiKeyValidationResult invalid() {
            return new DefaultApiKeyValidationResult(false, null, null);
        }
    }
}
