package ltd.idcu.est.ai.api.storage;

import ltd.idcu.est.ai.api.skill.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepository {
    
    void save(Skill skill);
    
    Optional<Skill> findById(String skillName);
    
    List<Skill> findAll();
    
    List<Skill> findByCategory(String category);
    
    void delete(String skillName);
    
    void deleteAll();
    
    boolean exists(String skillName);
    
    void loadAll();
    
    void saveAll();
}
