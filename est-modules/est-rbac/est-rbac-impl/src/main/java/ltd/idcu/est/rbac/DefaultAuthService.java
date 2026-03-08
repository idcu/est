package ltd.idcu.est.rbac;

import ltd.idcu.est.rbac.api.AuthService;
import ltd.idcu.est.rbac.api.RbacException;
import ltd.idcu.est.rbac.api.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Base64;

public class DefaultAuthService implements AuthService {
    
    private final Map<String, User> users;
    private final Map<String, String> userPasswords;
    private static final long TOKEN_VALIDITY = 3600000;
    
    public DefaultAuthService() {
        this.users = new ConcurrentHashMap<>();
        this.userPasswords = new ConcurrentHashMap<>();
        initializeDefaultUser();
    }
    
    private void initializeDefaultUser() {
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("admin");
        
        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("*");
        
        DefaultUser adminUser = new DefaultUser(
            "1",
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
            throw new RbacException("Invalid username or password");
        }
        
        User user = users.get(username);
        if (user == null || !user.isActive()) {
            throw new RbacException("User is not active");
        }
        
        return user;
    }
    
    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("permissions", user.getPermissions());
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("expiresAt", System.currentTimeMillis() + TOKEN_VALIDITY);
        
        return Base64.getEncoder().encodeToString(claims.toString().getBytes());
    }
    
    @Override
    public User validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            if (!decoded.contains("expiresAt")) {
                return null;
            }
            
            String username = users.keySet().stream().findFirst().orElse(null);
            return username != null ? users.get(username) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public String refreshToken(String token) {
        User user = validateToken(token);
        if (user != null) {
            return generateToken(user);
        }
        throw new RbacException("Invalid token");
    }
    
    @Override
    public void invalidateToken(String token) {
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
