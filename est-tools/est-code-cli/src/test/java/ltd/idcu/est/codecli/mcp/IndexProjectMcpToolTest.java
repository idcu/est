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

public class IndexProjectMcpToolTest {

    @TempDir
    Path tempDir;

    private FileIndex fileIndex;
    private ProjectIndexer indexer;
    private IndexProjectMcpTool indexTool;

    @BeforeEach
    void setUp() throws IOException {
        fileIndex = new FileIndex();
        indexer = new ProjectIndexer(fileIndex);
        indexTool = new IndexProjectMcpTool(fileIndex, indexer, tempDir.toString());
        
        Path testFile = tempDir.resolve("test.java");
        Files.writeString(testFile, "public class TestClass {}");
    }

    @AfterEach
    void tearDown() {
        fileIndex = null;
        indexer = null;
        indexTool = null;
    }

    @Test
    void testGetName() {
        assertEquals("est_index_project", indexTool.getName());
    }

    @Test
    void testGetDescription() {
        assertTrue(indexTool.getDescription().contains("Indexes the project files"));
    }

    @Test
    void testGetInputSchema() {
        String schema = indexTool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.contains("path"));
    }

    @Test
    void testExecuteWithoutArgs() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = indexTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertTrue(result.getMetadata().containsKey("path"));
        assertTrue(result.getMetadata().containsKey("indexedFiles"));
    }

    @Test
    void testExecuteWithPath() {
        Map<String, Object> args = new HashMap<>();
        args.put("path", tempDir.toString());
        
        McpToolResult result = indexTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertEquals(tempDir.toString(), result.getMetadata().get("path"));
    }

    @Test
    void testExecuteWithNullPath() {
        Map<String, Object> args = new HashMap<>();
        args.put("path", null);
        
        McpToolResult result = indexTool.execute(args);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getMetadata());
        assertTrue(result.getMetadata().containsKey("path"));
    }

    @Test
    void testIndexingSuccessMessage() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = indexTool.execute(args);
        
        assertTrue(result.isSuccess());
        String message = result.getMessage();
        assertTrue(message.contains("Project indexed successfully"));
        assertTrue(message.contains("Indexed files"));
        assertTrue(message.contains("Included extensions"));
    }

    @Test
    void testIndexedFilesMetadata() {
        Map<String, Object> args = new HashMap<>();
        
        McpToolResult result = indexTool.execute(args);
        
        assertTrue(result.isSuccess());
        Integer indexedFiles = (Integer) result.getMetadata().get("indexedFiles");
        assertNotNull(indexedFiles);
        assertTrue(indexedFiles > 0);
    }

    @Test
    void testExecuteWithNonExistentPath() {
        Map<String, Object> args = new HashMap<>();
        args.put("path", "/non/existent/path/that/should/never/exist");
        
        McpToolResult result = indexTool.execute(args);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Indexing failed"));
    }
}
