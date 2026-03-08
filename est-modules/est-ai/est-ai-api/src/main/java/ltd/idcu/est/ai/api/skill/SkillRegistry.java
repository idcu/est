package ltd.idcu.est.ai.api.skill;

import java.util.List;
import java.util.Optional;

public interface SkillRegistry {

    void register(Skill skill);

    void unregister(String skillName);

    Optional<Skill> getSkill(String skillName);

    List<Skill> listAllSkills();

    List<Skill> listSkillsByCategory(String category);

    SkillResult execute(String skillName, java.util.Map<String, Object> inputs);
}
