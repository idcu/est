package ltd.idcu.est.examples.ai;

import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.CodeGenerator;
import ltd.idcu.est.features.ai.api.LlmClient;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultCodeGenerator;
import ltd.idcu.est.features.ai.impl.llm.LlmClientFactory;
import ltd.idcu.est.features.ai.impl.llm.ZhipuAiLlmClient;
import ltd.idcu.est.features.ai.impl.llm.OpenAiLlmClient;
import ltd.idcu.est.features.ai.impl.llm.QwenLlmClient;

import java.util.Map;

public class LlmIntegrationExample {

    public static void main(String[] args) {
        System.out.println("=== EST AI - LLM集成示例 ===\n");

        example1_basicUsage();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example2_differentProviders();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example3_aiAssistantChat();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example4_codeGeneration();
    }

    public static void example1_basicUsage() {
        System.out.println("示例1: 基本LLM使用");
        System.out.println("-".repeat(40));

        LlmClient client = LlmClientFactory.create("zhipuai");
        System.out.println("Provider: " + client.getName());
        System.out.println("Model: " + client.getModel());
        
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            client.setApiKey(apiKey);
            System.out.println("API Key configured: ✅");
            
            if (client.isAvailable()) {
                System.out.println("\n尝试发送请求...");
                String response = client.generate("你好，请用一句话介绍EST框架");
                System.out.println("LLM响应: " + response);
            }
        } else {
            System.out.println("请设置 ZHIPUAI_API_KEY 环境变量来测试真实LLM调用");
            System.out.println("示例仅演示API结构，不进行实际调用");
        }
    }

    public static void example2_differentProviders() {
        System.out.println("示例2: 切换不同的LLM提供商");
        System.out.println("-".repeat(40));

        System.out.println("可用的LLM提供商:");
        for (String provider : LlmClientFactory.getAvailableProviders()) {
            LlmClient client = LlmClientFactory.create(provider);
            System.out.println("  - " + provider + " (" + client.getName() + ")");
        }

        System.out.println("\n切换默认提供商:");
        LlmClientFactory.setDefaultProvider("openai");
        LlmClient client = LlmClientFactory.create();
        System.out.println("默认提供商现在是: " + client.getName());

        LlmClientFactory.setDefaultProvider("zhipuai");
    }

    public static void example3_aiAssistantChat() {
        System.out.println("示例3: AI助手对话");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
        }

        System.out.println("快速参考 (web):");
        System.out.println(assistant.getQuickReference("web"));

        System.out.println("\n最佳实践 (error-handling):");
        System.out.println(assistant.getBestPractice("error-handling"));

        System.out.println("\n代码建议:");
        String suggestion = assistant.suggestCode("创建一个用户管理的REST API");
        System.out.println(suggestion);

        if (apiKey != null && !apiKey.isEmpty()) {
            System.out.println("\n--- 开始对话 ---");
            String response1 = assistant.chat("你好，我想学习EST框架");
            System.out.println("用户: 你好，我想学习EST框架");
            System.out.println("AI: " + response1);

            String response2 = assistant.chat("它有哪些主要模块？");
            System.out.println("\n用户: 它有哪些主要模块？");
            System.out.println("AI: " + response2);
        }
    }

    public static void example4_codeGeneration() {
        System.out.println("示例4: LLM驱动的代码生成");
        System.out.println("-".repeat(40));

        CodeGenerator generator = new DefaultCodeGenerator();
        
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            generator.getLlmClient().setApiKey(apiKey);
        }

        System.out.println("基础模板生成 (Entity):");
        String entityCode = generator.generateEntity("Product", "com.example.model", Map.of());
        System.out.println(entityCode);

        System.out.println("\n基础模板生成 (Service):");
        String serviceCode = generator.generateService("ProductService", "com.example.service", Map.of());
        System.out.println(serviceCode);

        System.out.println("\n--- LLM驱动生成 ---");
        if (apiKey != null && !apiKey.isEmpty()) {
            System.out.println("根据需求生成代码:");
            String requirement = """
                创建一个订单管理系统，包含：
                1. Order实体类，包含id、customerName、totalAmount、status字段
                2. OrderRepository接口
                3. OrderService服务类，包含CRUD操作
                4. 使用EST框架的Data模块
                """;
            String generatedCode = generator.generateFromRequirement(requirement);
            System.out.println(generatedCode);
        } else {
            System.out.println("请设置API密钥以测试LLM驱动的代码生成");
        }
    }
}
