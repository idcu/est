package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;
import ltd.idcu.est.test.annotation.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class ReadFileMcpToolTest {

    private Path tempDir;
    private ReadFileMcpTool tool;

    @BeforeEach
    void beforeEach() throws IOException {
        tempDir = Files.createTempDirectory("est-mcp-test");
        tool = new ReadFileMcpTool(tempDir);
    }

    @AfterEach
    void afterEach() throws IOException {
        deleteDirectory(tempDir);
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.list(path).forEach(child -> {
                try {
                    deleteDirectory(child);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        Files.deleteIfExists(path);
    }

    @Test
    void testGetName() {
        assertEquals("est_read_file", tool.getName());
    }

    @Test
    void testGetDescription() {
        assertNotNull(tool.getDescription());
        assertFalse(tool.getDescription().isEmpty());
    }

    @Test
    void testGetInputSchema() {
        Map<String, Object> schema = tool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.containsKey("type"));
        assertTrue(schema.containsKey("properties"));
        assertTrue(schema.containsKey("required"));
    }

    @Test
    void testReadExistingFile() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String testContent = "Hello, World!";
        Files.writeString(testFile, testContent);

        McpToolResult result = tool.execute(Map.of("path", "test.txt"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertEquals(testContent, result.getContent());
        assertNotNull(result.getMetadata());
        assertEquals("test.txt", result.getMetadata().get("path"));
        assertEquals(testContent.length(), result.getMetadata().get("size"));
    }

    @Test
    void testReadNonExistentFile() {
        McpToolResult result = tool.execute(Map.of("path", "nonexistent.txt"));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("File not found"));
    }

    @Test
    void testReadDirectory() throws IOException {
        Path testDir = tempDir.resolve("testdir");
        Files.createDirectory(testDir);

        McpToolResult result = tool.execute(Map.of("path", "testdir"));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Path is a directory"));
    }

    @Test
    void testReadPathOutsideWorkspace() {
        McpToolResult result = tool.execute(Map.of("path", "../../../etc/passwd"));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Access denied"));
    }

    @Test
    void testReadAbsolutePathOutsideWorkspace() {
        Path outsidePath = tempDir.getParent().resolve("outside.txt");
        McpToolResult result = tool.execute(Map.of("path", outsidePath.toString()));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Access denied"));
    }

    @Test
    void testReadEmptyFile() throws IOException {
        Path testFile = tempDir.resolve("empty.txt");
        Files.writeString(testFile, "");

        McpToolResult result = tool.execute(Map.of("path", "empty.txt"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("", result.getContent());
        assertEquals(0, result.getMetadata().get("size"));
    }

    @Test
    void testReadFileWithSpecialCharacters() throws IOException {
        Path testFile = tempDir.resolve("special.txt");
        String testContent = "Line 1\nLine 2\nLine 3\twith\tTabs";
        Files.writeString(testFile, testContent);

        McpToolResult result = tool.execute(Map.of("path", "special.txt"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(testContent, result.getContent());
    }

    @Test
    void testReadFileInSubdirectory() throws IOException {
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectory(subDir);
        Path testFile = subDir.resolve("nested.txt");
        String testContent = "Nested file content";
        Files.writeString(testFile, testContent);

        McpToolResult result = tool.execute(Map.of("path", "subdir/nested.txt"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(testContent, result.getContent());
    }

    @Test
    void testConstructorWithStringPath() throws IOException {
        ReadFileMcpTool stringPathTool = new ReadFileMcpTool(tempDir.toString());
        assertNotNull(stringPathTool);
        assertEquals("est_read_file", stringPathTool.getName());
    }
}
