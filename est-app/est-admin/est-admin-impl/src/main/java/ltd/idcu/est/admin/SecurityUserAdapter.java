package ltd.idcu.est.admin;

import ltd.idcu.est.security.api.User;

import java.util.Set;

public class SecurityUserAdapter implements User {
    
    private final ltd.idcu.est.admin.api.User delegate;
    
    public SecurityUserAdapter(ltd.idcu.est.admin.api.User delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public String getId() {
        return delegate.getId();
    }
    
    @Override
    public String getUsername() {
        return delegate.getUsername();
    }
    
    @Override
    public String getPassword() {
        return null;
    }
    
    @Override
    public void setPassword(String password) {
    }
    
    @Override
    public boolean isEnabled() {
        return delegate.isActive();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    public Set<String> getRoles() {
        return delegate.getRoles();
    }
    
    public Set<String> getPermissions() {
        return delegate.getPermissions();
    }
}
