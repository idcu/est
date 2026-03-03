package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.api.SecurityException;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BasicTokenProvider implements TokenProvider {
    
    private static final String TOKEN_PREFIX = "BASIC_";
    private static final int TOKEN_LENGTH = 32;
    
    private final SecurityConfig config;
    private final Crypto crypto;
    private final SecurityStats stats;
    private final Map<String, TokenInfo> tokenStore;
    private final Set<String> invalidatedTokens;
    private final ScheduledExecutorService cleanupExecutor;
    
    public BasicTokenProvider() {
        this(SecurityConfig.defaultConfig());
    }
    
    public BasicTokenProvider(SecurityConfig config) {
        this.config = config;
        this.crypto = new BasicCrypto(config.getSecretKey(), config.getAlgorithm(), config.getKeyLength());
        this.stats = new SecurityStats();
        this.tokenStore = new ConcurrentHashMap<>();
        this.invalidatedTokens = ConcurrentHashMap.newKeySet();
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "token-cleanup");
            t.setDaemon(true);
            return t;
        });
        this.cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredTokens, 5, 5, TimeUnit.MINUTES);
    }
    
    public BasicTokenProvider(String secretKey) {
        this(SecurityConfig.of(secretKey));
    }
    
    @Override
    public String generateToken(User user) {
        return generateToken(user, config.getTokenExpiration());
    }
    
    @Override
    public String generateToken(User user, Map<String, Object> claims) {
        return generateToken(user, config.getTokenExpiration());
    }
    
    @Override
    public String generateToken(User user, long expirationMillis) {
        if (user == null) {
            throw new SecurityException("User cannot be null", "INVALID_USER");
        }
        
        String tokenId = generateTokenId();
        long now = System.currentTimeMillis();
        long expiration = now + expirationMillis;
        
        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(TOKEN_PREFIX);
        tokenBuilder.append(tokenId);
        tokenBuilder.append(":");
        tokenBuilder.append(user.getId());
        tokenBuilder.append(":");
        tokenBuilder.append(user.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(now);
        tokenBuilder.append(":");
        tokenBuilder.append(expiration);
        
        String rawToken = tokenBuilder.toString();
        String encryptedToken = crypto.encrypt(rawToken);
        
        TokenInfo tokenInfo = new TokenInfo(
                tokenId,
                user.getId(),
                user.getUsername(),
                now,
                expiration,
                Collections.emptyMap()
        );
        tokenStore.put(tokenId, tokenInfo);
        
        stats.incrementTokenGeneratedCount();
        
        return encryptedToken;
    }
    
    @Override
    public Optional<Token> validateToken(String token) {
        stats.incrementTokenValidatedCount();
        
        if (token == null || token.isEmpty()) {
            return Optional.empty();
        }
        
        try {
            String decrypted = crypto.decrypt(token);
            
            if (!decrypted.startsWith(TOKEN_PREFIX)) {
                return Optional.empty();
            }
            
            String[] parts = decrypted.split(":");
            if (parts.length < 6) {
                return Optional.empty();
            }
            
            String tokenId = parts[0].substring(TOKEN_PREFIX.length());
            
            if (invalidatedTokens.contains(tokenId)) {
                stats.incrementTokenInvalidatedCount();
                return Optional.empty();
            }
            
            TokenInfo tokenInfo = tokenStore.get(tokenId);
            if (tokenInfo == null) {
                return Optional.empty();
            }
            
            if (tokenInfo.isExpired()) {
                stats.incrementTokenExpiredCount();
                tokenStore.remove(tokenId);
                return Optional.empty();
            }
            
            return Optional.of(new BasicToken(token, tokenInfo));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Token> parseToken(String token) {
        return validateToken(token);
    }
    
    @Override
    public boolean isTokenExpired(String token) {
        return validateToken(token)
                .map(Token::isExpired)
                .orElse(true);
    }
    
    @Override
    public boolean isTokenValid(String token) {
        return validateToken(token).isPresent();
    }
    
    @Override
    public String refresh(String token) {
        Optional<Token> validatedToken = validateToken(token);
        if (validatedToken.isEmpty()) {
            throw new SecurityException("Invalid token for refresh", "INVALID_TOKEN");
        }
        
        Token t = validatedToken.get();
        invalidate(token);
        
        return t.getSubject();
    }
    
    @Override
    public void invalidate(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        
        try {
            String decrypted = crypto.decrypt(token);
            String[] parts = decrypted.split(":");
            if (parts.length >= 1) {
                String tokenId = parts[0].substring(TOKEN_PREFIX.length());
                invalidatedTokens.add(tokenId);
                tokenStore.remove(tokenId);
                stats.incrementTokenInvalidatedCount();
            }
        } catch (Exception ignored) {
        }
    }
    
    @Override
    public long getExpirationTime(String token) {
        return validateToken(token)
                .map(Token::getExpiration)
                .map(Date::getTime)
                .orElse(0L);
    }
    
    @Override
    public Date getIssuedAt(String token) {
        return validateToken(token)
                .map(Token::getIssuedAt)
                .orElse(new Date(0));
    }
    
    @Override
    public String getSubject(String token) {
        return validateToken(token)
                .map(Token::getSubject)
                .orElse(null);
    }
    
    private String generateTokenId() {
        byte[] bytes = new byte[TOKEN_LENGTH / 2];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    private void cleanupExpiredTokens() {
        List<String> expiredTokenIds = new ArrayList<>();
        for (Map.Entry<String, TokenInfo> entry : tokenStore.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredTokenIds.add(entry.getKey());
            }
        }
        for (String tokenId : expiredTokenIds) {
            tokenStore.remove(tokenId);
            stats.incrementTokenExpiredCount();
        }
    }
    
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public SecurityStats getStats() {
        return stats.snapshot();
    }
    
    private static class TokenInfo {
        private final String tokenId;
        private final String userId;
        private final String username;
        private final long issuedAt;
        private final long expiration;
        private final Map<String, Object> claims;
        
        TokenInfo(String tokenId, String userId, String username, long issuedAt, long expiration, Map<String, Object> claims) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.username = username;
            this.issuedAt = issuedAt;
            this.expiration = expiration;
            this.claims = claims;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiration;
        }
        
        String getTokenId() { return tokenId; }
        String getUserId() { return userId; }
        String getUsername() { return username; }
        long getIssuedAt() { return issuedAt; }
        long getExpiration() { return expiration; }
        Map<String, Object> getClaims() { return claims; }
    }
    
    private static class BasicToken implements Token {
        private final String value;
        private final TokenInfo tokenInfo;
        
        BasicToken(String value, TokenInfo tokenInfo) {
            this.value = value;
            this.tokenInfo = tokenInfo;
        }
        
        @Override
        public String getValue() {
            return value;
        }
        
        @Override
        public String getSubject() {
            return tokenInfo.getUsername();
        }
        
        @Override
        public Date getIssuedAt() {
            return new Date(tokenInfo.getIssuedAt());
        }
        
        @Override
        public Date getExpiration() {
            return new Date(tokenInfo.getExpiration());
        }
        
        @Override
        public boolean isExpired() {
            return tokenInfo.isExpired();
        }
        
        @Override
        public boolean isValid() {
            return !isExpired();
        }
        
        @Override
        public Map<String, Object> getClaims() {
            return Collections.unmodifiableMap(tokenInfo.getClaims());
        }
        
        @Override
        public Object getClaim(String name) {
            return tokenInfo.getClaims().get(name);
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getClaim(String name, Class<T> type) {
            Object claim = getClaim(name);
            if (claim != null && type.isInstance(claim)) {
                return (T) claim;
            }
            return null;
        }
        
        @Override
        public Set<String> getRoles() {
            Object roles = getClaim("roles");
            if (roles instanceof Set<?> set) {
                return (Set<String>) set;
            }
            return Set.of();
        }
        
        @Override
        public Set<String> getPermissions() {
            Object permissions = getClaim("permissions");
            if (permissions instanceof Set<?> set) {
                return (Set<String>) set;
            }
            return Set.of();
        }
    }
    
    public static BasicTokenProvider create() {
        return new BasicTokenProvider();
    }
    
    public static BasicTokenProvider create(String secretKey) {
        return new BasicTokenProvider(secretKey);
    }
}
