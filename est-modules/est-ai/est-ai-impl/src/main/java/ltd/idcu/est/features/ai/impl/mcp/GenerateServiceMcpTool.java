package ltd.idcu.est.features.ai.impl.mcp;

import ltd.idcu.est.features.ai.api.mcp.McpTool;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import ltd.idcu.est.features.ai.impl.skill.GenerateServiceSkill;

import java.util.List;
import java.util.Map;

public class GenerateServiceMcpTool implements McpTool {

    private final GenerateServiceSkill skill = new GenerateServiceSkill();

    @Override
    public String getName() {
        return "est_generate_service";
    }

    @Override
    public String getDescription() {
        return "Generates a service interface and implementation using EST framework conventions";
    }

    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "serviceName", Map.of(
                                "type", "string",
                                "description", "Name of the service (without 'Service' suffix)"
                        ),
                        "packageName", Map.of(
                                "type", "string",
                                "description", "Package name (default: com.example.service)"
                        ),
                        "entityName", Map.of(
                                "type", "string",
                                "description", "Name of the entity class"
                        )
                ),
                "required", List.of("serviceName")
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
