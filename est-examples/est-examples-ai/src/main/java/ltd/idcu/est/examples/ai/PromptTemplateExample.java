package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.impl.Ai;

import java.util.List;
import java.util.Map;

public class PromptTemplateExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI Prompt Template Example ===\n");
        
        AiAssistant assistant = Ai.get();
        
        System.out.println("1. All available categories:");
        List<String> categories = assistant.getTemplateRegistry().getCategories();
        for (String category : categories) {
            System.out.println("  - " + category);
        }
        System.out.println();
        
        System.out.println("2. Web category templates:");
        List<PromptTemplate> webTemplates = assistant.getTemplateRegistry().getTemplatesByCategory("web");
        for (PromptTemplate template : webTemplates) {
            System.out.println("  - " + template.getName() + ": " + template.getDescription());
        }
        System.out.println();
        
        System.out.println("3. Generate prompt using template:");
        String prompt = Ai.prompt("web-app-basic", Map.of(
            "className", "MyWebApp",
            "appName", "My Application",
            "welcomeMessage", "Welcome to EST Framework"
        ));
        System.out.println(prompt);
        System.out.println();
    }
}
