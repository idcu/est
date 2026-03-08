package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.storage.SkillData;
import ltd.idcu.est.ai.api.storage.SkillRepository;
import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.utils.format.json.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultSkillRepository implements SkillRepository {
    
    private static final String SKILL_PREFIX = "skill-";
    
    private final StorageProvider storageProvider;
    private final Map<String, Skill> skillCache;
    private final Map<String, List<Skill>> categoryCache;
    
    public DefaultSkillRepository(StorageProvider storageProvider, Map<String, Skill> skillCache) {
        this.storageProvider = storageProvider;
        this.skillCache = skillCache;
        this.categoryCache = new ConcurrentHashMap<>();
    }
    
    public DefaultSkillRepository(StorageProvider storageProvider) {
        this(storageProvider, new ConcurrentHashMap<>());
    }
    
    @Override
    public void save(Skill skill) {
        String key = SKILL_PREFIX + skill.getName();
        SkillData data = toData(skill);
        String json = JsonUtils.toJson(data);
        storageProvider.save(key, json);
        
        Skill oldSkill = skillCache.put(skill.getName(), skill);
        if (oldSkill != null && !oldSkill.getCategory().equals(skill.getCategory())) {
            invalidateCategoryCache(oldSkill.getCategory());
        }
        invalidateCategoryCache(skill.getCategory());
    }
    
    @Override
    public Optional<Skill> findById(String skillName) {
        if (skillCache.containsKey(skillName)) {
            return Optional.of(skillCache.get(skillName));
        }
        
        String key = SKILL_PREFIX + skillName;
        String json = storageProvider.load(key);
        if (json != null) {
            SkillData data = JsonUtils.parseObject(json, SkillData.class);
            Skill skill = fromData(data);
            skillCache.put(skillName, skill);
            return Optional.of(skill);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Skill> findAll() {
        List<Skill> skills = new ArrayList<>();
        Map<String, String> allData = storageProvider.loadAll(SKILL_PREFIX);
        
        for (Map.Entry<String, String> entry : allData.entrySet()) {
            try {
                SkillData data = JsonUtils.parseObject(entry.getValue(), SkillData.class);
                Skill skill = fromData(data);
                skills.add(skill);
                skillCache.put(skill.getName(), skill);
            } catch (Exception e) {
            }
        }
        return skills;
    }
    
    @Override
    public List<Skill> findByCategory(String category) {
        if (categoryCache.containsKey(category)) {
            return Collections.unmodifiableList(categoryCache.get(category));
        }
        
        List<Skill> skills = findAll().stream()
                .filter(skill -> category.equals(skill.getCategory()))
                .collect(Collectors.toList());
        
        categoryCache.put(category, new ArrayList<>(skills));
        return Collections.unmodifiableList(skills);
    }
    
    private void invalidateCategoryCache(String category) {
        categoryCache.remove(category);
    }
    
    @Override
    public void delete(String skillName) {
        String key = SKILL_PREFIX + skillName;
        Skill oldSkill = skillCache.get(skillName);
        
        storageProvider.delete(key);
        skillCache.remove(skillName);
        
        if (oldSkill != null) {
            invalidateCategoryCache(oldSkill.getCategory());
        }
    }
    
    @Override
    public void deleteAll() {
        storageProvider.deleteAll(SKILL_PREFIX);
        skillCache.clear();
        categoryCache.clear();
    }
    
    @Override
    public boolean exists(String skillName) {
        if (skillCache.containsKey(skillName)) {
            return true;
        }
        String key = SKILL_PREFIX + skillName;
        return storageProvider.exists(key);
    }
    
    @Override
    public void loadAll() {
        findAll();
    }
    
    @Override
    public void saveAll() {
        for (Skill skill : skillCache.values()) {
            save(skill);
        }
    }
    
    private SkillData toData(Skill skill) {
        return new SkillData(
                skill.getName(),
                skill.getDescription(),
                skill.getCategory(),
                skill.getVersion(),
                skill.getInputSchema(),
                skill.getOutputSchema()
        );
    }
    
    private Skill fromData(SkillData data) {
        return new Skill() {
            @Override
            public String getName() {
                return data.getName();
            }
            
            @Override
            public String getDescription() {
                return data.getDescription();
            }
            
            @Override
            public String getCategory() {
                return data.getCategory();
            }
            
            @Override
            public String getVersion() {
                return data.getVersion();
            }
            
            @Override
            public Map<String, String> getInputSchema() {
                return data.getInputSchema();
            }
            
            @Override
            public Map<String, String> getOutputSchema() {
                return data.getOutputSchema();
            }
            
            @Override
            public ltd.idcu.est.ai.api.skill.SkillResult execute(Map<String, Object> inputs) {
                return ltd.idcu.est.ai.api.skill.SkillResult.failure(
                        "Persisted skill cannot be executed directly. " +
                        "Please use the original skill implementation."
                );
            }
            
            @Override
            public boolean canExecute(Map<String, Object> inputs) {
                return false;
            }
        };
    }
}
