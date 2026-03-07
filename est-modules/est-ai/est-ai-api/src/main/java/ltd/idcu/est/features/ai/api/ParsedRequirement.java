package ltd.idcu.est.features.ai.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsedRequirement {
    
    private String originalRequirement;
    private String projectName;
    private String projectType;
    private String description;
    private List<RequirementComponent> components;
    private List<String> features;
    private List<String> technicalRequirements;
    private Map<String, Object> metadata;
    private int complexityScore;
    private EstimatedTimeline estimatedTimeline;
    
    public ParsedRequirement() {
        this.components = new ArrayList<>();
        this.features = new ArrayList<>();
        this.technicalRequirements = new ArrayList<>();
        this.metadata = new HashMap<>();
    }
    
    public String getOriginalRequirement() {
        return originalRequirement;
    }
    
    public void setOriginalRequirement(String originalRequirement) {
        this.originalRequirement = originalRequirement;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectType() {
        return projectType;
    }
    
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<RequirementComponent> getComponents() {
        return components;
    }
    
    public void setComponents(List<RequirementComponent> components) {
        this.components = components;
    }
    
    public List<String> getFeatures() {
        return features;
    }
    
    public void setFeatures(List<String> features) {
        this.features = features;
    }
    
    public List<String> getTechnicalRequirements() {
        return technicalRequirements;
    }
    
    public void setTechnicalRequirements(List<String> technicalRequirements) {
        this.technicalRequirements = technicalRequirements;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public int getComplexityScore() {
        return complexityScore;
    }
    
    public void setComplexityScore(int complexityScore) {
        this.complexityScore = complexityScore;
    }
    
    public EstimatedTimeline getEstimatedTimeline() {
        return estimatedTimeline;
    }
    
    public void setEstimatedTimeline(EstimatedTimeline estimatedTimeline) {
        this.estimatedTimeline = estimatedTimeline;
    }
    
    public static class RequirementComponent {
        private String name;
        private String type;
        private String description;
        private int priority;
        
        public RequirementComponent(String name, String type, String description, int priority) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.priority = priority;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }
    
    public static class EstimatedTimeline {
        private int totalHours;
        private int designHours;
        private int codingHours;
        private int testingHours;
        private int deploymentHours;
        
        public EstimatedTimeline() {
        }
        
        public int getTotalHours() { return totalHours; }
        public void setTotalHours(int totalHours) { this.totalHours = totalHours; }
        public int getDesignHours() { return designHours; }
        public void setDesignHours(int designHours) { this.designHours = designHours; }
        public int getCodingHours() { return codingHours; }
        public void setCodingHours(int codingHours) { this.codingHours = codingHours; }
        public int getTestingHours() { return testingHours; }
        public void setTestingHours(int testingHours) { this.testingHours = testingHours; }
        public int getDeploymentHours() { return deploymentHours; }
        public void setDeploymentHours(int deploymentHours) { this.deploymentHours = deploymentHours; }
    }
}
