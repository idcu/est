package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;
import ltd.idcu.est.ai.impl.llm.LlmClientFactory;
import ltd.idcu.est.ai.impl.llm.ZhipuAiLlmClient;
import ltd.idcu.est.ai.impl.llm.OpenAiLlmClient;
import ltd.idcu.est.ai.impl.llm.QwenLlmClient;

import java.util.Map;

public class LlmIntegrationExample {

    public static void main(String[] args) {
        System.out.println("=== EST AI - LLM Integration Example ===\n");

        example1_basicUsage();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example2_differentProviders();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example3_aiAssistantChat();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example4_codeGeneration();
    }

    public static void example1_basicUsage() {
        System.out.println("Example 1: Basic LLM Usage");
        System.out.println("-".repeat(40));

        LlmClient client = LlmClientFactory.create("zhipuai");
        System.out.println("Provider: " + client.getName());
        System.out.println("Model: " + client.getModel());
        
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            client.setApiKey(apiKey);
            System.out.println("API Key configured: [X]");
            
            if (client.isAvailable()) {
                System.out.println("\nAttempting to send request...");
                String response = client.generate("Hello, please introduce EST Framework in one sentence");
                System.out.println("LLM Response: " + response);
            }
        } else {
            System.out.println("Please set ZHIPUAI_API_KEY environment variable to test real LLM calls");
            System.out.println("Example only demonstrates API structure, no actual calls made");
        }
    }

    public static void example2_differentProviders() {
        System.out.println("Example 2: Switching Different LLM Providers");
        System.out.println("-".repeat(40));

        System.out.println("Available LLM providers:");
        for (String provider : LlmClientFactory.getAvailableProviders()) {
            LlmClient client = LlmClientFactory.create(provider);
            System.out.println("  - " + provider + " (" + client.getName() + ")");
        }

        System.out.println("\nSwitching default provider:");
        LlmClientFactory.setDefaultProvider("openai");
        LlmClient client = LlmClientFactory.create();
        System.out.println("Default provider is now: " + client.getName());

        LlmClientFactory.setDefaultProvider("zhipuai");
    }

    public static void example3_aiAssistantChat() {
        System.out.println("Example 3: AI Assistant Chat");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
        }

        System.out.println("Quick Reference (web):");
        System.out.println(assistant.getQuickReference("web"));

        System.out.println("\nBest Practice (error-handling):");
        System.out.println(assistant.getBestPractice("error-handling"));

        System.out.println("\nCode Suggestion:");
        String suggestion = assistant.suggestCode("Create a user management REST API");
        System.out.println(suggestion);

        if (apiKey != null && !apiKey.isEmpty()) {
            System.out.println("\n--- Starting Chat ---");
            String response1 = assistant.chat("Hello, I want to learn EST Framework");
            System.out.println("User: Hello, I want to learn EST Framework");
            System.out.println("AI: " + response1);

            String response2 = assistant.chat("What are its main modules?");
            System.out.println("\nUser: What are its main modules?");
            System.out.println("AI: " + response2);
        }
    }

    public static void example4_codeGeneration() {
        System.out.println("Example 4: LLM-driven Code Generation");
        System.out.println("-".repeat(40));

        CodeGenerator generator = new DefaultCodeGenerator();
        
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            generator.getLlmClient().setApiKey(apiKey);
        }

        System.out.println("Basic template generation (Entity):");
        String entityCode = generator.generateEntity("Product", "com.example.model", Map.of());
        System.out.println(entityCode);

        System.out.println("\nBasic template generation (Service):");
        String serviceCode = generator.generateService("ProductService", "com.example.service", Map.of());
        System.out.println(serviceCode);

        System.out.println("\n--- LLM-driven Generation ---");
        if (apiKey != null && !apiKey.isEmpty()) {
            System.out.println("Generating code from requirements:");
            String requirement = """
                Create an order management system containing:
                1. Order entity class with id, customerName, totalAmount, status fields
                2. OrderRepository interface
                3. OrderService service class with CRUD operations
                4. Using EST Framework's Data module
                """;
            String generatedCode = generator.generateFromRequirement(requirement);
            System.out.println(generatedCode);
        } else {
            System.out.println("Please set API key to test LLM-driven code generation");
        }
    }
}
