package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class ListTemplatesMcpToolTest {

    private PromptLibrary promptLibrary;
    private ListTemplatesMcpTool tool;

    @BeforeEach
    void beforeEach() {
        promptLibrary = new PromptLibrary();
        tool = new ListTemplatesMcpTool(promptLibrary);
    }

    @Test
    void testGetName() {
        assertEquals("est_list_templates", tool.getName());
    }

    @Test
    void testGetDescription() {
        assertNotNull(tool.getDescription());
        assertFalse(tool.getDescription().isEmpty());
    }

    @Test
    void testGetInputSchema() {
        String schema = tool.getInputSchema();
        assertNotNull(schema);
        assertTrue(schema.contains("type"));
        assertTrue(schema.contains("object"));
    }

    @Test
    void testExecuteListTemplates() {
        McpToolResult result = tool.execute(Map.of());

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertNotNull(result.getContent());
        assertNotNull(result.getMetadata());
        assertNotNull(result.getMetadata().get("templateCount"));
        assertTrue(((Number) result.getMetadata().get("templateCount")).intValue() >= 0);
    }

    @Test
    void testExecuteWithArgs() {
        McpToolResult result = tool.execute(Map.of("someArg", "someValue"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getContent());
    }

    @Test
    void testTemplateCount() {
        McpToolResult result = tool.execute(Map.of());
        int templateCount = ((Number) result.getMetadata().get("templateCount")).intValue();
        assertEquals(promptLibrary.getAllTemplates().size(), templateCount);
    }
}
