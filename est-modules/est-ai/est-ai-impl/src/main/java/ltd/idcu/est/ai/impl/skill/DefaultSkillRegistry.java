package ltd.idcu.est.ai.impl.skill;

import ltd.idcu.est.ai.api.skill.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSkillRegistry implements SkillRegistry {

    private final Map<String, Skill> skills = new ConcurrentHashMap<>();

    public DefaultSkillRegistry() {
        initializeDefaultSkills();
    }

    private void initializeDefaultSkills() {
        register(new GenerateEntitySkill());
        register(new GenerateServiceSkill());
        register(new GenerateControllerSkill());
        register(new CodeReviewSkill());
    }

    @Override
    public void register(Skill skill) {
        skills.put(skill.getName(), skill);
    }

    @Override
    public void unregister(String skillName) {
        skills.remove(skillName);
    }

    @Override
    public Optional<Skill> getSkill(String skillName) {
        return Optional.ofNullable(skills.get(skillName));
    }

    @Override
    public List<Skill> listAllSkills() {
        return new ArrayList<>(skills.values());
    }

    @Override
    public List<Skill> listSkillsByCategory(String category) {
        return skills.values().stream()
                .filter(skill -> skill.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public SkillResult execute(String skillName, Map<String, Object> inputs) {
        Skill skill = skills.get(skillName);
        if (skill == null) {
            return SkillResult.failure("Skill not found: " + skillName);
        }

        if (!skill.canExecute(inputs)) {
            return SkillResult.failure("Cannot execute skill: " + skillName + " - invalid inputs");
        }

        long startTime = System.currentTimeMillis();
        try {
            SkillResult result = skill.execute(inputs);
            long executionTime = System.currentTimeMillis() - startTime;
            return new SkillResult(
                    result.isSuccess(),
                    result.getMessage(),
                    result.getOutputs(),
                    executionTime
            );
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            return new SkillResult(
                    false,
                    "Error executing skill: " + e.getMessage(),
                    null,
                    executionTime
            );
        }
    }
}
