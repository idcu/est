package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.AuthService;
import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.features.security.api.Token;
import ltd.idcu.est.features.security.jwt.JwtSecurity;
import ltd.idcu.est.features.security.jwt.JwtTokenProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAuthService implements AuthService {
    
    private final Map<String, User> users;
    private final Map<String, String> userPasswords;
    private final JwtTokenProvider tokenProvider;
    private static final String JWT_SECRET_KEY = "est-admin-secret-key-2026-for-jwt-token-signing";
    private static final long TOKEN_VALIDITY = 3600000;
    
    public DefaultAuthService() {
        this.users = new ConcurrentHashMap<>();
        this.userPasswords = new ConcurrentHashMap<>();
        this.tokenProvider = JwtSecurity.tokenProvider(JWT_SECRET_KEY);
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
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("permissions", user.getPermissions());
        SecurityUserAdapter adaptedUser = new SecurityUserAdapter(user);
        return tokenProvider.generateToken(adaptedUser, claims);
    }
    
    @Override
    public User validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        Optional<Token> validatedToken = tokenProvider.validateToken(token);
        if (validatedToken.isEmpty()) {
            return null;
        }
        
        Token t = validatedToken.get();
        String username = t.getSubject();
        return users.get(username);
    }
    
    public String refreshToken(String token) {
        return tokenProvider.refresh(token);
    }
    
    public void invalidateToken(String token) {
        tokenProvider.invalidate(token);
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
