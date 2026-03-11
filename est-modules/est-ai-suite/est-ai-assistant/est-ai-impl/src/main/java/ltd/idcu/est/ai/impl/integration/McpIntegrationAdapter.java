package ltd.idcu.est.ai.impl.integration;

import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.server.DefaultMcpServer;
import ltd.idcu.est.mcp.client.DefaultMcpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class McpIntegrationAdapter {
    
    private final McpServer mcpServer;
    private final McpClient mcpClient;
    
    public McpIntegrationAdapter() {
        this(new DefaultMcpServer(), new DefaultMcpClient());
    }
    
    public McpIntegrationAdapter(McpServer mcpServer, McpClient mcpClient) {
        this.mcpServer = mcpServer;
        this.mcpClient = mcpClient;
    }
    
    public void registerTool(String name, String description, McpToolExecutor executor) {
        McpTool tool = new SimpleMcpTool(name, description, executor);
        mcpServer.registerTool(tool);
    }
    
    public void registerResource(String uri, String name, String content) {
        McpResource resource = new SimpleMcpResource(uri, name, content);
        mcpServer.registerResource(resource);
    }
    
    public void registerPrompt(String name, String description, String template) {
        McpPrompt prompt = new SimpleMcpPrompt(name, description, template);
        mcpServer.registerPrompt(prompt);
    }
    
    public List<ToolInfo> listTools() {
        List<McpTool> tools = mcpServer.listTools();
        List<ToolInfo> infos = new ArrayList<>();
        for (McpTool tool : tools) {
            infos.add(new ToolInfo(tool.getName(), tool.getDescription()));
        }
        return infos;
    }
    
    public List<ResourceInfo> listResources() {
        List<McpResource> resources = mcpServer.listResources();
        List<ResourceInfo> infos = new ArrayList<>();
        for (McpResource resource : resources) {
            infos.add(new ResourceInfo(resource.getUri(), resource.getName()));
        }
        return infos;
    }
    
    public List<PromptInfo> listPrompts() {
        List<McpPrompt> prompts = mcpServer.listPrompts();
        List<PromptInfo> infos = new ArrayList<>();
        for (McpPrompt prompt : prompts) {
            infos.add(new PromptInfo(prompt.getName(), prompt.getDescription()));
        }
        return infos;
    }
    
    public ToolResult callTool(String toolName, Map<String, Object> arguments) {
        McpToolResult result = mcpServer.callTool(toolName, arguments);
        return new ToolResult(result.isSuccess(), result.getContent(), result.getErrorMessage());
    }
    
    public String readResource(String uri) {
        McpResource resource = mcpServer.readResource(uri);
        return resource != null ? resource.getContent() : null;
    }
    
    public void startServer() {
        if (!mcpServer.isRunning()) {
            mcpServer.start();
        }
    }
    
    public void stopServer() {
        if (mcpServer.isRunning()) {
            mcpServer.stop();
        }
    }
    
    public boolean isServerRunning() {
        return mcpServer.isRunning();
    }
    
    public McpServer getMcpServer() {
        return mcpServer;
    }
    
    public McpClient getMcpClient() {
        return mcpClient;
    }
    
    @FunctionalInterface
    public interface McpToolExecutor {
        Object execute(Map<String, Object> arguments) throws Exception;
    }
    
    private static class SimpleMcpTool implements McpTool {
        private final String name;
        private final String description;
        private final McpToolExecutor executor;
        
        public SimpleMcpTool(String name, String description, McpToolExecutor executor) {
            this.name = name;
            this.description = description;
            this.executor = executor;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public Object execute(Map<String, Object> arguments) throws Exception {
            return executor.execute(arguments);
        }
    }
    
    private static class SimpleMcpResource implements McpResource {
        private final String uri;
        private final String name;
        private final String content;
        
        public SimpleMcpResource(String uri, String name, String content) {
            this.uri = uri;
            this.name = name;
            this.content = content;
        }
        
        @Override
        public String getUri() {
            return uri;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getContent() {
            return content;
        }
        
        @Override
        public String getMimeType() {
            return "text/plain";
        }
    }
    
    private static class SimpleMcpPrompt implements McpPrompt {
        private final String name;
        private final String description;
        private final String template;
        
        public SimpleMcpPrompt(String name, String description, String template) {
            this.name = name;
            this.description = description;
            this.template = template;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public String getTemplate() {
            return template;
        }
    }
    
    public record ToolInfo(String name, String description) {}
    
    public record ResourceInfo(String uri, String name) {}
    
    public record PromptInfo(String name, String description) {}
    
    public record ToolResult(boolean success, String content, String errorMessage) {}
}
