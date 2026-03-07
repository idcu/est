package ltd.idcu.est.features.ai.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArchitectureSuggestion {
    
    private String name;
    private String description;
    private String pattern;
    private List<String> modules;
    private List<String> components;
    private Map<String, String> relationships;
    private List<String> benefits;
    private List<String> tradeoffs;
    private List<String> technologies;
    private String diagram;
    private Map<String, Object> metadata;
    
    public ArchitectureSuggestion() {
        this.modules = new ArrayList<>();
        this.components = new ArrayList<>();
        this.benefits = new ArrayList<>();
        this.tradeoffs = new ArrayList<>();
        this.technologies = new ArrayList<>();
    }
    
    public static ArchitectureSuggestion create(String name, String description) {
        ArchitectureSuggestion suggestion = new ArchitectureSuggestion();
        suggestion.name = name;
        suggestion.description = description;
        return suggestion;
    }
    
    public ArchitectureSuggestion pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }
    
    public ArchitectureSuggestion addModule(String module) {
        this.modules.add(module);
        return this;
    }
    
    public ArchitectureSuggestion addComponent(String component) {
        this.components.add(component);
        return this;
    }
    
    public ArchitectureSuggestion addRelationship(String from, String to) {
        if (this.relationships == null) {
            this.relationships = new java.util.HashMap<>();
        }
        this.relationships.put(from, to);
        return this;
    }
    
    public ArchitectureSuggestion addBenefit(String benefit) {
        this.benefits.add(benefit);
        return this;
    }
    
    public ArchitectureSuggestion addTradeoff(String tradeoff) {
        this.tradeoffs.add(tradeoff);
        return this;
    }
    
    public ArchitectureSuggestion addTechnology(String technology) {
        this.technologies.add(technology);
        return this;
    }
    
    public ArchitectureSuggestion diagram(String diagram) {
        this.diagram = diagram;
        return this;
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
    
    public String getPattern() {
        return pattern;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public List<String> getModules() {
        return modules;
    }
    
    public void setModules(List<String> modules) {
        this.modules = modules;
    }
    
    public List<String> getComponents() {
        return components;
    }
    
    public void setComponents(List<String> components) {
        this.components = components;
    }
    
    public Map<String, String> getRelationships() {
        return relationships;
    }
    
    public void setRelationships(Map<String, String> relationships) {
        this.relationships = relationships;
    }
    
    public List<String> getBenefits() {
        return benefits;
    }
    
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }
    
    public List<String> getTradeoffs() {
        return tradeoffs;
    }
    
    public void setTradeoffs(List<String> tradeoffs) {
        this.tradeoffs = tradeoffs;
    }
    
    public List<String> getTechnologies() {
        return technologies;
    }
    
    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }
    
    public String getDiagram() {
        return diagram;
    }
    
    public void setDiagram(String diagram) {
        this.diagram = diagram;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
