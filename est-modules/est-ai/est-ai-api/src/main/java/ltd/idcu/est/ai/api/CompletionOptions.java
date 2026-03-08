package ltd.idcu.est.ai.api;

import java.util.HashSet;
import java.util.Set;

public class CompletionOptions {
    
    private int maxSuggestions = 10;
    private double minConfidence = 0.0;
    private Set<CompletionSuggestion.CompletionType> allowedTypes = new HashSet<>();
    private String language = "java";
    private int contextLinesBefore = 10;
    private int contextLinesAfter = 5;
    private boolean useLlm = false;
    
    public CompletionOptions() {
        allowedTypes.add(CompletionSuggestion.CompletionType.CLASS);
        allowedTypes.add(CompletionSuggestion.CompletionType.METHOD);
        allowedTypes.add(CompletionSuggestion.CompletionType.VARIABLE);
        allowedTypes.add(CompletionSuggestion.CompletionType.KEYWORD);
        allowedTypes.add(CompletionSuggestion.CompletionType.SNIPPET);
    }
    
    public int getMaxSuggestions() {
        return maxSuggestions;
    }
    
    public CompletionOptions setMaxSuggestions(int maxSuggestions) {
        this.maxSuggestions = maxSuggestions;
        return this;
    }
    
    public double getMinConfidence() {
        return minConfidence;
    }
    
    public CompletionOptions setMinConfidence(double minConfidence) {
        this.minConfidence = minConfidence;
        return this;
    }
    
    public Set<CompletionSuggestion.CompletionType> getAllowedTypes() {
        return allowedTypes;
    }
    
    public CompletionOptions setAllowedTypes(Set<CompletionSuggestion.CompletionType> allowedTypes) {
        this.allowedTypes = allowedTypes;
        return this;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public CompletionOptions setLanguage(String language) {
        this.language = language;
        return this;
    }
    
    public int getContextLinesBefore() {
        return contextLinesBefore;
    }
    
    public CompletionOptions setContextLinesBefore(int contextLinesBefore) {
        this.contextLinesBefore = contextLinesBefore;
        return this;
    }
    
    public int getContextLinesAfter() {
        return contextLinesAfter;
    }
    
    public CompletionOptions setContextLinesAfter(int contextLinesAfter) {
        this.contextLinesAfter = contextLinesAfter;
        return this;
    }
    
    public boolean isUseLlm() {
        return useLlm;
    }
    
    public CompletionOptions setUseLlm(boolean useLlm) {
        this.useLlm = useLlm;
        return this;
    }
}
