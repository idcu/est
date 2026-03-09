package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SearchMcpToolTest {

    @TempDir
    Path tempDir;

    private FileIndex fileIndex;
    private ProjectIndexer indexer;
    private SearchMcpTool searchTool;

    @BeforeEach
    void setUp() throws IOException {
        fileIndex = new FileIndex();
        indexer = new ProjectIndexer(fileIndex);
        searchTool = new SearchMcpTool(fileIndex, indexer);
        
        Path testFile1 = tempDir.resolve("test1.java");
        Files.writeString(testFile1, "public class TestClass {\n    private String name;\n    public void testMethod() {}\n}");
        
        Path testFile2 = tempDir.resolve("test2.java");
        Files.writeString(testFile2, "public class AnotherClass {\n    private int value;\n    public void anotherMethod() {}\n}");
        
        indexer.indexProject(tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        fileIndex = null;
        indexer = null;
        searchTool = null;
    }

    @Test
    void testGetName() {
        assertEquals("est_search", searchTool.getName());
    }

    @Test
    void testGetDescription() {
        assertTrue(searchTool.getDescription().contains("Searches the indexed project files"));
    }

    @Test
    void testGetInputSchema() {
        String schema = searchTool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.contains("query"));
        assertTrue(schema.contains("limit"));
    }

    @Test
    void testExecuteSearchWithQuery() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", "class");
        
        McpToolResult result = searchTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertTrue(result.getMetadata().containsKey("query"));
        assertTrue(result.getMetadata().containsKey("totalResults"));
        assertTrue((Integer) result.getMetadata().get("totalResults") > 0);
    }

    @Test
    void testExecuteSearchWithLimit() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", "class");
        args.put("limit", 1);
        
        McpToolResult result = searchTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertEquals(1, result.getMetadata().get("returnedResults"));
    }

    @Test
    void testExecuteSearchNoResults() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", "nonexistentkeyword12345");
        
        McpToolResult result = searchTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("No results found"));
    }

    @Test
    void testExecuteSearchWithNullQuery() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", null);
        
        McpToolResult result = searchTool.execute(args);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Query is required"));
    }

    @Test
    void testExecuteSearchWithEmptyQuery() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", "");
        
        McpToolResult result = searchTool.execute(args);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Query is required"));
    }

    @Test
    void testExecuteSearchWithWhitespaceQuery() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", "   ");
        
        McpToolResult result = searchTool.execute(args);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Query is required"));
    }

    @Test
    void testExecuteSearchNoArgs() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = searchTool.execute(args);
        
        assertFalse(result.isSuccess());
    }

    @Test
    void testSearchResultFormatting() {
        Map<String, Object> args = new HashMap<>();
        args.put("query", "TestClass");
        
        McpToolResult result = searchTool.execute(args);
        
        assertTrue(result.isSuccess());
        String message = result.getMessage();
        assertTrue(message.contains("Search results for"));
        assertTrue(message.contains("score:"));
    }
}
