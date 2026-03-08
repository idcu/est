package ltd.idcu.est.ai.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArchitectureReview {
    
    private List<ArchitectureIssue> issues;
    private List<ArchitectureSuggestion> suggestions;
    private ArchitectureScore score;
    private String summary;
    private Map<String, Object> metadata;
    
    public ArchitectureReview() {
        this.issues = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }
    
    public static ArchitectureReview create() {
        return new ArchitectureReview();
    }
    
    public ArchitectureReview addIssue(ArchitectureIssue issue) {
        this.issues.add(issue);
        return this;
    }
    
    public ArchitectureReview addSuggestion(ArchitectureSuggestion suggestion) {
        this.suggestions.add(suggestion);
        return this;
    }
    
    public ArchitectureReview score(ArchitectureScore score) {
        this.score = score;
        return this;
    }
    
    public ArchitectureReview summary(String summary) {
        this.summary = summary;
        return this;
    }
    
    public List<ArchitectureIssue> getIssues() {
        return issues;
    }
    
    public void setIssues(List<ArchitectureIssue> issues) {
        this.issues = issues;
    }
    
    public List<ArchitectureSuggestion> getSuggestions() {
        return suggestions;
    }
    
    public void setSuggestions(List<ArchitectureSuggestion> suggestions) {
        this.suggestions = suggestions;
    }
    
    public ArchitectureScore getScore() {
        return score;
    }
    
    public void setScore(ArchitectureScore score) {
        this.score = score;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public static class ArchitectureIssue {
        private String id;
        private String description;
        private String category;
        private ArchitectureIssueSeverity severity;
        private String location;
        private String suggestion;
        
        public static ArchitectureIssue create(String description, ArchitectureIssueSeverity severity) {
            ArchitectureIssue issue = new ArchitectureIssue();
            issue.description = description;
            issue.severity = severity;
            return issue;
        }
        
        public ArchitectureIssue id(String id) {
            this.id = id;
            return this;
        }
        
        public ArchitectureIssue category(String category) {
            this.category = category;
            return this;
        }
        
        public ArchitectureIssue location(String location) {
            this.location = location;
            return this;
        }
        
        public ArchitectureIssue suggestion(String suggestion) {
            this.suggestion = suggestion;
            return this;
        }
        
        public String getId() {
            return id;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getCategory() {
            return category;
        }
        
        public ArchitectureIssueSeverity getSeverity() {
            return severity;
        }
        
        public String getLocation() {
            return location;
        }
        
        public String getSuggestion() {
            return suggestion;
        }
    }
    
    public static class ArchitectureScore {
        private int overall;
        private int modularity;
        private int scalability;
        private int maintainability;
        private int performance;
        private int security;
        
        public static ArchitectureScore create() {
            return new ArchitectureScore();
        }
        
        public ArchitectureScore overall(int overall) {
            this.overall = overall;
            return this;
        }
        
        public ArchitectureScore modularity(int modularity) {
            this.modularity = modularity;
            return this;
        }
        
        public ArchitectureScore scalability(int scalability) {
            this.scalability = scalability;
            return this;
        }
        
        public ArchitectureScore maintainability(int maintainability) {
            this.maintainability = maintainability;
            return this;
        }
        
        public ArchitectureScore performance(int performance) {
            this.performance = performance;
            return this;
        }
        
        public ArchitectureScore security(int security) {
            this.security = security;
            return this;
        }
        
        public int getOverall() {
            return overall;
        }
        
        public int getModularity() {
            return modularity;
        }
        
        public int getScalability() {
            return scalability;
        }
        
        public int getMaintainability() {
            return maintainability;
        }
        
        public int getPerformance() {
            return performance;
        }
        
        public int getSecurity() {
            return security;
        }
    }
    
    public enum ArchitectureIssueSeverity {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
}
