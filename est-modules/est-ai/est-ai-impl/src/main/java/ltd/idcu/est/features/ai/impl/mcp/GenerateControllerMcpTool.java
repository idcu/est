package ltd.idcu.est.features.ai.impl.mcp;

import ltd.idcu.est.features.ai.api.mcp.McpTool;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import ltd.idcu.est.features.ai.impl.skill.GenerateControllerSkill;

import java.util.List;
import java.util.Map;

public class GenerateControllerMcpTool implements McpTool {

    private final GenerateControllerSkill skill = new GenerateControllerSkill();

    @Override
    public String getName() {
        return "est_generate_controller";
    }

    @Override
    public String getDescription() {
        return "Generates a REST API controller with CRUD operations using EST Web framework";
    }

    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "controllerName", Map.of(
                                "type", "string",
                                "description", "Name of the controller (without 'Controller' suffix)"
                        ),
                        "packageName", Map.of(
                                "type", "string",
                                "description", "Package name (default: com.example.controller)"
                        )
                ),
                "required", List.of("controllerName")
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        SkillResult result = skill.execute(arguments);
        if (result.isSuccess()) {
            String code = (String) result.getOutputs().get("code");
            return McpToolResult.success(code);
        } else {
            return McpToolResult.error(result.getMessage());
        }
    }
}
