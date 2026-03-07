package ltd.idcu.est.features.ai.api.mcp;

import java.util.Map;

public interface McpTool {

    String getName();

    String getDescription();

    Map<String, Object> getInputSchema();

    McpToolResult execute(Map<String, Object> arguments);
}
