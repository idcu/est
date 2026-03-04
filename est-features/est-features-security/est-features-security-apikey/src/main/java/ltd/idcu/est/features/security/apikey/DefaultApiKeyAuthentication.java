package ltd.idcu.est.features.security.apikey;

import ltd.idcu.est.features.security.api.ApiKeyAuthentication;
import ltd.idcu.est.features.security.api.User;

public class DefaultApiKeyAuthentication implements ApiKeyAuthentication {
    
    private final String apiKey;
    private final String keyId;
    private final String clientId;
    private final User user;
    private boolean authenticated;
    private final Object credentials;
    private final Object details;
    
    public DefaultApiKeyAuthentication(String apiKey, String keyId, String clientId,
                                       User user, Object credentials, Object details) {
        this.apiKey = apiKey;
        this.keyId = keyId;
        this.clientId = clientId;
        this.user = user;
        this.authenticated = false;
        this.credentials = credentials;
        this.details = details;
    }
    
    @Override
    public String getApiKey() {
        return apiKey;
    }
    
    @Override
    public String getKeyId() {
        return keyId;
    }
    
    @Override
    public String getClientId() {
        return clientId;
    }
    
    @Override
    public String getToken() {
        return apiKey;
    }
    
    @Override
    public User getUser() {
        return user;
    }
    
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    @Override
    public Object getCredentials() {
        return credentials;
    }
    
    @Override
    public Object getDetails() {
        return details;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String apiKey;
        private String keyId;
        private String clientId;
        private User user;
        private Object credentials;
        private Object details;
        
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        
        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }
        
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        
        public Builder credentials(Object credentials) {
            this.credentials = credentials;
            return this;
        }
        
        public Builder details(Object details) {
            this.details = details;
            return this;
        }
        
        public DefaultApiKeyAuthentication build() {
            return new DefaultApiKeyAuthentication(apiKey, keyId, clientId,
                    user, credentials, details);
        }
    }
}
