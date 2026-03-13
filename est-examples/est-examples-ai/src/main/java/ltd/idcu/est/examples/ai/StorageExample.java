package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.impl.DefaultPromptTemplate;
import ltd.idcu.est.ai.impl.skill.GenerateEntitySkill;
import ltd.idcu.est.ai.impl.storage.DefaultPromptTemplateRepository;
import ltd.idcu.est.ai.impl.storage.DefaultSkillRepository;
import ltd.idcu.est.ai.impl.storage.JsonFileStorageProvider;
import ltd.idcu.est.ai.impl.storage.MemoryStorageProvider;
import ltd.idcu.est.ai.impl.storage.StorageProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class StorageExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST AI Storage System Example");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("This example demonstrates EST AI's storage system:");
        System.out.println("  - Memory storage (MemoryStorageProvider)");
        System.out.println("  - JSON file storage (JsonFileStorageProvider)");
        System.out.println("  - Prompt template persistence");
        System.out.println("  - Skill persistence");
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("Part 1: Storage Providers");
        System.out.println("=".repeat(60));

        storageProviderExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 2: Prompt Template Storage");
        System.out.println("=".repeat(60));

        promptTemplateStorageExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 3: Skill Storage");
        System.out.println("=".repeat(60));

        skillStorageExample();

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Storage system example run complete");
        System.out.println("=".repeat(60));
    }

    public static void run() {
        main(new String[]{});
    }

    private static void storageProviderExample() {
        System.out.println("\n--- Storage Provider Example ---");
        System.out.println("EST AI supports multiple storage methods, choose according to needs\n");

        System.out.println("[Method 1] Memory storage (MemoryStorageProvider)");
        System.out.println("Data stored in memory, extremely fast read/write speed");
        System.out.println("Suitable for temporary data, cache scenarios\n");

        StorageProvider memoryStorage = new MemoryStorageProvider();

        System.out.println("1. Basic storage operations:");
        memoryStorage.save("key1", "value1");
        memoryStorage.save("key2", "value2");
        memoryStorage.save("key3", "value3");

        System.out.println("   - key1 = " + memoryStorage.load("key1"));
        System.out.println("   - key2 = " + memoryStorage.load("key2"));

        System.out.println("\n2. Check if key exists:");
        System.out.println("   - Does key1 exist? " + memoryStorage.exists("key1"));
        System.out.println("   - Does key99 exist? " + memoryStorage.exists("key99"));

        System.out.println("\n3. Delete key:");
        memoryStorage.delete("key3");
        System.out.println("   - After deleting key3, key count: " + memoryStorage.keys().size());

        System.out.println("\n4. Get all keys:");
        System.out.println("   - All keys: " + memoryStorage.keys());

        System.out.println("\n5. Clear storage:");
        memoryStorage.clear();
        System.out.println("   - After clearing, key count: " + memoryStorage.keys().size());

        System.out.println("\n[Method 2] JSON file storage (JsonFileStorageProvider)");
        System.out.println("Data persisted to JSON file, survives restarts");
        System.out.println("Suitable for storing configuration, data that needs persistence\n");

        Path storageDir = Paths.get("ai-storage");
        StorageProvider jsonStorage = new JsonFileStorageProvider(storageDir);

        System.out.println("1. Write to JSON storage:");
        jsonStorage.save("config:theme", "dark");
        jsonStorage.save("config:language", "zh-CN");
        jsonStorage.save("config:fontSize", "14px");

        System.out.println("   - Theme: " + jsonStorage.load("config:theme"));
        System.out.println("   - Language: " + jsonStorage.load("config:language"));

        System.out.println("\n2. Storage directory: " + storageDir.toAbsolutePath());
        System.out.println("   - Data persisted to file");

        System.out.println("\n[Method 3] Storage statistics and advanced features");
        System.out.println("Storage system provides unified interface\n");

        System.out.println("   - Supported operations: save, load, delete, exists, keys, clear");
        System.out.println("   - Extensible: can implement custom StorageProvider");
        System.out.println("   - Flexible: choose appropriate storage method according to scenario");

        System.out.println("\n[X] Storage provider example complete\n");
    }

    private static void promptTemplateStorageExample() {
        System.out.println("\n--- Prompt Template Storage Example ---");
        System.out.println("Prompt templates can be persisted for reuse and sharing\n");

        System.out.println("[Method 1] Using memory storage");
        System.out.println("Suitable for development debugging, temporary templates\n");

        DefaultPromptTemplateRepository memoryRepo = new DefaultPromptTemplateRepository(new MemoryStorageProvider());

        System.out.println("1. Create and save prompt template:");
        PromptTemplate template1 = new DefaultPromptTemplate(
                "web-app-basic",
                "web",
                "Basic Web application template",
                """
                Please create a basic Web application with requirements:
                1. Class name: {className}
                2. Application name: {appName}
                3. Welcome message: {welcomeMessage}
                """,
                List.of("className", "appName", "welcomeMessage")
        );
        memoryRepo.save(template1);

        PromptTemplate template2 = new DefaultPromptTemplate(
                "entity-basic",
                "code-generation",
                "Basic Entity template",
                """
                Please create an Entity class:
                1. Class name: {entityName}
                2. Package name: {packageName}
                3. Fields: {fields}
                """,
                List.of("entityName", "packageName", "fields")
        );
        memoryRepo.save(template2);

        System.out.println("   - Saved 2 templates");

        System.out.println("\n2. Load template:");
        PromptTemplate loadedTemplate = memoryRepo.load("web-app-basic");
        System.out.println("   - Template name: " + loadedTemplate.getName());
        System.out.println("   - Template category: " + loadedTemplate.getCategory());
        System.out.println("   - Template description: " + loadedTemplate.getDescription());

        System.out.println("\n3. List all templates:");
        List<PromptTemplate> allTemplates = memoryRepo.loadAll();
        for (PromptTemplate template : allTemplates) {
            System.out.println("   - " + template.getName() + " (" + template.getCategory() + ")");
        }

        System.out.println("\n4. List templates by category:");
        List<PromptTemplate> webTemplates = memoryRepo.loadByCategory("web");
        System.out.println("   - Web category template count: " + webTemplates.size());

        System.out.println("\n5. Delete template:");
        memoryRepo.delete("entity-basic");
        System.out.println("   - Remaining template count after deletion: " + memoryRepo.loadAll().size());

        System.out.println("\n[Method 2] Using JSON file storage");
        System.out.println("Suitable for production environment, persistent storage\n");

        Path templateDir = Paths.get("ai-templates");
        DefaultPromptTemplateRepository jsonRepo = new DefaultPromptTemplateRepository(
                new JsonFileStorageProvider(templateDir)
        );

        System.out.println("1. Save template to file:");
        jsonRepo.save(template1);
        System.out.println("   - Template saved to: " + templateDir.toAbsolutePath());

        System.out.println("\n2. Load template from file:");
        PromptTemplate fromFile = jsonRepo.load("web-app-basic");
        System.out.println("   - Loaded from file successfully: " + fromFile.getName());

        System.out.println("\n[X] Prompt template storage example complete\n");
    }

    private static void skillStorageExample() {
        System.out.println("\n--- Skill Storage Example ---");
        System.out.println("Skills can be persisted for reuse and customization\n");

        System.out.println("[Method 1] Using memory storage");
        System.out.println("Suitable for development debugging\n");

        DefaultSkillRepository memoryRepo = new DefaultSkillRepository(new MemoryStorageProvider());

        System.out.println("1. Create and save Skill:");
        Skill entitySkill = new GenerateEntitySkill();
        memoryRepo.save(entitySkill);

        System.out.println("   - Saved Skill: " + entitySkill.getName());

        System.out.println("\n2. Load Skill:");
        Skill loadedSkill = memoryRepo.load(entitySkill.getId());
        System.out.println("   - Skill name: " + loadedSkill.getName());
        System.out.println("   - Skill description: " + loadedSkill.getDescription());

        System.out.println("\n3. Execute Skill:");
        Map<String, Object> inputs = Map.of(
                "className", "Product",
                "packageName", "com.example.entity",
                "fields", List.of("id:Long", "name:String", "price:Double")
        );
        var result = loadedSkill.execute(inputs);
        System.out.println("   - Skill executed successfully: " + result.isSuccess());
        if (result.isSuccess()) {
            System.out.println("   - Generated code:\n" + result.getOutputs().get("code"));
        }

        System.out.println("\n4. List all Skills:");
        List<Skill> allSkills = memoryRepo.loadAll();
        for (Skill skill : allSkills) {
            System.out.println("   - " + skill.getName() + " (" + skill.getCategory() + ")");
        }

        System.out.println("\n[Method 2] Using JSON file storage");
        System.out.println("Suitable for production environment, persistent storage\n");

        Path skillDir = Paths.get("ai-skills");
        DefaultSkillRepository jsonRepo = new DefaultSkillRepository(
                new JsonFileStorageProvider(skillDir)
        );

        System.out.println("1. Save Skill to file:");
        jsonRepo.save(entitySkill);
        System.out.println("   - Skill saved to: " + skillDir.toAbsolutePath());

        System.out.println("\n2. Load Skill from file:");
        Skill fromFile = jsonRepo.load(entitySkill.getId());
        System.out.println("   - Loaded from file successfully: " + fromFile.getName());

        System.out.println("\n[X] Skill storage example complete\n");
    }
}
