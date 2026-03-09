package ltd.idcu.est.codecli.prompts;

import ltd.idcu.est.test.api.Test;

import java.util.HashMap;
import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class PromptTemplateTest {

    @Test
    void testConstructorAndGetters() {
        PromptTemplate template = new PromptTemplate(
            "test-template",
            "Hello {{name}}, welcome to {{project}}!",
            "A test template"
        );

        assertEquals("test-template", template.getName());
        assertEquals("Hello {{name}}, welcome to {{project}}!", template.getTemplate());
        assertEquals("A test template", template.getDescription());
    }

    @Test
    void testFormatWithVariables() {
        PromptTemplate template = new PromptTemplate(
            "greeting",
            "Hello {{name}}! You are working on {{project}}.",
            "Greeting template"
        );

        Map<String, String> variables = new HashMap<>();
        variables.put("name", "Alice");
        variables.put("project", "EST Framework");

        String result = template.format(variables);
        assertEquals("Hello Alice! You are working on EST Framework.", result);
    }

    @Test
    void testFormatWithMissingVariables() {
        PromptTemplate template = new PromptTemplate(
            "simple",
            "Hello {{name}}!",
            "Simple template"
        );

        Map<String, String> variables = new HashMap<>();
        variables.put("other", "value");

        String result = template.format(variables);
        assertEquals("Hello {{name}}!", result);
    }

    @Test
    void testFormatWithEmptyVariables() {
        PromptTemplate template = new PromptTemplate(
            "empty",
            "Static template content",
            "No variables"
        );

        Map<String, String> variables = new HashMap<>();
        String result = template.format(variables);
        assertEquals("Static template content", result);
    }

    @Test
    void testFormatWithMultipleOccurrences() {
        PromptTemplate template = new PromptTemplate(
            "repeat",
            "{{greeting}} {{name}}! {{greeting}} again!",
            "Repeating variables"
        );

        Map<String, String> variables = new HashMap<>();
        variables.put("greeting", "Hi");
        variables.put("name", "Bob");

        String result = template.format(variables);
        assertEquals("Hi Bob! Hi again!", result);
    }

    @Test
    void testFormatWithSpecialCharacters() {
        PromptTemplate template = new PromptTemplate(
            "special",
            "Hello {{name}}! Your email is {{email}}.",
            "Special chars"
        );

        Map<String, String> variables = new HashMap<>();
        variables.put("name", "John Doe");
        variables.put("email", "john@example.com");

        String result = template.format(variables);
        assertEquals("Hello John Doe! Your email is john@example.com.", result);
    }

    @Test
    void testFormatWithNoPlaceholders() {
        PromptTemplate template = new PromptTemplate(
            "static",
            "This is a static template with no variables.",
            "Static template"
        );

        Map<String, String> variables = new HashMap<>();
        variables.put("unused", "value");

        String result = template.format(variables);
        assertEquals("This is a static template with no variables.", result);
    }

    @Test
    void testFormatWithNullValues() {
        PromptTemplate template = new PromptTemplate(
            "null-test",
            "Value: {{value}}",
            "Null test"
        );

        Map<String, String> variables = new HashMap<>();
        variables.put("value", null);

        String result = template.format(variables);
        assertEquals("Value: null", result);
    }
}
