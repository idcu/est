package ltd.idcu.est.ai.api;

public class CompletionSuggestion {
    
    private String id;
    private String text;
    private String description;
    private CompletionType type;
    private double confidence;
    private int cursorOffset;
    
    public CompletionSuggestion() {
    }
    
    public CompletionSuggestion(String id, String text, String description, CompletionType type, double confidence) {
        this.id = id;
        this.text = text;
        this.description = description;
        this.type = type;
        this.confidence = confidence;
        this.cursorOffset = text.length();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CompletionType getType() {
        return type;
    }
    
    public void setType(CompletionType type) {
        this.type = type;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    public int getCursorOffset() {
        return cursorOffset;
    }
    
    public void setCursorOffset(int cursorOffset) {
        this.cursorOffset = cursorOffset;
    }
    
    public enum CompletionType {
        CLASS,
        METHOD,
        VARIABLE,
        KEYWORD,
        SNIPPET,
        PARAMETER,
        IMPORT
    }
}
