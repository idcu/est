package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.storage.PromptTemplateRepository;
import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultPromptTemplateRepositoryTest {
    
    @Test
    public void testSaveAndFindById() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        PromptTemplate template = createTestTemplate("test-template", "Test Category", "Test Description", "Hello {name}!");
        repository.save(template);
        
        Optional<PromptTemplate> found = repository.findById("test-template");
        assertTrue(found.isPresent());
        assertEquals("test-template", found.get().getName());
        assertEquals("Test Category", found.get().getCategory());
        assertEquals("Test Description", found.get().getDescription());
    }
    
    @Test
    public void testFindAll() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        repository.save(createTestTemplate("template1", "cat1", "Desc1", "Template 1"));
        repository.save(createTestTemplate("template2", "cat1", "Desc2", "Template 2"));
        repository.save(createTestTemplate("template3", "cat2", "Desc3", "Template 3"));
        
        List<PromptTemplate> allTemplates = repository.findAll();
        assertEquals(3, allTemplates.size());
    }
    
    @Test
    public void testFindByCategory() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        repository.save(createTestTemplate("template1", "cat1", "Desc1", "Template 1"));
        repository.save(createTestTemplate("template2", "cat1", "Desc2", "Template 2"));
        repository.save(createTestTemplate("template3", "cat2", "Desc3", "Template 3"));
        
        List<PromptTemplate> cat1Templates = repository.findByCategory("cat1");
        assertEquals(2, cat1Templates.size());
        
        List<PromptTemplate> cat2Templates = repository.findByCategory("cat2");
        assertEquals(1, cat2Templates.size());
    }
    
    @Test
    public void testDelete() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        PromptTemplate template = createTestTemplate("template-to-delete", "cat", "Desc", "Content");
        repository.save(template);
        
        assertTrue(repository.exists("template-to-delete"));
        repository.delete("template-to-delete");
        assertFalse(repository.exists("template-to-delete"));
    }
    
    @Test
    public void testDeleteAll() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        repository.save(createTestTemplate("template1", "cat1", "Desc1", "Template 1"));
        repository.save(createTestTemplate("template2", "cat1", "Desc2", "Template 2"));
        
        assertEquals(2, repository.findAll().size());
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
    }
    
    @Test
    public void testExists() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        assertFalse(repository.exists("non-existent"));
        repository.save(createTestTemplate("test-template", "cat", "Desc", "Content"));
        assertTrue(repository.exists("test-template"));
    }
    
    @Test
    public void testCache() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        PromptTemplate template = createTestTemplate("cached-template", "cat", "Desc", "Content");
        repository.save(template);
        
        Optional<PromptTemplate> firstFind = repository.findById("cached-template");
        Optional<PromptTemplate> secondFind = repository.findById("cached-template");
        
        assertTrue(firstFind.isPresent());
        assertTrue(secondFind.isPresent());
    }
    
    @Test
    public void testFindNonExistent() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        Optional<PromptTemplate> found = repository.findById("non-existent");
        assertFalse(found.isPresent());
    }
    
    @Test
    public void testTemplateGenerate() {
        StorageProvider storageProvider = new MemoryStorageProvider();
        PromptTemplateRepository repository = new DefaultPromptTemplateRepository(storageProvider);
        
        PromptTemplate template = createTestTemplate("greeting", "general", "Greeting template", "Hello {name}! Welcome to {place}!");
        repository.save(template);
        
        Optional<PromptTemplate> found = repository.findById("greeting");
        assertTrue(found.isPresent());
        
        String result = found.get().generate(Map.of("name", "Alice", "place", "EST"));
        assertEquals("Hello Alice! Welcome to EST!", result);
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
}
