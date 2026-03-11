package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.rbac.DefaultTenant;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class TenantContextTest {

    @Test
    public void testDefaultTenantContextInitialization() {
        TenantContext context = new DefaultTenantContext();
        
        Assertions.assertNull(context.getCurrentTenant());
        Assertions.assertFalse(context.hasCurrentTenant());
        Assertions.assertNull(context.getCurrentTenantId());
        Assertions.assertNull(context.getCurrentTenantCode());
        Assertions.assertNull(context.getCurrentTenantMode());
    }

    @Test
    public void testDefaultTenantContextWithTenant() {
        Tenant tenant = new DefaultTenant(
            "tenant-123",
            "Test Tenant",
            "test",
            "test.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        TenantContext context = new DefaultTenantContext(tenant);
        
        Assertions.assertNotNull(context.getCurrentTenant());
        Assertions.assertTrue(context.hasCurrentTenant());
        Assertions.assertEquals("tenant-123", context.getCurrentTenantId());
        Assertions.assertEquals("test", context.getCurrentTenantCode());
        Assertions.assertEquals(Tenant.TenantMode.COLUMN, context.getCurrentTenantMode());
    }

    @Test
    public void testSetAndClearTenant() {
        TenantContext context = new DefaultTenantContext();
        
        Tenant tenant = new DefaultTenant(
            "tenant-456",
            "Another Tenant",
            "another",
            "another.example.com",
            Tenant.TenantMode.SCHEMA,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        
        Assertions.assertNotNull(context.getCurrentTenant());
        Assertions.assertTrue(context.hasCurrentTenant());
        Assertions.assertEquals("tenant-456", context.getCurrentTenantId());
        
        context.clearCurrentTenant();
        
        Assertions.assertNull(context.getCurrentTenant());
        Assertions.assertFalse(context.hasCurrentTenant());
    }
}
