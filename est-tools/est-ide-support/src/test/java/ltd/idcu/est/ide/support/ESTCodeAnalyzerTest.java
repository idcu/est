package ltd.idcu.est.ide.support;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ESTCodeAnalyzerTest {

    @Test
    public void testAnalyzeJavaFile() {
        ESTCodeAnalyzer analyzer = new ESTCodeAnalyzer();
        String content = "import org.springframework.web.bind.annotation.RestController;\n" +
                "\n" +
                "@RestController\n" +
                "public class TestController {\n" +
                "    // TODO: Implement this\n" +
                "    // FIXME: Fix this issue\n" +
                "}";

        List<Diagnostic> diagnostics = analyzer.analyze("TestController.java", content);

        assertNotNull(diagnostics);
        assertFalse(diagnostics.isEmpty());
    }

    @Test
    public void testGetJavaCompletions() {
        ESTCodeAnalyzer analyzer = new ESTCodeAnalyzer();
        String content = "public class TestClass {\n" +
                "}";

        List<CodeCompletionItem> completions = analyzer.getCompletions("TestClass.java", 2, 1, content);

        assertNotNull(completions);
        assertFalse(completions.isEmpty());

        boolean foundComponent = false;
        for (CodeCompletionItem item : completions) {
            if (item.getLabel().equals("@Component")) {
                foundComponent = true;
                break;
            }
        }
        assertTrue(foundComponent, "Should find @Component completion");
    }

    @Test
    public void testAnnotationRegistry() {
        List<String> annotationNames = ESTAnnotationRegistry.getAnnotationNames();
        assertNotNull(annotationNames);
        assertFalse(annotationNames.isEmpty());
        assertTrue(annotationNames.contains("Component"));
        assertTrue(annotationNames.contains("RestController"));
        assertTrue(annotationNames.contains("Repository"));
    }

    @Test
    public void testTemplateRegistry() {
        List<CodeTemplate> javaTemplates = ESTTemplateRegistry.getTemplatesByFileType(FileType.JAVA);
        assertNotNull(javaTemplates);
        assertFalse(javaTemplates.isEmpty());

        CodeTemplate restControllerTemplate = ESTTemplateRegistry.getTemplate("rest-controller");
        assertNotNull(restControllerTemplate);
        assertEquals("EST REST Controller", restControllerTemplate.getName());
    }
}
