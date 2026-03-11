package ltd.idcu.est.mcp.api;

import java.util.List;

public interface McpClient {
    
    void connect();
    
    void disconnect();
    
    boolean isConnected();
    
    List<McpTool> listTools();
    
    List<McpResource> listResources();
    
    List<McpPrompt> listPrompts();
    
    McpToolResult callTool(String toolName, Object arguments);
    
    McpResource readResource(String uri);
    
    String getServerInfo();
    
    void setServerUrl(String serverUrl);
    
    String getServerUrl();
}
