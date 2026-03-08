package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.CacheMonitorService;
import ltd.idcu.est.admin.api.MonitorService;
import ltd.idcu.est.admin.api.OnlineUser;
import ltd.idcu.est.admin.api.OnlineUserService;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MonitorController {
    
    private final MonitorService monitorService;
    private final OnlineUserService onlineUserService;
    private final CacheMonitorService cacheMonitorService;
    
    public MonitorController(MonitorService monitorService, 
                            OnlineUserService onlineUserService,
                            CacheMonitorService cacheMonitorService) {
        this.monitorService = monitorService;
        this.onlineUserService = onlineUserService;
        this.cacheMonitorService = cacheMonitorService;
    }
    
    @RequirePermission("monitor:service:query")
    public void getJvmMetrics(Request req, Response res) {
        try {
            Map<String, Object> metrics = monitorService.getJvmMetrics();
            res.json(ApiResponse.success(metrics));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:service:query")
    public void getSystemMetrics(Request req, Response res) {
        try {
            Map<String, Object> metrics = monitorService.getSystemMetrics();
            res.json(ApiResponse.success(metrics));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:service:query")
    public void getHealthChecks(Request req, Response res) {
        try {
            Map<String, Object> healthChecks = monitorService.getHealthChecks();
            res.json(ApiResponse.success(healthChecks));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:service:query")
    public void getAllMetrics(Request req, Response res) {
        try {
            Map<String, Object> metrics = monitorService.getAllMetrics();
            res.json(ApiResponse.success(metrics));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:online:list")
    public void getOnlineUsers(Request req, Response res) {
        try {
            List<OnlineUser> users = onlineUserService.getOnlineUsers();
            List<Map<String, Object>> userList = users.stream()
                .map(this::toOnlineUserMap)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("data", userList);
            result.put("count", onlineUserService.getOnlineUserCount());
            
            res.json(ApiResponse.success(result));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:online:logout")
    public void forceLogout(Request req, Response res) {
        try {
            String sessionId = req.getPathVariable("sessionId");
            onlineUserService.forceLogout(sessionId);
            res.json(ApiResponse.success("User force logged out successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:cache:query")
    public void getCacheStatistics(Request req, Response res) {
        try {
            Map<String, Object> statistics = cacheMonitorService.getCacheStatistics();
            res.json(ApiResponse.success(statistics));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:cache:query")
    public void getCacheKeys(Request req, Response res) {
        try {
            Map<String, Object> keys = cacheMonitorService.getCacheKeys();
            res.json(ApiResponse.success(keys));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:cache:clear")
    public void clearCache(Request req, Response res) {
        try {
            String cacheName = req.getPathVariable("cacheName");
            cacheMonitorService.clearCache(cacheName);
            res.json(ApiResponse.success("Cache cleared successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("monitor:cache:clear")
    public void clearAllCaches(Request req, Response res) {
        try {
            cacheMonitorService.clearAllCaches();
            res.json(ApiResponse.success("All caches cleared successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toOnlineUserMap(OnlineUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", user.getSessionId());
        map.put("userId", user.getUserId());
        map.put("username", user.getUsername());
        map.put("ip", user.getIp());
        map.put("browser", user.getBrowser());
        map.put("loginTime", user.getLoginTime());
        map.put("lastActivityTime", user.getLastActivityTime());
        return map;
    }
}
