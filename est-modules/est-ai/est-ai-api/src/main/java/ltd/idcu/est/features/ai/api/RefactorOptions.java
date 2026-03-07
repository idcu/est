package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public class RefactorOptions {
    
    private List<String> enabledCategories;
    private List<String> disabledCategories;
    private RefactorSuggestion.RefactorSeverity minSeverity;
    private boolean autoApply;
    private boolean includeCodeChange;
    private int maxSuggestions;
    private Map<String, Object> extraOptions;
    
    public RefactorOptions() {
        this.minSeverity = RefactorSuggestion.RefactorSeverity.LOW;
        this.autoApply = false;
        this.includeCodeChange = true;
        this.maxSuggestions = 20;
    }
    
    public static RefactorOptions defaults() {
        return new RefactorOptions();
    }
    
    public static RefactorOptions strict() {
        RefactorOptions options = new RefactorOptions();
        options.minSeverity = RefactorSuggestion.RefactorSeverity.HIGH;
        options.maxSuggestions = 10;
        return options;
    }
    
    public RefactorOptions enableCategory(String category) {
        if (this.enabledCategories == null) {
            this.enabledCategories = new java.util.ArrayList<>();
        }
        this.enabledCategories.add(category);
        return this;
    }
    
    public RefactorOptions disableCategory(String category) {
        if (this.disabledCategories == null) {
            this.disabledCategories = new java.util.ArrayList<>();
        }
        this.disabledCategories.add(category);
        return this;
    }
    
    public RefactorOptions minSeverity(RefactorSuggestion.RefactorSeverity severity) {
        this.minSeverity = severity;
        return this;
    }
    
    public RefactorOptions autoApply(boolean autoApply) {
        this.autoApply = autoApply;
        return this;
    }
    
    public RefactorOptions includeCodeChange(boolean includeCodeChange) {
        this.includeCodeChange = includeCodeChange;
        return this;
    }
    
    public RefactorOptions maxSuggestions(int maxSuggestions) {
        this.maxSuggestions = maxSuggestions;
        return this;
    }
    
    public List<String> getEnabledCategories() {
        return enabledCategories;
    }
    
    public List<String> getDisabledCategories() {
        return disabledCategories;
    }
    
    public RefactorSuggestion.RefactorSeverity getMinSeverity() {
        return minSeverity;
    }
    
    public boolean isAutoApply() {
        return autoApply;
    }
    
    public boolean isIncludeCodeChange() {
        return includeCodeChange;
    }
    
    public int getMaxSuggestions() {
        return maxSuggestions;
    }
    
    public Map<String, Object> getExtraOptions() {
        return extraOptions;
    }
}
