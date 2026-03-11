package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantAuditLog;
import ltd.idcu.est.rbac.DefaultTenant;
import ltd.idcu.est.rbac.DefaultTenantService;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.rbac.api.TenantService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;

public class TenantInterceptorsTest {

    @Test
    public void testDefaultConstructor() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Assertions.assertNotNull(interceptors);
        Assertions.assertNotNull(interceptors.getContextHolder());
        Assertions.assertNotNull(interceptors.getIsolationStrategy());
        Assertions.assertNotNull(interceptors.getSecurityFilter());
        Assertions.assertNotNull(interceptors.getAuditService());
    }

    @Test
    public void testSetTenantById() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant defaultTenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(defaultTenant);
        
        interceptors.setTenantById(defaultTenant.getId());
        
        Tenant currentTenant = interceptors.getCurrentTenant();
        Assertions.assertNotNull(currentTenant);
        Assertions.assertEquals(defaultTenant.getId(), currentTenant.getId());
        
        interceptors.clearTenant();
        Assertions.assertNull(interceptors.getCurrentTenant());
    }

    @Test
    public void testSetTenantByCode() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        interceptors.setTenantByCode("default");
        
        Tenant currentTenant = interceptors.getCurrentTenant();
        Assertions.assertNotNull(currentTenant);
        Assertions.assertEquals("default", currentTenant.getCode());
        
        interceptors.clearTenant();
    }

    @Test
    public void testSetTenantByCodeWithNonExistentCode() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            interceptors.setTenantByCode("non-existent-code");
        });
    }

    @Test
    public void testSetTenantByIdWithNonExistentId() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            interceptors.setTenantById("non-existent-id");
        });
    }

    @Test
    public void testProcessSqlInColumnMode() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = interceptors.processSql(originalSql);
        
        Assertions.assertTrue(processedSql.contains("tenant_id = '" + tenant.getId() + "'"));
        
        interceptors.clearTenant();
    }

    @Test
    public void testProcessSqlInSchemaMode() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant schemaTenant = tenantService.createTenant(
            "Schema Tenant",
            "schema-tenant",
            "schema.example.com",
            Tenant.TenantMode.SCHEMA,
            Long.MAX_VALUE
        );
        
        interceptors.setTenant(schemaTenant);
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = interceptors.processSql(originalSql);
        
        Assertions.assertTrue(processedSql.contains("tenant_schema-tenant.orders"));
        
        interceptors.clearTenant();
    }

    @Test
    public void testProcessSqlInDatabaseMode() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant dbTenant = tenantService.createTenant(
            "Database Tenant",
            "db-tenant",
            "db.example.com",
            Tenant.TenantMode.DATABASE,
            Long.MAX_VALUE
        );
        
        interceptors.setTenant(dbTenant);
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = interceptors.processSql(originalSql);
        
        Assertions.assertTrue(processedSql.startsWith("USE tenant_db-tenant;"));
        
        interceptors.clearTenant();
    }

    @Test
    public void testProcessSqlWithoutTenant() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = interceptors.processSql(originalSql);
        
        Assertions.assertEquals(originalSql, processedSql);
    }

    @Test
    public void testBeforeDataOperationForInsert() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        
        interceptors.beforeDataOperation(entity, "INSERT");
        
        Assertions.assertEquals(tenant.getId(), entity.getTenantId());
        Assertions.assertEquals(tenant.getCode(), entity.getTenantCode());
        
        interceptors.clearTenant();
    }

    @Test
    public void testBeforeDataOperationForUpdate() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId(tenant.getId());
        
        interceptors.beforeDataOperation(entity, "UPDATE");
        
        Assertions.assertEquals(tenant.getId(), entity.getTenantId());
        
        interceptors.clearTenant();
    }

    @Test
    public void testBeforeDataOperationForDelete() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId(tenant.getId());
        
        interceptors.beforeDataOperation(entity, "DELETE");
        
        interceptors.clearTenant();
    }

    @Test
    public void testAfterDataOperationForQuery() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId(tenant.getId());
        
        interceptors.afterDataOperation(entity, "QUERY");
        
        interceptors.clearTenant();
    }

    @Test
    public void testLogDataOperationSuccess() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        interceptors.logDataOperation("CREATE", "Order", "order-123", "Created order", true, null);
        
        List<TenantAuditLog> logs = interceptors.getAuditService().getRecentAuditLogs(10);
        
        Assertions.assertEquals(1, logs.size());
        Assertions.assertTrue(logs.get(0).isSuccess());
        
        interceptors.clearTenant();
    }

    @Test
    public void testLogDataOperationFailure() {
        TenantService tenantService = new DefaultTenantService();
        TenantInterceptors interceptors = new TenantInterceptors(tenantService);
        
        Tenant tenant = tenantService.getTenantByCode("default");
        interceptors.setTenant(tenant);
        
        interceptors.logDataOperation("DELETE", "Product", "prod-456", "Failed to delete", false, "Error message");
        
        List<TenantAuditLog> logs = interceptors.getAuditService().getRecentAuditLogs(10);
        
        Assertions.assertEquals(1, logs.size());
        Assertions.assertFalse(logs.get(0).isSuccess());
        Assertions.assertEquals("Error message", logs.get(0).getErrorMessage());
        
        interceptors.clearTenant();
    }
}
