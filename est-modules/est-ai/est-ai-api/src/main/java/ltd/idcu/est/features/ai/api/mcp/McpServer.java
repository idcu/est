package ltd.idcu.est.features.ai.api.mcp;

import java.util.List;

public interface McpServer {

    String getName();

    String getVersion();

    List<McpTool> getTools();

    void registerTool(McpTool tool);

    void unregisterTool(String toolName);

    McpToolResult callTool(String toolName, java.util.Map<String, Object> arguments);
}
