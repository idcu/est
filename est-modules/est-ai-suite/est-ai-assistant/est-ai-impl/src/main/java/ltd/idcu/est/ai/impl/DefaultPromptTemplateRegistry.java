package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.PromptTemplateRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultPromptTemplateRegistry implements PromptTemplateRegistry {
    
    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
    
    @Override
    public void register(PromptTemplate template) {
        templates.put(template.getName(), template);
    }
    
    @Override
    public void unregister(String name) {
        templates.remove(name);
    }
    
    @Override
    public Optional<PromptTemplate> getTemplate(String name) {
        return Optional.ofNullable(templates.get(name));
    }
    
    @Override
    public List<PromptTemplate> getAllTemplates() {
        return new ArrayList<>(templates.values());
    }
    
    @Override
    public List<PromptTemplate> getTemplatesByCategory(String category) {
        return templates.values().stream()
                .filter(t -> t.getCategory().equals(category))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getCategories() {
        return templates.values().stream()
                .map(PromptTemplate::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasTemplate(String name) {
        return templates.containsKey(name);
    }
    
    @Override
    public void clear() {
        templates.clear();
    }
}
