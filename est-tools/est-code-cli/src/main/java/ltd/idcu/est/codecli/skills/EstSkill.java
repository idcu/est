package ltd.idcu.est.codecli.skills;

import java.util.Map;

public interface EstSkill {
    
    String getName();
    
    String getDescription();
    
    String getPromptTemplate();
    
    boolean canHandle(String userInput);
    
    Map<String, Object> extractParameters(String userInput);
}
