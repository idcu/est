package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.*;

import java.util.*;

public class InMemoryUserDetailsService implements UserDetailsService {
    
    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersById = new HashMap<>();
    private final Map<String, Set<Role>> userRoles = new HashMap<>();
    private final Map<String, Set<Permission>> userPermissions = new HashMap<>();
    private final Map<String, Set<Permission>> rolePermissions = new HashMap<>();
    
    public InMemoryUserDetailsService() {
    }
    
    public InMemoryUserDetailsService addUser(User user) {
        if (user != null && user.getUsername() != null) {
            usersByUsername.put(user.getUsername(), user);
            if (user.getId() != null) {
                usersById.put(user.getId(), user);
            }
        }
        return this;
    }
    
    public InMemoryUserDetailsService addRole(String userId, Role role) {
        userRoles.computeIfAbsent(userId, k -> new HashSet<>()).add(role);
        return this;
    }
    
    public InMemoryUserDetailsService addRoles(String userId, Set<Role> roles) {
        userRoles.computeIfAbsent(userId, k -> new HashSet<>()).addAll(roles);
        return this;
    }
    
    public InMemoryUserDetailsService addPermission(String userId, Permission permission) {
        userPermissions.computeIfAbsent(userId, k -> new HashSet<>()).add(permission);
        return this;
    }
    
    public InMemoryUserDetailsService addPermissions(String userId, Set<Permission> permissions) {
        userPermissions.computeIfAbsent(userId, k -> new HashSet<>()).addAll(permissions);
        return this;
    }
    
    public InMemoryUserDetailsService addRolePermission(String roleId, Permission permission) {
        rolePermissions.computeIfAbsent(roleId, k -> new HashSet<>()).add(permission);
        return this;
    }
    
    public InMemoryUserDetailsService addRolePermissions(String roleId, Set<Permission> permissions) {
        rolePermissions.computeIfAbsent(roleId, k -> new HashSet<>()).addAll(permissions);
        return this;
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersByUsername.get(username));
    }
    
    @Override
    public Optional<User> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersById.get(id));
    }
    
    @Override
    public Set<Role> findRolesByUserId(String userId) {
        if (userId == null) {
            return Set.of();
        }
        return userRoles.getOrDefault(userId, Set.of());
    }
    
    @Override
    public Set<Permission> findPermissionsByUserId(String userId) {
        if (userId == null) {
            return Set.of();
        }
        
        Set<Permission> permissions = new HashSet<>(userPermissions.getOrDefault(userId, Set.of()));
        
        Set<Role> roles = findRolesByUserId(userId);
        for (Role role : roles) {
            permissions.addAll(findPermissionsByRoleId(role.getId()));
        }
        
        return permissions;
    }
    
    @Override
    public Set<Permission> findPermissionsByRoleId(String roleId) {
        if (roleId == null) {
            return Set.of();
        }
        return rolePermissions.getOrDefault(roleId, Set.of());
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return username != null && usersByUsername.containsKey(username);
    }
    
    @Override
    public User save(User user) {
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("User and username cannot be null");
        }
        
        usersByUsername.put(user.getUsername(), user);
        if (user.getId() != null) {
            usersById.put(user.getId(), user);
        }
        
        return user;
    }
    
    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }
        
        User user = usersById.remove(id);
        if (user != null && user.getUsername() != null) {
            usersByUsername.remove(user.getUsername());
        }
        
        userRoles.remove(id);
        userPermissions.remove(id);
    }
    
    public void clear() {
        usersByUsername.clear();
        usersById.clear();
        userRoles.clear();
        userPermissions.clear();
        rolePermissions.clear();
    }
    
    public int size() {
        return usersByUsername.size();
    }
    
    public static InMemoryUserDetailsService create() {
        return new InMemoryUserDetailsService();
    }
}
