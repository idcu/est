package ltd.idcu.est.admin.e2e;

import ltd.idcu.est.admin.api.LoginLog;
import ltd.idcu.est.admin.api.OperationLog;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;


import java.util.List;
import java.util.Map;

public class LogAndMonitorE2ETest extends AdminE2ETestBase {
    
    @Test
    @TestCase("测试完整日志和监控流�?)
    public void testCompleteLogAndMonitorFlow() {
        System.out.println("=== 测试完整日志和监控流�?===");
        setUp();
        
        testOperationLog();
        testLoginLog();
        testMonitorMetrics();
        testOnlineUser();
        testCacheMonitor();
        
        System.out.println("�?完整日志和监控流程测试通过\n");
    }
    
    @Test(displayName = "测试操作日志")
    public void testOperationLog() {
        System.out.println("1. 测试操作日志...");
        
        String userId = "test-user-001";
        String username = "testuser";
        String module = "用户管理";
        String operation = "新增用户";
        String method = "POST";
        String params = "{\"username\":\"test\"}";
        Long time = 100L;
        String ip = "192.168.1.1";
        String userAgent = "Mozilla/5.0";
        int status = 1;
        String errorMsg = null;
        
        OperationLog log = operationLogService.createOperationLog(
            userId, username, module, operation, method, params, 
            time, ip, userAgent, status, errorMsg
        );
        
        Assertions.assertNotNull(log, "Operation log should not be null");
        Assertions.assertNotNull(log.getId(), "Log ID should not be null");
        assertOperationLog(log, module, operation);
        
        OperationLog retrievedLog = operationLogService.getOperationLog(log.getId());
        Assertions.assertNotNull(retrievedLog, "Retrieved log should not be null");
        Assertions.assertEquals(log.getId(), retrievedLog.getId(), "Log IDs should match");
        
        List<OperationLog> allLogs = operationLogService.getAllOperationLogs();
        Assertions.assertNotNull(allLogs, "All logs should not be null");
        Assertions.assertTrue(allLogs.size() >= 1, "Should have at least one log");
        
        List<OperationLog> userLogs = operationLogService.getOperationLogsByUserId(userId);
        Assertions.assertNotNull(userLogs, "User logs should not be null");
        
        List<OperationLog> moduleLogs = operationLogService.getOperationLogsByModule(module);
        Assertions.assertNotNull(moduleLogs, "Module logs should not be null");
        
        operationLogService.deleteOperationLog(log.getId());
        OperationLog deletedLog = operationLogService.getOperationLog(log.getId());
        Assertions.assertNull(deletedLog, "Log should be deleted");
        
        System.out.println("   �?操作日志测试通过");
    }
    
    @Test(displayName = "测试登录日志")
    public void testLoginLog() {
        System.out.println("2. 测试登录日志...");
        
        String userId = "test-user-002";
        String username = "testuser2";
        String ip = "192.168.1.2";
        String userAgent = "Chrome/120.0";
        int status = 1;
        String errorMsg = null;
        
        LoginLog log = loginLogService.createLoginLog(
            userId, username, ip, userAgent, status, errorMsg
        );
        
        Assertions.assertNotNull(log, "Login log should not be null");
        Assertions.assertNotNull(log.getId(), "Log ID should not be null");
        assertLoginLog(log, username, status);
        
        LoginLog retrievedLog = loginLogService.getLoginLog(log.getId());
        Assertions.assertNotNull(retrievedLog, "Retrieved log should not be null");
        Assertions.assertEquals(log.getId(), retrievedLog.getId(), "Log IDs should match");
        
        List<LoginLog> allLogs = loginLogService.getAllLoginLogs();
        Assertions.assertNotNull(allLogs, "All logs should not be null");
        Assertions.assertTrue(allLogs.size() >= 1, "Should have at least one log");
        
        List<LoginLog> userLogs = loginLogService.getLoginLogsByUserId(userId);
        Assertions.assertNotNull(userLogs, "User logs should not be null");
        
        List<LoginLog> statusLogs = loginLogService.getLoginLogsByStatus(status);
        Assertions.assertNotNull(statusLogs, "Status logs should not be null");
        
        loginLogService.deleteLoginLog(log.getId());
        LoginLog deletedLog = loginLogService.getLoginLog(log.getId());
        Assertions.assertNull(deletedLog, "Log should be deleted");
        
        System.out.println("   �?登录日志测试通过");
    }
    
    @Test(displayName = "测试监控指标")
    public void testMonitorMetrics() {
        System.out.println("3. 测试监控指标...");
        
        Map<String, Object> jvmMetrics = monitorService.getJvmMetrics();
        Assertions.assertNotNull(jvmMetrics, "JVM metrics should not be null");
        Assertions.assertFalse(jvmMetrics.isEmpty(), "JVM metrics should not be empty");
        
        Map<String, Object> systemMetrics = monitorService.getSystemMetrics();
        Assertions.assertNotNull(systemMetrics, "System metrics should not be null");
        Assertions.assertFalse(systemMetrics.isEmpty(), "System metrics should not be empty");
        
        Map<String, Object> healthChecks = monitorService.getHealthChecks();
        Assertions.assertNotNull(healthChecks, "Health checks should not be null");
        
        Map<String, Object> allMetrics = monitorService.getAllMetrics();
        Assertions.assertNotNull(allMetrics, "All metrics should not be null");
        Assertions.assertTrue(allMetrics.containsKey("jvm"), "All metrics should contain JVM");
        Assertions.assertTrue(allMetrics.containsKey("system"), "All metrics should contain system");
        
        System.out.println("   �?监控指标测试通过");
    }
    
    @Test(displayName = "测试在线用户")
    public void testOnlineUser() {
        System.out.println("4. 测试在线用户...");
        
        List<?> onlineUsers = onlineUserService.getOnlineUsers();
        Assertions.assertNotNull(onlineUsers, "Online users should not be null");
        
        int onlineCount = onlineUserService.getOnlineUserCount();
        Assertions.assertTrue(onlineCount >= 0, "Online count should be >= 0");
        
        System.out.println("   �?在线用户测试通过");
    }
    
    @Test(displayName = "测试缓存监控")
    public void testCacheMonitor() {
        System.out.println("5. 测试缓存监控...");
        
        Map<String, Object> cacheStats = cacheMonitorService.getCacheStatistics();
        Assertions.assertNotNull(cacheStats, "Cache statistics should not be null");
        
        Map<String, Object> cacheKeys = cacheMonitorService.getCacheKeys();
        Assertions.assertNotNull(cacheKeys, "Cache keys should not be null");
        
        System.out.println("   �?缓存监控测试通过");
    }
    
    @Test(displayName = "测试操作日志清除")
    public void testOperationLogClear() {
        System.out.println("6. 测试操作日志清除...");
        
        operationLogService.createOperationLog(
            "user1", "testuser1", "模块1", "操作1", 
            "GET", "{}", 50L, "1.1.1.1", "Test", 1, null
        );
        operationLogService.createOperationLog(
            "user2", "testuser2", "模块2", "操作2", 
            "POST", "{}", 60L, "2.2.2.2", "Test", 1, null
        );
        
        List<OperationLog> beforeClear = operationLogService.getAllOperationLogs();
        Assertions.assertTrue(beforeClear.size() >= 2, "Should have at least 2 logs");
        
        operationLogService.clearOperationLogs();
        
        List<OperationLog> afterClear = operationLogService.getAllOperationLogs();
        Assertions.assertTrue(afterClear.isEmpty(), "Logs should be cleared");
        
        System.out.println("   �?操作日志清除测试通过");
    }
    
    @Test(displayName = "测试登录日志清除")
    public void testLoginLogClear() {
        System.out.println("7. 测试登录日志清除...");
        
        loginLogService.createLoginLog(
            "user1", "testuser1", "1.1.1.1", "Test", 1, null
        );
        loginLogService.createLoginLog(
            "user2", "testuser2", "2.2.2.2", "Test", 0, "Error"
        );
        
        List<LoginLog> beforeClear = loginLogService.getAllLoginLogs();
        Assertions.assertTrue(beforeClear.size() >= 2, "Should have at least 2 logs");
        
        loginLogService.clearLoginLogs();
        
        List<LoginLog> afterClear = loginLogService.getAllLoginLogs();
        Assertions.assertTrue(afterClear.isEmpty(), "Logs should be cleared");
        
        System.out.println("   �?登录日志清除测试通过");
    }
    
    @Test(displayName = "测试失败登录日志")
    public void testFailedLoginLog() {
        System.out.println("8. 测试失败登录日志...");
        
        String username = "faileduser";
        String ip = "10.0.0.1";
        String userAgent = "BadBrowser";
        int status = 0;
        String errorMsg = "Invalid credentials";
        
        LoginLog log = loginLogService.createLoginLog(
            null, username, ip, userAgent, status, errorMsg
        );
        
        Assertions.assertNotNull(log, "Failed login log should not be null");
        Assertions.assertEquals(status, log.getStatus(), "Status should be 0 (failed)");
        Assertions.assertEquals(errorMsg, log.getErrorMsg(), "Error message should match");
        
        System.out.println("   �?失败登录日志测试通过");
    }
}
