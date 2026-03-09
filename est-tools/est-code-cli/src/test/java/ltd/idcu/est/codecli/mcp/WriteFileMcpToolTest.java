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

public class WriteFileMcpToolTest {

    private Path tempDir;
    private WriteFileMcpTool tool;

    @BeforeEach
    void beforeEach() throws IOException {
        tempDir = Files.createTempDirectory("est-mcp-test");
        tool = new WriteFileMcpTool(tempDir);
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
        assertEquals("est_write_file", tool.getName());
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
    void testWriteNewFile() throws IOException {
        String testContent = "Hello, World!";
        McpToolResult result = tool.execute(Map.of("path", "test.txt", "content", testContent));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("File written successfully"));
        assertNotNull(result.getMetadata());
        assertEquals("test.txt", result.getMetadata().get("path"));
        assertEquals(testContent.length(), result.getMetadata().get("size"));
        assertEquals(false, result.getMetadata().get("appended"));

        Path testFile = tempDir.resolve("test.txt");
        assertTrue(Files.exists(testFile));
        assertEquals(testContent, Files.readString(testFile));
    }

    @Test
    void testWriteOverwriteExistingFile() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String originalContent = "Original content";
        Files.writeString(testFile, originalContent);

        String newContent = "New content";
        McpToolResult result = tool.execute(Map.of("path", "test.txt", "content", newContent));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(newContent, Files.readString(testFile));
    }

    @Test
    void testWriteAppendToFile() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String originalContent = "Line 1\n";
        Files.writeString(testFile, originalContent);

        String appendContent = "Line 2";
        McpToolResult result = tool.execute(Map.of(
            "path", "test.txt", 
            "content", appendContent,
            "append", true
        ));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(true, result.getMetadata().get("appended"));
        assertEquals(originalContent + appendContent, Files.readString(testFile));
    }

    @Test
    void testWriteFileInNonExistentDirectory() throws IOException {
        String testContent = "Content in subdir";
        McpToolResult result = tool.execute(Map.of("path", "subdir/test.txt", "content", testContent));

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Path testFile = tempDir.resolve("subdir/test.txt");
        assertTrue(Files.exists(testFile));
        assertEquals(testContent, Files.readString(testFile));
    }

    @Test
    void testWritePathOutsideWorkspace() {
        McpToolResult result = tool.execute(Map.of(
            "path", "../../../etc/passwd", 
            "content", "malicious"
        ));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Access denied"));
    }

    @Test
    void testWriteAbsolutePathOutsideWorkspace() {
        Path outsidePath = tempDir.getParent().resolve("outside.txt");
        McpToolResult result = tool.execute(Map.of(
            "path", outsidePath.toString(), 
            "content", "content"
        ));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Access denied"));
    }

    @Test
    void testWriteEmptyContent() throws IOException {
        McpToolResult result = tool.execute(Map.of("path", "empty.txt", "content", ""));

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Path testFile = tempDir.resolve("empty.txt");
        assertTrue(Files.exists(testFile));
        assertEquals("", Files.readString(testFile));
    }

    @Test
    void testWriteContentWithSpecialCharacters() throws IOException {
        String testContent = "Line 1\nLine 2\nLine 3\twith\tTabs";
        McpToolResult result = tool.execute(Map.of("path", "special.txt", "content", testContent));

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Path testFile = tempDir.resolve("special.txt");
        assertEquals(testContent, Files.readString(testFile));
    }

    @Test
    void testWriteAppendEmptyContent() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String originalContent = "Original";
        Files.writeString(testFile, originalContent);

        McpToolResult result = tool.execute(Map.of(
            "path", "test.txt", 
            "content", "",
            "append", true
        ));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(originalContent, Files.readString(testFile));
    }

    @Test
    void testConstructorWithStringPath() throws IOException {
        WriteFileMcpTool stringPathTool = new WriteFileMcpTool(tempDir.toString());
        assertNotNull(stringPathTool);
        assertEquals("est_write_file", stringPathTool.getName());
    }
}
