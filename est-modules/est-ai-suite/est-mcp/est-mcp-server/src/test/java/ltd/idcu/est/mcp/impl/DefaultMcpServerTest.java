package ltd.idcu.est.mcp.impl;

import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;

public class DefaultMcpServerTest {
    
    @Test
    public void testCreateMcpServer() {
        McpServer server = new DefaultMcpServer();
        assertNotNull(server);
    }
    
    @Test
    public void testRegisterTool() {
        DefaultMcpServer server = new DefaultMcpServer();
        
        McpTool tool = new McpTool();
        tool.setName("test_tool");
        tool.setDescription("Test tool description");
        
        server.registerTool(tool);
        
        List<McpTool> tools = server.listTools();
        assertEquals(1, tools.size());
        assertEquals("test_tool", tools.get(0).getName());
    }
    
    @Test
    public void testRegisterMultipleTools() {
        DefaultMcpServer server = new DefaultMcpServer();
        
        McpTool tool1 = new McpTool();
        tool1.setName("tool1");
        
        McpTool tool2 = new McpTool();
        tool2.setName("tool2");
        
        McpTool tool3 = new McpTool();
        tool3.setName("tool3");
        
        server.registerTool(tool1);
        server.registerTool(tool2);
        server.registerTool(tool3);
        
        List<McpTool> tools = server.listTools();
        assertEquals(3, tools.size());
    }
    
    @Test
    public void testRegisterResource() {
        DefaultMcpServer server = new DefaultMcpServer();
        
        McpResource resource = new McpResource();
        resource.setUri("file://test.txt");
        resource.setName("Test Resource");
        
        server.registerResource(resource);
        
        List<McpResource> resources = server.listResources();
        assertEquals(1, resources.size());
        assertEquals("file://test.txt", resources.get(0).getUri());
    }
    
    @Test
    public void testRegisterPrompt() {
        DefaultMcpServer server = new DefaultMcpServer();
        
        McpPrompt prompt = new McpPrompt();
        prompt.setName("test_prompt");
        prompt.setDescription("Test prompt");
        
        server.registerPrompt(prompt);
        
        List<McpPrompt> prompts = server.listPrompts();
        assertEquals(1, prompts.size());
        assertEquals("test_prompt", prompts.get(0).getName());
    }
    
    @Test
    public void testListToolsEmpty() {
        DefaultMcpServer server = new DefaultMcpServer();
        List<McpTool> tools = server.listTools();
        assertNotNull(tools);
        assertTrue(tools.isEmpty());
    }
    
    @Test
    public void testListResourcesEmpty() {
        DefaultMcpServer server = new DefaultMcpServer();
        List<McpResource> resources = server.listResources();
        assertNotNull(resources);
        assertTrue(resources.isEmpty());
    }
    
    @Test
    public void testListPromptsEmpty() {
        DefaultMcpServer server = new DefaultMcpServer();
        List<McpPrompt> prompts = server.listPrompts();
        assertNotNull(prompts);
        assertTrue(prompts.isEmpty());
    }
    
    @Test
    public void testCallTool() {
        DefaultMcpServer server = new DefaultMcpServer();
        
        McpToolResult result = server.callTool("test_tool", Map.of());
        assertNotNull(result);
        assertNotNull(result.getContent());
    }
    
    @Test
    public void testToolWithArguments() {
        DefaultMcpServer server = new DefaultMcpServer();
        
        Map<String, Object> args = Map.of(
            "param1", "value1",
            "param2", 42
        );
        
        McpToolResult result = server.callTool("test_tool", args);
        assertNotNull(result);
    }
}
