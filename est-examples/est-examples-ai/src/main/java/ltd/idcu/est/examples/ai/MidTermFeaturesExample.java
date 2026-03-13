package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.ai.impl.llm.LlmClientFactory;

import java.util.List;
import java.util.Map;

public class MidTermFeaturesExample {

    public static void main(String[] args) {
        System.out.println("=== EST AI - Mid-Term Features Example ===\n");

        example1_codeCompletion();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example2_refactorAssistant();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example3_architectureAdvisor();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example4_aiAssistantIntegration();
    }

    public static void example1_codeCompletion() {
        System.out.println("Example 1: Smart Code Completion");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String context = """
            package com.example;
            
            public class UserService {
                
                private UserRepository userRepository;
                
                public List<User> findAll() {
            """;
        
        System.out.println("Context code:\n" + context);
        System.out.println("\n--- Basic Completion Suggestions ---");
        
        CompletionOptions options = new CompletionOptions();
        List<CompletionSuggestion> suggestions = assistant.completeCode(context, options);
        
        for (CompletionSuggestion suggestion : suggestions) {
            System.out.println("\n[" + suggestion.getType() + "] " + suggestion.getDescription());
            System.out.println("Confidence: " + suggestion.getConfidence());
            System.out.println("Code:\n" + suggestion.getText());
        }
        
        System.out.println("\n--- LLM-Driven Completion (Requires API Key) ---");
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
            CompletionOptions llmOptions = new CompletionOptions().setUseLlm(true);
            List<CompletionSuggestion> llmSuggestions = assistant.completeCode(context, llmOptions);
            
            for (CompletionSuggestion suggestion : llmSuggestions) {
                System.out.println("\n[" + suggestion.getType() + "] " + suggestion.getDescription());
                System.out.println("Confidence: " + suggestion.getConfidence());
                System.out.println("Code:\n" + suggestion.getText());
            }
        } else {
            System.out.println("Set ZHIPUAI_API_KEY environment variable to test LLM-driven completion");
        }
    }

    public static void example2_refactorAssistant() {
        System.out.println("Example 2: AI Refactor Assistant");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String codeToReview = """
            package com.example;
            
            import java.util.ArrayList;
            import java.util.List;
            
            public class OrderProcessor {
                
                public void processOrders(List<String> orders) {
                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < orders.size(); i++) {
                        String order = orders.get(i);
                        if (order != null) {
                            System.out.println("Processing: " + order);
                            if (order.length() > 100) {
                                result.add(order);
                            }
                        }
                    }
                }
            }
            """;
        
        System.out.println("Code to review:\n" + codeToReview);
        System.out.println("\n--- Basic Refactor Analysis ---");
        
        RefactorOptions refactorOptions = RefactorOptions.defaults();
        List<RefactorSuggestion> refactorSuggestions = assistant.analyzeCode(codeToReview, refactorOptions);
        
        for (RefactorSuggestion suggestion : refactorSuggestions) {
            System.out.println("\n[" + suggestion.getSeverity() + "] " + suggestion.getTitle());
            System.out.println("Category: " + suggestion.getCategory());
            System.out.println("Description: " + suggestion.getDescription());
            if (!suggestion.getBenefits().isEmpty()) {
                System.out.println("Benefits: " + String.join(", ", suggestion.getBenefits()));
            }
        }
        
        System.out.println("\n--- LLM-Driven Refactor Analysis (Requires API Key) ---");
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
            RefactorOptions llmRefactorOptions = RefactorOptions.defaults().useLlm(true);
            List<RefactorSuggestion> llmRefactorSuggestions = assistant.analyzeCode(codeToReview, llmRefactorOptions);
            
            for (RefactorSuggestion suggestion : llmRefactorSuggestions) {
                System.out.println("\n[" + suggestion.getSeverity() + "] " + suggestion.getTitle());
                System.out.println("Description: " + suggestion.getDescription());
            }
        } else {
            System.out.println("Set ZHIPUAI_API_KEY environment variable to test LLM-driven refactor analysis");
        }
    }

    public static void example3_architectureAdvisor() {
        System.out.println("Example 3: Architecture Advisor");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String requirement = "I need to build an e-commerce platform with user management, product management, order management, and payment integration. Expected 100,000 DAU, 1000 concurrent orders per second.";
        
        System.out.println("Requirement: " + requirement);
        System.out.println("\n--- Basic Architecture Suggestions ---");
        
        ArchitectureOptions archOptions = ArchitectureOptions.defaults();
        ArchitectureSuggestion suggestion = assistant.suggestArchitecture(requirement, archOptions);
        
        System.out.println("Suggested solution: " + suggestion.getTitle());
        System.out.println("Description: " + suggestion.getDescription());
        System.out.println("Architecture pattern: " + suggestion.getPattern());
        System.out.println("\nRecommended modules: " + String.join(", ", suggestion.getModules()));
        System.out.println("Recommended components: " + String.join(", ", suggestion.getComponents()));
        System.out.println("\nBenefits:");
        for (String benefit : suggestion.getBenefits()) {
            System.out.println("  - " + benefit);
        }
        if (!suggestion.getTradeoffs().isEmpty()) {
            System.out.println("\nTradeoffs:");
            for (String tradeoff : suggestion.getTradeoffs()) {
                System.out.println("  - " + tradeoff);
            }
        }
        System.out.println("\nTech stack: " + String.join(", ", suggestion.getTechnologies()));
        if (suggestion.getDiagram() != null) {
            System.out.println("\nDiagram:\n" + suggestion.getDiagram());
        }
        
        System.out.println("\n--- Available Architecture Patterns ---");
        List<ArchitecturePattern> patterns = assistant.getArchitectureAdvisor().getAvailablePatterns();
        for (ArchitecturePattern pattern : patterns) {
            System.out.println("\n[" + pattern.getCategory() + "] " + pattern.getName());
            System.out.println("Description: " + pattern.getDescription());
            System.out.println("Recommended: " + (pattern.isRecommended() ? "[X]" : "[ ]"));
            System.out.println("Use case: " + pattern.getUseCase());
        }
        
        System.out.println("\n--- LLM-Driven Architecture Suggestions (Requires API Key) ---");
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
            ArchitectureOptions llmArchOptions = ArchitectureOptions.defaults().useLlm(true);
            ArchitectureSuggestion llmSuggestion = assistant.suggestArchitecture(requirement, llmArchOptions);
            
            System.out.println("AI-driven solution: " + llmSuggestion.getTitle());
            System.out.println("Description: " + llmSuggestion.getDescription());
        } else {
            System.out.println("Set ZHIPUAI_API_KEY environment variable to test LLM-driven architecture suggestions");
        }
    }

    public static void example4_aiAssistantIntegration() {
        System.out.println("Example 4: Complete AI Assistant Integration");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        System.out.println("AI Assistant Feature Overview:");
        System.out.println("  - Code Completion: " + (assistant.getCodeCompletion() != null ? "[X]" : "[ ]"));
        System.out.println("  - Refactor Assistant: " + (assistant.getRefactorAssistant() != null ? "[X]" : "[ ]"));
        System.out.println("  - Architecture Advisor: " + (assistant.getArchitectureAdvisor() != null ? "[X]" : "[ ]"));
        System.out.println("  - Code Generator: " + (assistant.getCodeGenerator() != null ? "[X]" : "[ ]"));
        System.out.println("  - LLM Client: " + (assistant.getLlmClient() != null ? "[X]" : "[ ]"));
        
        System.out.println("\n--- Switching LLM Providers ---");
        System.out.println("Available providers:");
        for (String provider : LlmClientFactory.getAvailableProviders()) {
            System.out.println("  - " + provider);
        }
        
        System.out.println("\n--- Quick Reference ---");
        System.out.println(assistant.getQuickReference("web"));
        
        System.out.println("\n--- Best Practices ---");
        System.out.println(assistant.getBestPractice("error-handling"));
        
        System.out.println("\n=== Mid-Term Features Integration Complete! ===");
        System.out.println("\nCore Features:");
        System.out.println("1. Smart Code Completion (CodeCompletion)");
        System.out.println("2. AI Refactor Assistant (RefactorAssistant)");
        System.out.println("3. Architecture Advisor (ArchitectureAdvisor)");
        System.out.println("4. LLM Ecosystem (7+ providers)");
        System.out.println("5. Unified AI Assistant Interface (AiAssistant)");
    }
}
