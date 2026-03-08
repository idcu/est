package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DefaultUser implements User {
    
    private final String id;
    private String username;
    private String password;
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    
    public DefaultUser() {
        this.id = UUID.randomUUID().toString();
    }
    
    public DefaultUser(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    public DefaultUser setUsername(String username) {
        this.username = username;
        return this;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public DefaultUser setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    public DefaultUser setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    public DefaultUser setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
        return this;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    public DefaultUser setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public DefaultUser setRoles(Set<String> roles) {
        this.roles = roles != null ? roles : new HashSet<>();
        return this;
    }
    
    public DefaultUser addRole(String role) {
        this.roles.add(role);
        return this;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public DefaultUser setPermissions(Set<String> permissions) {
        this.permissions = permissions != null ? permissions : new HashSet<>();
        return this;
    }
    
    public DefaultUser addPermission(String permission) {
        this.permissions.add(permission);
        return this;
    }
    
    public static DefaultUser of(String username, String password) {
        return new DefaultUser(username, password);
    }
    
    public static DefaultUserBuilder builder() {
        return new DefaultUserBuilder();
    }
    
    public static class DefaultUserBuilder {
        private String username;
        private String password;
        private boolean enabled = true;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private Set<String> roles = new HashSet<>();
        private Set<String> permissions = new HashSet<>();
        
        public DefaultUserBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public DefaultUserBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public DefaultUserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public DefaultUserBuilder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }
        
        public DefaultUserBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }
        
        public DefaultUserBuilder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }
        
        public DefaultUserBuilder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }
        
        public DefaultUserBuilder addRole(String role) {
            this.roles.add(role);
            return this;
        }
        
        public DefaultUserBuilder permissions(Set<String> permissions) {
            this.permissions = permissions;
            return this;
        }
        
        public DefaultUserBuilder addPermission(String permission) {
            this.permissions.add(permission);
            return this;
        }
        
        public DefaultUser build() {
            DefaultUser user = new DefaultUser(username, password);
            user.setEnabled(enabled);
            user.setAccountNonExpired(accountNonExpired);
            user.setAccountNonLocked(accountNonLocked);
            user.setCredentialsNonExpired(credentialsNonExpired);
            user.setRoles(roles);
            user.setPermissions(permissions);
            return user;
        }
    }
    
    @Override
    public String toString() {
        return "DefaultUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}
