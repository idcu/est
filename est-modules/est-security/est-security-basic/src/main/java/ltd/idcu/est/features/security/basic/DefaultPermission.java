package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.Permission;

import java.util.UUID;

public class DefaultPermission implements Permission {
    
    private final String id;
    private String name;
    private String resource;
    private String action;
    private String description;
    private boolean enabled = true;
    
    public DefaultPermission() {
        this.id = UUID.randomUUID().toString();
    }
    
    public DefaultPermission(String name) {
        this();
        this.name = name;
    }
    
    public DefaultPermission(String name, String resource, String action) {
        this(name);
        this.resource = resource;
        this.action = action;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public DefaultPermission setName(String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public String getResource() {
        return resource;
    }
    
    public DefaultPermission setResource(String resource) {
        this.resource = resource;
        return this;
    }
    
    @Override
    public String getAction() {
        return action;
    }
    
    public DefaultPermission setAction(String action) {
        this.action = action;
        return this;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public DefaultPermission setDescription(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public DefaultPermission setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public static DefaultPermission of(String name) {
        return new DefaultPermission(name);
    }
    
    public static DefaultPermission of(String name, String resource, String action) {
        return new DefaultPermission(name, resource, action);
    }
    
    @Override
    public String toString() {
        return "DefaultPermission{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
