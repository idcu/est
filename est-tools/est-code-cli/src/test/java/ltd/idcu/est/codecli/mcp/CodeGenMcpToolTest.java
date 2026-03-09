package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CodeGenMcpToolTest {

    private CodeGenMcpTool codeGenTool;

    @BeforeEach
    void setUp() {
        codeGenTool = new CodeGenMcpTool();
    }

    @AfterEach
    void tearDown() {
        codeGenTool = null;
    }

    @Test
    void testGetName() {
        assertEquals("est_codegen", codeGenTool.getName());
    }

    @Test
    void testGetDescription() {
        assertTrue(codeGenTool.getDescription().contains("Generates code"));
    }

    @Test
    void testGetInputSchema() {
        Map<String, Object> schema = codeGenTool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.containsKey("type"));
        assertTrue(schema.containsKey("properties"));
    }

    @Test
    void testExecuteWithBasicParams() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "entity");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertEquals("entity", result.getMetadata().get("generateType"));
        assertEquals("com.example", result.getMetadata().get("packageName"));
    }

    @Test
    void testExecuteWithAllParams() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "all");
        args.put("tableName", "users");
        args.put("className", "User");
        args.put("packageName", "com.mycompany.model");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertEquals("all", result.getMetadata().get("generateType"));
        assertEquals("users", result.getMetadata().get("tableName"));
        assertEquals("User", result.getMetadata().get("className"));
        assertEquals("com.mycompany.model", result.getMetadata().get("packageName"));
    }

    @Test
    void testExecuteWithEntityType() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "entity");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("entity", result.getMetadata().get("generateType"));
    }

    @Test
    void testExecuteWithControllerType() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "controller");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("controller", result.getMetadata().get("generateType"));
    }

    @Test
    void testExecuteWithServiceType() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "service");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("service", result.getMetadata().get("generateType"));
    }

    @Test
    void testExecuteWithMapperType() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "mapper");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("mapper", result.getMetadata().get("generateType"));
    }

    @Test
    void testExecuteWithAllType() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "all");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertEquals("all", result.getMetadata().get("generateType"));
    }

    @Test
    void testSuccessMessageContainsNote() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "entity");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        String message = result.getMessage();
        assertTrue(message.contains("Generating code"));
        assertTrue(message.contains("Note: Full codegen integration coming soon"));
        assertTrue(message.contains("use the est-codegen module directly"));
    }

    @Test
    void testAllTypeMessage() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "all");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Will generate: Entity, Controller, Service, Mapper"));
    }

    @Test
    void testSingleTypeMessage() {
        Map<String, Object> args = new HashMap<>();
        args.put("generateType", "entity");
        
        McpToolResult result = codeGenTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Will generate: entity"));
    }
}
