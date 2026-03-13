package ltd.idcu.est.examples.ai;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST AI Examples - Main Entry");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("This module contains various examples of EST AI features:");
        System.out.println("  1. Quick Start Example");
        System.out.println("  2. Code Generator Example");
        System.out.println("  3. Prompt Template Example");
        System.out.println("  4. LLM Integration Example");
        System.out.println("  5. Advanced Features Example");
        System.out.println("  6. Mid-term Features Example");
        System.out.println("  7. Long-term Features Example");
        System.out.println("  8. Storage System Example");
        System.out.println("  9. Configuration Management Example");
        System.out.println("  10. AI Assistant Web Example");
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("Quick Start");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("Run individual example (recommended):");
        System.out.println("  mvn exec:java -Dexec.mainClass=\"ltd.idcu.est.examples.ai.AiQuickStartExample\"");
        System.out.println();
        
        System.out.println("Available examples:");
        System.out.println("  - AiQuickStartExample      (Recommended first)");
        System.out.println("  - CodeGeneratorExample     (Code Generator)");
        System.out.println("  - PromptTemplateExample    (Prompt Template)");
        System.out.println("  - LlmIntegrationExample    (LLM Integration)");
        System.out.println("  - StorageExample           (Storage System)");
        System.out.println("  - ConfigExample            (Configuration)");
        System.out.println("  - AdvancedAiExample        (Advanced Features)");
        System.out.println("  - MidTermFeaturesExample   (Mid-term Features)");
        System.out.println("  - LongTermFeaturesExample  (Long-term Features)");
        System.out.println("  - AiAssistantWebExample    (Web Assistant)");
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("Tips");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("1. First time, recommend running AiQuickStartExample");
        System.out.println("2. To test real LLM, set corresponding API Key environment variables");
        System.out.println("   - ZHIPUAI_API_KEY: Zhipu AI");
        System.out.println("   - OPENAI_API_KEY: OpenAI");
        System.out.println("   - QWEN_API_KEY: Qwen");
        System.out.println("   - ERNIE_API_KEY: Ernie");
        System.out.println("   - DOUBAO_API_KEY: Doubao");
        System.out.println("   - KIMI_API_KEY: Kimi");
        System.out.println("3. Check docs/ai/ directory for more documentation");
        System.out.println("4. Check est-modules/est-ai/README.md for module details");
        System.out.println();
    }
    
    public static void run() {
        main(new String[]{});
    }
}
