package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.*;

import java.util.Set;

public class DefaultAuthorization implements Authorization {
    
    private final User user;
    private final Set<String> roles;
    private final Set<String> permissions;
    
    public DefaultAuthorization(User user, Set<String> roles, Set<String> permissions) {
        this.user = user;
        this.roles = roles != null ? roles : Set.of();
        this.permissions = permissions != null ? permissions : Set.of();
    }
    
    @Override
    public boolean hasRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            return false;
        }
        return roles.contains(roleName);
    }
    
    @Override
    public boolean hasAnyRole(String... roleNames) {
        if (roleNames == null || roleNames.length == 0) {
            return false;
        }
        for (String role : roleNames) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAllRoles(String... roleNames) {
        if (roleNames == null || roleNames.length == 0) {
            return true;
        }
        for (String role : roleNames) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasPermission(String permissionName) {
        if (permissionName == null || permissionName.isEmpty()) {
            return false;
        }
        return permissions.contains(permissionName);
    }
    
    @Override
    public boolean hasAnyPermission(String... permissionNames) {
        if (permissionNames == null || permissionNames.length == 0) {
            return false;
        }
        for (String permission : permissionNames) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAllPermissions(String... permissionNames) {
        if (permissionNames == null || permissionNames.length == 0) {
            return true;
        }
        for (String permission : permissionNames) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasPermission(String resource, String action) {
        if (resource == null || action == null) {
            return false;
        }
        String permission = resource + ":" + action;
        return hasPermission(permission);
    }
    
    @Override
    public boolean canAccess(String resource, String action) {
        return hasPermission(resource, action) || hasRole("ADMIN") || hasRole("SUPER_ADMIN");
    }
    
    public User getUser() {
        return user;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public static DefaultAuthorization of(User user, Set<String> roles, Set<String> permissions) {
        return new DefaultAuthorization(user, roles, permissions);
    }
    
    @Override
    public String toString() {
        return "DefaultAuthorization{" +
                "username='" + (user != null ? user.getUsername() : "null") + '\'' +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}
