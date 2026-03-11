package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantDataIsolationStrategy;
import ltd.idcu.est.rbac.DefaultTenant;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class TenantDataIsolationStrategyTest {

    @Test
    public void testDefaultConfiguration() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Assertions.assertEquals("tenant_id", strategy.getTenantColumnName());
    }

    @Test
    public void testCustomConfiguration() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy(
            "custom_tenant_id",
            "custom_schema_",
            "custom_db_"
        );
        
        Assertions.assertEquals("custom_tenant_id", strategy.getTenantColumnName());
    }

    @Test
    public void testGetTenantColumnValue() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "tenant-789",
            "Value Test Tenant",
            "valuetest",
            "valuetest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String value = strategy.getTenantColumnValue(tenant);
        Assertions.assertEquals("tenant-789", value);
        
        Assertions.assertNull(strategy.getTenantColumnValue(null));
    }

    @Test
    public void testApplySelectTenantFilterWithoutWhere() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "select-test",
            "Select Test Tenant",
            "selecttest",
            "selecttest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = strategy.applyTenantFilter(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.contains("WHERE tenant_id = 'select-test'"));
    }

    @Test
    public void testApplySelectTenantFilterWithWhere() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "select-where-test",
            "Select Where Test Tenant",
            "selectwheretest",
            "selectwheretest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "SELECT * FROM orders WHERE status = 'ACTIVE'";
        String processedSql = strategy.applyTenantFilter(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.contains("tenant_id = 'select-where-test' AND status = 'ACTIVE'"));
    }

    @Test
    public void testApplyInsertTenantFilter() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "insert-test",
            "Insert Test Tenant",
            "inserttest",
            "inserttest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "INSERT INTO orders (product, amount) VALUES ('Widget', 100)";
        String processedSql = strategy.applyTenantFilter(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.contains(", tenant_id"));
        Assertions.assertTrue(processedSql.contains(", 'insert-test'"));
    }

    @Test
    public void testApplyUpdateTenantFilterWithoutWhere() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "update-test",
            "Update Test Tenant",
            "updatetest",
            "updatetest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "UPDATE orders SET status = 'SHIPPED'";
        String processedSql = strategy.applyTenantFilter(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.contains("WHERE tenant_id = 'update-test'"));
    }

    @Test
    public void testApplyDeleteTenantFilterWithoutWhere() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "delete-test",
            "Delete Test Tenant",
            "deletetest",
            "deletetest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "DELETE FROM orders";
        String processedSql = strategy.applyTenantFilter(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.contains("WHERE tenant_id = 'delete-test'"));
    }

    @Test
    public void testApplyTenantSchema() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "schema-test",
            "Schema Test Tenant",
            "schematest",
            "schematest.example.com",
            Tenant.TenantMode.SCHEMA,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = strategy.applyTenantSchema(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.contains("tenant_schematest.orders"));
    }

    @Test
    public void testApplyTenantDatabase() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Tenant tenant = new DefaultTenant(
            "db-test",
            "Database Test Tenant",
            "dbtest",
            "dbtest.example.com",
            Tenant.TenantMode.DATABASE,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String originalSql = "SELECT * FROM orders";
        String processedSql = strategy.applyTenantDatabase(originalSql, tenant);
        
        Assertions.assertTrue(processedSql.startsWith("USE tenant_dbtest;"));
    }

    @Test
    public void testSupportsAllModes() {
        TenantDataIsolationStrategy strategy = new DefaultTenantDataIsolationStrategy();
        
        Assertions.assertTrue(strategy.supports(Tenant.TenantMode.COLUMN));
        Assertions.assertTrue(strategy.supports(Tenant.TenantMode.SCHEMA));
        Assertions.assertTrue(strategy.supports(Tenant.TenantMode.DATABASE));
    }
}
