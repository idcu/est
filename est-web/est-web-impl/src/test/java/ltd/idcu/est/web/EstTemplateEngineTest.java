package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstTemplateEngineTest {

    private final EstTemplateEngine engine = new EstTemplateEngine();

    @Test
    public void testSimpleVariableReplacement() {
        String template = "Hello, {{name}}!";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "World");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Hello, World!", result);
    }

    @Test
    public void testMultipleVariables() {
        String template = "{{greeting}}, {{name}}! You are {{age}} years old.";
        Map<String, Object> model = new HashMap<>();
        model.put("greeting", "Hi");
        model.put("name", "Alice");
        model.put("age", 30);
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Hi, Alice! You are 30 years old.", result);
    }

    @Test
    public void testMissingVariable() {
        String template = "Hello, {{name}}!";
        Map<String, Object> model = new HashMap<>();
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Hello, !", result);
    }

    @Test
    public void testIfConditionTrue() {
        String template = "{% if show %}Show this{% endif %}";
        Map<String, Object> model = new HashMap<>();
        model.put("show", true);
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Show this", result);
    }

    @Test
    public void testIfConditionFalse() {
        String template = "{% if show %}Show this{% endif %}";
        Map<String, Object> model = new HashMap<>();
        model.put("show", false);
        
        String result = engine.render(template, model);
        Assertions.assertEquals("", result);
    }

    @Test
    public void testIfElse() {
        String template = "{% if show %}Yes{% else %}No{% endif %}";
        Map<String, Object> model = new HashMap<>();
        model.put("show", false);
        
        String result = engine.render(template, model);
        Assertions.assertEquals("No", result);
    }

    @Test
    public void testIfElifElse() {
        String template = "{% if value == 1 %}One{% elif value == 2 %}Two{% else %}Other{% endif %}";
        Map<String, Object> model = new HashMap<>();
        model.put("value", 2);
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Two", result);
    }

    @Test
    public void testComparisonOperators() {
        String template1 = "{% if age > 18 %}Adult{% else %}Minor{% endif %}";
        Map<String, Object> model1 = new HashMap<>();
        model1.put("age", 20);
        Assertions.assertEquals("Adult", engine.render(template1, model1));

        String template2 = "{% if score >= 90 %}A{% elif score >= 80 %}B{% else %}C{% endif %}";
        Map<String, Object> model2 = new HashMap<>();
        model2.put("score", 85);
        Assertions.assertEquals("B", engine.render(template2, model2));
    }

    @Test
    public void testForLoop() {
        String template = "{% for item in items %}{{item}}{% endfor %}";
        Map<String, Object> model = new HashMap<>();
        model.put("items", List.of("a", "b", "c"));
        
        String result = engine.render(template, model);
        Assertions.assertEquals("abc", result);
    }

    @Test
    public void testForLoopWithIndex() {
        String template = "{% for item in items %}{{item_index}}:{{item}} {% endfor %}";
        Map<String, Object> model = new HashMap<>();
        model.put("items", List.of("a", "b"));
        
        String result = engine.render(template, model);
        Assertions.assertEquals("0:a 1:b ", result);
    }

    @Test
    public void testForLoopFirstLast() {
        String template = "{% for item in items %}{% if item_first %}First: {% endif %}{{item}}{% if item_last %}{% else %}, {% endif %}{% endfor %}";
        Map<String, Object> model = new HashMap<>();
        model.put("items", List.of("a", "b", "c"));
        
        String result = engine.render(template, model);
        Assertions.assertEquals("First: a, b, c", result);
    }

    @Test
    public void testUpperFilter() {
        String template = "{{name|upper}}";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "hello");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("HELLO", result);
    }

    @Test
    public void testLowerFilter() {
        String template = "{{name|lower}}";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "HELLO");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("hello", result);
    }

    @Test
    public void testCapitalizeFilter() {
        String template = "{{name|capitalize}}";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "hello world");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Hello world", result);
    }

    @Test
    public void testEscapeFilter() {
        String template = "{{html|escape}}";
        Map<String, Object> model = new HashMap<>();
        model.put("html", "<script>alert('xss')</script>");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("&lt;script&gt;alert(&#39;xss&#39;)&lt;/script&gt;", result);
    }

    @Test
    public void testLengthFilter() {
        String template = "Length: {{text|length}}";
        Map<String, Object> model = new HashMap<>();
        model.put("text", "hello");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Length: 5", result);
    }

    @Test
    public void testMultipleFilters() {
        String template = "{{name|upper|trim}}";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "  hello world  ");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("HELLO WORLD", result);
    }

    @Test
    public void testNestedPropertyAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "John");
        user.put("age", 25);
        
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        
        String template = "{{user.name}} is {{user.age}} years old";
        String result = engine.render(template, model);
        Assertions.assertEquals("John is 25 years old", result);
    }

    @Test
    public void testComments() {
        String template = "Hello {# This is a comment #}World!";
        Map<String, Object> model = new HashMap<>();
        
        String result = engine.render(template, model);
        Assertions.assertEquals("Hello World!", result);
    }

    @Test
    public void testTruthyValues() {
        String template = "{% if value %}Truthy{% else %}Falsy{% endif %}";
        
        Map<String, Object> model1 = new HashMap<>();
        model1.put("value", "hello");
        Assertions.assertEquals("Truthy", engine.render(template, model1));
        
        Map<String, Object> model2 = new HashMap<>();
        model2.put("value", "");
        Assertions.assertEquals("Falsy", engine.render(template, model2));
        
        Map<String, Object> model3 = new HashMap<>();
        model3.put("value", 42);
        Assertions.assertEquals("Truthy", engine.render(template, model3));
        
        Map<String, Object> model4 = new HashMap<>();
        model4.put("value", 0);
        Assertions.assertEquals("Falsy", engine.render(template, model4));
    }

    @Test
    public void testCustomFilter() {
        engine.registerFilter("reverse", obj -> {
            if (obj == null) return "";
            return new StringBuilder(obj.toString()).reverse().toString();
        });
        
        String template = "{{text|reverse}}";
        Map<String, Object> model = new HashMap<>();
        model.put("text", "hello");
        
        String result = engine.render(template, model);
        Assertions.assertEquals("olleh", result);
    }

    @Test
    public void testComplexTemplate() {
        String template = "<h1>{{title|upper}}</h1>\n" +
            "{% if items %}\n" +
            "<ul>\n" +
            "{% for item in items %}\n" +
            "    <li>{{item_index}}: {{item|capitalize}}</li>\n" +
            "{% endfor %}\n" +
            "</ul>\n" +
            "{% else %}\n" +
            "<p>No items found.</p>\n" +
            "{% endif %}";
        
        Map<String, Object> model = new HashMap<>();
        model.put("title", "shopping list");
        model.put("items", List.of("apple", "banana", "cherry"));
        
        String result = engine.render(template, model);
        Assertions.assertTrue(result.contains("<h1>SHOPPING LIST</h1>"));
        Assertions.assertTrue(result.contains("<li>0: Apple</li>"));
        Assertions.assertTrue(result.contains("<li>1: Banana</li>"));
        Assertions.assertTrue(result.contains("<li>2: Cherry</li>"));
    }
}
