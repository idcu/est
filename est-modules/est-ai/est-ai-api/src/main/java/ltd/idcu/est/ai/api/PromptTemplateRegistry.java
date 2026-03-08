package ltd.idcu.est.ai.api;

import java.util.List;
import java.util.Optional;

public interface PromptTemplateRegistry {
    
    void register(PromptTemplate template);
    
    void unregister(String name);
    
    Optional<PromptTemplate> getTemplate(String name);
    
    List<PromptTemplate> getAllTemplates();
    
    List<PromptTemplate> getTemplatesByCategory(String category);
    
    List<String> getCategories();
    
    boolean hasTemplate(String name);
    
    void clear();
}
