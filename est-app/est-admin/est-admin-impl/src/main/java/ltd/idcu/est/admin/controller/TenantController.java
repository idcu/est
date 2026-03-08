package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.Tenant;
import ltd.idcu.est.admin.api.TenantService;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TenantController {
    
    private final TenantService tenantService;
    
    public TenantController() {
        this.tenantService = Admin.createTenantService();
    }
    
    public void list(Request req, Response res) {
        try {
            List<Tenant> tenants = tenantService.getAllTenants();
            List<Map<String, Object>> tenantList = tenants.stream()
                .map(this::toTenantMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(tenantList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void get(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Tenant tenant = tenantService.getTenant(id);
            if (tenant == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Tenant not found"));
                return;
            }
            res.json(ApiResponse.success(toTenantMap(tenant)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void getByCode(Request req, Response res) {
        try {
            String code = req.getPathVariable("code");
            Tenant tenant = tenantService.getTenantByCode(code);
            if (tenant == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Tenant not found"));
                return;
            }
            res.json(ApiResponse.success(toTenantMap(tenant)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void getByDomain(Request req, Response res) {
        try {
            String domain = req.getPathVariable("domain");
            Tenant tenant = tenantService.getTenantByDomain(domain);
            if (tenant == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Tenant not found"));
                return;
            }
            res.json(ApiResponse.success(toTenantMap(tenant)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void create(Request req, Response res) {
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String domain = req.getParameter("domain");
            int mode = req.getIntParameter("mode", 0);
            long expiresAt = req.getLongParameter("expiresAt", Long.MAX_VALUE);
            
            Tenant.TenantMode tenantMode = Tenant.TenantMode.values()[mode];
            
            Tenant tenant = tenantService.createTenant(name, code, domain, tenantMode, expiresAt);
            res.setStatus(201);
            res.json(ApiResponse.success("Tenant created successfully", toTenantMap(tenant)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void update(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String name = req.getParameter("name");
            String domain = req.getParameter("domain");
            Boolean active = req.getParameter("active") != null ? req.getBooleanParameter("active", true) : null;
            Long expiresAt = req.getParameter("expiresAt") != null ? req.getLongParameter("expiresAt", Long.MAX_VALUE) : null;
            
            Tenant tenant = tenantService.updateTenant(id, name, domain, active, expiresAt);
            res.json(ApiResponse.success("Tenant updated successfully", toTenantMap(tenant)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void delete(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            tenantService.deleteTenant(id);
            res.json(ApiResponse.success("Tenant deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void setCurrent(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            tenantService.setCurrentTenant(id);
            res.json(ApiResponse.success("Current tenant set successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void getCurrent(Request req, Response res) {
        try {
            Tenant tenant = tenantService.getCurrentTenant();
            if (tenant == null) {
                res.json(ApiResponse.success(null));
                return;
            }
            res.json(ApiResponse.success(toTenantMap(tenant)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void clearCurrent(Request req, Response res) {
        try {
            tenantService.clearCurrentTenant();
            res.json(ApiResponse.success("Current tenant cleared successfully", null));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    private Map<String, Object> toTenantMap(Tenant tenant) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", tenant.getId());
        map.put("name", tenant.getName());
        map.put("code", tenant.getCode());
        map.put("domain", tenant.getDomain());
        map.put("mode", tenant.getMode().ordinal());
        map.put("active", tenant.isActive());
        map.put("createdAt", tenant.getCreatedAt());
        map.put("expiresAt", tenant.getExpiresAt());
        return map;
    }
}
