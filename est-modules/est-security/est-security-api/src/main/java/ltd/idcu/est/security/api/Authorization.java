package ltd.idcu.est.security.api;

public interface Authorization {
    
    boolean hasRole(String roleName);
    
    boolean hasAnyRole(String... roleNames);
    
    boolean hasAllRoles(String... roleNames);
    
    boolean hasPermission(String permissionName);
    
    boolean hasAnyPermission(String... permissionNames);
    
    boolean hasAllPermissions(String... permissionNames);
    
    boolean hasPermission(String resource, String action);
    
    boolean canAccess(String resource, String action);
}
