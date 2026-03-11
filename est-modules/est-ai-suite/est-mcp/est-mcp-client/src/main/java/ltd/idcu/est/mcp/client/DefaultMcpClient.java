package ltd.idcu.est.mcp.client;

import ltd.idcu.est.mcp.api.*;

import java.util.ArrayList;
import java.util.List;

public class DefaultMcpClient implements McpClient {
    
    private McpServer server;
    private String serverUrl;
    private boolean connected = false;
    
    public DefaultMcpClient() {
    }
    
    public DefaultMcpClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    
    public DefaultMcpClient(McpServer server) {
        this.server = server;
    }
    
    public void setServer(McpServer server) {
        this.server = server;
    }
    
    @Override
    public void connect() {
        if (server != null && !server.isRunning()) {
            server.start();
        }
        connected = true;
    }
    
    @Override
    public void disconnect() {
        if (server != null && server.isRunning()) {
            server.stop();
        }
        connected = false;
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public List<McpTool> listTools() {
        if (server != null) {
            return server.listTools();
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<McpResource> listResources() {
        if (server != null) {
            return server.listResources();
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<McpPrompt> listPrompts() {
        if (server != null) {
            return server.listPrompts();
        }
        return new ArrayList<>();
    }
    
    @Override
    public McpToolResult callTool(String toolName, Object arguments) {
        if (server != null) {
            return server.callTool(toolName, arguments);
        }
        McpToolResult result = new McpToolResult(false);
        result.setError("No server connected");
        return result;
    }
    
    @Override
    public McpResource readResource(String uri) {
        if (server != null) {
            return server.readResource(uri);
        }
        return null;
    }
    
    @Override
    public String getServerInfo() {
        if (server != null) {
            return server.getServerInfo();
        }
        return "{\"error\":\"No server connected\"}";
    }
    
    @Override
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    
    @Override
    public String getServerUrl() {
        return serverUrl;
    }
}
