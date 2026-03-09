package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import ltd.idcu.est.codecli.skills.SkillManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EstCodeCliMcpServerTest {

    @TempDir
    Path tempDir;

    private EstCodeCliMcpServer mcpServer;
    private FileIndex fileIndex;
    private ProjectIndexer indexer;
    private SkillManager skillManager;
    private PromptLibrary promptLibrary;

    @BeforeEach
    void setUp() {
        fileIndex = new FileIndex();
        indexer = new ProjectIndexer(fileIndex);
        skillManager = new SkillManager();
        promptLibrary = new PromptLibrary();
        mcpServer = new EstCodeCliMcpServer(tempDir.toString(), fileIndex, indexer, skillManager, promptLibrary);
    }

    @AfterEach
    void tearDown() {
        mcpServer = null;
        fileIndex = null;
        indexer = null;
        skillManager = null;
        promptLibrary = null;
    }

    @Test
    void testGetName() {
        assertEquals("est-code-cli-mcp-server", mcpServer.getName());
    }

    @Test
    void testGetVersion() {
        assertEquals("1.0.0", mcpServer.getVersion());
    }

    @Test
    void testGetTools() {
        var tools = mcpServer.getTools();
        assertNotNull(tools);
        assertFalse(tools.isEmpty());
        assertTrue(tools.size() >= 10);
    }

    @Test
    void testDefaultToolsAreRegistered() {
        var tools = mcpServer.getTools();
        var toolNames = tools.stream().map(McpTool::getName).toList();
        
        assertTrue(toolNames.contains("est_read_file"));
        assertTrue(toolNames.contains("est_write_file"));
        assertTrue(toolNames.contains("est_list_dir"));
        assertTrue(toolNames.contains("est_scaffold"));
        assertTrue(toolNames.contains("est_codegen"));
        assertTrue(toolNames.contains("est_index_project"));
        assertTrue(toolNames.contains("est_search"));
        assertTrue(toolNames.contains("est_list_skills"));
        assertTrue(toolNames.contains("est_list_templates"));
        assertTrue(toolNames.contains("est_run_tests"));
    }

    @Test
    void testRegisterTool() {
        class TestTool implements McpTool {
            @Override
            public String getName() { return "test_tool"; }
            @Override
            public String getDescription() { return "Test tool"; }
            @Override
            public String getInputSchema() { return "{}"; }
            @Override
            public McpToolResult execute(Map<String, Object> args) { return McpToolResult.success("test", new HashMap<>()); }
        }
        
        TestTool testTool = new TestTool();
        mcpServer.registerTool(testTool);
        
        var tools = mcpServer.getTools();
        assertTrue(tools.stream().anyMatch(t -> t.getName().equals("test_tool")));
    }

    @Test
    void testUnregisterTool() {
        mcpServer.unregisterTool("est_read_file");
        
        var tools = mcpServer.getTools();
        assertFalse(tools.stream().anyMatch(t -> t.getName().equals("est_read_file")));
    }

    @Test
    void testCallToolSuccess() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = mcpServer.callTool("est_list_skills", args);
        
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testCallToolNotFound() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = mcpServer.callTool("nonexistent_tool", args);
        
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Tool not found"));
    }

    @Test
    void testListTools() {
        String toolList = mcpServer.listTools();
        
        assertNotNull(toolList);
        assertTrue(toolList.contains("Available tools"));
        assertTrue(toolList.contains("est_read_file"));
        assertTrue(toolList.contains("est_write_file"));
        assertTrue(toolList.contains("est_list_dir"));
    }

    @Test
    void testMultipleToolRegistrations() {
        class Tool1 implements McpTool {
            @Override public String getName() { return "tool1"; }
            @Override public String getDescription() { return "Tool 1"; }
            @Override public String getInputSchema() { return "{}"; }
            @Override public McpToolResult execute(Map<String, Object> args) { return McpToolResult.success("t1", new HashMap<>()); }
        }
        
        class Tool2 implements McpTool {
            @Override public String getName() { return "tool2"; }
            @Override public String getDescription() { return "Tool 2"; }
            @Override public String getInputSchema() { return "{}"; }
            @Override public McpToolResult execute(Map<String, Object> args) { return McpToolResult.success("t2", new HashMap<>()); }
        }
        
        mcpServer.registerTool(new Tool1());
        mcpServer.registerTool(new Tool2());
        
        var tools = mcpServer.getTools();
        assertEquals(12, tools.size());
    }

    @Test
    void testUnregisterNonExistentTool() {
        assertDoesNotThrow(() -> mcpServer.unregisterTool("nonexistent_tool"));
    }

    @Test
    void testServerInitialization() {
        assertNotNull(mcpServer);
        assertNotNull(mcpServer.getName());
        assertNotNull(mcpServer.getVersion());
        assertNotNull(mcpServer.getTools());
    }
}
