package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Set;

public interface UserService {
    
    User createUser(String username, String password, String email, Set<String> roles, Set<String> permissions);
    
    User getUser(String id);
    
    User getUserByUsername(String username);
    
    List<User> getAllUsers();
    
    User updateUser(String id, String email, Set<String> roles, Set<String> permissions, boolean active);
    
    void deleteUser(String id);
    
    void changePassword(String userId, String oldPassword, String newPassword);
    
    void resetPassword(String userId, String newPassword);
    
    void assignRoles(String userId, Set<String> roleIds);
    
    void assignPermissions(String userId, Set<String> permissions);
}
