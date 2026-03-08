package ltd.idcu.est.ai.impl.mcp;

import ltd.idcu.est.ai.api.mcp.*;
import ltd.idcu.est.ai.impl.Ai;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMcpServer implements McpServer {

    private final Map<String, McpTool> tools = new ConcurrentHashMap<>();

    public DefaultMcpServer() {
        initializeDefaultTools();
    }

    private void initializeDefaultTools() {
        registerTool(new GenerateEntityMcpTool());
        registerTool(new GenerateServiceMcpTool());
        registerTool(new GenerateControllerMcpTool());
        registerTool(new CodeReviewMcpTool());
        registerTool(new GetQuickReferenceMcpTool());
    }

    @Override
    public String getName() {
        return "est-mcp-server";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public List<McpTool> getTools() {
        return new ArrayList<>(tools.values());
    }

    @Override
    public void registerTool(McpTool tool) {
        tools.put(tool.getName(), tool);
    }

    @Override
    public void unregisterTool(String toolName) {
        tools.remove(toolName);
    }

    @Override
    public McpToolResult callTool(String toolName, Map<String, Object> arguments) {
        McpTool tool = tools.get(toolName);
        if (tool == null) {
            return McpToolResult.error("Tool not found: " + toolName);
        }

        try {
            return tool.execute(arguments);
        } catch (Exception e) {
            return McpToolResult.error("Error executing tool: " + e.getMessage());
        }
    }

    public String toJsonSchema() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"server\": {\n");
        sb.append("    \"name\": \"").append(getName()).append("\",\n");
        sb.append("    \"version\": \"").append(getVersion()).append("\"\n");
        sb.append("  },\n");
        sb.append("  \"tools\": [\n");
        
        List<McpTool> toolList = new ArrayList<>(tools.values());
        for (int i = 0; i < toolList.size(); i++) {
            McpTool tool = toolList.get(i);
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(tool.getName()).append("\",\n");
            sb.append("      \"description\": \"").append(tool.getDescription()).append("\",\n");
            sb.append("      \"inputSchema\": ").append(mapToJson(tool.getInputSchema())).append("\n");
            sb.append("    }");
            if (i < toolList.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        List<Map.Entry<String, Object>> entries = new ArrayList<>(map.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Object> entry = entries.get(i);
            sb.append("\"").append(entry.getKey()).append("\": \"");
            sb.append(entry.getValue().toString().replace("\"", "\\\""));
            sb.append("\"");
            if (i < entries.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
