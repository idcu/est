package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.PromptTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultPromptTemplate implements PromptTemplate {
    
    private final String name;
    private final String category;
    private final String description;
    private final String template;
    private final Map<String, String> requiredVariables;
    
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");
    
    public DefaultPromptTemplate(String name, String category, String description, String template) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.template = template;
        this.requiredVariables = extractVariables(template);
    }
    
    public DefaultPromptTemplate(String name, String category, String description, String template, 
                                   Map<String, String> requiredVariables) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.template = template;
        this.requiredVariables = requiredVariables;
    }
    
    private Map<String, String> extractVariables(String template) {
        Map<String, String> variables = new HashMap<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        while (matcher.find()) {
            variables.put(matcher.group(1), "");
        }
        return variables;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getCategory() {
        return category;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getTemplate() {
        return template;
    }
    
    @Override
    public String generate(Map<String, String> variables) {
        if (!isValid(variables)) {
            throw new IllegalArgumentException("Missing required variables");
        }
        
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }
    
    @Override
    public Map<String, String> getRequiredVariables() {
        return new HashMap<>(requiredVariables);
    }
    
    @Override
    public boolean isValid(Map<String, String> variables) {
        for (String requiredVar : requiredVariables.keySet()) {
            if (!variables.containsKey(requiredVar) || variables.get(requiredVar) == null) {
                return false;
            }
        }
        return true;
    }
}
