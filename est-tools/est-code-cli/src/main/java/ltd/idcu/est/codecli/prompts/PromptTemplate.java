package ltd.idcu.est.codecli.prompts;

import java.util.Map;

public class PromptTemplate {
    
    private final String name;
    private final String template;
    private final String description;
    
    public PromptTemplate(String name, String template, String description) {
        this.name = name;
        this.template = template;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getTemplate() {
        return template;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String format(Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }
}
