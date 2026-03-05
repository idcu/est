
import ltd.idcu.est.web.EstTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class TestFix {
    public static void main(String[] args) {
        EstTemplateEngine engine = new EstTemplateEngine();
        
        System.out.println("Testing testIfElifElse:");
        Map<String, Object> model1 = new HashMap<>();
        model1.put("value", 2);
        String template1 = "{% if value == 1 %}One{% elif value == 2 %}Two{% else %}Other{% endif %}";
        String result1 = engine.render(template1, model1);
        System.out.println("Expected: Two, Got: " + result1);
        System.out.println("Pass: " + "Two".equals(result1));
        
        System.out.println("\nTesting testComparisonOperators:");
        Map<String, Object> model2 = new HashMap<>();
        model2.put("age", 20);
        String template2 = "{% if age > 18 %}Adult{% else %}Minor{% endif %}";
        String result2 = engine.render(template2, model2);
        System.out.println("Expected: Adult, Got: " + result2);
        
        Map<String, Object> model3 = new HashMap<>();
        model3.put("score", 85);
        String template3 = "{% if score >= 90 %}A{% elif score >= 80 %}B{% else %}C{% endif %}";
        String result3 = engine.render(template3, model3);
        System.out.println("Expected: B, Got: " + result3);
    }
}
