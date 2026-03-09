package ltd.idcu.est.codecli.prompts;

import ltd.idcu.est.test.api.Test;
import ltd.idcu.est.test.api.BeforeEach;

import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class PromptLibraryTest {

    private PromptLibrary promptLibrary;

    @BeforeEach
    void beforeEach() {
        promptLibrary = new PromptLibrary();
    }

    @Test
    void testDefaultTemplatesRegistered() {
        List<PromptTemplate> templates = promptLibrary.getAllTemplates();
        assertEquals(6, templates.size());
    }

    @Test
    void testHasDefaultTemplates() {
        assertTrue(promptLibrary.hasTemplate("est_code_generator"));
        assertTrue(promptLibrary.hasTemplate("est_bug_fixer"));
        assertTrue(promptLibrary.hasTemplate("est_test_generator"));
        assertTrue(promptLibrary.hasTemplate("est_documentation"));
        assertTrue(promptLibrary.hasTemplate("est_performance_optimization"));
        assertTrue(promptLibrary.hasTemplate("est_security_audit"));
    }

    @Test
    void testGetTemplate() {
        PromptTemplate template = promptLibrary.getTemplate("est_code_generator");
        assertNotNull(template);
        assertEquals("est_code_generator", template.getName());
    }

    @Test
    void testGetNonExistentTemplate() {
        PromptTemplate template = promptLibrary.getTemplate("nonexistent_template");
        assertNull(template);
    }

    @Test
    void testRegisterTemplate() {
        PromptTemplate newTemplate = new PromptTemplate(
            "custom_template",
            "Custom template content",
            "Custom description"
        );
        promptLibrary.registerTemplate(newTemplate);

        assertTrue(promptLibrary.hasTemplate("custom_template"));
        PromptTemplate retrieved = promptLibrary.getTemplate("custom_template");
        assertEquals("custom_template", retrieved.getName());
    }

    @Test
    void testUnregisterTemplate() {
        assertTrue(promptLibrary.hasTemplate("est_code_generator"));

        promptLibrary.unregisterTemplate("est_code_generator");

        assertFalse(promptLibrary.hasTemplate("est_code_generator"));
    }

    @Test
    void testUnregisterNonExistentTemplate() {
        promptLibrary.unregisterTemplate("nonexistent");
        List<PromptTemplate> templates = promptLibrary.getAllTemplates();
        assertEquals(6, templates.size());
    }

    @Test
    void testGetAllTemplates() {
        List<PromptTemplate> templates = promptLibrary.getAllTemplates();
        assertEquals(6, templates.size());

        promptLibrary.registerTemplate(new PromptTemplate("new1", "t1", "d1"));
        promptLibrary.registerTemplate(new PromptTemplate("new2", "t2", "d2"));

        templates = promptLibrary.getAllTemplates();
        assertEquals(8, templates.size());
    }

    @Test
    void testListTemplates() {
        String list = promptLibrary.listTemplates();
        assertNotNull(list);
        assertTrue(list.contains("Available Prompt Templates"));
        assertTrue(list.contains("est_code_generator"));
        assertTrue(list.contains("est_bug_fixer"));
    }

    @Test
    void testRegisterOverwritesExisting() {
        PromptTemplate original = promptLibrary.getTemplate("est_code_generator");
        assertNotNull(original);

        PromptTemplate replacement = new PromptTemplate(
            "est_code_generator",
            "New template content",
            "New description"
        );
        promptLibrary.registerTemplate(replacement);

        PromptTemplate retrieved = promptLibrary.getTemplate("est_code_generator");
        assertEquals("New template content", retrieved.getTemplate());
    }
}
