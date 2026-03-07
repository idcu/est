package ltd.idcu.est.features.security.api;

import java.security.Principal;

public interface User extends Principal {
    
    String getId();
    
    String getUsername();
    
    String getPassword();
    
    void setPassword(String password);
    
    boolean isEnabled();
    
    boolean isAccountNonExpired();
    
    boolean isAccountNonLocked();
    
    boolean isCredentialsNonExpired();
    
    default String getName() {
        return getUsername();
    }
}
