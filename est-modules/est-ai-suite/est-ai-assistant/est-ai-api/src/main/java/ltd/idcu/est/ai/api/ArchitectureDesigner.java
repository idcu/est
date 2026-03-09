package ltd.idcu.est.ai.api;

import java.util.List;
import java.util.Map;

public interface ArchitectureDesigner {
    
    ArchitectureDesign designArchitecture(ParsedRequirement requirement);
    
    List<ArchitecturePattern> recommendPatterns(ParsedRequirement requirement);
    
    ArchitectureDesign refineDesign(ArchitectureDesign currentDesign, String feedback);
    
    Map<String, Object> validateArchitecture(ArchitectureDesign design);
    
    List<ArchitecturePattern> recommendPatternsByType(String projectType, int complexity);
    
    Map<String, List<ArchitecturePattern>> categorizePatterns(List<ArchitecturePattern> patterns);
    
    List<String> suggestTechStack(ParsedRequirement requirement);
    
    Map<String, Object> analyzeTradeoffs(ArchitectureDesign design);
    
    List<ArchitecturePattern> comparePatterns(String patternType1, String patternType2);
}
