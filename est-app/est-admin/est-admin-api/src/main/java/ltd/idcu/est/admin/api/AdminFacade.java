package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Set;

public interface AdminFacade {
    
    UserService getUserService();
    
    RoleService getRoleService();
    
    MenuService getMenuService();
    
    DepartmentService getDepartmentService();
    
    TenantService getTenantService();
    
    AuthService getAuthService();
    
    User login(String username, String password);
    
    String generateToken(User user);
    
    User validateToken(String token);
    
    boolean hasPermission(User user, String permission);
    
    boolean hasRole(User user, String role);
    
    List<Menu> getUserMenus(User user);
}
