package ltd.idcu.est.admin.api;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.PromptTemplate;

import java.util.List;
import java.util.Map;

public interface AiAssistantService {
    
    AiAssistant getAiAssistant();
    
    String chat(String message);
    
    String chat(List<LlmMessage> messages);
    
    String generateCode(String requirement);
    
    String suggestCode(String requirement);
    
    String explainCode(String code);
    
    String optimizeCode(String code);
    
    String getQuickReference(String topic);
    
    String getBestPractice(String category);
    
    String getTutorial(String topic);
    
    String generatePrompt(String templateName, Map<String, String> variables);
    
    List<PromptTemplate> getTemplates();
    
    PromptTemplate getTemplate(String name);
    
    void registerTemplate(PromptTemplate template);
    
    void unregisterTemplate(String name);
}
