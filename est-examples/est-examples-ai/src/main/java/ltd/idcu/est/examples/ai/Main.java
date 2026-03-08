package ltd.idcu.est.examples.ai;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST AI 示例 - 主入口");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("本模块包含 EST AI 的各种功能示例：");
        System.out.println("  1. 快速入门示例");
        System.out.println("  2. 代码生成器示例");
        System.out.println("  3. 提示词模板示例");
        System.out.println("  4. LLM 集成示例");
        System.out.println("  5. 高级功能示例");
        System.out.println("  6. 中期功能示例");
        System.out.println("  7. 长期功能示例");
        System.out.println("  8. 存储系统示例");
        System.out.println("  9. 配置管理示例");
        System.out.println("  10. AI 助手 Web 示例");
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("快速开始");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("运行单个示例（推荐）：");
        System.out.println("  mvn exec:java -Dexec.mainClass=\"ltd.idcu.est.examples.ai.AiQuickStartExample\"");
        System.out.println();
        
        System.out.println("可用的示例：");
        System.out.println("  - AiQuickStartExample      （推荐首先运行）");
        System.out.println("  - CodeGeneratorExample     （代码生成器）");
        System.out.println("  - PromptTemplateExample    （提示词模板）");
        System.out.println("  - LlmIntegrationExample    （LLM 集成）");
        System.out.println("  - StorageExample           （存储系统）");
        System.out.println("  - ConfigExample            （配置管理）");
        System.out.println("  - AdvancedAiExample        （高级功能）");
        System.out.println("  - MidTermFeaturesExample   （中期功能）");
        System.out.println("  - LongTermFeaturesExample  （长期功能）");
        System.out.println("  - AiAssistantWebExample    （Web 助手）");
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("提示");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("1. 首次使用，建议先运行 AiQuickStartExample");
        System.out.println("2. 如需测试真实 LLM，请设置对应的 API Key 环境变量");
        System.out.println("   - ZHIPUAI_API_KEY: 智谱 AI");
        System.out.println("   - OPENAI_API_KEY: OpenAI");
        System.out.println("   - QWEN_API_KEY: 通义千问");
        System.out.println("   - ERNIE_API_KEY: 文心一言");
        System.out.println("   - DOUBAO_API_KEY: 豆包");
        System.out.println("   - KIMI_API_KEY: Kimi");
        System.out.println("3. 查看 docs/ai/ 目录获取更多文档");
        System.out.println("4. 查看 est-modules/est-ai/README.md 了解模块详情");
        System.out.println();
    }
    
    public static void run() {
        main(new String[]{});
    }
}
