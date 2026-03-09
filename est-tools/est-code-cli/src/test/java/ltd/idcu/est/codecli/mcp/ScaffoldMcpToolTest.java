package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScaffoldMcpToolTest {

    private ScaffoldMcpTool scaffoldTool;

    @BeforeEach
    void setUp() {
        scaffoldTool = new ScaffoldMcpTool();
    }

    @AfterEach
    void tearDown() {
        scaffoldTool = null;
    }

    @Test
    void testGetName() {
        assertEquals("est_scaffold", scaffoldTool.getName());
    }

    @Test
    void testGetDescription() {
        assertTrue(scaffoldTool.getDescription().contains("Creates a new EST project"));
    }

    @Test
    void testGetInputSchema() {
        Map<String, Object> schema = scaffoldTool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.containsKey("type"));
        assertTrue(schema.containsKey("properties"));
    }

    @Test
    void testExecuteWithBasicParams() {
        Map<String, Object> args = new HashMap<>();
        args.put("projectType", "basic");
        args.put("projectName", "test-project");
        
        McpToolResult result = scaffoldTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertEquals("basic", result.getMetadata().get("projectType"));
        assertEquals("test-project", result.getMetadata().get("projectName"));
        assertEquals("com.example", result.getMetadata().get("groupId"));
        assertEquals("com.example.testproject", result.getMetadata().get("packageName"));
        assertEquals("21", result.getMetadata().get("javaVersion"));
        assertEquals(false, result.getMetadata().get("initGit"));
    }

    @Test
    void testExecuteWithAllParams() {
        Map<String, Object> args = new HashMap<>();
        args.put("projectType", "web");
        args.put("projectName", "my-web-app");
        args.put("groupId", "com.mycompany");
        args.put("packageName", "com.mycompany.webapp");
        args.put("javaVersion", "17");
        args.put("initGit", true);
        
        McpToolResult result = scaffoldTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertEquals("web", result.getMetadata().get("projectType"));
        assertEquals("my-web-app", result.getMetadata().get("projectName"));
        assertEquals("com.mycompany", result.getMetadata().get("groupId"));
        assertEquals("com.mycompany.webapp", result.getMetadata().get("packageName"));
        assertEquals("17", result.getMetadata().get("javaVersion"));
        assertEquals(true, result.getMetadata().get("initGit"));
    }

    @Test
    void testExecuteWithNullPackageName() {
        Map<String, Object> args = new HashMap<>();
        args.put("projectType", "api");
        args.put("projectName", "my-api");
        args.put("groupId", "org.example");
        args.put("packageName", null);
        
        McpToolResult result = scaffoldTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("org.example.myapi", result.getMetadata().get("packageName"));
    }

    @Test
    void testExecuteWithEmptyPackageName() {
        Map<String, Object> args = new HashMap<>();
        args.put("projectType", "library");
        args.put("projectName", "my-lib");
        args.put("groupId", "net.test");
        args.put("packageName", "");
        
        McpToolResult result = scaffoldTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("net.test.mylib", result.getMetadata().get("packageName"));
    }

    @Test
    void testSuccessMessageContainsNote() {
        Map<String, Object> args = new HashMap<>();
        args.put("projectType", "cli");
        args.put("projectName", "test-cli");
        
        McpToolResult result = scaffoldTool.execute(args);
        
        assertTrue(result.isSuccess());
        String message = result.getMessage();
        assertTrue(message.contains("Creating EST project"));
        assertTrue(message.contains("Note: Full scaffold integration coming soon"));
        assertTrue(message.contains("use the est-scaffold module directly"));
    }
}
