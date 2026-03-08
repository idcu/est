package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;
import ltd.idcu.est.admin.controller.AdminController;
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Router;
import ltd.idcu.est.web.api.RouteHandler;

public class DefaultAdminApplication implements AdminApplication {
    
    private String name;
    private volatile boolean running;
    private final WebApplication webApplication;
    private final AdminController adminController;
    
    public DefaultAdminApplication() {
        this("EST Admin Console");
    }
    
    public DefaultAdminApplication(String name) {
        this.name = name;
        this.webApplication = Web.create(name, "1.0.0");
        this.adminController = new AdminController();
        this.running = false;
        setupRoutes();
    }
    
    private void setupRoutes() {
        webApplication.routes(router -> {
            router.get("/admin", (RouteHandler) adminController::dashboard);
            router.get("/admin/", (RouteHandler) adminController::dashboard);
            
            router.group("/admin/api", (r, group) -> {
                r.post("/login", (RouteHandler) adminController::login);
                r.post("/logout", (RouteHandler) adminController::logout);
                r.get("/stats", (RouteHandler) adminController::systemStats);
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
