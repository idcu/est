package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.AiAssistantService;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiController {
    
    private final AiAssistantService aiAssistantService;
    
    public AiController() {
        this.aiAssistantService = Admin.createAiAssistantService();
    }
    
    @RequirePermission("ai:chat")
    public void chat(Request req, Response res) {
        try {
            String message = req.getParameter("message");
            String response = aiAssistantService.chat(message);
            res.json(ApiResponse.success(response));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:code:generate")
    public void generateCode(Request req, Response res) {
        try {
            String requirement = req.getParameter("requirement");
            String code = aiAssistantService.generateCode(requirement);
            res.json(ApiResponse.success(code));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:code:suggest")
    public void suggestCode(Request req, Response res) {
        try {
            String requirement = req.getParameter("requirement");
            String suggestion = aiAssistantService.suggestCode(requirement);
            res.json(ApiResponse.success(suggestion));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:code:explain")
    public void explainCode(Request req, Response res) {
        try {
            String code = req.getParameter("code");
            String explanation = aiAssistantService.explainCode(code);
            res.json(ApiResponse.success(explanation));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:code:optimize")
    public void optimizeCode(Request req, Response res) {
        try {
            String code = req.getParameter("code");
            String optimizedCode = aiAssistantService.optimizeCode(code);
            res.json(ApiResponse.success(optimizedCode));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:reference")
    public void getQuickReference(Request req, Response res) {
        try {
            String topic = req.getParameter("topic");
            String reference = aiAssistantService.getQuickReference(topic);
            res.json(ApiResponse.success(reference));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:bestpractice")
    public void getBestPractice(Request req, Response res) {
        try {
            String category = req.getParameter("category");
            String practice = aiAssistantService.getBestPractice(category);
            res.json(ApiResponse.success(practice));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:tutorial")
    public void getTutorial(Request req, Response res) {
        try {
            String topic = req.getParameter("topic");
            String tutorial = aiAssistantService.getTutorial(topic);
            res.json(ApiResponse.success(tutorial));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:template:list")
    public void listTemplates(Request req, Response res) {
        try {
            List<PromptTemplate> templates = aiAssistantService.getTemplates();
            List<Map<String, Object>> templateList = templates.stream()
                .map(this::toTemplateMap)
                .collect(java.util.stream.Collectors.toList());
            res.json(ApiResponse.success(templateList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("ai:template:generate")
    public void generatePrompt(Request req, Response res) {
        try {
            String templateName = req.getParameter("templateName");
            
            Map<String, String> variables = new HashMap<>();
            String variableNames = req.getParameter("variableNames");
            String variableValues = req.getParameter("variableValues");
            
            if (variableNames != null && variableValues != null) {
                String[] names = variableNames.split(",");
                String[] values = variableValues.split(",");
                if (names.length == values.length) {
                    for (int i = 0; i < names.length; i++) {
                        variables.put(names[i].trim(), values[i].trim());
                    }
                }
            }
            
            String prompt = aiAssistantService.generatePrompt(templateName, variables);
            res.json(ApiResponse.success(prompt));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toTemplateMap(PromptTemplate template) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", template.getName());
        map.put("category", template.getCategory());
        map.put("description", template.getDescription());
        map.put("template", template.getTemplate());
        map.put("requiredVariables", template.getRequiredVariables());
        return map;
    }
}
