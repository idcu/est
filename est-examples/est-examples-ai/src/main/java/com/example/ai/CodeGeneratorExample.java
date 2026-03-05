package com.example.ai;

import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.CodeGenerator;
import ltd.idcu.est.features.ai.impl.Ai;

import java.util.Map;

public class CodeGeneratorExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI 代码生成器示例 ===\n");
        
        AiAssistant assistant = Ai.get();
        CodeGenerator generator = assistant.getCodeGenerator();
        
        System.out.println("1. 生成Web应用代码:");
        String webAppCode = generator.generateWebApp("MyFirstApp", "com.example.app", Map.of());
        System.out.println(webAppCode);
        System.out.println();
        
        System.out.println("2. 生成Controller代码:");
        String controllerCode = generator.generateController("UserController", "com.example.controller", Map.of());
        System.out.println(controllerCode);
        System.out.println();
        
        System.out.println("3. 生成Service代码:");
        String serviceCode = generator.generateService("UserService", "com.example.service", Map.of());
        System.out.println(serviceCode);
        System.out.println();
        
        System.out.println("4. 生成Entity代码:");
        String entityCode = generator.generateEntity("Product", "com.example.entity", Map.of());
        System.out.println(entityCode);
        System.out.println();
        
        System.out.println("5. 生成POM.xml:");
        String pomXml = generator.generatePomXml("MyProject", "com.example", "my-project", "1.0.0");
        System.out.println(pomXml);
        System.out.println();
    }
}
