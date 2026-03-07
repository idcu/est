package ltd.idcu.est.examples.ai;

import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.mcp.McpServer;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.features.ai.impl.llm.ZhipuAiLlmClient;
import ltd.idcu.est.features.ai.impl.mcp.DefaultMcpServer;

import java.util.List;
import java.util.Map;

public class AdvancedAiExample {

    public static void main(String[] args) {
        System.out.println("=== EST AI Advanced Example ===\n");

        AiAssistant aiAssistant = new DefaultAiAssistant();

        demoSkillSystem(aiAssistant);
        demoMcpServer();
        demoLlmClient(aiAssistant);
    }

    private static void demoSkillSystem(AiAssistant aiAssistant) {
        System.out.println("--- Skill System Demo ---");

        System.out.println("\n1. Generate Entity:");
        SkillResult entityResult = aiAssistant.getSkillRegistry().execute("generate-entity", 
                Map.of("className", "Product", 
                       "packageName", "com.example.entity",
                       "fields", List.of("id:Long", "name:String", "price:Double", "createdAt:LocalDateTime")));
        
        if (entityResult.isSuccess()) {
            System.out.println(entityResult.getOutputs().get("code"));
        }

        System.out.println("\n2. Generate Service:");
        SkillResult serviceResult = aiAssistant.getSkillRegistry().execute("generate-service",
                Map.of("serviceName", "Product",
                       "packageName", "com.example.service",
                       "entityName", "Product"));
        
        if (serviceResult.isSuccess()) {
            System.out.println(serviceResult.getOutputs().get("code"));
        }

        System.out.println("\n3. Code Review:");
        String codeToReview = """
            public class BadExample {
                public void doSomething() {
                    System.out.println("Hello");
                    try {
                    } catch (Exception e) {}
                }
            }
            """;
        
        SkillResult reviewResult = aiAssistant.getSkillRegistry().execute("code-review",
                Map.of("code", codeToReview));
        
        if (reviewResult.isSuccess()) {
            System.out.println("Issues: " + reviewResult.getOutputs().get("issues"));
            System.out.println("Suggestions: " + reviewResult.getOutputs().get("suggestions"));
            System.out.println("Score: " + reviewResult.getOutputs().get("score") + "/100");
        }
    }

    private static void demoMcpServer() {
        System.out.println("\n--- MCP Server Demo ---");

        McpServer mcpServer = new DefaultMcpServer();

        System.out.println("\n1. MCP Server Info:");
        System.out.println("Name: " + mcpServer.getName());
        System.out.println("Version: " + mcpServer.getVersion());
        System.out.println("Available Tools: " + mcpServer.getTools().size());

        System.out.println("\n2. Available Tools:");
        mcpServer.getTools().forEach(tool -> {
            System.out.println("- " + tool.getName() + ": " + tool.getDescription());
        });

        System.out.println("\n3. Use MCP Tool to Generate Controller:");
        McpToolResult controllerResult = mcpServer.callTool("est_generate_controller",
                Map.of("controllerName", "Product",
                       "packageName", "com.example.controller"));
        
        if (controllerResult.isSuccess()) {
            System.out.println(controllerResult.getContent());
        }

        System.out.println("\n4. Get Quick Reference:");
        McpToolResult refResult = mcpServer.callTool("est_get_quick_reference",
                Map.of("topic", "web"));
        
        if (refResult.isSuccess()) {
            System.out.println(refResult.getContent());
        }
    }

    private static void demoLlmClient(AiAssistant aiAssistant) {
        System.out.println("\n--- LLM Client Demo ---");

        ZhipuAiLlmClient llmClient = new ZhipuAiLlmClient();
        System.out.println("LLM Client: " + llmClient.getName());
        System.out.println("Default Model: " + llmClient.getModel());
        System.out.println("Available: " + (llmClient.isAvailable() ? "Yes" : "No (API Key needed)"));

        System.out.println("\nHint: To use Zhipu AI, set your API Key:");
        System.out.println("  llmClient.setApiKey(\"your-api-key-here\");");
        System.out.println("  aiAssistant.setLlmClient(llmClient);");

        aiAssistant.setLlmClient(llmClient);
        System.out.println("\nLLM Client set to AiAssistant");
    }
}
