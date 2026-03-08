package ltd.idcu.est.ai.api;

import java.util.Map;

public interface PromptTemplate {
    
    String getName();
    
    String getCategory();
    
    String getDescription();
    
    String getTemplate();
    
    String generate(Map<String, String> variables);
    
    Map<String, String> getRequiredVariables();
    
    boolean isValid(Map<String, String> variables);
}
