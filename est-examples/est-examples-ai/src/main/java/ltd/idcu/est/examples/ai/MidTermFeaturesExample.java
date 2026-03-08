package ltd.idcu.est.examples.ai;

import ltd.idcu.est.features.ai.api.*;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.features.ai.impl.llm.LlmClientFactory;

import java.util.List;
import java.util.Map;

public class MidTermFeaturesExample {

    public static void main(String[] args) {
        System.out.println("=== EST AI - 中期目标功能示例 ===\n");

        example1_codeCompletion();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example2_refactorAssistant();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example3_architectureAdvisor();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        example4_aiAssistantIntegration();
    }

    public static void example1_codeCompletion() {
        System.out.println("示例1: 智能代码补全");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String context = """
            package com.example;
            
            public class UserService {
                
                private UserRepository userRepository;
                
                public List<User> findAll() {
            """;
        
        System.out.println("上下文代码:\n" + context);
        System.out.println("\n--- 基础补全建议 ---");
        
        CompletionOptions options = new CompletionOptions();
        List<CompletionSuggestion> suggestions = assistant.completeCode(context, options);
        
        for (CompletionSuggestion suggestion : suggestions) {
            System.out.println("\n[" + suggestion.getType() + "] " + suggestion.getDescription());
            System.out.println("置信度: " + suggestion.getConfidence());
            System.out.println("代码:\n" + suggestion.getText());
        }
        
        System.out.println("\n--- LLM驱动补全 (需要配置API密钥) ---");
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
            CompletionOptions llmOptions = new CompletionOptions().setUseLlm(true);
            List<CompletionSuggestion> llmSuggestions = assistant.completeCode(context, llmOptions);
            
            for (CompletionSuggestion suggestion : llmSuggestions) {
                System.out.println("\n[" + suggestion.getType() + "] " + suggestion.getDescription());
                System.out.println("置信度: " + suggestion.getConfidence());
                System.out.println("代码:\n" + suggestion.getText());
            }
        } else {
            System.out.println("请设置 ZHIPUAI_API_KEY 环境变量以测试LLM驱动补全");
        }
    }

    public static void example2_refactorAssistant() {
        System.out.println("示例2: AI重构助手");
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
        
        System.out.println("待审查代码:\n" + codeToReview);
        System.out.println("\n--- 基础重构分析 ---");
        
        RefactorOptions refactorOptions = RefactorOptions.defaults();
        List<RefactorSuggestion> refactorSuggestions = assistant.analyzeCode(codeToReview, refactorOptions);
        
        for (RefactorSuggestion suggestion : refactorSuggestions) {
            System.out.println("\n[" + suggestion.getSeverity() + "] " + suggestion.getTitle());
            System.out.println("类别: " + suggestion.getCategory());
            System.out.println("描述: " + suggestion.getDescription());
            if (!suggestion.getBenefits().isEmpty()) {
                System.out.println("优势: " + String.join(", ", suggestion.getBenefits()));
            }
        }
        
        System.out.println("\n--- LLM驱动重构分析 (需要配置API密钥) ---");
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
            RefactorOptions llmRefactorOptions = RefactorOptions.defaults().useLlm(true);
            List<RefactorSuggestion> llmRefactorSuggestions = assistant.analyzeCode(codeToReview, llmRefactorOptions);
            
            for (RefactorSuggestion suggestion : llmRefactorSuggestions) {
                System.out.println("\n[" + suggestion.getSeverity() + "] " + suggestion.getTitle());
                System.out.println("描述: " + suggestion.getDescription());
            }
        } else {
            System.out.println("请设置 ZHIPUAI_API_KEY 环境变量以测试LLM驱动重构分析");
        }
    }

    public static void example3_architectureAdvisor() {
        System.out.println("示例3: 架构顾问");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        String requirement = "我需要构建一个电商平台，包含用户管理、商品管理、订单管理、支付集成等功能。预计日活用户10万，并发订单1000笔/秒。";
        
        System.out.println("需求: " + requirement);
        System.out.println("\n--- 基础架构建议 ---");
        
        ArchitectureOptions archOptions = ArchitectureOptions.defaults();
        ArchitectureSuggestion suggestion = assistant.suggestArchitecture(requirement, archOptions);
        
        System.out.println("建议方案: " + suggestion.getTitle());
        System.out.println("描述: " + suggestion.getDescription());
        System.out.println("架构模式: " + suggestion.getPattern());
        System.out.println("\n推荐模块: " + String.join(", ", suggestion.getModules()));
        System.out.println("推荐组件: " + String.join(", ", suggestion.getComponents()));
        System.out.println("\n优势:");
        for (String benefit : suggestion.getBenefits()) {
            System.out.println("  - " + benefit);
        }
        if (!suggestion.getTradeoffs().isEmpty()) {
            System.out.println("\n权衡:");
            for (String tradeoff : suggestion.getTradeoffs()) {
                System.out.println("  - " + tradeoff);
            }
        }
        System.out.println("\n技术栈: " + String.join(", ", suggestion.getTechnologies()));
        if (suggestion.getDiagram() != null) {
            System.out.println("\n架构图:\n" + suggestion.getDiagram());
        }
        
        System.out.println("\n--- 可用架构模式 ---");
        List<ArchitecturePattern> patterns = assistant.getArchitectureAdvisor().getAvailablePatterns();
        for (ArchitecturePattern pattern : patterns) {
            System.out.println("\n[" + pattern.getCategory() + "] " + pattern.getName());
            System.out.println("描述: " + pattern.getDescription());
            System.out.println("推荐: " + (pattern.isRecommended() ? "是" : "否"));
            System.out.println("适用场景: " + pattern.getUseCase());
        }
        
        System.out.println("\n--- LLM驱动架构建议 (需要配置API密钥) ---");
        String apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            assistant.getLlmClient().setApiKey(apiKey);
            ArchitectureOptions llmArchOptions = ArchitectureOptions.defaults().useLlm(true);
            ArchitectureSuggestion llmSuggestion = assistant.suggestArchitecture(requirement, llmArchOptions);
            
            System.out.println("AI驱动方案: " + llmSuggestion.getTitle());
            System.out.println("描述: " + llmSuggestion.getDescription());
        } else {
            System.out.println("请设置 ZHIPUAI_API_KEY 环境变量以测试LLM驱动架构建议");
        }
    }

    public static void example4_aiAssistantIntegration() {
        System.out.println("示例4: AI助手完整集成");
        System.out.println("-".repeat(40));

        AiAssistant assistant = new DefaultAiAssistant();
        
        System.out.println("AI助手功能概览:");
        System.out.println("  - 代码补全: " + (assistant.getCodeCompletion() != null ? "✅" : "❌"));
        System.out.println("  - 重构助手: " + (assistant.getRefactorAssistant() != null ? "✅" : "❌"));
        System.out.println("  - 架构顾问: " + (assistant.getArchitectureAdvisor() != null ? "✅" : "❌"));
        System.out.println("  - 代码生成器: " + (assistant.getCodeGenerator() != null ? "✅" : "❌"));
        System.out.println("  - LLM客户端: " + (assistant.getLlmClient() != null ? "✅" : "❌"));
        
        System.out.println("\n--- 切换LLM提供商 ---");
        System.out.println("可用提供商:");
        for (String provider : LlmClientFactory.getAvailableProviders()) {
            System.out.println("  - " + provider);
        }
        
        System.out.println("\n--- 快速参考 ---");
        System.out.println(assistant.getQuickReference("web"));
        
        System.out.println("\n--- 最佳实践 ---");
        System.out.println(assistant.getBestPractice("error-handling"));
        
        System.out.println("\n=== 中期目标功能集成完成! ===");
        System.out.println("\n核心功能:");
        System.out.println("1. 智能代码补全 (CodeCompletion)");
        System.out.println("2. AI重构助手 (RefactorAssistant)");
        System.out.println("3. 架构顾问 (ArchitectureAdvisor)");
        System.out.println("4. LLM生态系统 (7+ 提供商)");
        System.out.println("5. AI助手统一接口 (AiAssistant)");
    }
}
