package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.LoginLog;
import ltd.idcu.est.admin.api.LoginLogService;
import ltd.idcu.est.admin.api.OperationLog;
import ltd.idcu.est.admin.api.OperationLogService;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogController {
    
    private final OperationLogService operationLogService;
    private final LoginLogService loginLogService;
    
    public LogController() {
        this.operationLogService = Admin.createOperationLogService();
        this.loginLogService = Admin.createLoginLogService();
    }
    
    @RequirePermission("system:log:operation:list")
    public void listOperationLogs(Request req, Response res) {
        try {
            List<OperationLog> logs = operationLogService.getAllOperationLogs();
            List<Map<String, Object>> logList = logs.stream()
                .map(this::toOperationLogMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(logList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:operation:query")
    public void getOperationLog(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            OperationLog log = operationLogService.getOperationLog(id);
            if (log == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Operation log not found"));
                return;
            }
            res.json(ApiResponse.success(toOperationLogMap(log)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:operation:delete")
    public void deleteOperationLog(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            operationLogService.deleteOperationLog(id);
            res.json(ApiResponse.success("Operation log deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:operation:clear")
    public void clearOperationLogs(Request req, Response res) {
        try {
            operationLogService.clearOperationLogs();
            res.json(ApiResponse.success("Operation logs cleared successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:login:list")
    public void listLoginLogs(Request req, Response res) {
        try {
            List<LoginLog> logs = loginLogService.getAllLoginLogs();
            List<Map<String, Object>> logList = logs.stream()
                .map(this::toLoginLogMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(logList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:login:query")
    public void getLoginLog(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            LoginLog log = loginLogService.getLoginLog(id);
            if (log == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Login log not found"));
                return;
            }
            res.json(ApiResponse.success(toLoginLogMap(log)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:login:delete")
    public void deleteLoginLog(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            loginLogService.deleteLoginLog(id);
            res.json(ApiResponse.success("Login log deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("system:log:login:clear")
    public void clearLoginLogs(Request req, Response res) {
        try {
            loginLogService.clearLoginLogs();
            res.json(ApiResponse.success("Login logs cleared successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toOperationLogMap(OperationLog log) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", log.getId());
        map.put("userId", log.getUserId());
        map.put("username", log.getUsername());
        map.put("module", log.getModule());
        map.put("operation", log.getOperation());
        map.put("method", log.getMethod());
        map.put("params", log.getParams());
        map.put("time", log.getTime());
        map.put("ip", log.getIp());
        map.put("userAgent", log.getUserAgent());
        map.put("status", log.getStatus());
        map.put("errorMsg", log.getErrorMsg());
        map.put("createdAt", log.getCreatedAt());
        return map;
    }
    
    private Map<String, Object> toLoginLogMap(LoginLog log) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", log.getId());
        map.put("userId", log.getUserId());
        map.put("username", log.getUsername());
        map.put("ip", log.getIp());
        map.put("userAgent", log.getUserAgent());
        map.put("status", log.getStatus());
        map.put("errorMsg", log.getErrorMsg());
        map.put("createdAt", log.getCreatedAt());
        return map;
    }
}
