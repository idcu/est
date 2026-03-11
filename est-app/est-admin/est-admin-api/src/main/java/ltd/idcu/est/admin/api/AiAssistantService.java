package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Map;

public interface AiAssistantService {
    
    Object getAiAssistant();
    
    String chat(String message);
    
    String chat(List<?> messages);
    
    String generateCode(String requirement);
    
    String suggestCode(String requirement);
    
    String explainCode(String code);
    
    String optimizeCode(String code);
    
    String getQuickReference(String topic);
    
    String getBestPractice(String category);
    
    String getTutorial(String topic);
    
    String generatePrompt(String templateName, Map<String, String> variables);
    
    List<?> getTemplates();
    
    Object getTemplate(String name);
    
    void registerTemplate(Object template);
    
    void unregisterTemplate(String name);
}
