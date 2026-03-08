package ltd.idcu.est.ai.api;

public class ArchitecturePattern {
    
    private String id;
    private String name;
    private String description;
    private String category;
    private boolean isRecommended;
    private String useCase;
    
    public ArchitecturePattern() {
    }
    
    public static ArchitecturePattern create(String id, String name) {
        ArchitecturePattern pattern = new ArchitecturePattern();
        pattern.id = id;
        pattern.name = name;
        return pattern;
    }
    
    public ArchitecturePattern description(String description) {
        this.description = description;
        return this;
    }
    
    public ArchitecturePattern category(String category) {
        this.category = category;
        return this;
    }
    
    public ArchitecturePattern recommended(boolean isRecommended) {
        this.isRecommended = isRecommended;
        return this;
    }
    
    public ArchitecturePattern useCase(String useCase) {
        this.useCase = useCase;
        return this;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isRecommended() {
        return isRecommended;
    }
    
    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }
    
    public String getUseCase() {
        return useCase;
    }
    
    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }
}
