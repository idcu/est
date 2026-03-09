package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RunTestsMcpToolTest {

    @TempDir
    Path tempDir;

    private RunTestsMcpTool runTestsTool;

    @BeforeEach
    void setUp() {
        runTestsTool = new RunTestsMcpTool(tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        runTestsTool = null;
    }

    @Test
    void testGetName() {
        assertEquals("est_run_tests", runTestsTool.getName());
    }

    @Test
    void testGetDescription() {
        assertTrue(runTestsTool.getDescription().contains("Runs Maven tests"));
    }

    @Test
    void testGetInputSchema() {
        String schema = runTestsTool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.contains("type"));
    }

    @Test
    void testExecuteWithoutArgs() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = runTestsTool.execute(args);
        
        assertNotNull(result);
    }

    @Test
    void testExecuteWithTestType() {
        Map<String, Object> args = new HashMap<>();
        args.put("type", "test");
        
        McpToolResult result = runTestsTool.execute(args);
        
        assertNotNull(result);
    }

    @Test
    void testExecuteWithCompileType() {
        Map<String, Object> args = new HashMap<>();
        args.put("type", "compile");
        
        McpToolResult result = runTestsTool.execute(args);
        
        assertNotNull(result);
    }

    @Test
    void testExecuteWithNullWorkDir() {
        RunTestsMcpTool nullWorkDirTool = new RunTestsMcpTool(null);
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = nullWorkDirTool.execute(args);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Failed to run tests"));
    }

    @Test
    void testExecuteWithNonExistentWorkDir() {
        RunTestsMcpTool nonExistentTool = new RunTestsMcpTool("/non/existent/path/that/should/never/exist");
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = nonExistentTool.execute(args);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Failed to run tests"));
    }
}
