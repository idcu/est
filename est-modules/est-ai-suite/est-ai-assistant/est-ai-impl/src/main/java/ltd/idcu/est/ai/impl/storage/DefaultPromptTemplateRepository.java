package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.storage.PromptTemplateData;
import ltd.idcu.est.ai.api.storage.PromptTemplateRepository;
import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.utils.format.json.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultPromptTemplateRepository implements PromptTemplateRepository {
    
    private static final String TEMPLATE_PREFIX = "template-";
    
    private final StorageProvider storageProvider;
    private final Map<String, PromptTemplate> templateCache;
    private final Map<String, List<PromptTemplate>> categoryCache;
    
    public DefaultPromptTemplateRepository(StorageProvider storageProvider, Map<String, PromptTemplate> templateCache) {
        this.storageProvider = storageProvider;
        this.templateCache = templateCache;
        this.categoryCache = new ConcurrentHashMap<>();
    }
    
    public DefaultPromptTemplateRepository(StorageProvider storageProvider) {
        this(storageProvider, new ConcurrentHashMap<>());
    }
    
    @Override
    public void save(PromptTemplate template) {
        String key = TEMPLATE_PREFIX + template.getName();
        PromptTemplateData data = toData(template);
        String json = JsonUtils.toJson(data);
        storageProvider.save(key, json);
        
        PromptTemplate oldTemplate = templateCache.put(template.getName(), template);
        if (oldTemplate != null && !oldTemplate.getCategory().equals(template.getCategory())) {
            invalidateCategoryCache(oldTemplate.getCategory());
        }
        invalidateCategoryCache(template.getCategory());
    }
    
    @Override
    public Optional<PromptTemplate> findById(String templateName) {
        if (templateCache.containsKey(templateName)) {
            return Optional.of(templateCache.get(templateName));
        }
        
        String key = TEMPLATE_PREFIX + templateName;
        String json = storageProvider.load(key);
        if (json != null) {
            PromptTemplateData data = JsonUtils.parseObject(json, PromptTemplateData.class);
            PromptTemplate template = fromData(data);
            templateCache.put(templateName, template);
            return Optional.of(template);
        }
        return Optional.empty();
    }
    
    @Override
    public List<PromptTemplate> findAll() {
        List<PromptTemplate> templates = new ArrayList<>();
        Map<String, String> allData = storageProvider.loadAll(TEMPLATE_PREFIX);
        
        for (Map.Entry<String, String> entry : allData.entrySet()) {
            try {
                PromptTemplateData data = JsonUtils.parseObject(entry.getValue(), PromptTemplateData.class);
                PromptTemplate template = fromData(data);
                templates.add(template);
                templateCache.put(template.getName(), template);
            } catch (Exception e) {
            }
        }
        return templates;
    }
    
    @Override
    public List<PromptTemplate> findByCategory(String category) {
        if (categoryCache.containsKey(category)) {
            return Collections.unmodifiableList(categoryCache.get(category));
        }
        
        List<PromptTemplate> templates = findAll().stream()
                .filter(template -> category.equals(template.getCategory()))
                .collect(Collectors.toList());
        
        categoryCache.put(category, new ArrayList<>(templates));
        return Collections.unmodifiableList(templates);
    }
    
    private void invalidateCategoryCache(String category) {
        categoryCache.remove(category);
    }
    
    @Override
    public void delete(String templateName) {
        String key = TEMPLATE_PREFIX + templateName;
        PromptTemplate oldTemplate = templateCache.get(templateName);
        
        storageProvider.delete(key);
        templateCache.remove(templateName);
        
        if (oldTemplate != null) {
            invalidateCategoryCache(oldTemplate.getCategory());
        }
    }
    
    @Override
    public void deleteAll() {
        storageProvider.deleteAll(TEMPLATE_PREFIX);
        templateCache.clear();
        categoryCache.clear();
    }
    
    @Override
    public boolean exists(String templateName) {
        if (templateCache.containsKey(templateName)) {
            return true;
        }
        String key = TEMPLATE_PREFIX + templateName;
        return storageProvider.exists(key);
    }
    
    @Override
    public void loadAll() {
        findAll();
    }
    
    @Override
    public void saveAll() {
        for (PromptTemplate template : templateCache.values()) {
            save(template);
        }
    }
    
    private PromptTemplateData toData(PromptTemplate template) {
        return new PromptTemplateData(
                template.getName(),
                template.getCategory(),
                template.getDescription(),
                template.getTemplate(),
                template.getRequiredVariables()
        );
    }
    
    private PromptTemplate fromData(PromptTemplateData data) {
        return new PromptTemplate() {
            @Override
            public String getName() {
                return data.getName();
            }
            
            @Override
            public String getCategory() {
                return data.getCategory();
            }
            
            @Override
            public String getDescription() {
                return data.getDescription();
            }
            
            @Override
            public String getTemplate() {
                return data.getTemplate();
            }
            
            @Override
            public String generate(Map<String, String> variables) {
                String result = data.getTemplate();
                for (Map.Entry<String, String> entry : variables.entrySet()) {
                    String placeholder = "{" + entry.getKey() + "}";
                    result = result.replace(placeholder, entry.getValue());
                }
                return result;
            }
            
            @Override
            public Map<String, String> getRequiredVariables() {
                return data.getRequiredVariables();
            }
            
            @Override
            public boolean isValid(Map<String, String> variables) {
                if (data.getRequiredVariables() == null) {
                    return true;
                }
                for (String varName : data.getRequiredVariables().keySet()) {
                    if (!variables.containsKey(varName)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
}
