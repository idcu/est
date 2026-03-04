package ltd.idcu.est.features.security.oauth2;

import ltd.idcu.est.features.security.api.OAuth2Token;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class DefaultOAuth2Token implements OAuth2Token {
    
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final Set<String> scopes;
    private final Date accessTokenExpiresAt;
    private final Date refreshTokenExpiresAt;
    private final String subject;
    private final Date issuedAt;
    private final Map<String, Object> claims;
    private final Set<String> roles;
    private final Set<String> permissions;
    
    public DefaultOAuth2Token(String accessToken, String refreshToken, String tokenType,
                              Set<String> scopes, Date accessTokenExpiresAt, Date refreshTokenExpiresAt,
                              String subject, Date issuedAt, Map<String, Object> claims,
                              Set<String> roles, Set<String> permissions) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.scopes = scopes;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.subject = subject;
        this.issuedAt = issuedAt;
        this.claims = claims != null ? claims : Map.of();
        this.roles = roles != null ? roles : Set.of();
        this.permissions = permissions != null ? permissions : Set.of();
    }
    
    @Override
    public String getAccessToken() {
        return accessToken;
    }
    
    @Override
    public String getRefreshToken() {
        return refreshToken;
    }
    
    @Override
    public String getTokenType() {
        return tokenType;
    }
    
    @Override
    public Set<String> getScopes() {
        return scopes;
    }
    
    @Override
    public Date getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }
    
    @Override
    public Date getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }
    
    @Override
    public boolean isAccessTokenExpired() {
        return accessTokenExpiresAt != null && new Date().after(accessTokenExpiresAt);
    }
    
    @Override
    public boolean isRefreshTokenExpired() {
        return refreshTokenExpiresAt != null && new Date().after(refreshTokenExpiresAt);
    }
    
    @Override
    public String getValue() {
        return accessToken;
    }
    
    @Override
    public String getSubject() {
        return subject;
    }
    
    @Override
    public Date getIssuedAt() {
        return issuedAt;
    }
    
    @Override
    public Date getExpiration() {
        return accessTokenExpiresAt;
    }
    
    @Override
    public boolean isExpired() {
        return isAccessTokenExpired();
    }
    
    @Override
    public boolean isValid() {
        return !isExpired();
    }
    
    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }
    
    @Override
    public Object getClaim(String name) {
        return claims.get(name);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClaim(String name, Class<T> type) {
        Object claim = claims.get(name);
        return claim != null && type.isInstance(claim) ? (T) claim : null;
    }
    
    @Override
    public Set<String> getRoles() {
        return roles;
    }
    
    @Override
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Set<String> scopes = Set.of();
        private Date accessTokenExpiresAt;
        private Date refreshTokenExpiresAt;
        private String subject;
        private Date issuedAt = new Date();
        private Map<String, Object> claims = Map.of();
        private Set<String> roles = Set.of();
        private Set<String> permissions = Set.of();
        
        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
        
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        
        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
        
        public Builder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }
        
        public Builder accessTokenExpiresAt(Date accessTokenExpiresAt) {
            this.accessTokenExpiresAt = accessTokenExpiresAt;
            return this;
        }
        
        public Builder refreshTokenExpiresAt(Date refreshTokenExpiresAt) {
            this.refreshTokenExpiresAt = refreshTokenExpiresAt;
            return this;
        }
        
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }
        
        public Builder issuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }
        
        public Builder claims(Map<String, Object> claims) {
            this.claims = claims;
            return this;
        }
        
        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }
        
        public Builder permissions(Set<String> permissions) {
            this.permissions = permissions;
            return this;
        }
        
        public DefaultOAuth2Token build() {
            return new DefaultOAuth2Token(accessToken, refreshToken, tokenType, scopes,
                    accessTokenExpiresAt, refreshTokenExpiresAt, subject, issuedAt,
                    claims, roles, permissions);
        }
    }
}
