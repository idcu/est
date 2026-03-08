package ltd.idcu.est.ai.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeploymentStatus {
    
    private String deploymentId;
    private String projectName;
    private DeploymentState state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<DeploymentStepStatus> stepStatuses;
    private String errorMessage;
    private String logs;
    
    public DeploymentStatus() {
        this.stepStatuses = new ArrayList<>();
    }
    
    public String getDeploymentId() {
        return deploymentId;
    }
    
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public DeploymentState getState() {
        return state;
    }
    
    public void setState(DeploymentState state) {
        this.state = state;
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
    
    public List<DeploymentStepStatus> getStepStatuses() {
        return stepStatuses;
    }
    
    public void setStepStatuses(List<DeploymentStepStatus> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getLogs() {
        return logs;
    }
    
    public void setLogs(String logs) {
        this.logs = logs;
    }
    
    public void addStepStatus(DeploymentStepStatus stepStatus) {
        this.stepStatuses.add(stepStatus);
    }
    
    public boolean isCompleted() {
        return state == DeploymentState.SUCCESS || state == DeploymentState.FAILED;
    }
    
    public boolean isSuccessful() {
        return state == DeploymentState.SUCCESS;
    }
    
    public static class DeploymentStepStatus {
        private String stepName;
        private DeploymentState state;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String output;
        private String error;
        
        public DeploymentStepStatus(String stepName, DeploymentState state) {
            this.stepName = stepName;
            this.state = state;
        }
        
        public String getStepName() { return stepName; }
        public void setStepName(String stepName) { this.stepName = stepName; }
        public DeploymentState getState() { return state; }
        public void setState(DeploymentState state) { this.state = state; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
        public String getOutput() { return output; }
        public void setOutput(String output) { this.output = output; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
    
    public enum DeploymentState {
        PENDING,
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        ROLLING_BACK,
        ROLLED_BACK
    }
}
