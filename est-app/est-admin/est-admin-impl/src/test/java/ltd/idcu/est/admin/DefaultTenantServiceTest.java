package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Tenant;
import ltd.idcu.est.admin.api.TenantService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;

public class DefaultTenantServiceTest {

    @Test
    public void testCreateTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        long expiresAt = System.currentTimeMillis() + 86400000L;
        Tenant tenant = tenantService.createTenant(
            "测试租户", 
            "test", 
            "test.example.com", 
            Tenant.TenantMode.COLUMN, 
            expiresAt
        );
        
        Assertions.assertNotNull(tenant);
        Assertions.assertNotNull(tenant.getId());
        Assertions.assertEquals("测试租户", tenant.getName());
        Assertions.assertEquals("test", tenant.getCode());
        Assertions.assertEquals("test.example.com", tenant.getDomain());
        Assertions.assertEquals(Tenant.TenantMode.COLUMN, tenant.getMode());
        Assertions.assertTrue(tenant.isActive());
        Assertions.assertEquals(expiresAt, tenant.getExpiresAt());
    }

    @Test
    public void testCreateTenantWithDuplicateCode() {
        TenantService tenantService = new DefaultTenantService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            tenantService.createTenant(
                "重复租户", 
                "default", 
                null, 
                Tenant.TenantMode.COLUMN, 
                Long.MAX_VALUE
            );
        });
    }

    @Test
    public void testCreateTenantWithDuplicateDomain() {
        TenantService tenantService = new DefaultTenantService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            tenantService.createTenant(
                "重复租户", 
                "test2", 
                "localhost", 
                Tenant.TenantMode.COLUMN, 
                Long.MAX_VALUE
            );
        });
    }

    @Test
    public void testGetTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant defaultTenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(defaultTenant);
        
        Tenant tenant = tenantService.getTenant(defaultTenant.getId());
        Assertions.assertNotNull(tenant);
        Assertions.assertEquals("default", tenant.getCode());
    }

    @Test
    public void testGetTenantByCode() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant tenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(tenant);
        Assertions.assertEquals("default", tenant.getCode());
    }

    @Test
    public void testGetTenantByDomain() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant tenant = tenantService.getTenantByDomain("localhost");
        Assertions.assertNotNull(tenant);
        Assertions.assertEquals("localhost", tenant.getDomain());
    }

    @Test
    public void testGetAllTenants() {
        TenantService tenantService = new DefaultTenantService();
        
        List<Tenant> tenants = tenantService.getAllTenants();
        Assertions.assertNotNull(tenants);
        Assertions.assertTrue(tenants.size() >= 1);
    }

    @Test
    public void testUpdateTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant defaultTenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(defaultTenant);
        
        long newExpiresAt = System.currentTimeMillis() + 172800000L;
        Tenant updatedTenant = tenantService.updateTenant(
            defaultTenant.getId(),
            "更新后的默认租户",
            "updated.example.com",
            true,
            newExpiresAt
        );
        
        Assertions.assertNotNull(updatedTenant);
        Assertions.assertEquals("更新后的默认租户", updatedTenant.getName());
        Assertions.assertEquals("updated.example.com", updatedTenant.getDomain());
        Assertions.assertEquals(newExpiresAt, updatedTenant.getExpiresAt());
    }

    @Test
    public void testUpdateTenantWithDuplicateDomain() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant defaultTenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(defaultTenant);
        
        tenantService.createTenant(
            "另一个租户", 
            "another", 
            "another.example.com", 
            Tenant.TenantMode.COLUMN, 
            Long.MAX_VALUE
        );
        
        Assertions.assertThrows(AdminException.class, () -> {
            tenantService.updateTenant(
                defaultTenant.getId(),
                null,
                "another.example.com",
                true,
                Long.MAX_VALUE
            );
        });
    }

    @Test
    public void testDeleteTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant tenant = tenantService.createTenant(
            "要删除的租户", 
            "todelete", 
            "todelete.example.com", 
            Tenant.TenantMode.COLUMN, 
            Long.MAX_VALUE
        );
        Assertions.assertNotNull(tenant);
        
        tenantService.deleteTenant(tenant.getId());
        Tenant deletedTenant = tenantService.getTenant(tenant.getId());
        Assertions.assertNull(deletedTenant);
    }

    @Test
    public void testSetAndGetCurrentTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant defaultTenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(defaultTenant);
        
        Assertions.assertNull(tenantService.getCurrentTenant());
        
        tenantService.setCurrentTenant(defaultTenant.getId());
        Tenant currentTenant = tenantService.getCurrentTenant();
        
        Assertions.assertNotNull(currentTenant);
        Assertions.assertEquals(defaultTenant.getId(), currentTenant.getId());
    }

    @Test
    public void testSetCurrentTenantWithNonExistentTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            tenantService.setCurrentTenant("non-existent-id");
        });
    }

    @Test
    public void testSetCurrentTenantWithInactiveTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant tenant = tenantService.createTenant(
            "非活跃租户", 
            "inactive", 
            "inactive.example.com", 
            Tenant.TenantMode.COLUMN, 
            Long.MAX_VALUE
        );
        Assertions.assertNotNull(tenant);
        
        tenantService.updateTenant(tenant.getId(), null, null, false, Long.MAX_VALUE);
        
        Assertions.assertThrows(AdminException.class, () -> {
            tenantService.setCurrentTenant(tenant.getId());
        });
    }

    @Test
    public void testClearCurrentTenant() {
        TenantService tenantService = new DefaultTenantService();
        
        Tenant defaultTenant = tenantService.getTenantByCode("default");
        Assertions.assertNotNull(defaultTenant);
        
        tenantService.setCurrentTenant(defaultTenant.getId());
        Assertions.assertNotNull(tenantService.getCurrentTenant());
        
        tenantService.clearCurrentTenant();
        Assertions.assertNull(tenantService.getCurrentTenant());
    }
}
