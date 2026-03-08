package ltd.idcu.est.security.api;

import java.util.Set;

public class SecurityConfig {
    
    private String secretKey;
    private String algorithm = "AES";
    private long tokenExpiration = 3600000;
    private long refreshTokenExpiration = 86400000;
    private int keyLength = 256;
    private boolean requireSecureChannel = false;
    private Set<String> permittedEndpoints = Set.of("/login", "/register", "/health");
    private Set<String> publicEndpoints = Set.of("/public/**");
    private int maxLoginAttempts = 5;
    private long lockoutDuration = 1800000;
    private boolean enableTokenRefresh = true;
    private String issuer = "est-security";
    
    public SecurityConfig() {
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public SecurityConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
    
    public SecurityConfig setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }
    
    public long getTokenExpiration() {
        return tokenExpiration;
    }
    
    public SecurityConfig setTokenExpiration(long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
        return this;
    }
    
    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
    
    public SecurityConfig setRefreshTokenExpiration(long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
        return this;
    }
    
    public int getKeyLength() {
        return keyLength;
    }
    
    public SecurityConfig setKeyLength(int keyLength) {
        this.keyLength = keyLength;
        return this;
    }
    
    public boolean isRequireSecureChannel() {
        return requireSecureChannel;
    }
    
    public SecurityConfig setRequireSecureChannel(boolean requireSecureChannel) {
        this.requireSecureChannel = requireSecureChannel;
        return this;
    }
    
    public Set<String> getPermittedEndpoints() {
        return permittedEndpoints;
    }
    
    public SecurityConfig setPermittedEndpoints(Set<String> permittedEndpoints) {
        this.permittedEndpoints = permittedEndpoints;
        return this;
    }
    
    public Set<String> getPublicEndpoints() {
        return publicEndpoints;
    }
    
    public SecurityConfig setPublicEndpoints(Set<String> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
        return this;
    }
    
    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }
    
    public SecurityConfig setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
        return this;
    }
    
    public long getLockoutDuration() {
        return lockoutDuration;
    }
    
    public SecurityConfig setLockoutDuration(long lockoutDuration) {
        this.lockoutDuration = lockoutDuration;
        return this;
    }
    
    public boolean isEnableTokenRefresh() {
        return enableTokenRefresh;
    }
    
    public SecurityConfig setEnableTokenRefresh(boolean enableTokenRefresh) {
        this.enableTokenRefresh = enableTokenRefresh;
        return this;
    }
    
    public String getIssuer() {
        return issuer;
    }
    
    public SecurityConfig setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }
    
    public static SecurityConfig defaultConfig() {
        return new SecurityConfig();
    }
    
    public static SecurityConfig of(String secretKey) {
        return new SecurityConfig().setSecretKey(secretKey);
    }
    
    @Override
    public String toString() {
        return "SecurityConfig{" +
                "algorithm='" + algorithm + '\'' +
                ", tokenExpiration=" + tokenExpiration +
                ", refreshTokenExpiration=" + refreshTokenExpiration +
                ", keyLength=" + keyLength +
                ", issuer='" + issuer + '\'' +
                '}';
    }
}
