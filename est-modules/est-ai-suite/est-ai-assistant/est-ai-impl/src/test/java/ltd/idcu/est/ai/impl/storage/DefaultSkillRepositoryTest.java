package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;
import ltd.idcu.est.ai.api.storage.SkillRepository;
import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultSkillRepositoryTest {
    
    @Test
    public void testSaveAndFindById() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        Skill skill = createTestSkill("test-skill", "Test Description", "test-category");
        repository.save(skill);
        
        Optional<Skill> found = repository.findById("test-skill");
        assertTrue(found.isPresent());
        assertEquals("test-skill", found.get().getName());
        assertEquals("Test Description", found.get().getDescription());
        assertEquals("test-category", found.get().getCategory());
    }
    
    @Test
    public void testFindAll() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        repository.save(createTestSkill("skill1", "Desc1", "cat1"));
        repository.save(createTestSkill("skill2", "Desc2", "cat1"));
        repository.save(createTestSkill("skill3", "Desc3", "cat2"));
        
        List<Skill> allSkills = repository.findAll();
        assertEquals(3, allSkills.size());
    }
    
    @Test
    public void testFindByCategory() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        repository.save(createTestSkill("skill1", "Desc1", "cat1"));
        repository.save(createTestSkill("skill2", "Desc2", "cat1"));
        repository.save(createTestSkill("skill3", "Desc3", "cat2"));
        
        List<Skill> cat1Skills = repository.findByCategory("cat1");
        assertEquals(2, cat1Skills.size());
        
        List<Skill> cat2Skills = repository.findByCategory("cat2");
        assertEquals(1, cat2Skills.size());
    }
    
    @Test
    public void testDelete() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        Skill skill = createTestSkill("skill-to-delete", "Desc", "cat");
        repository.save(skill);
        
        assertTrue(repository.exists("skill-to-delete"));
        repository.delete("skill-to-delete");
        assertFalse(repository.exists("skill-to-delete"));
    }
    
    @Test
    public void testDeleteAll() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        repository.save(createTestSkill("skill1", "Desc1", "cat1"));
        repository.save(createTestSkill("skill2", "Desc2", "cat1"));
        
        assertEquals(2, repository.findAll().size());
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
    }
    
    @Test
    public void testExists() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        assertFalse(repository.exists("non-existent"));
        repository.save(createTestSkill("test-skill", "Desc", "cat"));
        assertTrue(repository.exists("test-skill"));
    }
    
    @Test
    public void testCache() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        Skill skill = createTestSkill("cached-skill", "Desc", "cat");
        repository.save(skill);
        
        Optional<Skill> firstFind = repository.findById("cached-skill");
        Optional<Skill> secondFind = repository.findById("cached-skill");
        
        assertTrue(firstFind.isPresent());
        assertTrue(secondFind.isPresent());
    }
    
    @Test
    public void testFindNonExistent() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        SkillRepository repository = new DefaultSkillRepository(storageProvider);
        
        Optional<Skill> found = repository.findById("non-existent");
        assertFalse(found.isPresent());
    }
    
    private Skill createTestSkill(String name, String description, String category) {
        return new Skill() {
            @Override
            public String getName() { return name; }
            
            @Override
            public String getDescription() { return description; }
            
            @Override
            public String getCategory() { return category; }
            
            @Override
            public String getVersion() { return "1.0.0"; }
            
            @Override
            public Map<String, String> getInputSchema() { return Map.of(); }
            
            @Override
            public Map<String, String> getOutputSchema() { return Map.of(); }
            
            @Override
            public SkillResult execute(Map<String, Object> inputs) {
                return SkillResult.success("Test result");
            }
            
            @Override
            public boolean canExecute(Map<String, Object> inputs) {
                return true;
            }
        };
    }
}
