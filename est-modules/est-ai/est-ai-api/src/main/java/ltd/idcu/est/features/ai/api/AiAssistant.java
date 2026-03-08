package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public interface AiAssistant {
    
    PromptTemplateRegistry getTemplateRegistry();
    
    CodeGenerator getCodeGenerator();
    
    ProjectScaffold getProjectScaffold();
    
    LlmClient getLlmClient();
    
    void setLlmClient(LlmClient llmClient);
    
    String getQuickReference(String topic);
    
    String getBestPractice(String category);
    
    String getTutorial(String topic);
    
    String generatePrompt(String templateName, Map<String, String> variables);
    
    String suggestCode(String requirement);
    
    String explainCode(String code);
    
    String optimizeCode(String code);
    
    String chat(String message);
    
    String chat(List<LlmMessage> messages);
}
