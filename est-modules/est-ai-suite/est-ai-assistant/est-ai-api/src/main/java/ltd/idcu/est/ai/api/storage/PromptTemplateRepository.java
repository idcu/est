package ltd.idcu.est.ai.api.storage;

import ltd.idcu.est.ai.api.PromptTemplate;

import java.util.List;
import java.util.Optional;

public interface PromptTemplateRepository {
    
    void save(PromptTemplate template);
    
    Optional<PromptTemplate> findById(String templateName);
    
    List<PromptTemplate> findAll();
    
    List<PromptTemplate> findByCategory(String category);
    
    void delete(String templateName);
    
    void deleteAll();
    
    boolean exists(String templateName);
    
    void loadAll();
    
    void saveAll();
}
