package ltd.idcu.est.features.ai.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchitectureDesign {
    
    private String designName;
    private String description;
    private List<ModuleDesign> modules;
    private List<ComponentDesign> components;
    private List<ArchitecturePattern> patterns;
    private Map<String, String> technologyStack;
    private List<DesignDecision> decisions;
    private List<String> tradeoffs;
    private int scalabilityScore;
    private int maintainabilityScore;
    private int performanceScore;
    
    public ArchitectureDesign() {
        this.modules = new ArrayList<>();
        this.components = new ArrayList<>();
        this.patterns = new ArrayList<>();
        this.technologyStack = new HashMap<>();
        this.decisions = new ArrayList<>();
        this.tradeoffs = new ArrayList<>();
    }
    
    public String getDesignName() {
        return designName;
    }
    
    public void setDesignName(String designName) {
        this.designName = designName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<ModuleDesign> getModules() {
        return modules;
    }
    
    public void setModules(List<ModuleDesign> modules) {
        this.modules = modules;
    }
    
    public List<ComponentDesign> getComponents() {
        return components;
    }
    
    public void setComponents(List<ComponentDesign> components) {
        this.components = components;
    }
    
    public List<ArchitecturePattern> getPatterns() {
        return patterns;
    }
    
    public void setPatterns(List<ArchitecturePattern> patterns) {
        this.patterns = patterns;
    }
    
    public Map<String, String> getTechnologyStack() {
        return technologyStack;
    }
    
    public void setTechnologyStack(Map<String, String> technologyStack) {
        this.technologyStack = technologyStack;
    }
    
    public List<DesignDecision> getDecisions() {
        return decisions;
    }
    
    public void setDecisions(List<DesignDecision> decisions) {
        this.decisions = decisions;
    }
    
    public List<String> getTradeoffs() {
        return tradeoffs;
    }
    
    public void setTradeoffs(List<String> tradeoffs) {
        this.tradeoffs = tradeoffs;
    }
    
    public int getScalabilityScore() {
        return scalabilityScore;
    }
    
    public void setScalabilityScore(int scalabilityScore) {
        this.scalabilityScore = scalabilityScore;
    }
    
    public int getMaintainabilityScore() {
        return maintainabilityScore;
    }
    
    public void setMaintainabilityScore(int maintainabilityScore) {
        this.maintainabilityScore = maintainabilityScore;
    }
    
    public int getPerformanceScore() {
        return performanceScore;
    }
    
    public void setPerformanceScore(int performanceScore) {
        this.performanceScore = performanceScore;
    }
    
    public static class ModuleDesign {
        private String name;
        private String description;
        private String responsibility;
        private List<String> dependencies;
        private List<String> interfaces;
        
        public ModuleDesign(String name, String description, String responsibility) {
            this.name = name;
            this.description = description;
            this.responsibility = responsibility;
            this.dependencies = new ArrayList<>();
            this.interfaces = new ArrayList<>();
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getResponsibility() { return responsibility; }
        public void setResponsibility(String responsibility) { this.responsibility = responsibility; }
        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
        public List<String> getInterfaces() { return interfaces; }
        public void setInterfaces(List<String> interfaces) { this.interfaces = interfaces; }
    }
    
    public static class ComponentDesign {
        private String name;
        private String type;
        private String description;
        private String module;
        private List<String> inputs;
        private List<String> outputs;
        
        public ComponentDesign(String name, String type, String description, String module) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.module = module;
            this.inputs = new ArrayList<>();
            this.outputs = new ArrayList<>();
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public List<String> getInputs() { return inputs; }
        public void setInputs(List<String> inputs) { this.inputs = inputs; }
        public List<String> getOutputs() { return outputs; }
        public void setOutputs(List<String> outputs) { this.outputs = outputs; }
    }
    
    public static class DesignDecision {
        private String decision;
        private String rationale;
        private String alternatives;
        private int priority;
        
        public DesignDecision(String decision, String rationale, int priority) {
            this.decision = decision;
            this.rationale = rationale;
            this.priority = priority;
            this.alternatives = "";
        }
        
        public String getDecision() { return decision; }
        public void setDecision(String decision) { this.decision = decision; }
        public String getRationale() { return rationale; }
        public void setRationale(String rationale) { this.rationale = rationale; }
        public String getAlternatives() { return alternatives; }
        public void setAlternatives(String alternatives) { this.alternatives = alternatives; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }
}
