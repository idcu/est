package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;
import ltd.idcu.est.admin.api.AuthService;
import ltd.idcu.est.admin.api.UserService;
import ltd.idcu.est.admin.api.RoleService;
import ltd.idcu.est.admin.api.MenuService;
import ltd.idcu.est.admin.api.DepartmentService;
import ltd.idcu.est.admin.api.TenantService;
import ltd.idcu.est.admin.api.OperationLogService;
import ltd.idcu.est.admin.api.LoginLogService;
import ltd.idcu.est.admin.api.MonitorService;
import ltd.idcu.est.admin.api.OnlineUserService;
import ltd.idcu.est.admin.api.CacheMonitorService;
import ltd.idcu.est.admin.api.EmailService;
import ltd.idcu.est.admin.api.SmsService;
import ltd.idcu.est.admin.api.OssService;
import ltd.idcu.est.admin.api.AiAssistantService;

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
    
    public static OperationLogService createOperationLogService() {
        return new DefaultOperationLogService();
    }
    
    public static LoginLogService createLoginLogService() {
        return new DefaultLoginLogService();
    }
    
    public static MonitorService createMonitorService() {
        return new DefaultMonitorService();
    }
    
    public static OnlineUserService createOnlineUserService() {
        return new DefaultOnlineUserService();
    }
    
    public static CacheMonitorService createCacheMonitorService() {
        return new DefaultCacheMonitorService();
    }
    
    public static EmailService createEmailService() {
        return new DefaultEmailService();
    }
    
    public static SmsService createSmsService() {
        return new DefaultSmsService();
    }
    
    public static OssService createOssService() {
        return new DefaultOssService();
    }
    
    public static AiAssistantService createAiAssistantService() {
        return new DefaultAiAssistantService();
    }
}
