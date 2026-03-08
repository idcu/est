package ltd.idcu.est.rbac.api;

import java.util.Set;

public interface User {
    
    String getId();
    
    String getUsername();
    
    String getEmail();
    
    Set<String> getRoles();
    
    Set<String> getPermissions();
    
    boolean isActive();
    
    long getCreatedAt();
}
