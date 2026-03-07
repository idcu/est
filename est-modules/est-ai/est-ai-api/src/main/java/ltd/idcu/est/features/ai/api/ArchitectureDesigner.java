package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public interface ArchitectureDesigner {
    
    ArchitectureDesign designArchitecture(ParsedRequirement requirement);
    
    List<ArchitecturePattern> recommendPatterns(ParsedRequirement requirement);
    
    ArchitectureDesign refineDesign(ArchitectureDesign currentDesign, String feedback);
    
    Map<String, Object> validateArchitecture(ArchitectureDesign design);
}
