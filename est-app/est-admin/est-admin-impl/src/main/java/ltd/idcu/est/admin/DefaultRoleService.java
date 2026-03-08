package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Role;
import ltd.idcu.est.admin.api.RoleService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRoleService implements RoleService {
    
    private final Map<String, Role> rolesById;
    private final Map<String, Role> rolesByName;
    private final Map<String, Set<String>> roleMenus;
    
    public DefaultRoleService() {
        this.rolesById = new ConcurrentHashMap<>();
        this.rolesByName = new ConcurrentHashMap<>();
        this.roleMenus = new ConcurrentHashMap<>();
        initializeDefaultRoles();
    }
    
    private void initializeDefaultRoles() {
        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("*");
        
        DefaultRole adminRole = new DefaultRole(
            UUID.randomUUID().toString(),
            "admin",
            "超级管理员",
            adminPermissions,
            false
        );
        
        rolesById.put(adminRole.getId(), adminRole);
        rolesByName.put(adminRole.getName(), adminRole);
        
        DefaultRole userRole = new DefaultRole(
            UUID.randomUUID().toString(),
            "user",
            "普通用户",
            new HashSet<>(),
            true
        );
        
        rolesById.put(userRole.getId(), userRole);
        rolesByName.put(userRole.getName(), userRole);
    }
    
    @Override
    public Role createRole(String name, String description, Set<String> permissions, boolean isDefault) {
        if (rolesByName.containsKey(name)) {
            throw new AdminException("Role already exists: " + name);
        }
        
        String id = UUID.randomUUID().toString();
        DefaultRole role = new DefaultRole(
            id,
            name,
            description,
            permissions != null ? permissions : new HashSet<>(),
            isDefault
        );
        
        rolesById.put(id, role);
        rolesByName.put(name, role);
        
        return role;
    }
    
    @Override
    public Role getRole(String id) {
        return rolesById.get(id);
    }
    
    @Override
    public Role getRoleByName(String name) {
        return rolesByName.get(name);
    }
    
    @Override
    public List<Role> getAllRoles() {
        return new ArrayList<>(rolesById.values());
    }
    
    @Override
    public Role updateRole(String id, String name, String description, Set<String> permissions, boolean isDefault) {
        Role existingRole = rolesById.get(id);
        if (existingRole == null) {
            throw new AdminException("Role not found: " + id);
        }
        
        if (!existingRole.getName().equals(name) && rolesByName.containsKey(name)) {
            throw new AdminException("Role name already exists: " + name);
        }
        
        DefaultRole updatedRole = new DefaultRole(
            id,
            name != null ? name : existingRole.getName(),
            description != null ? description : existingRole.getDescription(),
            permissions != null ? permissions : existingRole.getPermissions(),
            isDefault
        );
        
        rolesById.put(id, updatedRole);
        rolesByName.remove(existingRole.getName());
        rolesByName.put(updatedRole.getName(), updatedRole);
        
        return updatedRole;
    }
    
    @Override
    public void deleteRole(String id) {
        Role role = rolesById.remove(id);
        if (role != null) {
            rolesByName.remove(role.getName());
            roleMenus.remove(id);
        }
    }
    
    @Override
    public void assignPermissions(String roleId, Set<String> permissions) {
        Role role = rolesById.get(roleId);
        if (role == null) {
            throw new AdminException("Role not found: " + roleId);
        }
        updateRole(roleId, role.getName(), role.getDescription(), permissions, role.isDefault());
    }
    
    @Override
    public void assignMenus(String roleId, Set<String> menuIds) {
        if (!rolesById.containsKey(roleId)) {
            throw new AdminException("Role not found: " + roleId);
        }
        roleMenus.put(roleId, menuIds != null ? new HashSet<>(menuIds) : new HashSet<>());
    }
    
    public Set<String> getRoleMenus(String roleId) {
        return roleMenus.getOrDefault(roleId, new HashSet<>());
    }
}
