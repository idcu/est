package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.impl.Ai;

public class AiQuickStartExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI Assistant Quick Start Example ===\n");
        
        System.out.println("1. Get quick reference:");
        System.out.println(Ai.quickRef("web"));
        System.out.println();
        
        System.out.println("2. Get best practice:");
        System.out.println(Ai.bestPractice("error-handling"));
        System.out.println();
        
        System.out.println("3. Get tutorial:");
        System.out.println(Ai.tutorial("first-app"));
        System.out.println();
        
        System.out.println("4. Code suggestion:");
        System.out.println(Ai.suggest("I need to create a web application to handle HTTP requests"));
        System.out.println();
        
        System.out.println("5. Code explanation:");
        System.out.println(Ai.explain("WebApplication app = Web.create(\"MyApp\", \"1.0.0\");"));
        System.out.println();
        
        System.out.println("6. Code optimization suggestion:");
        System.out.println(Ai.optimize("app.get(\"/\", (req, res) -> { res.send(\"Hello\"); });"));
        System.out.println();
    }
}
