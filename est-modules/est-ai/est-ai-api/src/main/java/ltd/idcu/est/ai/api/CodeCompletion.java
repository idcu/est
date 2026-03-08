package ltd.idcu.est.ai.api;

import java.util.List;

public interface CodeCompletion {
    
    List<CompletionSuggestion> complete(String context, CompletionOptions options);
    
    CompletionSuggestion getBestSuggestion(String context, CompletionOptions options);
}
