package ltd.idcu.est.admin.e2e;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.*;
import ltd.idcu.est.test.Assertions;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AdminE2ETestBase {
    
    protected AuthService authService;
    protected UserService userService;
    protected RoleService roleService;
    protected MenuService menuService;
    protected DepartmentService departmentService;
    protected TenantService tenantService;
    protected OperationLogService operationLogService;
    protected LoginLogService loginLogService;
    protected MonitorService monitorService;
    protected OnlineUserService onlineUserService;
    protected CacheMonitorService cacheMonitorService;
    
    protected User adminUser;
    protected String adminToken;
    
    protected void setUp() {
        authService = Admin.createAuthService();
        userService = Admin.createUserService();
        roleService = Admin.createRoleService();
        menuService = Admin.createMenuService();
        departmentService = Admin.createDepartmentService();
        tenantService = Admin.createTenantService();
        operationLogService = Admin.createOperationLogService();
        loginLogService = Admin.createLoginLogService();
        monitorService = Admin.createMonitorService();
        onlineUserService = Admin.createOnlineUserService();
        cacheMonitorService = Admin.createCacheMonitorService();
        
        adminUser = authService.authenticate("admin", "admin123");
        adminToken = authService.generateToken(adminUser);
    }
    
    protected User createTestUser(String username, String email) {
        Set<String> roles = Set.of("user");
        Set<String> permissions = Set.of("system:user:list", "system:user:query");
        return userService.createUser(username, "password123", email, roles, permissions);
    }
    
    protected void assertUserEquals(User expected, User actual) {
        Assertions.assertEquals(expected.getUsername(), actual.getUsername(), "Username should match");
        Assertions.assertEquals(expected.getEmail(), actual.getEmail(), "Email should match");
        Assertions.assertEquals(expected.getRoles(), actual.getRoles(), "Roles should match");
        Assertions.assertEquals(expected.getPermissions(), actual.getPermissions(), "Permissions should match");
    }
    
    protected void assertMenuEquals(Menu expected, Menu actual) {
        Assertions.assertEquals(expected.getName(), actual.getName(), "Menu name should match");
        Assertions.assertEquals(expected.getPath(), actual.getPath(), "Menu path should match");
        Assertions.assertEquals(expected.getParentId(), actual.getParentId(), "Menu parentId should match");
    }
    
    protected void assertOperationLog(OperationLog log, String expectedModule, String expectedOperation) {
        Assertions.assertNotNull(log, "Operation log should not be null");
        Assertions.assertEquals(expectedModule, log.getModule(), "Module should match");
        Assertions.assertEquals(expectedOperation, log.getOperation(), "Operation should match");
    }
    
    protected void assertLoginLog(LoginLog log, String expectedUsername, int expectedStatus) {
        Assertions.assertNotNull(log, "Login log should not be null");
        Assertions.assertEquals(expectedUsername, log.getUsername(), "Username should match");
        Assertions.assertEquals(expectedStatus, log.getStatus(), "Status should match");
    }
    
    protected void assertMonitorMetrics(Map<String, Object> metrics, String... expectedKeys) {
        Assertions.assertNotNull(metrics, "Metrics should not be null");
        for (String key : expectedKeys) {
            Assertions.assertTrue(metrics.containsKey(key), "Metrics should contain key: " + key);
        }
    }
}
