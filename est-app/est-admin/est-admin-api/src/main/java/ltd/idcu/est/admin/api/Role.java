package ltd.idcu.est.admin.api;

import java.util.Set;

public interface Role {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    Set<String> getPermissions();
    
    boolean isDefault();
}
