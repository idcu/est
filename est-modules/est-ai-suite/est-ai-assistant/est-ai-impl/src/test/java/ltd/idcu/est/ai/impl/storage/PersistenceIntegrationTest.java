package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;
import ltd.idcu.est.ai.api.storage.PromptTemplateRepository;
import ltd.idcu.est.ai.api.storage.SkillRepository;
import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;
import java.util.Optional;

public class PersistenceIntegrationTest {
    
    @Test
    public void testAiAssistantWithMemoryStorage() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        AiAssistant assistant = new DefaultAiAssistant(storageProvider);
        
        assertNotNull(assistant.getStorageProvider());
        assertEquals("memory", assistant.getStorageProvider().getName());
        assertNotNull(assistant.getSkillRepository());
        assertNotNull(assistant.getPromptTemplateRepository());
    }
    
    @Test
    public void testAiAssistantSkillPersistence() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        AiAssistant assistant = new DefaultAiAssistant(storageProvider);
        
        SkillRepository skillRepo = assistant.getSkillRepository();
        Skill testSkill = createTestSkill("integration-skill", "Integration Test", "test");
        skillRepo.save(testSkill);
        
        Optional<Skill> found = skillRepo.findById("integration-skill");
        assertTrue(found.isPresent());
        assertEquals("integration-skill", found.get().getName());
    }
    
    @Test
    public void testAiAssistantTemplatePersistence() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        AiAssistant assistant = new DefaultAiAssistant(storageProvider);
        
        PromptTemplateRepository templateRepo = assistant.getPromptTemplateRepository();
        PromptTemplate testTemplate = createTestTemplate("integration-template", "integration", "Integration Test", "Test {content}");
        templateRepo.save(testTemplate);
        
        Optional<PromptTemplate> found = templateRepo.findById("integration-template");
        assertTrue(found.isPresent());
        assertEquals("integration-template", found.get().getName());
    }
    
    @Test
    public void testStorageProviderSwitch() {
        StorageProvider storageProvider1 = new MemoryStorageProvider();
        AiAssistant assistant = new DefaultAiAssistant(storageProvider1);
        
        assertEquals("memory", assistant.getStorageProvider().getName());
        
        StorageProvider storageProvider2 = new JsonFileStorageProvider("test-switch-storage");
        assistant.setStorageProvider(storageProvider2);
        
        assertEquals("json-file", assistant.getStorageProvider().getName());
        cleanUpSwitchStorage();
    }
    
    @Test
    public void testJsonFileStoragePersistence() {
        String storageDir = "test-persistence-storage";
        
        Skill testSkill = createTestSkill("persistent-skill", "Persistent", "test");
        PromptTemplate testTemplate = createTestTemplate("persistent-template", "persistent", "Persistent", "Content {x}");
        
        StorageProvider storageProvider1 = new JsonFileStorageProvider(storageDir);
        AiAssistant assistant1 = new DefaultAiAssistant(storageProvider1);
        assistant1.getSkillRepository().save(testSkill);
        assistant1.getPromptTemplateRepository().save(testTemplate);
        
        StorageProvider storageProvider2 = new JsonFileStorageProvider(storageDir);
        AiAssistant assistant2 = new DefaultAiAssistant(storageProvider2);
        
        Optional<Skill> foundSkill = assistant2.getSkillRepository().findById("persistent-skill");
        Optional<PromptTemplate> foundTemplate = assistant2.getPromptTemplateRepository().findById("persistent-template");
        
        assertTrue(foundSkill.isPresent());
        assertTrue(foundTemplate.isPresent());
        cleanUpPersistenceStorage(storageDir);
    }
    
    @Test
    public void testSkillRegistryAndRepository() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        AiAssistant assistant = new DefaultAiAssistant(storageProvider);
        
        assertNotNull(assistant.getSkillRegistry());
        assertNotNull(assistant.getSkillRepository());
        
        Skill testSkill = createTestSkill("registry-test-skill", "Registry Test", "test");
        assistant.getSkillRepository().save(testSkill);
        
        assertFalse(assistant.getSkillRegistry().getSkill("registry-test-skill").isPresent());
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
                return SkillResult.success("Integration test result");
            }
            
            @Override
            public boolean canExecute(Map<String, Object> inputs) {
                return true;
            }
        };
    }
    
    private PromptTemplate createTestTemplate(String name, String category, String description, String templateContent) {
        return new PromptTemplate() {
            @Override
            public String getName() { return name; }
            
            @Override
            public String getCategory() { return category; }
            
            @Override
            public String getDescription() { return description; }
            
            @Override
            public String getTemplate() { return templateContent; }
            
            @Override
            public String generate(Map<String, String> variables) {
                String result = templateContent;
                for (Map.Entry<String, String> entry : variables.entrySet()) {
                    String placeholder = "{" + entry.getKey() + "}";
                    result = result.replace(placeholder, entry.getValue());
                }
                return result;
            }
            
            @Override
            public Map<String, String> getRequiredVariables() { return Map.of(); }
            
            @Override
            public boolean isValid(Map<String, String> variables) { return true; }
        };
    }
    
    private void cleanUpSwitchStorage() {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("test-switch-storage");
            if (java.nio.file.Files.exists(path)) {
                java.nio.file.Files.list(path)
                     .filter(p -> p.toString().endsWith(".json"))
                     .forEach(p -> {
                         try {
                             java.nio.file.Files.deleteIfExists(p);
                         } catch (Exception e) {}
                     });
                java.nio.file.Files.deleteIfExists(path);
            }
        } catch (Exception e) {}
    }
    
    private void cleanUpPersistenceStorage(String dir) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(dir);
            if (java.nio.file.Files.exists(path)) {
                java.nio.file.Files.list(path)
                     .filter(p -> p.toString().endsWith(".json"))
                     .forEach(p -> {
                         try {
                             java.nio.file.Files.deleteIfExists(p);
                         } catch (Exception e) {}
                     });
                java.nio.file.Files.deleteIfExists(path);
            }
        } catch (Exception e) {}
    }
}
