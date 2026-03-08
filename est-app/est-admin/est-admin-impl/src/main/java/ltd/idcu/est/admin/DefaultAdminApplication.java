package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;
import ltd.idcu.est.admin.controller.AdminController;
import ltd.idcu.est.admin.controller.UserController;
import ltd.idcu.est.admin.controller.RoleController;
import ltd.idcu.est.admin.controller.MenuController;
import ltd.idcu.est.admin.controller.DepartmentController;
import ltd.idcu.est.admin.controller.TenantController;
import ltd.idcu.est.admin.controller.LogController;
import ltd.idcu.est.admin.controller.MonitorController;
import ltd.idcu.est.admin.controller.IntegrationController;
import ltd.idcu.est.admin.controller.AiController;
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
    private final LogController logController;
    private final MonitorController monitorController;
    private final IntegrationController integrationController;
    private final AiController aiController;
    
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
        this.logController = new LogController();
        this.monitorController = new MonitorController(
            Admin.createMonitorService(),
            Admin.createOnlineUserService(),
            Admin.createCacheMonitorService()
        );
        this.integrationController = new IntegrationController();
        this.aiController = new AiController();
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
                r.get("/tree", (RouteHandler) menuController::tree);
                r.get("/user", (RouteHandler) menuController::userMenus);
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
            
            router.group("/admin/api/logs", (r, group) -> {
                r.get("/operations", (RouteHandler) logController::listOperationLogs);
                r.get("/operations/{id}", (RouteHandler) logController::getOperationLog);
                r.delete("/operations/{id}", (RouteHandler) logController::deleteOperationLog);
                r.post("/operations/clear", (RouteHandler) logController::clearOperationLogs);
                r.get("/logins", (RouteHandler) logController::listLoginLogs);
                r.get("/logins/{id}", (RouteHandler) logController::getLoginLog);
                r.delete("/logins/{id}", (RouteHandler) logController::deleteLoginLog);
                r.post("/logins/clear", (RouteHandler) logController::clearLoginLogs);
            });
            
            router.group("/admin/api/monitor", (r, group) -> {
                r.get("/jvm", (RouteHandler) monitorController::getJvmMetrics);
                r.get("/system", (RouteHandler) monitorController::getSystemMetrics);
                r.get("/health", (RouteHandler) monitorController::getHealthChecks);
                r.get("/all", (RouteHandler) monitorController::getAllMetrics);
                r.get("/online-users", (RouteHandler) monitorController::getOnlineUsers);
                r.post("/online-users/{sessionId}/force-logout", (RouteHandler) monitorController::forceLogout);
                r.get("/cache", (RouteHandler) monitorController::getCacheStatistics);
                r.get("/cache/keys", (RouteHandler) monitorController::getCacheKeys);
                r.post("/cache/{cacheName}/clear", (RouteHandler) monitorController::clearCache);
                r.post("/cache/clear-all", (RouteHandler) monitorController::clearAllCaches);
            });
            
            router.group("/admin/api/integration", (r, group) -> {
                r.post("/email/send", (RouteHandler) integrationController::sendEmail);
                r.get("/email/templates", (RouteHandler) integrationController::listEmailTemplates);
                r.post("/sms/send", (RouteHandler) integrationController::sendSms);
                r.get("/sms/templates", (RouteHandler) integrationController::listSmsTemplates);
                r.get("/oss/buckets", (RouteHandler) integrationController::listBuckets);
                r.get("/oss/files", (RouteHandler) integrationController::listFiles);
                r.post("/oss/upload", (RouteHandler) integrationController::uploadFile);
                r.post("/oss/delete", (RouteHandler) integrationController::deleteFile);
            });
            
            router.group("/admin/api/ai", (r, group) -> {
                r.post("/chat", (RouteHandler) aiController::chat);
                r.post("/code/generate", (RouteHandler) aiController::generateCode);
                r.post("/code/suggest", (RouteHandler) aiController::suggestCode);
                r.post("/code/explain", (RouteHandler) aiController::explainCode);
                r.post("/code/optimize", (RouteHandler) aiController::optimizeCode);
                r.get("/reference", (RouteHandler) aiController::getQuickReference);
                r.get("/bestpractice", (RouteHandler) aiController::getBestPractice);
                r.get("/tutorial", (RouteHandler) aiController::getTutorial);
                r.get("/templates", (RouteHandler) aiController::listTemplates);
                r.post("/templates/generate", (RouteHandler) aiController::generatePrompt);
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
            System.out.println("�?EST Admin Console 启动成功�?);
            System.out.println("=".repeat(80));
            System.out.println("\n访问地址�?);
            System.out.println("  - http://localhost:" + port + "/admin          (管理后台)");
            System.out.println("\n默认登录账号�?);
            System.out.println("  - 用户�? admin");
            System.out.println("  - 密码: admin123");
            System.out.println("\n�?Ctrl+C 停止服务�?);
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
