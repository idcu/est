package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.LlmResponse;
import ltd.idcu.est.ai.impl.Ai;
import ltd.idcu.est.ai.impl.llm.LlmClientFactory;

import java.util.List;
import java.util.Map;

public class NewLlmProvidersExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI 新LLM提供商示�?===\n");
        
        System.out.println("📋 可用的LLM提供�?");
        for (String provider : LlmClientFactory.getAvailableProviders()) {
            System.out.println("  - " + provider);
        }
        System.out.println();
        
        System.out.println("1. Anthropic Claude 示例");
        System.out.println("   使用方式:");
        System.out.println("   LlmClient claude = LlmClientFactory.create(\"anthropic\");");
        System.out.println("   claude.setApiKey(\"your-api-key\");");
        System.out.println("   String response = claude.generate(\"Hello\");");
        System.out.println();
        
        System.out.println("2. Google Gemini 示例");
        System.out.println("   使用方式:");
        System.out.println("   LlmClient gemini = LlmClientFactory.create(\"gemini\");");
        System.out.println("   gemini.setApiKey(\"your-api-key\");");
        System.out.println("   String response = gemini.generate(\"Hello\");");
        System.out.println();
        
        System.out.println("3. 通过AiAssistant使用");
        AiAssistant assistant = Ai.create();
        System.out.println("   可以通过配置文件设置默认提供�?);
        System.out.println("   或者动态切�?");
        System.out.println("   LlmClient claude = LlmClientFactory.create(\"anthropic\", \"your-key\");");
        System.out.println("   assistant.setLlmClient(claude);");
        System.out.println();
        
        System.out.println("4. 对话示例 (伪代�?");
        System.out.println("   \"\"\"");
        System.out.println("   LlmClient client = LlmClientFactory.create(\"anthropic\", \"your-key\");");
        System.out.println("   List<LlmMessage> messages = List.of(");
        System.out.println("       new LlmMessage(\"user\", \"你好\")");
        System.out.println("   );");
        System.out.println("   LlmResponse response = client.chat(messages);");
        System.out.println("   System.out.println(response.getContent());");
        System.out.println("   \"\"\"");
        System.out.println();
        
        System.out.println("5. 流式响应示例 (伪代�?");
        System.out.println("   \"\"\"");
        System.out.println("   client.chatStream(messages, new StreamCallback() {");
        System.out.println("       @Override");
        System.out.println("       public void onToken(String token, int index) {");
        System.out.println("           System.out.print(token);");
        System.out.println("       }");
        System.out.println("   });");
        System.out.println("   \"\"\"");
        System.out.println();
        
        System.out.println("6. 配置文件示例 (est-ai-config.yml)");
        System.out.println("   \"\"\"");
        System.out.println("   ai:");
        System.out.println("     llm:");
        System.out.println("       default: anthropic");
        System.out.println("       providers:");
        System.out.println("         anthropic:");
        System.out.println("           api-key: ${ANTHROPIC_API_KEY}");
        System.out.println("           model: claude-3-opus-20240229");
        System.out.println("         gemini:");
        System.out.println("           api-key: ${GEMINI_API_KEY}");
        System.out.println("           model: gemini-2.0-pro-exp");
        System.out.println("   \"\"\"");
        System.out.println();
        
        System.out.println("🎉 提示:");
        System.out.println("   - 所有提供商都使用统一的LlmClient接口");
        System.out.println("   - 支持流式响应和函数调�?);
        System.out.println("   - 可以通过环境变量设置API密钥");
        System.out.println("   - 查看VectorStoreExample了解向量数据库用�?);
    }
}
