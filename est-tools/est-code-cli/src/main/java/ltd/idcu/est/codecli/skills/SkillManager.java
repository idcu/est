package ltd.idcu.est.codecli.skills;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SkillManager {
    
    private final Map<String, EstSkill> skills = new ConcurrentHashMap<>();
    
    public SkillManager() {
        registerDefaultSkills();
    }
    
    private void registerDefaultSkills() {
        registerSkill(new CodeReviewSkill());
        registerSkill(new RefactorSkill());
        registerSkill(new ArchitectureSkill());
    }
    
    public void registerSkill(EstSkill skill) {
        skills.put(skill.getName(), skill);
    }
    
    public void unregisterSkill(String skillName) {
        skills.remove(skillName);
    }
    
    public EstSkill getSkill(String name) {
        return skills.get(name);
    }
    
    public List<EstSkill> getAllSkills() {
        return new ArrayList<>(skills.values());
    }
    
    public EstSkill findMatchingSkill(String userInput) {
        for (EstSkill skill : skills.values()) {
            if (skill.canHandle(userInput)) {
                return skill;
            }
        }
        return null;
    }
    
    public boolean hasSkill(String name) {
        return skills.containsKey(name);
    }
    
    public String listSkills() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available EST Skills:\n\n");
        
        for (EstSkill skill : skills.values()) {
            sb.append("  - ").append(skill.getName()).append("\n");
            sb.append("    ").append(skill.getDescription()).append("\n\n");
        }
        
        return sb.toString();
    }
}
