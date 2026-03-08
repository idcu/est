package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.*;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhaseThreeTest {
    
    @Test
    @TestCase("测试阶段三核心功�?)
    public void testPhaseThreeCoreFeatures() {
        System.out.println("=== 开始阶段三核心功能测试 ===\n");
        
        testAuthService();
        testMenuService();
        testLogService();
        testMonitorService();
        
        System.out.println("\n=== 阶段三核心功能测试完�?===");
    }
    
    @Test(displayName = "测试认证服务功能")
    public void testAuthService() {
        System.out.println("1. 测试认证服务...");
        
        AuthService authService = Admin.createAuthService();
        
        User user = authService.authenticate("admin", "admin123");
        Assertions.assertNotNull(user, "认证用户不能为空");
        Assertions.assertEquals("admin", user.getUsername(), "用户名应该是admin");
        
        String token = authService.generateToken(user);
        Assertions.assertNotNull(token, "Token不能为空");
        
        User validatedUser = authService.validateToken(token);
        Assertions.assertNotNull(validatedUser, "Token验证用户不能为空");
        Assertions.assertEquals(user.getId(), validatedUser.getId(), "用户ID应该匹配");
        
        boolean hasPermission = authService.hasPermission(user, "system:user:list");
        Assertions.assertTrue(hasPermission, "admin用户应该有所有权�?);
        
        boolean hasRole = authService.hasRole(user, "admin");
        Assertions.assertTrue(hasRole, "admin用户应该有admin角色");
        
        String refreshedToken = authService.refreshToken(token);
        Assertions.assertNotNull(refreshedToken, "刷新Token不能为空");
        
        authService.invalidateToken(token);
        User invalidatedUser = authService.validateToken(token);
        Assertions.assertNull(invalidatedUser, "Token失效后应该验证失�?);
        
        System.out.println("   �?认证服务测试通过");
    }
    
    @Test(displayName = "测试菜单服务功能")
    public void testMenuService() {
        System.out.println("2. 测试菜单服务...");
        
        MenuService menuService = Admin.createMenuService();
        
        List<Menu> allMenus = menuService.getAllMenus();
        Assertions.assertTrue(allMenus.size() > 0, "菜单列表应该不为�?);
        
        List<Menu> menuTree = menuService.getMenuTree();
        Assertions.assertTrue(menuTree.size() > 0, "菜单树应该不为空");
        
        AuthService authService = Admin.createAuthService();
        User user = authService.authenticate("admin", "admin123");
        List<Menu> userMenus = menuService.getUserMenus(user);
        Assertions.assertTrue(userMenus.size() > 0, "用户菜单应该不为�?);
        
        Menu firstMenu = allMenus.get(0);
        Menu retrievedMenu = menuService.getMenu(firstMenu.getId());
        Assertions.assertNotNull(retrievedMenu, "获取菜单应该成功");
        Assertions.assertEquals(firstMenu.getName(), retrievedMenu.getName(), "菜单名称应该匹配");
        
        System.out.println("   �?菜单服务测试通过");
    }
    
    @Test(displayName = "测试日志服务功能")
    public void testLogService() {
        System.out.println("3. 测试日志服务...");
        
        OperationLogService operationLogService = Admin.createOperationLogService();
        LoginLogService loginLogService = Admin.createLoginLogService();
        
        OperationLog opLog = operationLogService.createOperationLog(
            "1", "admin", "用户管理", "新增用户", 
            "POST", "{}", 100L, "127.0.0.1", 
            "Mozilla/5.0", 1, null
        );
        Assertions.assertNotNull(opLog, "创建操作日志应该成功");
        
        List<OperationLog> opLogs = operationLogService.getAllOperationLogs();
        Assertions.assertTrue(opLogs.size() > 0, "操作日志列表应该不为�?);
        
        OperationLog retrievedOpLog = operationLogService.getOperationLog(opLog.getId());
        Assertions.assertNotNull(retrievedOpLog, "获取操作日志应该成功");
        
        LoginLog loginLog = loginLogService.createLoginLog(
            "1", "admin", "127.0.0.1", "Mozilla/5.0", 1, null
        );
        Assertions.assertNotNull(loginLog, "创建登录日志应该成功");
        
        List<LoginLog> loginLogs = loginLogService.getAllLoginLogs();
        Assertions.assertTrue(loginLogs.size() > 0, "登录日志列表应该不为�?);
        
        LoginLog retrievedLoginLog = loginLogService.getLoginLog(loginLog.getId());
        Assertions.assertNotNull(retrievedLoginLog, "获取登录日志应该成功");
        
        operationLogService.deleteOperationLog(opLog.getId());
        OperationLog deletedOpLog = operationLogService.getOperationLog(opLog.getId());
        Assertions.assertNull(deletedOpLog, "删除操作日志后应该找不到");
        
        loginLogService.deleteLoginLog(loginLog.getId());
        LoginLog deletedLoginLog = loginLogService.getLoginLog(loginLog.getId());
        Assertions.assertNull(deletedLoginLog, "删除登录日志后应该找不到");
        
        System.out.println("   �?日志服务测试通过");
    }
    
    @Test(displayName = "测试监控服务功能")
    public void testMonitorService() {
        System.out.println("4. 测试监控服务...");
        
        MonitorService monitorService = Admin.createMonitorService();
        OnlineUserService onlineUserService = Admin.createOnlineUserService();
        CacheMonitorService cacheMonitorService = Admin.createCacheMonitorService();
        
        Map<String, Object> jvmMetrics = monitorService.getJvmMetrics();
        Assertions.assertNotNull(jvmMetrics, "JVM监控指标应该不为�?);
        
        Map<String, Object> systemMetrics = monitorService.getSystemMetrics();
        Assertions.assertNotNull(systemMetrics, "系统监控指标应该不为�?);
        
        Map<String, Object> healthChecks = monitorService.getHealthChecks();
        Assertions.assertNotNull(healthChecks, "健康检查应该不为空");
        
        Map<String, Object> allMetrics = monitorService.getAllMetrics();
        Assertions.assertNotNull(allMetrics, "所有监控指标应该不为空");
        
        List<OnlineUser> onlineUsers = onlineUserService.getOnlineUsers();
        Assertions.assertNotNull(onlineUsers, "在线用户列表应该不为�?);
        
        int onlineCount = onlineUserService.getOnlineUserCount();
        Assertions.assertTrue(onlineCount >= 0, "在线用户数应该大于等�?");
        
        Map<String, Object> cacheStats = cacheMonitorService.getCacheStatistics();
        Assertions.assertNotNull(cacheStats, "缓存统计应该不为�?);
        
        Map<String, Object> cacheKeys = cacheMonitorService.getCacheKeys();
        Assertions.assertNotNull(cacheKeys, "缓存键应该不为空");
        
        System.out.println("   �?监控服务测试通过");
    }
}
