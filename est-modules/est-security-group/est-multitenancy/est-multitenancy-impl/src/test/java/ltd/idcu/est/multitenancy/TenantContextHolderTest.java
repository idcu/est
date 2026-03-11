package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.multitenancy.api.TenantContextHolder;
import ltd.idcu.est.rbac.DefaultTenant;
import ltd.idcu.est.rbac.api.Tenant;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class TenantContextHolderTest {

    @Test
    public void testSingletonInstance() {
        TenantContextHolder holder1 = ThreadLocalTenantContextHolder.getInstance();
        TenantContextHolder holder2 = ThreadLocalTenantContextHolder.getInstance();
        
        Assertions.assertNotNull(holder1);
        Assertions.assertSame(holder1, holder2);
    }

    @Test
    public void testGetContext() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Assertions.assertNotNull(context);
        Assertions.assertFalse(context.hasCurrentTenant());
    }

    @Test
    public void testThreadLocalIsolation() throws InterruptedException {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        
        Tenant tenant1 = new DefaultTenant(
            "thread-1-tenant",
            "Thread 1 Tenant",
            "thread1",
            "thread1.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        Tenant tenant2 = new DefaultTenant(
            "thread-2-tenant",
            "Thread 2 Tenant",
            "thread2",
            "thread2.example.com",
            Tenant.TenantMode.SCHEMA,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        String[] thread1TenantId = new String[1];
        String[] thread2TenantId = new String[1];
        
        Thread thread1 = new Thread(() -> {
            TenantContext context = holder.getContext();
            context.setCurrentTenant(tenant1);
            thread1TenantId[0] = context.getCurrentTenantId();
        });
        
        Thread thread2 = new Thread(() -> {
            TenantContext context = holder.getContext();
            context.setCurrentTenant(tenant2);
            thread2TenantId[0] = context.getCurrentTenantId();
        });
        
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        
        Assertions.assertEquals("thread-1-tenant", thread1TenantId[0]);
        Assertions.assertEquals("thread-2-tenant", thread2TenantId[0]);
        
        TenantContext mainContext = holder.getContext();
        Assertions.assertFalse(mainContext.hasCurrentTenant());
    }

    @Test
    public void testClearContext() {
        TenantContextHolder holder = ThreadLocalTenantContextHolder.getInstance();
        TenantContext context = holder.getContext();
        
        Tenant tenant = new DefaultTenant(
            "clear-test-tenant",
            "Clear Test Tenant",
            "clear",
            "clear.example.com",
            Tenant.TenantMode.COLUMN,
            true,
            System.currentTimeMillis(),
            Long.MAX_VALUE
        );
        
        context.setCurrentTenant(tenant);
        Assertions.assertTrue(context.hasCurrentTenant());
        
        holder.clearContext();
        
        TenantContext newContext = holder.getContext();
        Assertions.assertFalse(newContext.hasCurrentTenant());
    }
}
