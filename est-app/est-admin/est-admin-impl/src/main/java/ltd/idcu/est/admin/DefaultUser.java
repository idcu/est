package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.User;

import java.util.HashSet;
import java.util.Set;

public class DefaultUser implements User {
    
    private final String id;
    private final String username;
    private final String email;
    private final Set<String> roles;
    private final Set<String> permissions;
    private final boolean active;
    private final long createdAt;
    
    public DefaultUser(String id, String username, String email) {
        this(id, username, email, new HashSet<>(), new HashSet<>(), true, System.currentTimeMillis());
    }
    
    public DefaultUser(String id, String username, String email, Set<String> roles, Set<String> permissions, boolean active, long createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = new HashSet<>(roles);
        this.permissions = new HashSet<>(permissions);
        this.active = active;
        this.createdAt = createdAt;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public String getEmail() {
        return email;
    }
    
    @Override
    public Set<String> getRoles() {
        return new HashSet<>(roles);
    }
    
    @Override
    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void addRole(String role) {
        roles.add(role);
    }
    
    public void addPermission(String permission) {
        permissions.add(permission);
    }
}
