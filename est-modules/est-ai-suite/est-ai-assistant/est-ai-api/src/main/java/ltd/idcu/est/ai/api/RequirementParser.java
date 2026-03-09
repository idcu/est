package ltd.idcu.est.ai.api;

import java.util.List;
import java.util.Map;

public interface RequirementParser {
    
    ParsedRequirement parse(String naturalLanguageRequirement);
    
    List<String> extractComponents(String requirement);
    
    Map<String, Object> getRequirementsMetadata(String requirement);
    
    List<String> extractKeywords(String requirement);
    
    Map<String, Integer> analyzeSentiment(String requirement);
    
    List<String> extractEntities(String requirement);
    
    Map<String, List<String>> classifyRequirements(String requirement);
    
    List<String> suggestPriorities(String requirement);
}
