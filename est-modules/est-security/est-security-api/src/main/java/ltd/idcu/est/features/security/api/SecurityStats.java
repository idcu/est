package ltd.idcu.est.features.security.api;

public class SecurityStats {
    
    private long authenticationCount;
    private long successfulAuthentications;
    private long failedAuthentications;
    private long authorizationCount;
    private long grantedAuthorizations;
    private long deniedAuthorizations;
    private long tokenGeneratedCount;
    private long tokenValidatedCount;
    private long tokenExpiredCount;
    private long tokenInvalidatedCount;
    
    public SecurityStats() {
        this.authenticationCount = 0;
        this.successfulAuthentications = 0;
        this.failedAuthentications = 0;
        this.authorizationCount = 0;
        this.grantedAuthorizations = 0;
        this.deniedAuthorizations = 0;
        this.tokenGeneratedCount = 0;
        this.tokenValidatedCount = 0;
        this.tokenExpiredCount = 0;
        this.tokenInvalidatedCount = 0;
    }
    
    public long getAuthenticationCount() {
        return authenticationCount;
    }
    
    public void incrementAuthenticationCount() {
        this.authenticationCount++;
    }
    
    public long getSuccessfulAuthentications() {
        return successfulAuthentications;
    }
    
    public void incrementSuccessfulAuthentications() {
        this.successfulAuthentications++;
    }
    
    public long getFailedAuthentications() {
        return failedAuthentications;
    }
    
    public void incrementFailedAuthentications() {
        this.failedAuthentications++;
    }
    
    public long getAuthorizationCount() {
        return authorizationCount;
    }
    
    public void incrementAuthorizationCount() {
        this.authorizationCount++;
    }
    
    public long getGrantedAuthorizations() {
        return grantedAuthorizations;
    }
    
    public void incrementGrantedAuthorizations() {
        this.grantedAuthorizations++;
    }
    
    public long getDeniedAuthorizations() {
        return deniedAuthorizations;
    }
    
    public void incrementDeniedAuthorizations() {
        this.deniedAuthorizations++;
    }
    
    public long getTokenGeneratedCount() {
        return tokenGeneratedCount;
    }
    
    public void incrementTokenGeneratedCount() {
        this.tokenGeneratedCount++;
    }
    
    public long getTokenValidatedCount() {
        return tokenValidatedCount;
    }
    
    public void incrementTokenValidatedCount() {
        this.tokenValidatedCount++;
    }
    
    public long getTokenExpiredCount() {
        return tokenExpiredCount;
    }
    
    public void incrementTokenExpiredCount() {
        this.tokenExpiredCount++;
    }
    
    public long getTokenInvalidatedCount() {
        return tokenInvalidatedCount;
    }
    
    public void incrementTokenInvalidatedCount() {
        this.tokenInvalidatedCount++;
    }
    
    public double getAuthenticationSuccessRate() {
        if (authenticationCount == 0) {
            return 0.0;
        }
        return (double) successfulAuthentications / authenticationCount;
    }
    
    public double getAuthorizationGrantRate() {
        if (authorizationCount == 0) {
            return 0.0;
        }
        return (double) grantedAuthorizations / authorizationCount;
    }
    
    public void reset() {
        this.authenticationCount = 0;
        this.successfulAuthentications = 0;
        this.failedAuthentications = 0;
        this.authorizationCount = 0;
        this.grantedAuthorizations = 0;
        this.deniedAuthorizations = 0;
        this.tokenGeneratedCount = 0;
        this.tokenValidatedCount = 0;
        this.tokenExpiredCount = 0;
        this.tokenInvalidatedCount = 0;
    }
    
    public SecurityStats snapshot() {
        SecurityStats snapshot = new SecurityStats();
        snapshot.authenticationCount = this.authenticationCount;
        snapshot.successfulAuthentications = this.successfulAuthentications;
        snapshot.failedAuthentications = this.failedAuthentications;
        snapshot.authorizationCount = this.authorizationCount;
        snapshot.grantedAuthorizations = this.grantedAuthorizations;
        snapshot.deniedAuthorizations = this.deniedAuthorizations;
        snapshot.tokenGeneratedCount = this.tokenGeneratedCount;
        snapshot.tokenValidatedCount = this.tokenValidatedCount;
        snapshot.tokenExpiredCount = this.tokenExpiredCount;
        snapshot.tokenInvalidatedCount = this.tokenInvalidatedCount;
        return snapshot;
    }
    
    @Override
    public String toString() {
        return "SecurityStats{" +
                "authenticationCount=" + authenticationCount +
                ", successfulAuthentications=" + successfulAuthentications +
                ", failedAuthentications=" + failedAuthentications +
                ", authenticationSuccessRate=" + String.format("%.2f%%", getAuthenticationSuccessRate() * 100) +
                ", authorizationCount=" + authorizationCount +
                ", grantedAuthorizations=" + grantedAuthorizations +
                ", deniedAuthorizations=" + deniedAuthorizations +
                ", authorizationGrantRate=" + String.format("%.2f%%", getAuthorizationGrantRate() * 100) +
                ", tokenGeneratedCount=" + tokenGeneratedCount +
                ", tokenValidatedCount=" + tokenValidatedCount +
                ", tokenExpiredCount=" + tokenExpiredCount +
                ", tokenInvalidatedCount=" + tokenInvalidatedCount +
                '}';
    }
}
