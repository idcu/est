package ltd.idcu.est.features.ai.api;

import java.util.List;

public interface RefactorAssistant {
    
    List<RefactorSuggestion> analyze(String code);
    
    List<RefactorSuggestion> analyze(String code, RefactorOptions options);
    
    String applyRefactoring(String code, RefactorSuggestion suggestion);
    
    List<RefactorSuggestion> getAvailableRefactorings();
    
    boolean canApply(String code, RefactorSuggestion suggestion);
}
