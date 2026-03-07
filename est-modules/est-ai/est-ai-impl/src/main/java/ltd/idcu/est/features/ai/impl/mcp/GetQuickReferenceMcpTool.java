package ltd.idcu.est.features.ai.impl.mcp;

import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.mcp.McpTool;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

import java.util.List;
import java.util.Map;

public class GetQuickReferenceMcpTool implements McpTool {

    private final AiAssistant aiAssistant = new DefaultAiAssistant();

    @Override
    public String getName() {
        return "est_get_quick_reference";
    }

    @Override
    public String getDescription() {
        return "Gets quick reference documentation for EST framework features";
    }

    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "topic", Map.of(
                                "type", "string",
                                "description", "Topic to get reference for (web, config, collection, di)"
                        )
                ),
                "required", List.of("topic")
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        String topic = (String) arguments.get("topic");
        String reference = aiAssistant.getQuickReference(topic);
        return McpToolResult.success(reference);
    }
}
