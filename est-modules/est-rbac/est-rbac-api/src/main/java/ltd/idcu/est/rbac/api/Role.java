package ltd.idcu.est.rbac.api;

import java.util.Set;

public interface Role {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    Set<String> getPermissions();
    
    boolean isDefault();
}
