package ltd.idcu.est.ai.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefactorSuggestion {
    
    private String id;
    private String name;
    private String description;
    private String category;
    private RefactorSeverity severity;
    private int startLine;
    private int endLine;
    private int startColumn;
    private int endColumn;
    private String beforeCode;
    private String afterCode;
    private List<String> benefits;
    private Map<String, Object> metadata;
    
    public RefactorSuggestion() {
        this.benefits = new ArrayList<>();
        this.severity = RefactorSeverity.MEDIUM;
    }
    
    public static RefactorSuggestion create(String name, String description) {
        RefactorSuggestion suggestion = new RefactorSuggestion();
        suggestion.name = name;
        suggestion.description = description;
        return suggestion;
    }
    
    public RefactorSuggestion id(String id) {
        this.id = id;
        return this;
    }
    
    public RefactorSuggestion category(String category) {
        this.category = category;
        return this;
    }
    
    public RefactorSuggestion severity(RefactorSeverity severity) {
        this.severity = severity;
        return this;
    }
    
    public RefactorSuggestion location(int startLine, int endLine) {
        this.startLine = startLine;
        this.endLine = endLine;
        return this;
    }
    
    public RefactorSuggestion codeChange(String before, String after) {
        this.beforeCode = before;
        this.afterCode = after;
        return this;
    }
    
    public RefactorSuggestion addBenefit(String benefit) {
        this.benefits.add(benefit);
        return this;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public RefactorSeverity getSeverity() {
        return severity;
    }
    
    public void setSeverity(RefactorSeverity severity) {
        this.severity = severity;
    }
    
    public int getStartLine() {
        return startLine;
    }
    
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }
    
    public int getEndLine() {
        return endLine;
    }
    
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
    
    public int getStartColumn() {
        return startColumn;
    }
    
    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }
    
    public int getEndColumn() {
        return endColumn;
    }
    
    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
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
    
    public List<String> getBenefits() {
        return benefits;
    }
    
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public enum RefactorSeverity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}
