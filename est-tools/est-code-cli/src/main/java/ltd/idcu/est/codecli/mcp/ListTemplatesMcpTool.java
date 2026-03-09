package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.prompts.PromptLibrary;

import java.util.HashMap;
import java.util.Map;

public class ListTemplatesMcpTool implements McpTool {
    
    private final PromptLibrary promptLibrary;
    
    public ListTemplatesMcpTool(PromptLibrary promptLibrary) {
        this.promptLibrary = promptLibrary;
    }
    
    @Override
    public String getName() {
        return "est_list_templates";
    }
    
    @Override
    public String getDescription() {
        return "Lists all available prompt templates";
    }
    
    @Override
    public String getInputSchema() {
        return "{\"type\":\"object\",\"properties\":{}}";
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> args) {
        try {
            String templatesList = promptLibrary.listTemplates();
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("templateCount", promptLibrary.getAllTemplates().size());
            
            return McpToolResult.success(templatesList, metadata);
        } catch (Exception e) {
            return McpToolResult.error("Failed to list templates: " + e.getMessage());
        }
    }
}
