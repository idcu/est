package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.AuthService;
import ltd.idcu.est.admin.api.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAuthService implements AuthService {
    
    private final Map<String, User> users;
    private final Map<String, String> userPasswords;
    private final Map<String, User> tokenUsers;
    private final Map<String, Long> tokenExpiry;
    private static final long TOKEN_VALIDITY = 3600000;
    
    public DefaultAuthService() {
        this.users = new ConcurrentHashMap<>();
        this.userPasswords = new ConcurrentHashMap<>();
        this.tokenUsers = new ConcurrentHashMap<>();
        this.tokenExpiry = new ConcurrentHashMap<>();
        initializeDefaultUser();
    }
    
    private void initializeDefaultUser() {
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("admin");
        
        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("*");
        
        DefaultUser adminUser = new DefaultUser(
            "admin",
            "admin",
            "admin@example.com",
            adminRoles,
            adminPermissions,
            true,
            System.currentTimeMillis()
        );
        
        users.put("admin", adminUser);
        userPasswords.put("admin", "admin123");
    }
    
    @Override
    public User authenticate(String username, String password) {
        String storedPassword = userPasswords.get(username);
        if (storedPassword == null || !storedPassword.equals(password)) {
            throw new AdminException("Invalid username or password");
        }
        
        User user = users.get(username);
        if (user == null || !user.isActive()) {
            throw new AdminException("User is not active");
        }
        
        return user;
    }
    
    @Override
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        tokenUsers.put(token, user);
        tokenExpiry.put(token, System.currentTimeMillis() + TOKEN_VALIDITY);
        return token;
    }
    
    @Override
    public User validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        Long expiry = tokenExpiry.get(token);
        if (expiry == null || System.currentTimeMillis() > expiry) {
            tokenUsers.remove(token);
            tokenExpiry.remove(token);
            return null;
        }
        
        return tokenUsers.get(token);
    }
    
    @Override
    public boolean hasPermission(User user, String permission) {
        if (user == null) {
            return false;
        }
        
        Set<String> userPermissions = user.getPermissions();
        if (userPermissions.contains("*")) {
            return true;
        }
        
        return userPermissions.contains(permission);
    }
    
    @Override
    public boolean hasRole(User user, String role) {
        if (user == null) {
            return false;
        }
        
        return user.getRoles().contains(role);
    }
    
    public void addUser(User user, String password) {
        users.put(user.getUsername(), user);
        userPasswords.put(user.getUsername(), password);
    }
}
