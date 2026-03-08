package ltd.idcu.est.admin.api;

public interface AuthService {
    
    User authenticate(String username, String password);
    
    String generateToken(User user);
    
    User validateToken(String token);
    
    boolean hasPermission(User user, String permission);
    
    boolean hasRole(User user, String role);
}
