package ltd.idcu.est.security.api;

import java.util.Optional;
import java.util.Set;

public interface UserDetailsService {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findById(String id);
    
    Set<Role> findRolesByUserId(String userId);
    
    Set<Permission> findPermissionsByUserId(String userId);
    
    Set<Permission> findPermissionsByRoleId(String roleId);
    
    boolean existsByUsername(String username);
    
    User save(User user);
    
    void deleteById(String id);
}
