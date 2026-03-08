package ltd.idcu.est.ai.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeploymentPlan {
    
    private String projectName;
    private String version;
    private DeploymentEnvironment environment;
    private List<DeploymentStep> steps;
    private Map<String, String> configuration;
    private List<String> dependencies;
    private int estimatedTimeMinutes;
    
    public DeploymentPlan() {
        this.steps = new ArrayList<>();
        this.configuration = new HashMap<>();
        this.dependencies = new ArrayList<>();
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public DeploymentEnvironment getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(DeploymentEnvironment environment) {
        this.environment = environment;
    }
    
    public List<DeploymentStep> getSteps() {
        return steps;
    }
    
    public void setSteps(List<DeploymentStep> steps) {
        this.steps = steps;
    }
    
    public Map<String, String> getConfiguration() {
        return configuration;
    }
    
    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }
    
    public List<String> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
    
    public int getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }
    
    public void setEstimatedTimeMinutes(int estimatedTimeMinutes) {
        this.estimatedTimeMinutes = estimatedTimeMinutes;
    }
    
    public void addStep(DeploymentStep step) {
        this.steps.add(step);
    }
    
    public static class DeploymentStep {
        private String name;
        private String description;
        private String command;
        private int order;
        private boolean critical;
        private int timeoutSeconds;
        
        public DeploymentStep(String name, String description, int order) {
            this.name = name;
            this.description = description;
            this.order = order;
            this.critical = true;
            this.timeoutSeconds = 300;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCommand() { return command; }
        public void setCommand(String command) { this.command = command; }
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
        public boolean isCritical() { return critical; }
        public void setCritical(boolean critical) { this.critical = critical; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    }
    
    public enum DeploymentEnvironment {
        DEVELOPMENT,
        TESTING,
        STAGING,
        PRODUCTION
    }
}
