package ltd.idcu.est.codecli.acp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.config.CliConfig;
import ltd.idcu.est.codecli.mcp.EstCodeCliMcpServer;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import ltd.idcu.est.codecli.skills.SkillManager;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcpServer {
    
    private final int port;
    private final String workDir;
    private final CliConfig config;
    private final FileIndex fileIndex;
    private final ProjectIndexer indexer;
    private final SkillManager skillManager;
    private final PromptLibrary promptLibrary;
    private final EstCodeCliMcpServer mcpServer;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running;
    
    public AcpServer(int port, String workDir) {
        this.port = port;
        this.workDir = workDir;
        this.config = CliConfig.load();
        this.fileIndex = new FileIndex();
        this.indexer = new ProjectIndexer(fileIndex);
        this.skillManager = new SkillManager();
        this.promptLibrary = new PromptLibrary();
        this.mcpServer = new EstCodeCliMcpServer(workDir, fileIndex, indexer, skillManager, promptLibrary);
        this.executorService = Executors.newCachedThreadPool();
    }
    
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        
        System.out.println("EST Code CLI ACP Server started on port " + port);
        System.out.println("Available tools: " + mcpServer.getTools().size() + " MCP tools");
        System.out.println("Press Ctrl+C to stop");
        
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try (
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String response = handleMessage(line);
                writer.println(response);
            }
            
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }
    }
    
    private String handleMessage(String message) {
        try {
            if (message.startsWith("{") && message.endsWith("}")) {
                return handleJsonMessage(message);
            } else {
                return createErrorResponse("Invalid message format");
            }
        } catch (Exception e) {
            return createErrorResponse("Error processing message: " + e.getMessage());
        }
    }
    
    private String handleJsonMessage(String json) {
        String method = extractJsonValue(json, "method");
        String id = extractJsonValue(json, "id");
        
        if (method == null) {
            return createErrorResponse("Missing method");
        }
        
        switch (method) {
            case "initialize":
                return handleInitialize(id);
            case "ping":
                return handlePing(id);
            case "tools/list":
                return handleListTools(id);
            case "tools/call":
                return handleCallTool(json, id);
            default:
                return createErrorResponse("Unknown method: " + method);
        }
    }
    
    private String handleInitialize(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"jsonrpc\":\"2.0\",");
        sb.append("\"result\":{");
        sb.append("\"serverInfo\":{\"name\":\"est-code-cli-acp\",\"version\":\"1.1.0\"},");
        sb.append("\"capabilities\":{\"tools\":{}}");
        sb.append("}");
        if (id != null) {
            sb.append(",\"id\":\"").append(escapeJson(id)).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String handlePing(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"jsonrpc\":\"2.0\",");
        sb.append("\"result\":{}");
        if (id != null) {
            sb.append(",\"id\":\"").append(escapeJson(id)).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String handleListTools(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"jsonrpc\":\"2.0\",");
        sb.append("\"result\":{\"tools\":[");
        
        List<McpTool> tools = mcpServer.getTools();
        for (int i = 0; i < tools.size(); i++) {
            McpTool tool = tools.get(i);
            if (i > 0) sb.append(",");
            sb.append("{");
            sb.append("\"name\":\"").append(escapeJson(tool.getName())).append("\",");
            sb.append("\"description\":\"").append(escapeJson(tool.getDescription())).append("\"");
            sb.append("}");
        }
        
        sb.append("]}");
        if (id != null) {
            sb.append(",\"id\":\"").append(escapeJson(id)).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String handleCallTool(String json, String id) {
        String toolName = extractJsonValue(json, "name");
        
        if (toolName == null || toolName.isEmpty()) {
            return createErrorResponse("Missing tool name");
        }
        
        Map<String, Object> arguments = parseArguments(json);
        
        try {
            McpToolResult result = mcpServer.callTool(toolName, arguments);
            
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"jsonrpc\":\"2.0\",");
            sb.append("\"result\":{");
            sb.append("\"content\":[");
            sb.append("{\"type\":\"text\",\"text\":\"").append(escapeJson(result.getContent())).append("\"}");
            sb.append("]");
            sb.append("}");
            if (id != null) {
                sb.append(",\"id\":\"").append(escapeJson(id)).append("\"");
            }
            sb.append("}");
            return sb.toString();
            
        } catch (Exception e) {
            return createErrorResponse("Error calling tool: " + e.getMessage());
        }
    }
    
    private Map<String, Object> parseArguments(String json) {
        Map<String, Object> args = new HashMap<>();
        String argsKey = "\"arguments\":";
        int argsIndex = json.indexOf(argsKey);
        if (argsIndex != -1) {
            int start = argsIndex + argsKey.length();
            int depth = 0;
            int end = start;
            for (int i = start; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '{') depth++;
                else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        end = i + 1;
                        break;
                    }
                }
            }
            if (end > start) {
                String argsJson = json.substring(start, end);
                String[] pairs = argsJson.replace("{", "").replace("}", "").split(",");
                for (String pair : pairs) {
                    String[] kv = pair.split(":", 2);
                    if (kv.length == 2) {
                        String key = kv[0].trim().replace("\"", "");
                        String value = kv[1].trim().replace("\"", "");
                        args.put(key, value);
                    }
                }
            }
        }
        return args;
    }
    
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) {
            return null;
        }
        
        int valueStart = keyIndex + searchKey.length();
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        
        if (valueStart >= json.length()) {
            return null;
        }
        
        char firstChar = json.charAt(valueStart);
        if (firstChar == '\"') {
            int valueEnd = json.indexOf('\"', valueStart + 1);
            if (valueEnd != -1) {
                return json.substring(valueStart + 1, valueEnd);
            }
        } else {
            int valueEnd = json.indexOf(',', valueStart);
            if (valueEnd == -1) {
                valueEnd = json.indexOf('}', valueStart);
            }
            if (valueEnd != -1) {
                return json.substring(valueStart, valueEnd).trim();
            }
        }
        
        return null;
    }
    
    private String createErrorResponse(String message) {
        return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"" + escapeJson(message) + "\"},\"id\":null}";
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
        executorService.shutdown();
    }
    
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 3000;
        String workDir = args.length > 1 ? args[1] : System.getProperty("user.dir");
        
        try {
            AcpServer server = new AcpServer(port, workDir);
            
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start ACP server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
