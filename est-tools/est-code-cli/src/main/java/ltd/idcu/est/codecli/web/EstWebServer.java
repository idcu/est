package ltd.idcu.est.codecli.web;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.codecli.config.CliConfig;
import ltd.idcu.est.codecli.mcp.EstCodeCliMcpServer;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import ltd.idcu.est.codecli.skills.SkillManager;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EstWebServer {
    
    private final int port;
    private final String workDir;
    private final AiAssistant aiAssistant;
    private final CliConfig config;
    private final FileIndex fileIndex;
    private final ProjectIndexer indexer;
    private final SkillManager skillManager;
    private final PromptLibrary promptLibrary;
    private final EstCodeCliMcpServer mcpServer;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running;
    
    public EstWebServer(int port, String workDir) {
        this.port = port;
        this.workDir = workDir;
        this.aiAssistant = new DefaultAiAssistant();
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
        
        System.out.println("EST Code CLI Web Server started on http://localhost:" + port);
        System.out.println("Available API endpoints:");
        System.out.println("  GET  /api/status      - Server status");
        System.out.println("  POST /api/chat        - Chat with AI");
        System.out.println("  GET  /api/tools       - List MCP tools");
        System.out.println("  POST /api/tools/call  - Call MCP tool");
        System.out.println("  GET  /api/skills      - List EST skills");
        System.out.println("  GET  /api/templates   - List prompt templates");
        System.out.println("  POST /api/search      - Search files");
        System.out.println("  GET  /api/config      - Get config");
        System.out.println("  POST /api/config      - Update config");
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
            String requestLine = reader.readLine();
            if (requestLine == null) {
                return;
            }
            
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                sendError(writer, 400, "Bad Request");
                return;
            }
            
            String method = parts[0];
            String path = parts[1];
            
            if (path.startsWith("/api/")) {
                handleApiRequest(method, path, reader, writer, output);
            } else if ("GET".equals(method)) {
                handleGet(path, writer, output);
            } else {
                sendError(writer, 405, "Method Not Allowed");
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
    
    private void handleApiRequest(String method, String path, BufferedReader reader, 
                                    PrintWriter writer, OutputStream output) throws IOException {
        String apiPath = path.substring("/api".length());
        
        if ("GET".equals(method) && "/status".equals(apiPath)) {
            handleApiStatus(writer, output);
        } else if ("POST".equals(method) && "/chat".equals(apiPath)) {
            handleApiChat(reader, writer, output);
        } else if ("GET".equals(method) && "/tools".equals(apiPath)) {
            handleApiListTools(writer, output);
        } else if ("POST".equals(method) && "/tools/call".equals(apiPath)) {
            handleApiCallTool(reader, writer, output);
        } else if ("GET".equals(method) && "/skills".equals(apiPath)) {
            handleApiListSkills(writer, output);
        } else if ("GET".equals(method) && "/templates".equals(apiPath)) {
            handleApiListTemplates(writer, output);
        } else if ("POST".equals(method) && "/search".equals(apiPath)) {
            handleApiSearch(reader, writer, output);
        } else if ("GET".equals(method) && "/config".equals(apiPath)) {
            handleApiGetConfig(writer, output);
        } else if ("POST".equals(method) && "/config".equals(apiPath)) {
            handleApiUpdateConfig(reader, writer, output);
        } else {
            sendJsonError(writer, output, 404, "API endpoint not found: " + path);
        }
    }
    
    private void handleApiStatus(PrintWriter writer, OutputStream output) throws IOException {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("server", "est-code-cli-web");
        status.put("version", "1.1.0");
        status.put("status", "running");
        status.put("workDir", workDir);
        
        sendJsonResponse(writer, output, ApiResponse.success(status));
    }
    
    private void handleApiChat(BufferedReader reader, PrintWriter writer, 
                                  OutputStream output) throws IOException {
        String body = readRequestBody(reader);
        String message = extractJsonValue(body, "message");
        
        if (message == null || message.isEmpty()) {
            sendJsonError(writer, output, 400, "Message is required");
            return;
        }
        
        try {
            String response = aiAssistant.chat(message);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("response", response);
            sendJsonResponse(writer, output, ApiResponse.success(data));
        } catch (Exception e) {
            sendJsonError(writer, output, 500, "Error processing chat: " + e.getMessage());
        }
    }
    
    private void handleApiListTools(PrintWriter writer, OutputStream output) throws IOException {
        List<Map<String, Object>> tools = new ArrayList<>();
        mcpServer.getTools().forEach(tool -> {
            Map<String, Object> toolInfo = new LinkedHashMap<>();
            toolInfo.put("name", tool.getName());
            toolInfo.put("description", tool.getDescription());
            tools.add(toolInfo);
        });
        
        sendJsonResponse(writer, output, ApiResponse.success(tools));
    }
    
    private void handleApiCallTool(BufferedReader reader, PrintWriter writer, 
                                      OutputStream output) throws IOException {
        String body = readRequestBody(reader);
        String toolName = extractJsonValue(body, "name");
        
        if (toolName == null || toolName.isEmpty()) {
            sendJsonError(writer, output, 400, "Tool name is required");
            return;
        }
        
        Map<String, Object> arguments = parseArguments(body);
        
        try {
            var result = mcpServer.callTool(toolName, arguments);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("success", result.isSuccess());
            data.put("content", result.getContent());
            data.put("metadata", result.getMetadata());
            sendJsonResponse(writer, output, ApiResponse.success(data));
        } catch (Exception e) {
            sendJsonError(writer, output, 500, "Error calling tool: " + e.getMessage());
        }
    }
    
    private void handleApiListSkills(PrintWriter writer, OutputStream output) throws IOException {
        List<Map<String, Object>> skills = new ArrayList<>();
        skillManager.getAllSkills().forEach(skill -> {
            Map<String, Object> skillInfo = new LinkedHashMap<>();
            skillInfo.put("name", skill.getName());
            skillInfo.put("description", skill.getDescription());
            skills.add(skillInfo);
        });
        
        sendJsonResponse(writer, output, ApiResponse.success(skills));
    }
    
    private void handleApiListTemplates(PrintWriter writer, OutputStream output) throws IOException {
        List<Map<String, Object>> templates = new ArrayList<>();
        promptLibrary.getAllTemplates().forEach(template -> {
            Map<String, Object> templateInfo = new LinkedHashMap<>();
            templateInfo.put("name", template.getName());
            templateInfo.put("description", template.getDescription());
            templates.add(templateInfo);
        });
        
        sendJsonResponse(writer, output, ApiResponse.success(templates));
    }
    
    private void handleApiSearch(BufferedReader reader, PrintWriter writer, 
                                   OutputStream output) throws IOException {
        String body = readRequestBody(reader);
        String query = extractJsonValue(body, "query");
        String limitStr = extractJsonValue(body, "limit");
        int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;
        
        if (query == null || query.isEmpty()) {
            sendJsonError(writer, output, 400, "Query is required");
            return;
        }
        
        List<Map<String, Object>> results = new ArrayList<>();
        var searchResults = fileIndex.search(query, limit);
        searchResults.forEach(result -> {
            Map<String, Object> resultInfo = new LinkedHashMap<>();
            resultInfo.put("path", result.getFilePath());
            resultInfo.put("score", result.getScore());
            resultInfo.put("snippet", result.getSnippet());
            results.add(resultInfo);
        });
        
        sendJsonResponse(writer, output, ApiResponse.success(results));
    }
    
    private void handleApiGetConfig(PrintWriter writer, OutputStream output) throws IOException {
        Map<String, Object> configData = new LinkedHashMap<>();
        configData.put("nickname", config.getNickname());
        configData.put("workDir", config.getWorkDir());
        configData.put("planningMode", config.isPlanningMode());
        configData.put("hitlEnabled", config.isHitlEnabled());
        
        sendJsonResponse(writer, output, ApiResponse.success(configData));
    }
    
    private void handleApiUpdateConfig(BufferedReader reader, PrintWriter writer, 
                                          OutputStream output) throws IOException {
        String body = readRequestBody(reader);
        
        String nickname = extractJsonValue(body, "nickname");
        String planningModeStr = extractJsonValue(body, "planningMode");
        String hitlEnabledStr = extractJsonValue(body, "hitlEnabled");
        
        if (nickname != null) {
            config.setNickname(nickname);
        }
        if (planningModeStr != null) {
            config.setPlanningMode(Boolean.parseBoolean(planningModeStr));
        }
        if (hitlEnabledStr != null) {
            config.setHitlEnabled(Boolean.parseBoolean(hitlEnabledStr));
        }
        
        try {
            config.save();
            handleApiGetConfig(writer, output);
        } catch (IOException e) {
            sendJsonError(writer, output, 500, "Error saving config: " + e.getMessage());
        }
    }
    
    private String readRequestBody(BufferedReader reader) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        int contentLength = 0;
        
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.substring("content-length:".length()).trim());
            }
        }
        
        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            reader.read(buffer, 0, contentLength);
            body.append(buffer);
        }
        
        return body.toString();
    }
    
    private Map<String, Object> parseArguments(String body) {
        Map<String, Object> args = new HashMap<>();
        String argsKey = "\"arguments\":";
        int argsIndex = body.indexOf(argsKey);
        if (argsIndex != -1) {
            int start = argsIndex + argsKey.length();
            int depth = 0;
            int end = start;
            for (int i = start; i < body.length(); i++) {
                char c = body.charAt(i);
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
                String argsJson = body.substring(start, end);
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
    
    private void sendJsonResponse(PrintWriter writer, OutputStream output, ApiResponse response) throws IOException {
        String json = response.toJson();
        byte[] content = json.getBytes(StandardCharsets.UTF_8);
        
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: application/json; charset=UTF-8");
        writer.println("Content-Length: " + content.length);
        writer.println();
        writer.flush();
        
        output.write(content);
        output.flush();
    }
    
    private void sendJsonError(PrintWriter writer, OutputStream output, int code, String message) throws IOException {
        ApiResponse response = ApiResponse.error(message);
        String json = response.toJson();
        byte[] content = json.getBytes(StandardCharsets.UTF_8);
        
        writer.println("HTTP/1.1 " + code + " " + getStatusMessage(code));
        writer.println("Content-Type: application/json; charset=UTF-8");
        writer.println("Content-Length: " + content.length);
        writer.println();
        writer.flush();
        
        output.write(content);
        output.flush();
    }
    
    private String getStatusMessage(int code) {
        switch (code) {
            case 400: return "Bad Request";
            case 404: return "Not Found";
            case 405: return "Method Not Allowed";
            case 500: return "Internal Server Error";
            default: return "Error";
        }
    }
    
    private void handleGet(String path, PrintWriter writer, OutputStream output) throws IOException {
        if ("/".equals(path)) {
            path = "/index.html";
        }
        
        String resourcePath = "/web" + path;
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
        
        if (resourceStream != null) {
            byte[] content = readAllBytes(resourceStream);
            String contentType = getContentType(path);
            
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: " + contentType);
            writer.println("Content-Length: " + content.length);
            writer.println();
            writer.flush();
            
            output.write(content);
            output.flush();
        } else {
            sendError(writer, 404, "Not Found");
        }
    }
    
    private byte[] readAllBytes(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        } else if (path.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        } else if (path.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        } else if (path.endsWith(".json")) {
            return "application/json; charset=UTF-8";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else if (path.endsWith(".svg")) {
            return "image/svg+xml";
        }
        return "text/plain; charset=UTF-8";
    }
    
    private void sendError(PrintWriter writer, int code, String message) {
        writer.println("HTTP/1.1 " + code + " " + getStatusMessage(code));
        writer.println("Content-Type: text/plain; charset=UTF-8");
        writer.println();
        writer.println(message);
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
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        String workDir = args.length > 1 ? args[1] : System.getProperty("user.dir");
        
        try {
            EstWebServer server = new EstWebServer(port, workDir);
            
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
