package ltd.idcu.est.ai.api;

import java.util.List;
import java.util.Map;

public interface ArchitectureAdvisor {
    
    ArchitectureSuggestion suggest(String requirement);
    
    ArchitectureSuggestion suggest(String requirement, ArchitectureOptions options);
    
    List<ArchitecturePattern> getAvailablePatterns();
    
    ArchitectureReview review(String architectureDescription);
    
    ArchitectureReview review(Map<String, Object> architectureModel);
    
    List<ArchitectureSuggestion> optimize(String currentArchitecture);
}
