package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;
import ltd.idcu.est.test.annotation.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class ListDirMcpToolTest {

    private Path tempDir;
    private ListDirMcpTool tool;

    @BeforeEach
    void beforeEach() throws IOException {
        tempDir = Files.createTempDirectory("est-mcp-test");
        tool = new ListDirMcpTool(tempDir);
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
        assertEquals("est_list_dir", tool.getName());
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
    }

    @Test
    void testListEmptyDirectory() {
        McpToolResult result = tool.execute(Map.of("path", "."));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertNotNull(result.getContent());
        assertNotNull(result.getMetadata());
        assertEquals(".", result.getMetadata().get("path"));
        List<?> entries = (List<?>) result.getMetadata().get("entries");
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }

    @Test
    void testListDirectoryWithFiles() throws IOException {
        Files.writeString(tempDir.resolve("file1.txt"), "content1");
        Files.writeString(tempDir.resolve("file2.txt"), "content2");

        McpToolResult result = tool.execute(Map.of("path", "."));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        List<?> entries = (List<?>) result.getMetadata().get("entries");
        assertNotNull(entries);
        assertEquals(2, entries.size());
    }

    @Test
    void testListDirectoryWithSubdirectories() throws IOException {
        Files.createDirectory(tempDir.resolve("subdir1"));
        Files.createDirectory(tempDir.resolve("subdir2"));
        Files.writeString(tempDir.resolve("file.txt"), "content");

        McpToolResult result = tool.execute(Map.of("path", "."));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        List<?> entries = (List<?>) result.getMetadata().get("entries");
        assertNotNull(entries);
        assertEquals(3, entries.size());
    }

    @Test
    void testListNonExistentDirectory() {
        McpToolResult result = tool.execute(Map.of("path", "nonexistent"));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Directory not found"));
    }

    @Test
    void testListFileInsteadOfDirectory() throws IOException {
        Files.writeString(tempDir.resolve("file.txt"), "content");

        McpToolResult result = tool.execute(Map.of("path", "file.txt"));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Path is not a directory"));
    }

    @Test
    void testListPathOutsideWorkspace() {
        McpToolResult result = tool.execute(Map.of("path", "../../../etc"));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Access denied"));
    }

    @Test
    void testListAbsolutePathOutsideWorkspace() {
        Path outsidePath = tempDir.getParent();
        McpToolResult result = tool.execute(Map.of("path", outsidePath.toString()));

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Access denied"));
    }

    @Test
    void testListSubdirectory() throws IOException {
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectory(subDir);
        Files.writeString(subDir.resolve("nested.txt"), "nested content");

        McpToolResult result = tool.execute(Map.of("path", "subdir"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        List<?> entries = (List<?>) result.getMetadata().get("entries");
        assertNotNull(entries);
        assertEquals(1, entries.size());
    }

    @Test
    void testListDefaultPath() throws IOException {
        Files.writeString(tempDir.resolve("file.txt"), "content");

        McpToolResult result = tool.execute(Map.of());

        assertNotNull(result);
        assertTrue(result.isSuccess());
        List<?> entries = (List<?>) result.getMetadata().get("entries");
        assertNotNull(entries);
        assertEquals(1, entries.size());
    }

    @Test
    void testEntryMetadata() throws IOException {
        Files.writeString(tempDir.resolve("test.txt"), "test content");
        Files.createDirectory(tempDir.resolve("testdir"));

        McpToolResult result = tool.execute(Map.of("path", "."));

        assertNotNull(result);
        List<Map<String, Object>> entries = (List<Map<String, Object>>) result.getMetadata().get("entries");
        assertNotNull(entries);
        assertEquals(2, entries.size());

        for (Map<String, Object> entry : entries) {
            assertTrue(entry.containsKey("name"));
            assertTrue(entry.containsKey("type"));
            assertTrue(entry.containsKey("size"));
        }
    }

    @Test
    void testContentFormat() throws IOException {
        Files.writeString(tempDir.resolve("file.txt"), "content");
        Files.createDirectory(tempDir.resolve("subdir"));

        McpToolResult result = tool.execute(Map.of("path", "."));

        assertNotNull(result);
        String content = result.getContent();
        assertTrue(content.contains("[FILE]"));
        assertTrue(content.contains("[DIR]"));
        assertTrue(content.contains("file.txt"));
        assertTrue(content.contains("subdir"));
    }

    @Test
    void testConstructorWithStringPath() throws IOException {
        ListDirMcpTool stringPathTool = new ListDirMcpTool(tempDir.toString());
        assertNotNull(stringPathTool);
        assertEquals("est_list_dir", stringPathTool.getName());
    }
}
