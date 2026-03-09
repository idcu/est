package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.skills.SkillManager;

import java.util.HashMap;
import java.util.Map;

public class ListSkillsMcpTool implements McpTool {
    
    private final SkillManager skillManager;
    
    public ListSkillsMcpTool(SkillManager skillManager) {
        this.skillManager = skillManager;
    }
    
    @Override
    public String getName() {
        return "est_list_skills";
    }
    
    @Override
    public String getDescription() {
        return "Lists all available EST skills";
    }
    
    @Override
    public String getInputSchema() {
        return "{\"type\":\"object\",\"properties\":{}}";
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> args) {
        try {
            String skillsList = skillManager.listSkills();
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("skillCount", skillManager.getAllSkills().size());
            
            return McpToolResult.success(skillsList, metadata);
        } catch (Exception e) {
            return McpToolResult.error("Failed to list skills: " + e.getMessage());
        }
    }
}
