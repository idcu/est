package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantAuditLog;
import ltd.idcu.est.multitenancy.api.TenantAuditService;
import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.multitenancy.api.TenantContextHolder;
import ltd.idcu.est.rbac.DefaultTenant;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;

public class TenantAuditServiceTest {

    @Test
    public void testDefaultConstructor() {
        TenantAuditService service = new DefaultTenantAuditService();
        
        Assertions.assertNotNull(service);
    }

    @Test
    public void testCustomConstructor() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantAuditService service = new DefaultTenantAuditService(holder);
        
        Assertions.assertNotNull(service);
    }

    @Test
    public void testLogSuccess() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "audit-test",
            "Audit Test Tenant",
            "audittest",
            "audittest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantAuditService service = new DefaultTenantAuditService(holder);
        
        service.logSuccess("CREATE", "Order", "order-123", "Created new order");
        
        List<TenantAuditLog> logs = service.getRecentAuditLogs(10);
        
        Assertions.assertNotNull(logs);
        Assertions.assertEquals(1, logs.size());
        
        TenantAuditLog log = logs.get(0);
        Assertions.assertEquals("audit-test", log.getTenantId());
        Assertions.assertEquals("audittest", log.getTenantCode());
        Assertions.assertEquals("CREATE", log.getOperation());
        Assertions.assertEquals("Order", log.getResourceType());
        Assertions.assertEquals("order-123", log.getResourceId());
        Assertions.assertEquals("Created new order", log.getDetails());
        Assertions.assertTrue(log.isSuccess());
        Assertions.assertNull(log.getErrorMessage());
        
        holder.clearContext();
    }

    @Test
    public void testLogFailure() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "audit-failure-test",
            "Audit Failure Test Tenant",
            "auditfailuretest",
            "auditfailuretest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantAuditService service = new DefaultTenantAuditService(holder);
        
        service.logFailure("DELETE", "Product", "prod-456", "Failed to delete product", "Permission denied");
        
        List<TenantAuditLog> logs = service.getRecentAuditLogs(10);
        
        Assertions.assertNotNull(logs);
        Assertions.assertEquals(1, logs.size());
        
        TenantAuditLog log = logs.get(0);
        Assertions.assertEquals("audit-failure-test", log.getTenantId());
        Assertions.assertEquals("auditfailuretest", log.getTenantCode());
        Assertions.assertEquals("DELETE", log.getOperation());
        Assertions.assertEquals("Product", log.getResourceType());
        Assertions.assertEquals("prod-456", log.getResourceId());
        Assertions.assertEquals("Failed to delete product", log.getDetails());
        Assertions.assertFalse(log.isSuccess());
        Assertions.assertEquals("Permission denied", log.getErrorMessage());
        
        holder.clearContext();
    }

    @Test
    public void testLogOperation() {
        TenantAuditService service = new DefaultTenantAuditService();
        
        TenantAuditLog log = DefaultTenantAuditLog.builder()
            .id("log-789")
            .tenantId("manual-tenant")
            .tenantCode("manual")
            .operation("UPDATE")
            .resourceType("User")
            .resourceId("user-789")
            .details("Updated user profile")
            .success(true)
            .build();
        
        service.logOperation(log);
        
        List<TenantAuditLog> logs = service.getRecentAuditLogs(10);
        
        Assertions.assertNotNull(logs);
        Assertions.assertEquals(1, logs.size());
        Assertions.assertEquals("log-789", logs.get(0).getId());
    }

    @Test
    public void testGetAuditLogsByTenant() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant1 = new DefaultTenant(
            "tenant-1",
            "Tenant 1",
            "tenant1",
            "tenant1.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        Tenant tenant2 = new DefaultTenant(
            "tenant-2",
            "Tenant 2",
            "tenant2",
            "tenant2.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        TenantAuditService service = new DefaultTenantAuditService(holder);
        
        context.setCurrentTenant(tenant1);
        service.logSuccess("CREATE", "Order", "order-t1-1", "Order 1 for tenant 1");
        service.logSuccess("CREATE", "Order", "order-t1-2", "Order 2 for tenant 1");
        
        context.setCurrentTenant(tenant2);
        service.logSuccess("CREATE", "Order", "order-t2-1", "Order 1 for tenant 2");
        
        List<TenantAuditLog> tenant1Logs = service.getAuditLogsByTenant("tenant-1", 10);
        List<TenantAuditLog> tenant2Logs = service.getAuditLogsByTenant("tenant-2", 10);
        
        Assertions.assertEquals(2, tenant1Logs.size());
        Assertions.assertEquals(1, tenant2Logs.size());
        
        holder.clearContext();
    }

    @Test
    public void testGetAuditLogsByResource() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "resource-test",
            "Resource Test Tenant",
            "resourcetest",
            "resourcetest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantAuditService service = new DefaultTenantAuditService(holder);
        
        service.logSuccess("CREATE", "Product", "prod-xyz", "Created product");
        service.logSuccess("UPDATE", "Product", "prod-xyz", "Updated product");
        service.logSuccess("READ", "Product", "prod-xyz", "Read product");
        
        List<TenantAuditLog> resourceLogs = service.getAuditLogsByResource("Product", "prod-xyz", 10);
        
        Assertions.assertEquals(3, resourceLogs.size());
        
        holder.clearContext();
    }

    @Test
    public void testGetRecentAuditLogsWithLimit() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "recent-test",
            "Recent Test Tenant",
            "recenttest",
            "recenttest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantAuditService service = new DefaultTenantAuditService(holder);
        
        for (int i = 0; i < 5; i++) {
            service.logSuccess("OP", "Type", "id-" + i, "Details " + i);
        }
        
        List<TenantAuditLog> recentLogs = service.getRecentAuditLogs(3);
        
        Assertions.assertEquals(3, recentLogs.size());
        
        holder.clearContext();
    }

    @Test
    public void testDefaultTenantAuditLogBuilder() {
        DefaultTenantAuditLog log = DefaultTenantAuditLog.builder()
            .id("test-log-id")
            .tenantId("test-tenant-id")
            .tenantCode("test-tenant-code")
            .userId("test-user-id")
            .operation("TEST_OP")
            .resourceType("TestResource")
            .resourceId("test-resource-id")
            .details("Test details")
            .ipAddress("192.168.1.1")
            .userAgent("Test Browser")
            .timestamp(1234567890L)
            .success(true)
            .errorMessage(null)
            .build();
        
        Assertions.assertEquals("test-log-id", log.getId());
        Assertions.assertEquals("test-tenant-id", log.getTenantId());
        Assertions.assertEquals("test-tenant-code", log.getTenantCode());
        Assertions.assertEquals("test-user-id", log.getUserId());
        Assertions.assertEquals("TEST_OP", log.getOperation());
        Assertions.assertEquals("TestResource", log.getResourceType());
        Assertions.assertEquals("test-resource-id", log.getResourceId());
        Assertions.assertEquals("Test details", log.getDetails());
        Assertions.assertEquals("192.168.1.1", log.getIpAddress());
        Assertions.assertEquals("Test Browser", log.getUserAgent());
        Assertions.assertEquals(1234567890L, log.getTimestamp());
        Assertions.assertTrue(log.isSuccess());
        Assertions.assertNull(log.getErrorMessage());
    }
}
