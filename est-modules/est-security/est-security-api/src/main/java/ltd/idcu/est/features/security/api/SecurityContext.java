package ltd.idcu.est.features.security.api;

import java.util.Optional;
import java.util.Set;

public interface SecurityContext {
    
    Optional<Authentication> getAuthentication();
    
    void setAuthentication(Authentication authentication);
    
    Optional<User> getCurrentUser();
    
    String getCurrentUsername();
    
    Set<String> getCurrentRoles();
    
    Set<String> getCurrentPermissions();
    
    boolean hasRole(String roleName);
    
    boolean hasPermission(String permissionName);
    
    boolean isAuthenticated();
    
    void clear();
}
