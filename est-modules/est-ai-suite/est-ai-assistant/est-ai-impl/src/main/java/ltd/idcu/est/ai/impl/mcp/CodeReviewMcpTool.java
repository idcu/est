package ltd.idcu.est.ai.impl.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.ai.api.skill.SkillResult;
import ltd.idcu.est.ai.impl.skill.CodeReviewSkill;

import java.util.List;
import java.util.Map;

public class CodeReviewMcpTool implements McpTool {

    private final CodeReviewSkill skill = new CodeReviewSkill();

    @Override
    public String getName() {
        return "est_code_review";
    }

    @Override
    public String getDescription() {
        return "Performs a basic code review and provides improvement suggestions";
    }

    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "code", Map.of(
                                "type", "string",
                                "description", "The Java code to review"
                        )
                ),
                "required", List.of("code")
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public McpToolResult execute(Map<String, Object> arguments) {
        SkillResult result = skill.execute(arguments);
        if (result.isSuccess()) {
            List<String> issues = (List<String>) result.getOutputs().get("issues");
            List<String> suggestions = (List<String>) result.getOutputs().get("suggestions");
            Integer score = (Integer) result.getOutputs().get("score");

            StringBuilder content = new StringBuilder();
            content.append("=== Code Review Result ===\n\n");
            content.append("Quality Score: ").append(score).append("/100\n\n");

            if (!issues.isEmpty()) {
                content.append("Issues Found:\n");
                for (String issue : issues) {
                    content.append("- ").append(issue).append("\n");
                }
                content.append("\n");
            }

            if (!suggestions.isEmpty()) {
                content.append("Suggestions:\n");
                for (String suggestion : suggestions) {
                    content.append("- ").append(suggestion).append("\n");
                }
            }

            return McpToolResult.success(content.toString(), result.getOutputs());
        } else {
            return McpToolResult.error(result.getMessage());
        }
    }
}
