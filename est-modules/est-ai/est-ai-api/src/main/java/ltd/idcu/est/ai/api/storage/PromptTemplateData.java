package ltd.idcu.est.ai.api.storage;

import java.util.Map;

public class PromptTemplateData {
    
    private String name;
    private String category;
    private String description;
    private String template;
    private Map<String, String> requiredVariables;
    
    public PromptTemplateData() {
    }
    
    public PromptTemplateData(String name, String category, String description, 
                             String template, Map<String, String> requiredVariables) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.template = template;
        this.requiredVariables = requiredVariables;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTemplate() {
        return template;
    }
    
    public void setTemplate(String template) {
        this.template = template;
    }
    
    public Map<String, String> getRequiredVariables() {
        return requiredVariables;
    }
    
    public void setRequiredVariables(Map<String, String> requiredVariables) {
        this.requiredVariables = requiredVariables;
    }
}
