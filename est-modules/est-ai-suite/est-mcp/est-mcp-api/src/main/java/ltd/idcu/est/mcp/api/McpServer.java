package ltd.idcu.est.mcp.api;

import java.util.List;

public interface McpServer {
    
    void registerTool(McpTool tool);
    
    void registerResource(McpResource resource);
    
    void registerPrompt(McpPrompt prompt);
    
    void unregisterTool(String toolName);
    
    void unregisterResource(String uri);
    
    void unregisterPrompt(String promptName);
    
    List<McpTool> listTools();
    
    List<McpResource> listResources();
    
    List<McpPrompt> listPrompts();
    
    McpToolResult callTool(String toolName, Object arguments);
    
    McpResource readResource(String uri);
    
    String getServerInfo();
    
    void start();
    
    void stop();
    
    boolean isRunning();
}
