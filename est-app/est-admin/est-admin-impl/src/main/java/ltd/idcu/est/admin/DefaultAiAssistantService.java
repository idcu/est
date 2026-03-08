package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AiAssistantService;
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.PromptTemplateRegistry;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultAiAssistantService implements AiAssistantService {
    
    private final AiAssistant aiAssistant;
    
    public DefaultAiAssistantService() {
        this.aiAssistant = new DefaultAiAssistant();
    }
    
    public DefaultAiAssistantService(AiAssistant aiAssistant) {
        this.aiAssistant = aiAssistant;
    }
    
    @Override
    public AiAssistant getAiAssistant() {
        return aiAssistant;
    }
    
    @Override
    public String chat(String message) {
        return aiAssistant.chat(message);
    }
    
    @Override
    public String chat(List<LlmMessage> messages) {
        return aiAssistant.chat(messages);
    }
    
    @Override
    public String generateCode(String requirement) {
        return aiAssistant.getCodeGenerator().generateFromRequirement(requirement);
    }
    
    @Override
    public String suggestCode(String requirement) {
        return aiAssistant.suggestCode(requirement);
    }
    
    @Override
    public String explainCode(String code) {
        return aiAssistant.explainCode(code);
    }
    
    @Override
    public String optimizeCode(String code) {
        return aiAssistant.optimizeCode(code);
    }
    
    @Override
    public String getQuickReference(String topic) {
        return aiAssistant.getQuickReference(topic);
    }
    
    @Override
    public String getBestPractice(String category) {
        return aiAssistant.getBestPractice(category);
    }
    
    @Override
    public String getTutorial(String topic) {
        return aiAssistant.getTutorial(topic);
    }
    
    @Override
    public String generatePrompt(String templateName, Map<String, String> variables) {
        return aiAssistant.generatePrompt(templateName, variables);
    }
    
    @Override
    public List<PromptTemplate> getTemplates() {
        PromptTemplateRegistry registry = aiAssistant.getTemplateRegistry();
        return new ArrayList<>(registry.getAllTemplates());
    }
    
    @Override
    public PromptTemplate getTemplate(String name) {
        PromptTemplateRegistry registry = aiAssistant.getTemplateRegistry();
        return registry.getTemplate(name).orElse(null);
    }
    
    @Override
    public void registerTemplate(PromptTemplate template) {
        PromptTemplateRegistry registry = aiAssistant.getTemplateRegistry();
        registry.register(template);
    }
    
    @Override
    public void unregisterTemplate(String name) {
        PromptTemplateRegistry registry = aiAssistant.getTemplateRegistry();
        registry.unregister(name);
    }
}
