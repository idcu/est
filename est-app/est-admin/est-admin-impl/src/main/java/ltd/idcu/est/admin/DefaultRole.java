package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.Role;

import java.util.HashSet;
import java.util.Set;

public class DefaultRole implements Role {
    
    private final String id;
    private final String name;
    private final String description;
    private final Set<String> permissions;
    private final boolean isDefault;
    
    public DefaultRole(String id, String name, String description) {
        this(id, name, description, new HashSet<>(), false);
    }
    
    public DefaultRole(String id, String name, String description, Set<String> permissions, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = new HashSet<>(permissions);
        this.isDefault = isDefault;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
    
    @Override
    public boolean isDefault() {
        return isDefault;
    }
    
    public void addPermission(String permission) {
        permissions.add(permission);
    }
}
