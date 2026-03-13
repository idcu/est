package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.Ai;

import java.util.Map;

public class CodeGeneratorExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI Code Generator Example ===\n");
        
        AiAssistant assistant = Ai.get();
        CodeGenerator generator = assistant.getCodeGenerator();
        
        System.out.println("1. Generate Web App code:");
        String webAppCode = generator.generateWebApp("MyFirstApp", "com.example.app", Map.of());
        System.out.println(webAppCode);
        System.out.println();
        
        System.out.println("2. Generate Controller code:");
        String controllerCode = generator.generateController("UserController", "com.example.controller", Map.of());
        System.out.println(controllerCode);
        System.out.println();
        
        System.out.println("3. Generate Service code:");
        String serviceCode = generator.generateService("UserService", "com.example.service", Map.of());
        System.out.println(serviceCode);
        System.out.println();
        
        System.out.println("4. Generate Entity code:");
        String entityCode = generator.generateEntity("Product", "com.example.entity", Map.of());
        System.out.println(entityCode);
        System.out.println();
        
        System.out.println("5. Generate POM.xml:");
        String pomXml = generator.generatePomXml("MyProject", "com.example", "my-project", "1.0.0");
        System.out.println(pomXml);
        System.out.println();
    }
}
