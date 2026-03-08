package ltd.idcu.est.rbac;

import ltd.idcu.est.rbac.api.RbacException;
import ltd.idcu.est.rbac.api.User;
import ltd.idcu.est.rbac.api.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultUserService implements UserService {
    
    private final Map<String, User> usersById;
    private final Map<String, User> usersByUsername;
    private final Map<String, String> userPasswords;
    
    public DefaultUserService() {
        this.usersById = new ConcurrentHashMap<>();
        this.usersByUsername = new ConcurrentHashMap<>();
        this.userPasswords = new ConcurrentHashMap<>();
        initializeDefaultUser();
    }
    
    private void initializeDefaultUser() {
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("admin");
        
        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("*");
        
        DefaultUser adminUser = new DefaultUser(
            UUID.randomUUID().toString(),
            "admin",
            "admin@example.com",
            adminRoles,
            adminPermissions,
            true,
            System.currentTimeMillis()
        );
        
        usersById.put(adminUser.getId(), adminUser);
        usersByUsername.put(adminUser.getUsername(), adminUser);
        userPasswords.put(adminUser.getId(), "admin123");
    }
    
    @Override
    public User createUser(String username, String password, String email, Set<String> roles, Set<String> permissions) {
        if (usersByUsername.containsKey(username)) {
            throw new RbacException("Username already exists: " + username);
        }
        
        String id = UUID.randomUUID().toString();
        DefaultUser user = new DefaultUser(
            id,
            username,
            email,
            roles != null ? roles : new HashSet<>(),
            permissions != null ? permissions : new HashSet<>(),
            true,
            System.currentTimeMillis()
        );
        
        usersById.put(id, user);
        usersByUsername.put(username, user);
        userPasswords.put(id, password);
        
        return user;
    }
    
    @Override
    public User getUser(String id) {
        return usersById.get(id);
    }
    
    @Override
    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }
    
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersById.values());
    }
    
    @Override
    public User updateUser(String id, String email, Set<String> roles, Set<String> permissions, boolean active) {
        User existingUser = usersById.get(id);
        if (existingUser == null) {
            throw new RbacException("User not found: " + id);
        }
        
        DefaultUser updatedUser = new DefaultUser(
            existingUser.getId(),
            existingUser.getUsername(),
            email != null ? email : existingUser.getEmail(),
            roles != null ? roles : existingUser.getRoles(),
            permissions != null ? permissions : existingUser.getPermissions(),
            active,
            existingUser.getCreatedAt()
        );
        
        usersById.put(id, updatedUser);
        usersByUsername.put(updatedUser.getUsername(), updatedUser);
        
        return updatedUser;
    }
    
    @Override
    public void deleteUser(String id) {
        User user = usersById.remove(id);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
            userPasswords.remove(id);
        }
    }
    
    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        String storedPassword = userPasswords.get(userId);
        if (storedPassword == null) {
            throw new RbacException("User not found: " + userId);
        }
        if (!storedPassword.equals(oldPassword)) {
            throw new RbacException("Invalid old password");
        }
        userPasswords.put(userId, newPassword);
    }
    
    @Override
    public void resetPassword(String userId, String newPassword) {
        if (!usersById.containsKey(userId)) {
            throw new RbacException("User not found: " + userId);
        }
        userPasswords.put(userId, newPassword);
    }
    
    @Override
    public void assignRoles(String userId, Set<String> roleIds) {
        User user = usersById.get(userId);
        if (user == null) {
            throw new RbacException("User not found: " + userId);
        }
        updateUser(userId, user.getEmail(), roleIds, user.getPermissions(), user.isActive());
    }
    
    @Override
    public void assignPermissions(String userId, Set<String> permissions) {
        User user = usersById.get(userId);
        if (user == null) {
            throw new RbacException("User not found: " + userId);
        }
        updateUser(userId, user.getEmail(), user.getRoles(), permissions, user.isActive());
    }
    
    public String getPassword(String userId) {
        return userPasswords.get(userId);
    }
}
