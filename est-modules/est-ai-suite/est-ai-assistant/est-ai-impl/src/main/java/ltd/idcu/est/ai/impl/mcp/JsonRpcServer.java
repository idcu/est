package ltd.idcu.est.ai.impl.mcp;

import ltd.idcu.est.ai.api.mcp.McpServer;
import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRpcServer {

    private final McpServer mcpServer;

    public JsonRpcServer(McpServer mcpServer) {
        this.mcpServer = mcpServer;
    }

    public void startStdioServer() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out, true);

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String response = handleRequest(line);
            writer.println(response);
            writer.flush();
        }
    }

    public String handleRequest(String jsonRequest) {
        try {
            Map<String, Object> request = parseJson(jsonRequest);
            String method = (String) request.get("method");
            Map<String, Object> params = (Map<String, Object>) request.getOrDefault("params", new HashMap<>());
            Object id = request.get("id");

            Object result;
            switch (method) {
                case "initialize":
                    result = handleInitialize(params);
                    break;
                case "tools/list":
                    result = handleToolsList();
                    break;
                case "tools/call":
                    result = handleToolsCall(params);
                    break;
                default:
                    return createErrorResponse(id, -32601, "Method not found: " + method);
            }

            return createSuccessResponse(id, result);
        } catch (Exception e) {
            return createErrorResponse(null, -32603, "Internal error: " + e.getMessage());
        }
    }

    private Map<String, Object> handleInitialize(Map<String, Object> params) {
        Map<String, Object> serverInfo = new HashMap<>();
        serverInfo.put("name", mcpServer.getName());
        serverInfo.put("version", mcpServer.getVersion());

        Map<String, Object> capabilities = new HashMap<>();
        Map<String, Object> tools = new HashMap<>();
        tools.put("listChanged", false);
        capabilities.put("tools", tools);

        Map<String, Object> result = new HashMap<>();
        result.put("protocolVersion", "2024-11-05");
        result.put("server", serverInfo);
        result.put("capabilities", capabilities);
        return result;
    }

    private Map<String, Object> handleToolsList() {
        Map<String, Object> result = new HashMap<>();
        result.put("tools", mcpServer.getTools().stream().map(this::toolToMap).toList());
        return result;
    }

    private Map<String, Object> handleToolsCall(Map<String, Object> params) {
        String toolName = (String) params.get("name");
        Map<String, Object> arguments = (Map<String, Object>) params.getOrDefault("arguments", new HashMap<>());

        McpToolResult toolResult = mcpServer.callTool(toolName, arguments);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> contentItem = new HashMap<>();
        contentItem.put("type", "text");
        contentItem.put("text", toolResult.getContent());
        result.put("content", List.of(contentItem));
        result.put("isError", toolResult.isError());

        return result;
    }

    private Map<String, Object> toolToMap(McpTool tool) {
        Map<String, Object> toolMap = new HashMap<>();
        toolMap.put("name", tool.getName());
        toolMap.put("description", tool.getDescription());
        toolMap.put("inputSchema", tool.getInputSchema());
        return toolMap;
    }

    private String createSuccessResponse(Object id, Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("result", result);
        return toJson(response);
    }

    private String createErrorResponse(Object id, int code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);

        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("error", error);
        return toJson(response);
    }

    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new HashMap<>();
        json = json.trim();
        
        if (json.startsWith("{")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            for (String pair : pairs) {
                String[] kv = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
                if (kv.length == 2) {
                    String key = kv[0].trim().replace("\"", "");
                    String value = kv[1].trim();
                    
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        result.put(key, value.substring(1, value.length() - 1));
                    } else if (value.equals("true")) {
                        result.put(key, true);
                    } else if (value.equals("false")) {
                        result.put(key, false);
                    } else if (value.equals("null")) {
                        result.put(key, null);
                    } else if (value.startsWith("{")) {
                        result.put(key, parseJson(value));
                    } else {
                        try {
                            result.put(key, Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            try {
                                result.put(key, Double.parseDouble(value));
                            } catch (NumberFormatException e2) {
                                result.put(key, value);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(valueToJson(entry.getValue()));
        }
        
        sb.append("}");
        return sb.toString();
    }

    private String valueToJson(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeJson((String) value) + "\"";
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else if (value instanceof Map) {
            return toJson((Map<String, Object>) value);
        } else if (value instanceof List) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            List<?> list = (List<?>) value;
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(valueToJson(list.get(i)));
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + escapeJson(value.toString()) + "\"";
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    public static void main(String[] args) throws IOException {
        McpServer server = new DefaultMcpServer();
        JsonRpcServer jsonRpcServer = new JsonRpcServer(server);
        jsonRpcServer.startStdioServer();
    }
}
