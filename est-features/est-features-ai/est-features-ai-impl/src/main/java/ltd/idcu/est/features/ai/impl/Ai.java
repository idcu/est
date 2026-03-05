package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.AiAssistant;

public class Ai {
    
    private static final AiAssistant INSTANCE = new DefaultAiAssistant();
    
    private Ai() {
    }
    
    public static AiAssistant get() {
        return INSTANCE;
    }
    
    public static String quickRef(String topic) {
        return INSTANCE.getQuickReference(topic);
    }
    
    public static String bestPractice(String category) {
        return INSTANCE.getBestPractice(category);
    }
    
    public static String tutorial(String topic) {
        return INSTANCE.getTutorial(topic);
    }
    
    public static String prompt(String templateName, java.util.Map<String, String> variables) {
        return INSTANCE.generatePrompt(templateName, variables);
    }
    
    public static String suggest(String requirement) {
        return INSTANCE.suggestCode(requirement);
    }
    
    public static String explain(String code) {
        return INSTANCE.explainCode(code);
    }
    
    public static String optimize(String code) {
        return INSTANCE.optimizeCode(code);
    }
}
