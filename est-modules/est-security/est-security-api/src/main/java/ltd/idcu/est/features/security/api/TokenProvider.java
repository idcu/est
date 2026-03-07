package ltd.idcu.est.features.security.api;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface TokenProvider {
    
    String generateToken(User user);
    
    String generateToken(User user, Map<String, Object> claims);
    
    String generateToken(User user, long expirationMillis);
    
    Optional<Token> validateToken(String token);
    
    Optional<Token> parseToken(String token);
    
    boolean isTokenExpired(String token);
    
    boolean isTokenValid(String token);
    
    String refresh(String token);
    
    void invalidate(String token);
    
    long getExpirationTime(String token);
    
    Date getIssuedAt(String token);
    
    String getSubject(String token);
}
