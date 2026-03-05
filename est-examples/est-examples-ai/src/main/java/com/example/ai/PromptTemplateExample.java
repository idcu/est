package com.example.ai;

import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.PromptTemplate;
import ltd.idcu.est.features.ai.impl.Ai;

import java.util.List;
import java.util.Map;

public class PromptTemplateExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI 提示词模板示例 ===\n");
        
        AiAssistant assistant = Ai.get();
        
        System.out.println("1. 所有可用类别:");
        List<String> categories = assistant.getTemplateRegistry().getCategories();
        for (String category : categories) {
            System.out.println("  - " + category);
        }
        System.out.println();
        
        System.out.println("2. Web类别的模板:");
        List<PromptTemplate> webTemplates = assistant.getTemplateRegistry().getTemplatesByCategory("web");
        for (PromptTemplate template : webTemplates) {
            System.out.println("  - " + template.getName() + ": " + template.getDescription());
        }
        System.out.println();
        
        System.out.println("3. 使用模板生成提示词:");
        String prompt = Ai.prompt("web-app-basic", Map.of(
            "className", "MyWebApp",
            "appName", "我的应用",
            "welcomeMessage", "欢迎使用EST框架！"
        ));
        System.out.println(prompt);
        System.out.println();
    }
}
