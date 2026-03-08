package ltd.idcu.est.security.oauth2;

import ltd.idcu.est.security.api.OAuth2Authentication;
import ltd.idcu.est.security.api.OAuth2Token;
import ltd.idcu.est.security.api.User;

import java.util.Set;

public class DefaultOAuth2Authentication implements OAuth2Authentication {
    
    private final String clientId;
    private final Set<String> scopes;
    private final String grantType;
    private final OAuth2Token oAuth2Token;
    private final User user;
    private boolean authenticated;
    private final Object credentials;
    private final Object details;
    
    public DefaultOAuth2Authentication(String clientId, Set<String> scopes, String grantType,
                                       OAuth2Token oAuth2Token, User user, Object credentials, Object details) {
        this.clientId = clientId;
        this.scopes = scopes;
        this.grantType = grantType;
        this.oAuth2Token = oAuth2Token;
        this.user = user;
        this.authenticated = false;
        this.credentials = credentials;
        this.details = details;
    }
    
    @Override
    public String getClientId() {
        return clientId;
    }
    
    @Override
    public Set<String> getScopes() {
        return scopes;
    }
    
    @Override
    public String getGrantType() {
        return grantType;
    }
    
    @Override
    public OAuth2Token getOAuth2Token() {
        return oAuth2Token;
    }
    
    @Override
    public String getToken() {
        return oAuth2Token != null ? oAuth2Token.getValue() : null;
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
        private String clientId;
        private Set<String> scopes = Set.of();
        private String grantType;
        private OAuth2Token oAuth2Token;
        private User user;
        private Object credentials;
        private Object details;
        
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public Builder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }
        
        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }
        
        public Builder oAuth2Token(OAuth2Token oAuth2Token) {
            this.oAuth2Token = oAuth2Token;
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
        
        public DefaultOAuth2Authentication build() {
            return new DefaultOAuth2Authentication(clientId, scopes, grantType,
                    oAuth2Token, user, credentials, details);
        }
    }
}
