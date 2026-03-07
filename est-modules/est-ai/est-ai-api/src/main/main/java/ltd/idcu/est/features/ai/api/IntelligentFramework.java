package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public interface IntelligentFramework {
    
    void learnFromUsage(String usageData);
    
    List<BestPractice> getLearnedBestPractices();
    
    OptimizationSuggestion optimizePerformance(String componentName);
    
    List<PredictedIssue> predictIssues();
    
    SelfHealingAction selfHeal(String issueId);
}
