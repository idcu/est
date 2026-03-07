package ltd.idcu.est.features.ai.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BestPractice {
    
    private String id;
    private String title;
    private String description;
    private String category;
    private String codeExample;
    private List<String> benefits;
    private int usageCount;
    private double effectivenessScore;
    private LocalDateTime learnedAt;
    private String source;
    
    public BestPractice() {
        this.benefits = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
    
    public String getCodeExample() {
        return codeExample;
    }
    
    public void setCodeExample(String codeExample) {
        this.codeExample = codeExample;
    }
    
    public List<String> getBenefits() {
        return benefits;
    }
    
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }
    
    public int getUsageCount() {
        return usageCount;
    }
    
    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
    
    public double getEffectivenessScore() {
        return effectivenessScore;
    }
    
    public void setEffectivenessScore(double effectivenessScore) {
        this.effectivenessScore = effectivenessScore;
    }
    
    public LocalDateTime getLearnedAt() {
        return learnedAt;
    }
    
    public void setLearnedAt(LocalDateTime learnedAt) {
        this.learnedAt = learnedAt;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public void incrementUsageCount() {
        this.usageCount++;
    }
}
