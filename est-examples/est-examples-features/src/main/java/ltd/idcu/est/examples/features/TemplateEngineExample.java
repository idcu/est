package ltd.idcu.est.examples.features;

import ltd.idcu.est.web.EstTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateEngineExample {
    public static void main(String[] args) {
        EstTemplateEngine engine = new EstTemplateEngine();
        
        System.out.println("=== EST Template Engine Examples ===\n");
        
        example1SimpleVariables(engine);
        example2Conditionals(engine);
        example3Loops(engine);
        example4Filters(engine);
        example5NestedProperties(engine);
        example6CustomFilter(engine);
        example7ComplexTemplate(engine);
    }
    
    private static void example1SimpleVariables(EstTemplateEngine engine) {
        System.out.println("1. Simple Variables:");
        String template = "Hello, {{name}}! Welcome to {{framework}}.";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "Developer");
        model.put("framework", "EST Framework");
        
        String result = engine.render(template, model);
        System.out.println("   Template: " + template);
        System.out.println("   Result:   " + result);
        System.out.println();
    }
    
    private static void example2Conditionals(EstTemplateEngine engine) {
        System.out.println("2. Conditionals:");
        String template = "{% if isAdmin %}Welcome Admin!{% else %}Welcome Guest!{% endif %}";
        Map<String, Object> model1 = new HashMap<>();
        model1.put("isAdmin", true);
        
        Map<String, Object> model2 = new HashMap<>();
        model2.put("isAdmin", false);
        
        System.out.println("   Template: " + template);
        System.out.println("   Admin:    " + engine.render(template, model1));
        System.out.println("   Guest:    " + engine.render(template, model2));
        System.out.println();
    }
    
    private static void example3Loops(EstTemplateEngine engine) {
        System.out.println("3. Loops:");
        String template = "{% for item in items %}{{item_index}}: {{item}} {% endfor %}";
        Map<String, Object> model = new HashMap<>();
        model.put("items", List.of("Java", "Python", "JavaScript", "Go"));
        
        String result = engine.render(template, model);
        System.out.println("   Result: " + result);
        System.out.println();
    }
    
    private static void example4Filters(EstTemplateEngine engine) {
        System.out.println("4. Filters:");
        String template = "Original: {{text}}, Upper: {{text|upper}}, Lower: {{text|lower}}, Length: {{text|length}}";
        Map<String, Object> model = new HashMap<>();
        model.put("text", "Hello EST Framework");
        
        String result = engine.render(template, model);
        System.out.println("   Result: " + result);
        System.out.println();
    }
    
    private static void example5NestedProperties(EstTemplateEngine engine) {
        System.out.println("5. Nested Properties:");
        Map<String, Object> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("email", "john@example.com");
        user.put("age", 30);
        
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        
        String template = "User: {{user.name}}, Email: {{user.email}}, Age: {{user.age}}";
        String result = engine.render(template, model);
        System.out.println("   Result: " + result);
        System.out.println();
    }
    
    private static void example6CustomFilter(EstTemplateEngine engine) {
        System.out.println("6. Custom Filter:");
        engine.registerFilter("reverse", obj -> {
            if (obj == null) return "";
            return new StringBuilder(obj.toString()).reverse().toString();
        });
        
        String template = "Original: {{text}}, Reversed: {{text|reverse}}";
        Map<String, Object> model = new HashMap<>();
        model.put("text", "Hello World");
        
        String result = engine.render(template, model);
        System.out.println("   Result: " + result);
        System.out.println();
    }
    
    private static void example7ComplexTemplate(EstTemplateEngine engine) {
        System.out.println("7. Complex Template:");
        
        String template = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>{{title|upper}}</title>
            </head>
            <body>
                <h1>{{title|capitalize}}</h1>
                {% if items %}
                    <h2>Products ({{items|length}}):</h2>
                    <ul>
                    {% for item in items %}
                        <li>
                            {% if item_first %}<strong>{% endif %}
                            {{item_index + 1}}. {{item.name}} - ${{item.price}}
                            {% if item_last %}</strong>{% endif %}
                        </li>
                    {% endfor %}
                    </ul>
                {% else %}
                    <p>No products available.</p>
                {% endif %}
            </body>
            </html>
            """;
        
        Map<String, Object> product1 = new HashMap<>();
        product1.put("name", "Laptop");
        product1.put("price", 999.99);
        
        Map<String, Object> product2 = new HashMap<>();
        product2.put("name", "Mouse");
        product2.put("price", 29.99);
        
        Map<String, Object> product3 = new HashMap<>();
        product3.put("name", "Keyboard");
        product3.put("price", 79.99);
        
        Map<String, Object> model = new HashMap<>();
        model.put("title", "product catalog");
        model.put("items", List.of(product1, product2, product3));
        
        String result = engine.render(template, model);
        System.out.println("   Result (truncated):");
        System.out.println(result.substring(0, Math.min(500, result.length())) + "...");
        System.out.println();
    }
}
