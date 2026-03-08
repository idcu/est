package ltd.idcu.est.ai.api;

import java.util.ArrayList;
import java.util.List;

public class OptimizationSuggestion {
    
    private String componentName;
    private String suggestionType;
    private String description;
    private String beforeCode;
    private String afterCode;
    private double expectedImprovement;
    private List<String> steps;
    private int priority;
    
    public OptimizationSuggestion() {
        this.steps = new ArrayList<>();
    }
    
    public String getComponentName() {
        return componentName;
    }
    
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
    
    public String getSuggestionType() {
        return suggestionType;
    }
    
    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getBeforeCode() {
        return beforeCode;
    }
    
    public void setBeforeCode(String beforeCode) {
        this.beforeCode = beforeCode;
    }
    
    public String getAfterCode() {
        return afterCode;
    }
    
    public void setAfterCode(String afterCode) {
        this.afterCode = afterCode;
    }
    
    public double getExpectedImprovement() {
        return expectedImprovement;
    }
    
    public void setExpectedImprovement(double expectedImprovement) {
        this.expectedImprovement = expectedImprovement;
    }
    
    public List<String> getSteps() {
        return steps;
    }
    
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
