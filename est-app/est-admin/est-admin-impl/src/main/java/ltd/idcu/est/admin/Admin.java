package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;
import ltd.idcu.est.admin.api.AuthService;

public class Admin {
    
    private Admin() {
    }
    
    public static AdminApplication create(String name, String version) {
        return new DefaultAdminApplication(name);
    }
    
    public static AdminApplication create() {
        return create("EST Admin Console", "1.0.0");
    }
    
    public static AuthService createAuthService() {
        return new DefaultAuthService();
    }
}
