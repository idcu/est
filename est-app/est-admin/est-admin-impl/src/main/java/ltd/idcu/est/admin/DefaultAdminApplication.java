package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;
import ltd.idcu.est.admin.controller.AdminController;
import ltd.idcu.est.admin.controller.UserController;
import ltd.idcu.est.admin.controller.RoleController;
import ltd.idcu.est.admin.controller.MenuController;
import ltd.idcu.est.admin.controller.DepartmentController;
import ltd.idcu.est.admin.controller.TenantController;
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Router;
import ltd.idcu.est.web.api.RouteHandler;

public class DefaultAdminApplication implements AdminApplication {
    
    private String name;
    private volatile boolean running;
    private final WebApplication webApplication;
    private final AdminController adminController;
    private final UserController userController;
    private final RoleController roleController;
    private final MenuController menuController;
    private final DepartmentController departmentController;
    private final TenantController tenantController;
    
    public DefaultAdminApplication() {
        this("EST Admin Console");
    }
    
    public DefaultAdminApplication(String name) {
        this.name = name;
        this.webApplication = Web.create(name, "1.0.0");
        this.adminController = new AdminController();
        this.userController = new UserController();
        this.roleController = new RoleController();
        this.menuController = new MenuController();
        this.departmentController = new DepartmentController();
        this.tenantController = new TenantController();
        this.running = false;
        setupRoutes();
    }
    
    private void setupRoutes() {
        webApplication.routes(router -> {
            router.get("/admin", (RouteHandler) adminController::dashboard);
            router.get("/admin/", (RouteHandler) adminController::dashboard);
            
            router.group("/admin/api", (r, group) -> {
                r.post("/auth/login", (RouteHandler) adminController::login);
                r.post("/auth/logout", (RouteHandler) adminController::logout);
                r.get("/auth/current", (RouteHandler) adminController::getCurrentUser);
                r.post("/auth/refresh-token", (RouteHandler) adminController::refreshToken);
                r.get("/stats", (RouteHandler) adminController::systemStats);
            });
            
            router.group("/admin/api/users", (r, group) -> {
                r.get("", (RouteHandler) userController::list);
                r.post("", (RouteHandler) userController::create);
                r.get("/{id}", (RouteHandler) userController::get);
                r.put("/{id}", (RouteHandler) userController::update);
                r.delete("/{id}", (RouteHandler) userController::delete);
                r.post("/{id}/change-password", (RouteHandler) userController::changePassword);
                r.post("/{id}/reset-password", (RouteHandler) userController::resetPassword);
                r.post("/{id}/assign-roles", (RouteHandler) userController::assignRoles);
                r.post("/{id}/assign-permissions", (RouteHandler) userController::assignPermissions);
            });
            
            router.group("/admin/api/roles", (r, group) -> {
                r.get("", (RouteHandler) roleController::list);
                r.post("", (RouteHandler) roleController::create);
                r.get("/{id}", (RouteHandler) roleController::get);
                r.put("/{id}", (RouteHandler) roleController::update);
                r.delete("/{id}", (RouteHandler) roleController::delete);
                r.post("/{id}/assign-permissions", (RouteHandler) roleController::assignPermissions);
                r.post("/{id}/assign-menus", (RouteHandler) roleController::assignMenus);
            });
            
            router.group("/admin/api/menus", (r, group) -> {
                r.get("", (RouteHandler) menuController::list);
                r.post("", (RouteHandler) menuController::create);
                r.get("/{id}", (RouteHandler) menuController::get);
                r.put("/{id}", (RouteHandler) menuController::update);
                r.delete("/{id}", (RouteHandler) menuController::delete);
            });
            
            router.group("/admin/api/departments", (r, group) -> {
                r.get("", (RouteHandler) departmentController::list);
                r.post("", (RouteHandler) departmentController::create);
                r.get("/{id}", (RouteHandler) departmentController::get);
                r.put("/{id}", (RouteHandler) departmentController::update);
                r.delete("/{id}", (RouteHandler) departmentController::delete);
            });
            
            router.group("/admin/api/tenants", (r, group) -> {
                r.get("", (RouteHandler) tenantController::list);
                r.post("", (RouteHandler) tenantController::create);
                r.get("/current", (RouteHandler) tenantController::getCurrent);
                r.get("/{id}", (RouteHandler) tenantController::get);
                r.get("/code/{code}", (RouteHandler) tenantController::getByCode);
                r.get("/domain/{domain}", (RouteHandler) tenantController::getByDomain);
                r.put("/{id}", (RouteHandler) tenantController::update);
                r.delete("/{id}", (RouteHandler) tenantController::delete);
                r.post("/{id}/set-current", (RouteHandler) tenantController::setCurrent);
                r.post("/clear-current", (RouteHandler) tenantController::clearCurrent);
            });
        });
    }
    
    @Override
    public void run(int port) {
        if (running) {
            throw new ltd.idcu.est.admin.api.AdminException("Admin application is already running");
        }
        
        running = true;
        
        webApplication.onStartup(() -> {
            System.out.println("\n".repeat(2));
            System.out.println("=".repeat(80));
            System.out.println("✓ EST Admin Console 启动成功！");
            System.out.println("=".repeat(80));
            System.out.println("\n访问地址：");
            System.out.println("  - http://localhost:" + port + "/admin          (管理后台)");
            System.out.println("\n默认登录账号：");
            System.out.println("  - 用户名: admin");
            System.out.println("  - 密码: admin123");
            System.out.println("\n按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(80));
        });
        
        webApplication.run(port);
    }
    
    @Override
    public void stop() {
        if (!running) {
            return;
        }
        
        running = false;
        webApplication.stop();
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }
    
    @Override
    public void registerController(Object controller) {
    }
}
