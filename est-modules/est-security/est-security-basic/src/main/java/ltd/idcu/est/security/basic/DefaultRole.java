package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.Role;

import java.util.UUID;

public class DefaultRole implements Role {
    
    private final String id;
    private String name;
    private String description;
    private boolean enabled = true;
    
    public DefaultRole() {
        this.id = UUID.randomUUID().toString();
    }
    
    public DefaultRole(String name) {
        this();
        this.name = name;
    }
    
    public DefaultRole(String name, String description) {
        this(name);
        this.description = description;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public DefaultRole setName(String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public DefaultRole setDescription(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public DefaultRole setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public static DefaultRole of(String name) {
        return new DefaultRole(name);
    }
    
    public static DefaultRole of(String name, String description) {
        return new DefaultRole(name, description);
    }
    
    @Override
    public String toString() {
        return "DefaultRole{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
