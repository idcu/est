package ltd.idcu.est.mcp.server;

import ltd.idcu.est.mcp.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DefaultMcpServer implements McpServer {
    
    private final Map<String, McpTool> tools = new ConcurrentHashMap<>();
    private final Map<String, McpResource> resources = new ConcurrentHashMap<>();
    private final Map<String, McpPrompt> prompts = new ConcurrentHashMap<>();
    private final Map<String, Function<Object, McpToolResult>> toolHandlers = new ConcurrentHashMap<>();
    
    private boolean running = false;
    private final String serverName;
    private final String serverVersion;
    
    public DefaultMcpServer() {
        this("EST MCP Server", "1.0.0");
    }
    
    public DefaultMcpServer(String serverName, String serverVersion) {
        this.serverName = serverName;
        this.serverVersion = serverVersion;
    }
    
    @Override
    public void registerTool(McpTool tool) {
        tools.put(tool.getName(), tool);
    }
    
    public void registerTool(McpTool tool, Function<Object, McpToolResult> handler) {
        tools.put(tool.getName(), tool);
        toolHandlers.put(tool.getName(), handler);
    }
    
    @Override
    public void registerResource(McpResource resource) {
        resources.put(resource.getUri(), resource);
    }
    
    @Override
    public void registerPrompt(McpPrompt prompt) {
        prompts.put(prompt.getName(), prompt);
    }
    
    @Override
    public void unregisterTool(String toolName) {
        tools.remove(toolName);
        toolHandlers.remove(toolName);
    }
    
    @Override
    public void unregisterResource(String uri) {
        resources.remove(uri);
    }
    
    @Override
    public void unregisterPrompt(String promptName) {
        prompts.remove(promptName);
    }
    
    @Override
    public List<McpTool> listTools() {
        return new ArrayList<>(tools.values());
    }
    
    @Override
    public List<McpResource> listResources() {
        return new ArrayList<>(resources.values());
    }
    
    @Override
    public List<McpPrompt> listPrompts() {
        return new ArrayList<>(prompts.values());
    }
    
    @Override
    public McpToolResult callTool(String toolName, Object arguments) {
        Function<Object, McpToolResult> handler = toolHandlers.get(toolName);
        if (handler != null) {
            try {
                return handler.apply(arguments);
            } catch (Exception e) {
                McpToolResult result = new McpToolResult(false);
                result.setError("Tool execution failed: " + e.getMessage());
                return result;
            }
        }
        
        McpToolResult result = new McpToolResult(false);
        result.setError("Tool not found: " + toolName);
        return result;
    }
    
    @Override
    public McpResource readResource(String uri) {
        return resources.get(uri);
    }
    
    @Override
    public String getServerInfo() {
        return String.format("{\"name\":\"%s\",\"version\":\"%s\"}", serverName, serverVersion);
    }
    
    @Override
    public void start() {
        if (!running) {
            running = true;
            System.out.println(serverName + " v" + serverVersion + " started");
        }
    }
    
    @Override
    public void stop() {
        if (running) {
            running = false;
            System.out.println(serverName + " stopped");
        }
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
}
