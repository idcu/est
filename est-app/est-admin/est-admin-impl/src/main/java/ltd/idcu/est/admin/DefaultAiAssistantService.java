package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AiAssistantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultAiAssistantService implements AiAssistantService {
    
    public DefaultAiAssistantService() {
    }
    
    @Override
    public Object getAiAssistant() {
        throw new UnsupportedOperationException("AI Assistant not available");
    }
    
    @Override
    public String chat(String message) {
        return "AI Assistant not available";
    }
    
    @Override
    public String chat(List<?> messages) {
        return "AI Assistant not available";
    }
    
    @Override
    public String generateCode(String requirement) {
        return "AI Code Generator not available";
    }
    
    @Override
    public String suggestCode(String requirement) {
        return "AI Code Suggestion not available";
    }
    
    @Override
    public String explainCode(String code) {
        return "AI Code Explanation not available";
    }
    
    @Override
    public String optimizeCode(String code) {
        return "AI Code Optimization not available";
    }
    
    @Override
    public String getQuickReference(String topic) {
        return "AI Quick Reference not available";
    }
    
    @Override
    public String getBestPractice(String category) {
        return "AI Best Practice not available";
    }
    
    @Override
    public String getTutorial(String topic) {
        return "AI Tutorial not available";
    }
    
    @Override
    public String generatePrompt(String templateName, Map<String, String> variables) {
        return "AI Prompt Generation not available";
    }
    
    @Override
    public List<?> getTemplates() {
        return new ArrayList<>();
    }
    
    @Override
    public Object getTemplate(String name) {
        return null;
    }
    
    @Override
    public void registerTemplate(Object template) {
    }
    
    @Override
    public void unregisterTemplate(String name) {
    }
}
