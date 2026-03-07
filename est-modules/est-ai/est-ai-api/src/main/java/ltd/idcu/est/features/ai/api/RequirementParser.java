package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public interface RequirementParser {
    
    ParsedRequirement parse(String naturalLanguageRequirement);
    
    List<String> extractComponents(String requirement);
    
    Map<String, Object> getRequirementsMetadata(String requirement);
}
