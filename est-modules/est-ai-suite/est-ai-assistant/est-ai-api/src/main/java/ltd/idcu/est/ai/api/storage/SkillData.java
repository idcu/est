package ltd.idcu.est.ai.api.storage;

import java.util.Map;

public class SkillData {
    
    private String name;
    private String description;
    private String category;
    private String version;
    private Map<String, String> inputSchema;
    private Map<String, String> outputSchema;
    
    public SkillData() {
    }
    
    public SkillData(String name, String description, String category, String version, 
                     Map<String, String> inputSchema, Map<String, String> outputSchema) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.version = version;
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Map<String, String> getInputSchema() {
        return inputSchema;
    }
    
    public void setInputSchema(Map<String, String> inputSchema) {
        this.inputSchema = inputSchema;
    }
    
    public Map<String, String> getOutputSchema() {
        return outputSchema;
    }
    
    public void setOutputSchema(Map<String, String> outputSchema) {
        this.outputSchema = outputSchema;
    }
}
