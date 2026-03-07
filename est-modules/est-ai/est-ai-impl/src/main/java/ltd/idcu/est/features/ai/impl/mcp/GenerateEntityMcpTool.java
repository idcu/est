package ltd.idcu.est.features.ai.impl.mcp;

import ltd.idcu.est.features.ai.api.mcp.McpTool;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import ltd.idcu.est.features.ai.impl.skill.GenerateEntitySkill;

import java.util.List;
import java.util.Map;

public class GenerateEntityMcpTool implements McpTool {

    private final GenerateEntitySkill skill = new GenerateEntitySkill();

    @Override
    public String getName() {
        return "est_generate_entity";
    }

    @Override
    public String getDescription() {
        return "Generates a Java entity class with getters and setters using EST framework conventions";
    }

    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "className", Map.of(
                                "type", "string",
                                "description", "Name of the entity class"
                        ),
                        "packageName", Map.of(
                                "type", "string",
                                "description", "Package name (default: com.example.entity)"
                        ),
                        "fields", Map.of(
                                "type", "array",
                                "description", "List of fields in format 'name:type'"
                        )
                ),
                "required", List.of("className")
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
