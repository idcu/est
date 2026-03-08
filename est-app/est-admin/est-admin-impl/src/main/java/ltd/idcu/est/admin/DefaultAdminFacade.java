package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.*;

import java.util.List;
import java.util.Set;

public class DefaultAdminFacade implements AdminFacade {
    
    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final DepartmentService departmentService;
    private final TenantService tenantService;
    private final AuthService authService;
    
    public DefaultAdminFacade() {
        this.userService = new DefaultUserService();
        this.roleService = new DefaultRoleService();
        this.menuService = new DefaultMenuService();
        this.departmentService = new DefaultDepartmentService();
        this.tenantService = new DefaultTenantService();
        this.authService = new DefaultAuthService();
    }
    
    public DefaultAdminFacade(UserService userService, RoleService roleService, 
                               MenuService menuService, DepartmentService departmentService,
                               TenantService tenantService, AuthService authService) {
        this.userService = userService;
        this.roleService = roleService;
        this.menuService = menuService;
        this.departmentService = departmentService;
        this.tenantService = tenantService;
        this.authService = authService;
    }
    
    @Override
    public UserService getUserService() {
        return userService;
    }
    
    @Override
    public RoleService getRoleService() {
        return roleService;
    }
    
    @Override
    public MenuService getMenuService() {
        return menuService;
    }
    
    @Override
    public DepartmentService getDepartmentService() {
        return departmentService;
    }
    
    @Override
    public TenantService getTenantService() {
        return tenantService;
    }
    
    @Override
    public AuthService getAuthService() {
        return authService;
    }
    
    @Override
    public User login(String username, String password) {
        return authService.authenticate(username, password);
    }
    
    @Override
    public String generateToken(User user) {
        return authService.generateToken(user);
    }
    
    @Override
    public User validateToken(String token) {
        return authService.validateToken(token);
    }
    
    @Override
    public boolean hasPermission(User user, String permission) {
        return authService.hasPermission(user, permission);
    }
    
    @Override
    public boolean hasRole(User user, String role) {
        return authService.hasRole(user, role);
    }
    
    @Override
    public List<Menu> getUserMenus(User user) {
        return menuService.getUserMenus(user);
    }
}
