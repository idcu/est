package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;
import ltd.idcu.est.admin.api.AuthService;
import ltd.idcu.est.admin.api.UserService;
import ltd.idcu.est.admin.api.RoleService;
import ltd.idcu.est.admin.api.MenuService;
import ltd.idcu.est.admin.api.DepartmentService;
import ltd.idcu.est.admin.api.TenantService;

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
    
    public static UserService createUserService() {
        return new DefaultUserService();
    }
    
    public static RoleService createRoleService() {
        return new DefaultRoleService();
    }
    
    public static MenuService createMenuService() {
        return new DefaultMenuService();
    }
    
    public static DepartmentService createDepartmentService() {
        return new DefaultDepartmentService();
    }
    
    public static TenantService createTenantService() {
        return new DefaultTenantService();
    }
}
