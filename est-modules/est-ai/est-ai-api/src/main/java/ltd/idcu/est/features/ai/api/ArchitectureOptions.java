package ltd.idcu.est.features.ai.api;

import java.util.List;
import java.util.Map;

public class ArchitectureOptions {
    
    private List<String> preferredPatterns;
    private List<String> excludedPatterns;
    private String projectType;
    private int teamSize;
    private long expectedUsers;
    private boolean cloudNative;
    private boolean microservices;
    private List<String> constraints;
    private Map<String, Object> extraOptions;
    
    public ArchitectureOptions() {
        this.projectType = "web";
        this.teamSize = 5;
        this.expectedUsers = 10000;
        this.cloudNative = false;
        this.microservices = false;
    }
    
    public static ArchitectureOptions defaults() {
        return new ArchitectureOptions();
    }
    
    public static ArchitectureOptions microservices() {
        ArchitectureOptions options = new ArchitectureOptions();
        options.microservices = true;
        options.cloudNative = true;
        return options;
    }
    
    public static ArchitectureOptions monolith() {
        ArchitectureOptions options = new ArchitectureOptions();
        options.microservices = false;
        return options;
    }
    
    public ArchitectureOptions preferPattern(String pattern) {
        if (this.preferredPatterns == null) {
            this.preferredPatterns = new java.util.ArrayList<>();
        }
        this.preferredPatterns.add(pattern);
        return this;
    }
    
    public ArchitectureOptions excludePattern(String pattern) {
        if (this.excludedPatterns == null) {
            this.excludedPatterns = new java.util.ArrayList<>();
        }
        this.excludedPatterns.add(pattern);
        return this;
    }
    
    public ArchitectureOptions projectType(String projectType) {
        this.projectType = projectType;
        return this;
    }
    
    public ArchitectureOptions teamSize(int teamSize) {
        this.teamSize = teamSize;
        return this;
    }
    
    public ArchitectureOptions expectedUsers(long expectedUsers) {
        this.expectedUsers = expectedUsers;
        return this;
    }
    
    public ArchitectureOptions cloudNative(boolean cloudNative) {
        this.cloudNative = cloudNative;
        return this;
    }
    
    public ArchitectureOptions microservices(boolean microservices) {
        this.microservices = microservices;
        return this;
    }
    
    public ArchitectureOptions addConstraint(String constraint) {
        if (this.constraints == null) {
            this.constraints = new java.util.ArrayList<>();
        }
        this.constraints.add(constraint);
        return this;
    }
    
    public List<String> getPreferredPatterns() {
        return preferredPatterns;
    }
    
    public List<String> getExcludedPatterns() {
        return excludedPatterns;
    }
    
    public String getProjectType() {
        return projectType;
    }
    
    public int getTeamSize() {
        return teamSize;
    }
    
    public long getExpectedUsers() {
        return expectedUsers;
    }
    
    public boolean isCloudNative() {
        return cloudNative;
    }
    
    public boolean isMicroservices() {
        return microservices;
    }
    
    public List<String> getConstraints() {
        return constraints;
    }
    
    public Map<String, Object> getExtraOptions() {
        return extraOptions;
    }
}
