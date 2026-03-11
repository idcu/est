package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.multitenancy.api.TenantContextHolder;
import ltd.idcu.est.multitenancy.api.TenantDataSecurityFilter;
import ltd.idcu.est.rbac.DefaultTenant;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class TenantDataSecurityFilterTest {

    @Test
    public void testDefaultConstructor() {
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter();
        
        Assertions.assertNotNull(filter);
    }

    @Test
    public void testCustomConstructor() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        
        Assertions.assertNotNull(filter);
    }

    @Test
    public void testBeforeQuerySetsTenantInfo() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "query-test",
            "Query Test Tenant",
            "querytest",
            "querytest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        
        filter.beforeQuery(entity);
        
        Assertions.assertEquals("query-test", entity.getTenantId());
        Assertions.assertEquals("querytest", entity.getTenantCode());
        
        holder.clearContext();
    }

    @Test
    public void testBeforeInsertSetsTenantInfo() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
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
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        
        filter.beforeInsert(entity);
        
        Assertions.assertEquals("insert-test", entity.getTenantId());
        Assertions.assertEquals("inserttest", entity.getTenantCode());
        
        holder.clearContext();
    }

    @Test
    public void testBeforeUpdateValidatesAndSetsTenantInfo() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
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
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId("update-test");
        
        filter.beforeUpdate(entity);
        
        Assertions.assertEquals("update-test", entity.getTenantId());
        Assertions.assertEquals("updatetest", entity.getTenantCode());
        
        holder.clearContext();
    }

    @Test
    public void testBeforeDeleteValidatesTenantAccess() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
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
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId("delete-test");
        
        filter.beforeDelete(entity);
        
        holder.clearContext();
    }

    @Test
    public void testAfterQueryValidatesTenantAccess() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "after-query-test",
            "After Query Test Tenant",
            "afterquerytest",
            "afterquerytest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId("after-query-test");
        
        filter.afterQuery(entity);
        
        holder.clearContext();
    }

    @Test
    public void testValidateTenantAccessThrowsExceptionForDifferentTenant() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "validate-test",
            "Validate Test Tenant",
            "validatetest",
            "validatetest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId("different-tenant-id");
        
        Assertions.assertThrows(SecurityException.class, () -> {
            filter.validateTenantAccess(entity);
        });
        
        holder.clearContext();
    }

    @Test
    public void testValidateTenantAccessThrowsExceptionWithoutTenantContext() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        TestTenantAwareEntity entity = new TestTenantAwareEntity("1", "Test Entity");
        entity.setTenantId("some-tenant-id");
        
        Assertions.assertThrows(SecurityException.class, () -> {
            filter.validateTenantAccess(entity);
        });
    }

    @Test
    public void testNoTenantAwareEntityDoesNothing() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "no-aware-test",
            "No Aware Test Tenant",
            "noawaretest",
            "noawaretest.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        TenantDataSecurityFilter filter = new DefaultTenantDataSecurityFilter(holder);
        Object nonTenantAware = new Object();
        
        filter.beforeQuery(nonTenantAware);
        filter.beforeInsert(nonTenantAware);
        filter.beforeUpdate(nonTenantAware);
        filter.beforeDelete(nonTenantAware);
        filter.afterQuery(nonTenantAware);
        filter.validateTenantAccess(nonTenantAware);
        
        holder.clearContext();
    }
}
