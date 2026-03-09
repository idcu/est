package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.admin.api.WorkflowService;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.workflow.api.WorkflowDefinition;
import ltd.idcu.est.workflow.api.WorkflowInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkflowController {
    
    private final WorkflowService workflowService;
    
    public WorkflowController() {
        this.workflowService = Admin.createWorkflowService();
    }
    
    @RequirePermission("workflow:definition:list")
    public void listDefinitions(Request req, Response res) {
        try {
            List<WorkflowDefinition> definitions = workflowService.getAllDefinitions();
            List<Map<String, Object>> definitionList = definitions.stream()
                .map(this::toDefinitionMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(definitionList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:definition:query")
    public void getDefinition(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Optional<WorkflowDefinition> definitionOpt = workflowService.getDefinition(id);
            if (definitionOpt.isPresent()) {
                res.json(ApiResponse.success(toDefinitionMap(definitionOpt.get())));
            } else {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Workflow definition not found"));
            }
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:definition:add")
    public void createDefinition(Request req, Response res) {
        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String jsonDefinition = req.getParameter("jsonDefinition");
            
            WorkflowDefinition definition = workflowService.createDefinition(name, description, jsonDefinition);
            res.setStatus(201);
            res.json(ApiResponse.success("Workflow definition created successfully", toDefinitionMap(definition)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:definition:edit")
    public void updateDefinition(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String jsonDefinition = req.getParameter("jsonDefinition");
            
            WorkflowDefinition definition = workflowService.updateDefinition(id, name, description, jsonDefinition);
            res.json(ApiResponse.success("Workflow definition updated successfully", toDefinitionMap(definition)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:definition:delete")
    public void deleteDefinition(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            workflowService.deleteDefinition(id);
            res.json(ApiResponse.success("Workflow definition deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:list")
    public void listInstances(Request req, Response res) {
        try {
            String definitionId = req.getParameter("definitionId");
            List<WorkflowInstance> instances;
            if (definitionId != null && !definitionId.isEmpty()) {
                instances = workflowService.getInstancesByDefinition(definitionId);
            } else {
                instances = workflowService.getAllInstances();
            }
            List<Map<String, Object>> instanceList = instances.stream()
                .map(this::toInstanceMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(instanceList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:query")
    public void getInstance(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Optional<WorkflowInstance> instanceOpt = workflowService.getInstance(id);
            if (instanceOpt.isPresent()) {
                res.json(ApiResponse.success(toInstanceMap(instanceOpt.get())));
            } else {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Workflow instance not found"));
            }
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:start")
    public void startInstance(Request req, Response res) {
        try {
            String definitionId = req.getParameter("definitionId");
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) req.getAttribute("variables");
            
            WorkflowInstance instance = workflowService.startWorkflow(definitionId, variables);
            res.setStatus(201);
            res.json(ApiResponse.success("Workflow instance started successfully", toInstanceMap(instance)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:pause")
    public void pauseInstance(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            boolean success = workflowService.pauseWorkflow(id);
            if (success) {
                res.json(ApiResponse.success("Workflow instance paused successfully", null));
            } else {
                res.setStatus(400);
                res.json(ApiResponse.badRequest("Failed to pause workflow instance"));
            }
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:resume")
    public void resumeInstance(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            boolean success = workflowService.resumeWorkflow(id);
            if (success) {
                res.json(ApiResponse.success("Workflow instance resumed successfully", null));
            } else {
                res.setStatus(400);
                res.json(ApiResponse.badRequest("Failed to resume workflow instance"));
            }
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:cancel")
    public void cancelInstance(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            boolean success = workflowService.cancelWorkflow(id);
            if (success) {
                res.json(ApiResponse.success("Workflow instance cancelled successfully", null));
            } else {
                res.setStatus(400);
                res.json(ApiResponse.badRequest("Failed to cancel workflow instance"));
            }
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:retry")
    public void retryInstance(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            boolean success = workflowService.retryWorkflow(id);
            if (success) {
                res.json(ApiResponse.success("Workflow instance retried successfully", null));
            } else {
                res.setStatus(400);
                res.json(ApiResponse.badRequest("Failed to retry workflow instance"));
            }
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:variables")
    public void getInstanceVariables(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Map<String, Object> variables = workflowService.getInstanceVariables(id);
            res.json(ApiResponse.success(variables));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("workflow:instance:variables")
    public void setInstanceVariable(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String key = req.getParameter("key");
            Object value = req.getParameter("value");
            
            workflowService.setInstanceVariable(id, key, value);
            res.json(ApiResponse.success("Variable set successfully", null));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    private Map<String, Object> toDefinitionMap(WorkflowDefinition definition) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", definition.getId());
        map.put("name", definition.getName());
        map.put("description", definition.getDescription());
        map.put("version", definition.getVersion());
        map.put("createdAt", definition.getCreatedAt());
        return map;
    }
    
    private Map<String, Object> toInstanceMap(WorkflowInstance instance) {
        Map<String, Object> map = new HashMap<>();
        map.put("instanceId", instance.getInstanceId());
        map.put("definitionId", instance.getWorkflowDefinitionId());
        map.put("status", instance.getStatus().name());
        map.put("startTime", instance.getStartTime());
        map.put("endTime", instance.getEndTime());
        map.put("duration", instance.getDuration());
        map.put("currentNodeId", instance.getCurrentNodeId());
        return map;
    }
}
