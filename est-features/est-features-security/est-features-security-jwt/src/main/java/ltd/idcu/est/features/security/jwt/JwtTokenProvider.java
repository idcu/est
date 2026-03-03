package ltd.idcu.est.features.security.jwt;

import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.api.SecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JwtTokenProvider implements TokenProvider {
    
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String HMAC_SHA256 = "HmacSHA256";
    
    private final SecurityConfig config;
    private final SecretKeySpec secretKey;
    private final SecurityStats stats;
    private final Map<String, JwtTokenInfo> tokenStore;
    private final Set<String> invalidatedTokens;
    private final ScheduledExecutorService cleanupExecutor;
    
    public JwtTokenProvider() {
        this(SecurityConfig.defaultConfig());
    }
    
    public JwtTokenProvider(SecurityConfig config) {
        this.config = config;
        this.secretKey = createSecretKey(config.getSecretKey());
        this.stats = new SecurityStats();
        this.tokenStore = new ConcurrentHashMap<>();
        this.invalidatedTokens = ConcurrentHashMap.newKeySet();
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "jwt-cleanup");
            t.setDaemon(true);
            return t;
        });
        this.cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredTokens, 5, 5, TimeUnit.MINUTES);
    }
    
    public JwtTokenProvider(String secretKey) {
        this(SecurityConfig.of(secretKey));
    }
    
    private SecretKeySpec createSecretKey(String secret) {
        if (secret == null || secret.isEmpty()) {
            byte[] randomKey = new byte[32];
            new SecureRandom().nextBytes(randomKey);
            return new SecretKeySpec(randomKey, HMAC_SHA256);
        }
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
    }
    
    @Override
    public String generateToken(User user) {
        return generateToken(user, config.getTokenExpiration());
    }
    
    @Override
    public String generateToken(User user, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        long expiration = now + config.getTokenExpiration();
        
        Map<String, Object> allClaims = new HashMap<>();
        if (claims != null) {
            allClaims.putAll(claims);
        }
        
        return generateJwtToken(user, now, expiration, allClaims);
    }
    
    @Override
    public String generateToken(User user, long expirationMillis) {
        long now = System.currentTimeMillis();
        long expiration = now + expirationMillis;
        
        return generateJwtToken(user, now, expiration, new HashMap<>());
    }
    
    private String generateJwtToken(User user, long issuedAt, long expiration, Map<String, Object> claims) {
        if (user == null) {
            throw new SecurityException("User cannot be null", "INVALID_USER");
        }
        
        String tokenId = generateTokenId();
        
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("jti", tokenId);
        payload.put("iss", config.getIssuer());
        payload.put("sub", user.getUsername());
        payload.put("aud", config.getIssuer());
        payload.put("iat", issuedAt / 1000);
        payload.put("exp", expiration / 1000);
        payload.put("nbf", issuedAt / 1000);
        payload.put("user_id", user.getId());
        
        if (!claims.isEmpty()) {
            payload.putAll(claims);
        }
        
        String headerEncoded = base64UrlEncode(JWT_HEADER);
        String payloadEncoded = base64UrlEncode(toJson(payload));
        String signature = sign(headerEncoded + "." + payloadEncoded);
        
        String token = headerEncoded + "." + payloadEncoded + "." + signature;
        
        JwtTokenInfo tokenInfo = new JwtTokenInfo(
                tokenId,
                user.getId(),
                user.getUsername(),
                issuedAt,
                expiration,
                claims
        );
        tokenStore.put(tokenId, tokenInfo);
        
        stats.incrementTokenGeneratedCount();
        
        return token;
    }
    
    @Override
    public Optional<Token> validateToken(String token) {
        stats.incrementTokenValidatedCount();
        
        if (token == null || token.isEmpty()) {
            return Optional.empty();
        }
        
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }
            
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];
            
            String expectedSignature = sign(header + "." + payload);
            if (!constantTimeEquals(signature, expectedSignature)) {
                return Optional.empty();
            }
            
            String payloadJson = base64UrlDecode(payload);
            Map<String, Object> claims = parseJson(payloadJson);
            
            String tokenId = (String) claims.get("jti");
            if (tokenId != null && invalidatedTokens.contains(tokenId)) {
                stats.incrementTokenInvalidatedCount();
                return Optional.empty();
            }
            
            long exp = ((Number) claims.get("exp")).longValue() * 1000;
            if (System.currentTimeMillis() > exp) {
                stats.incrementTokenExpiredCount();
                return Optional.empty();
            }
            
            String subject = (String) claims.get("sub");
            String userId = (String) claims.get("user_id");
            long iat = ((Number) claims.get("iat")).longValue() * 1000;
            
            JwtTokenInfo tokenInfo = new JwtTokenInfo(
                    tokenId,
                    userId,
                    subject,
                    iat,
                    exp,
                    claims
            );
            
            return Optional.of(new JwtToken(token, tokenInfo));
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
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payloadJson = base64UrlDecode(parts[1]);
                Map<String, Object> claims = parseJson(payloadJson);
                String tokenId = (String) claims.get("jti");
                if (tokenId != null) {
                    invalidatedTokens.add(tokenId);
                    tokenStore.remove(tokenId);
                    stats.incrementTokenInvalidatedCount();
                }
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
    
    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKey);
            byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(signature);
        } catch (Exception e) {
            throw new SecurityException("JWT signing failed", "JWT_SIGN_ERROR", e);
        }
    }
    
    private String generateTokenId() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return base64UrlEncode(bytes);
    }
    
    private String base64UrlEncode(String input) {
        return base64UrlEncode(input.getBytes(StandardCharsets.UTF_8));
    }
    
    private String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }
    
    private String base64UrlDecode(String input) {
        return new String(Base64.getUrlDecoder().decode(input), StandardCharsets.UTF_8);
    }
    
    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(escapeJson((String) value)).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value instanceof Collection<?> collection) {
                sb.append("[");
                boolean firstItem = true;
                for (Object item : collection) {
                    if (!firstItem) {
                        sb.append(",");
                    }
                    if (item instanceof String) {
                        sb.append("\"").append(escapeJson((String) item)).append("\"");
                    } else {
                        sb.append(item);
                    }
                    firstItem = false;
                }
                sb.append("]");
            } else if (value == null) {
                sb.append("null");
            } else {
                sb.append("\"").append(escapeJson(value.toString())).append("\"");
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String escapeJson(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) {
            return result;
        }
        
        int depth = 0;
        StringBuilder currentKey = new StringBuilder();
        StringBuilder currentValue = new StringBuilder();
        boolean inKey = true;
        boolean inString = false;
        boolean escaped = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (escaped) {
                if (inKey) {
                    currentKey.append(c);
                } else {
                    currentValue.append(c);
                }
                escaped = false;
                continue;
            }
            
            if (c == '\\' && inString) {
                escaped = true;
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                if (inKey) {
                    continue;
                }
            }
            
            if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ':' && depth == 0 && inKey) {
                    inKey = false;
                    continue;
                } else if (c == ',' && depth == 0 && !inKey) {
                    String key = currentKey.toString().trim();
                    String value = currentValue.toString().trim();
                    result.put(key, parseValue(value));
                    currentKey = new StringBuilder();
                    currentValue = new StringBuilder();
                    inKey = true;
                    continue;
                }
            }
            
            if (inKey) {
                currentKey.append(c);
            } else {
                currentValue.append(c);
            }
        }
        
        if (currentKey.length() > 0) {
            String key = currentKey.toString().trim();
            String value = currentValue.toString().trim();
            result.put(key, parseValue(value));
        }
        
        return result;
    }
    
    private Object parseValue(String value) {
        value = value.trim();
        
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        if ("null".equalsIgnoreCase(value)) {
            return null;
        }
        
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
    
    private void cleanupExpiredTokens() {
        List<String> expiredTokenIds = new ArrayList<>();
        for (Map.Entry<String, JwtTokenInfo> entry : tokenStore.entrySet()) {
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
    
    private static class JwtTokenInfo {
        private final String tokenId;
        private final String userId;
        private final String username;
        private final long issuedAt;
        private final long expiration;
        private final Map<String, Object> claims;
        
        JwtTokenInfo(String tokenId, String userId, String username, long issuedAt, long expiration, Map<String, Object> claims) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.username = username;
            this.issuedAt = issuedAt;
            this.expiration = expiration;
            this.claims = claims != null ? claims : new HashMap<>();
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
    
    private static class JwtToken implements Token {
        private final String value;
        private final JwtTokenInfo tokenInfo;
        
        JwtToken(String value, JwtTokenInfo tokenInfo) {
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
        @SuppressWarnings("unchecked")
        public Set<String> getRoles() {
            Object roles = getClaim("roles");
            if (roles instanceof Collection<?> collection) {
                return new HashSet<>((Collection<? extends String>) collection);
            }
            return Set.of();
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public Set<String> getPermissions() {
            Object permissions = getClaim("permissions");
            if (permissions instanceof Collection<?> collection) {
                return new HashSet<>((Collection<? extends String>) collection);
            }
            return Set.of();
        }
    }
    
    public static JwtTokenProvider create() {
        return new JwtTokenProvider();
    }
    
    public static JwtTokenProvider create(String secretKey) {
        return new JwtTokenProvider(secretKey);
    }
}
