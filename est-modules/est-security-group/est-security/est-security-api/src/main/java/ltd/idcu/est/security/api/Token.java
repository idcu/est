package ltd.idcu.est.security.api;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface Token {
    
    String getValue();
    
    String getSubject();
    
    Date getIssuedAt();
    
    Date getExpiration();
    
    boolean isExpired();
    
    boolean isValid();
    
    Map<String, Object> getClaims();
    
    Object getClaim(String name);
    
    <T> T getClaim(String name, Class<T> type);
    
    Set<String> getRoles();
    
    Set<String> getPermissions();
}
