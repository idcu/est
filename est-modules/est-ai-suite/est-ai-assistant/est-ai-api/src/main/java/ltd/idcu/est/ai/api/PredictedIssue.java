package ltd.idcu.est.ai.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PredictedIssue {
    
    private String issueId;
    private String issueType;
    private String severity;
    private String description;
    private String affectedComponent;
    private LocalDateTime predictedTime;
    private double confidence;
    private List<String> preventionSteps;
    
    public PredictedIssue() {
        this.preventionSteps = new ArrayList<>();
    }
    
    public String getIssueId() {
        return issueId;
    }
    
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    
    public String getIssueType() {
        return issueType;
    }
    
    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAffectedComponent() {
        return affectedComponent;
    }
    
    public void setAffectedComponent(String affectedComponent) {
        this.affectedComponent = affectedComponent;
    }
    
    public LocalDateTime getPredictedTime() {
        return predictedTime;
    }
    
    public void setPredictedTime(LocalDateTime predictedTime) {
        this.predictedTime = predictedTime;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    public List<String> getPreventionSteps() {
        return preventionSteps;
    }
    
    public void setPreventionSteps(List<String> preventionSteps) {
        this.preventionSteps = preventionSteps;
    }
}
