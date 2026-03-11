package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.admin.api.WorkflowService;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkflowController {
    
    public WorkflowController() {
    }
    
    @RequirePermission("workflow:definition:list")
    public void listDefinitions(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:definition:query")
    public void getDefinition(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:definition:add")
    public void createDefinition(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:definition:edit")
    public void updateDefinition(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:definition:delete")
    public void deleteDefinition(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:list")
    public void listInstances(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:query")
    public void getInstance(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:start")
    public void startInstance(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:pause")
    public void pauseInstance(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:resume")
    public void resumeInstance(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:cancel")
    public void cancelInstance(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:retry")
    public void retryInstance(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:variables")
    public void getInstanceVariables(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
    
    @RequirePermission("workflow:instance:variables")
    public void setInstanceVariable(Request req, Response res) {
        res.setStatus(501);
        res.json(ApiResponse.error("Workflow Service not available"));
    }
}
