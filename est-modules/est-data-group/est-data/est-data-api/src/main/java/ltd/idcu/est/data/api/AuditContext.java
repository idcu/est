package ltd.idcu.est.data.api;

public class AuditContext {
    
    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();
    
    public static void setCurrentUser(String username) {
        CURRENT_USER.set(username);
    }
    
    public static String getCurrentUser() {
        return CURRENT_USER.get();
    }
    
    public static void clear() {
        CURRENT_USER.remove();
    }
}
