
import ltd.idcu.est.web.EstTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class DebugTemplateTest {
    public static void main(String[] args) {
        EstTemplateEngine engine = new EstTemplateEngine();
        
        System.out.println("=== Test 1: testIfElifElse ===");
        Map<String, Object> model1 = new HashMap<>();
        model1.put("value", 2);
        String template1 = "{% if value == 1 %}One{% elif value == 2 %}Two{% else %}Other{% endif %}";
        String result1 = engine.render(template1, model1);
        System.out.println("Template: " + template1);
        System.out.println("Result:   [" + result1 + "]");
        System.out.println("Expected: [Two]");
        System.out.println("Match? " + "Two".equals(result1));
        System.out.println();
        
        System.out.println("=== Test 2: testComparisonOperators Part 1 ===");
        Map<String, Object> model2 = new HashMap<>();
        model2.put("age", 20);
        String template2 = "{% if age > 18 %}Adult{% else %}Minor{% endif %}";
        String result2 = engine.render(template2, model2);
        System.out.println("Template: " + template2);
        System.out.println("Result:   [" + result2 + "]");
        System.out.println();
        
        System.out.println("=== Test 3: testComparisonOperators Part 2 ===");
        Map<String, Object> model3 = new HashMap<>();
        model3.put("score", 85);
        String template3 = "{% if score >= 90 %}A{% elif score >= 80 %}B{% else %}C{% endif %}";
        String result3 = engine.render(template3, model3);
        System.out.println("Template: " + template3);
        System.out.println("Result:   [" + result3 + "]");
    }
}
