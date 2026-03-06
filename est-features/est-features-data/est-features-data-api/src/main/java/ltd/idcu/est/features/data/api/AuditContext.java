package ltd.idcu.est.features.data.api;

public class AuditContext {
    
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    
    public static void setCurrentUser(String user) {
        currentUser.set(user);
    }
    
    public static String getCurrentUser() {
        return currentUser.get();
    }
    
    public static void clear() {
        currentUser.remove();
    }
}
