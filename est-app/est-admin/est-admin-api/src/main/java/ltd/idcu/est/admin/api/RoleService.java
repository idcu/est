package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Set;

public interface RoleService {
    
    Role createRole(String name, String description, Set<String> permissions, boolean isDefault);
    
    Role getRole(String id);
    
    Role getRoleByName(String name);
    
    List<Role> getAllRoles();
    
    Role updateRole(String id, String name, String description, Set<String> permissions, boolean isDefault);
    
    void deleteRole(String id);
    
    void assignPermissions(String roleId, Set<String> permissions);
    
    void assignMenus(String roleId, Set<String> menuIds);
}
