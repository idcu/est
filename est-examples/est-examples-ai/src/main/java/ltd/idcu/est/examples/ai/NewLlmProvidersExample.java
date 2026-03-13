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
        System.out.println("=== EST AI New LLM Providers Example ===\n");
        
        System.out.println("[List] Available LLM providers:");
        for (String provider : LlmClientFactory.getAvailableProviders()) {
            System.out.println("  - " + provider);
        }
        System.out.println();
        
        System.out.println("1. Anthropic Claude Example");
        System.out.println("   Usage:");
        System.out.println("   LlmClient claude = LlmClientFactory.create(\"anthropic\");");
        System.out.println("   claude.setApiKey(\"your-api-key\");");
        System.out.println("   String response = claude.generate(\"Hello\");");
        System.out.println();
        
        System.out.println("2. Google Gemini Example");
        System.out.println("   Usage:");
        System.out.println("   LlmClient gemini = LlmClientFactory.create(\"gemini\");");
        System.out.println("   gemini.setApiKey(\"your-api-key\");");
        System.out.println("   String response = gemini.generate(\"Hello\");");
        System.out.println();
        
        System.out.println("3. Using via AiAssistant");
        AiAssistant assistant = Ai.create();
        System.out.println("   Can set default provider via config file");
        System.out.println("   Or switch dynamically");
        System.out.println("   LlmClient claude = LlmClientFactory.create(\"anthropic\", \"your-key\");");
        System.out.println("   assistant.setLlmClient(claude);");
        System.out.println();
        
        System.out.println("4. Chat Example (pseudocode)");
        System.out.println("   \"\"\"");
        System.out.println("   LlmClient client = LlmClientFactory.create(\"anthropic\", \"your-key\");");
        System.out.println("   List<LlmMessage> messages = List.of(");
        System.out.println("       new LlmMessage(\"user\", \"Hello\")");
        System.out.println("   );");
        System.out.println("   LlmResponse response = client.chat(messages);");
        System.out.println("   System.out.println(response.getContent());");
        System.out.println("   \"\"\"");
        System.out.println();
        
        System.out.println("5. Streaming Response Example (pseudocode)");
        System.out.println("   \"\"\"");
        System.out.println("   client.chatStream(messages, new StreamCallback() {");
        System.out.println("       @Override");
        System.out.println("       public void onToken(String token, int index) {");
        System.out.println("           System.out.print(token);");
        System.out.println("       }");
        System.out.println("   });");
        System.out.println("   \"\"\"");
        System.out.println();
        
        System.out.println("6. Config File Example (est-ai-config.yml)");
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
        
        System.out.println("[X] Tips:");
        System.out.println("   - All providers use unified LlmClient interface");
        System.out.println("   - Supports streaming responses and function calling");
        System.out.println("   - API keys can be set via environment variables");
        System.out.println("   - See VectorStoreExample for vector database usage");
    }
}
