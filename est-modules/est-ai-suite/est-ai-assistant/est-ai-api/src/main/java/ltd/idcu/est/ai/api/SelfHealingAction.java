package ltd.idcu.est.ai.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SelfHealingAction {
    
    private String actionId;
    private String issueId;
    private String actionType;
    private String description;
    private HealingStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> executedSteps;
    private String result;
    
    public SelfHealingAction() {
        this.executedSteps = new ArrayList<>();
    }
    
    public String getActionId() {
        return actionId;
    }
    
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
    
    public String getIssueId() {
        return issueId;
    }
    
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public HealingStatus getStatus() {
        return status;
    }
    
    public void setStatus(HealingStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public List<String> getExecutedSteps() {
        return executedSteps;
    }
    
    public void setExecutedSteps(List<String> executedSteps) {
        this.executedSteps = executedSteps;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public enum HealingStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        ROLLED_BACK
    }
}
