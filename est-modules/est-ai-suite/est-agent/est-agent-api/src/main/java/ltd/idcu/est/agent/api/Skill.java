package ltd.idcu.est.agent.api;

public interface Skill {
    
    String getName();
    
    String getDescription();
    
    void initialize(SkillContext context);
    
    SkillResult execute(SkillInput input);
    
    void cleanup();
    
    boolean canHandle(String task);
    
    default int getPriority() {
        return 0;
    }
    
    default List<String> getDependencies() {
        return List.of();
    }
}
