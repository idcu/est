package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.skills.SkillManager;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class ListSkillsMcpToolTest {

    private SkillManager skillManager;
    private ListSkillsMcpTool tool;

    @BeforeEach
    void beforeEach() {
        skillManager = new SkillManager();
        tool = new ListSkillsMcpTool(skillManager);
    }

    @Test
    void testGetName() {
        assertEquals("est_list_skills", tool.getName());
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
    void testExecuteListSkills() {
        McpToolResult result = tool.execute(Map.of());

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertNotNull(result.getContent());
        assertTrue(result.getContent().contains("Available EST Skills"));
        assertTrue(result.getContent().contains("Code Review"));
        assertTrue(result.getContent().contains("Refactor"));
        assertNotNull(result.getMetadata());
        assertNotNull(result.getMetadata().get("skillCount"));
        assertTrue(((Number) result.getMetadata().get("skillCount")).intValue() > 0);
    }

    @Test
    void testExecuteWithArgs() {
        McpToolResult result = tool.execute(Map.of("someArg", "someValue"));

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getContent());
    }

    @Test
    void testSkillCount() {
        McpToolResult result = tool.execute(Map.of());
        int skillCount = ((Number) result.getMetadata().get("skillCount")).intValue();
        assertEquals(skillManager.getAllSkills().size(), skillCount);
    }

    @Test
    void testContentFormat() {
        McpToolResult result = tool.execute(Map.of());
        String content = result.getContent();
        assertTrue(content.contains("-"));
        assertTrue(content.contains("\n"));
    }
}
