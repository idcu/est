package ltd.idcu.est.scaffold;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeSnippetGeneratorTest {

    @Test
    public void testListTemplates() {
        List<String> templates = CodeSnippetGenerator.listTemplates();
        
        Assertions.assertNotNull(templates);
        Assertions.assertTrue(templates.size() > 0);
        Assertions.assertTrue(templates.contains("controller"));
        Assertions.assertTrue(templates.contains("model"));
        Assertions.assertTrue(templates.contains("service"));
    }

    @Test
    public void testGetTemplate() {
        CodeSnippetGenerator.CodeTemplate template = CodeSnippetGenerator.getTemplate("model");
        
        Assertions.assertNotNull(template);
        Assertions.assertEquals("数据模型", template.name);
        Assertions.assertTrue(template.variables.contains("package"));
        Assertions.assertTrue(template.variables.contains("className"));
    }

    @Test
    public void testGenerateModel() throws IOException {
        Map<String, String> variables = new HashMap<>();
        variables.put("package", "com.example");
        variables.put("className", "User");
        
        String code = CodeSnippetGenerator.generate("model", variables);
        
        Assertions.assertNotNull(code);
        Assertions.assertTrue(code.contains("package com.example;"));
        Assertions.assertTrue(code.contains("public class User"));
    }

    @Test
    public void testGenerateService() throws IOException {
        Map<String, String> variables = new HashMap<>();
        variables.put("package", "com.example");
        variables.put("className", "UserService");
        variables.put("model", "User");
        
        String code = CodeSnippetGenerator.generate("service", variables);
        
        Assertions.assertNotNull(code);
        Assertions.assertTrue(code.contains("package com.example;"));
        Assertions.assertTrue(code.contains("public class UserService"));
        Assertions.assertTrue(code.contains("User"));
    }

    @Test
    public void testGenerateMiddleware() throws IOException {
        Map<String, String> variables = new HashMap<>();
        variables.put("package", "com.example");
        variables.put("className", "LoggingMiddleware");
        
        String code = CodeSnippetGenerator.generate("middleware", variables);
        
        Assertions.assertNotNull(code);
        Assertions.assertTrue(code.contains("package com.example;"));
        Assertions.assertTrue(code.contains("public class LoggingMiddleware"));
        Assertions.assertTrue(code.contains("implements Middleware"));
    }

    @Test
    public void testGenerateSingleton() throws IOException {
        Map<String, String> variables = new HashMap<>();
        variables.put("package", "com.example");
        variables.put("className", "ConfigManager");
        
        String code = CodeSnippetGenerator.generate("singleton", variables);
        
        Assertions.assertNotNull(code);
        Assertions.assertTrue(code.contains("package com.example;"));
        Assertions.assertTrue(code.contains("public class ConfigManager"));
        Assertions.assertTrue(code.contains("getInstance()"));
    }

    @Test
    public void testGenerateInvalidTemplate() {
        Map<String, String> variables = new HashMap<>();
        try {
            CodeSnippetGenerator.generate("nonexistent-template", variables);
            Assertions.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("Template not found"));
        } catch (IOException e) {
            Assertions.fail("Unexpected IOException: " + e.getMessage());
        }
    }
}
