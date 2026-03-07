package ltd.idcu.est.migration.rules;

import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.MigrationResult;

import java.util.HashMap;
import java.util.Map;

public class SpringBootImportMigrationRule extends AbstractMigrationRule {
    private static final Map<String, String> IMPORT_MAPPINGS = new HashMap<>();

    static {
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.RestController", "ltd.idcu.est.web.api.RestController");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.Controller", "ltd.idcu.est.web.api.Controller");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.GetMapping", "ltd.idcu.est.web.api.GetMapping");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.PostMapping", "ltd.idcu.est.web.api.PostMapping");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.PutMapping", "ltd.idcu.est.web.api.PutMapping");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.DeleteMapping", "ltd.idcu.est.web.api.DeleteMapping");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.RequestMapping", "ltd.idcu.est.web.api.RequestMapping");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.PathVariable", "ltd.idcu.est.web.api.PathVariable");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.RequestParam", "ltd.idcu.est.web.api.RequestParam");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.RequestBody", "ltd.idcu.est.web.api.RequestBody");
        IMPORT_MAPPINGS.put("org.springframework.web.bind.annotation.ResponseBody", "ltd.idcu.est.web.api.ResponseBody");
        IMPORT_MAPPINGS.put("org.springframework.stereotype.Component", "ltd.idcu.est.core.api.annotation.Component");
        IMPORT_MAPPINGS.put("org.springframework.stereotype.Service", "ltd.idcu.est.core.api.annotation.Component");
        IMPORT_MAPPINGS.put("org.springframework.stereotype.Repository", "ltd.idcu.est.features.data.api.Repository");
        IMPORT_MAPPINGS.put("org.springframework.context.annotation.Configuration", "ltd.idcu.est.core.api.annotation.Configuration");
        IMPORT_MAPPINGS.put("org.springframework.beans.factory.annotation.Autowired", "ltd.idcu.est.core.api.annotation.Inject");
        IMPORT_MAPPINGS.put("org.springframework.beans.factory.annotation.Value", "ltd.idcu.est.core.api.annotation.Value");
        IMPORT_MAPPINGS.put("org.springframework.transaction.annotation.Transactional", "ltd.idcu.est.features.data.api.Transactional");
        IMPORT_MAPPINGS.put("org.springframework.scheduling.annotation.Scheduled", "ltd.idcu.est.features.scheduler.api.Scheduled");
        IMPORT_MAPPINGS.put("org.springframework.context.event.EventListener", "ltd.idcu.est.features.event.api.EventListener");
        IMPORT_MAPPINGS.put("org.springframework.cache.annotation.Cacheable", "ltd.idcu.est.features.cache.api.Cacheable");
    }

    public SpringBootImportMigrationRule() {
        super(
                "spring-boot-import-migration",
                "Migrate Spring Boot imports to EST Framework imports",
                100
        );
    }

    @Override
    public boolean canApply(String content, MigrationConfig config) {
        if (config.getSourceFramework() != MigrationConfig.SourceFramework.SPRING_BOOT) {
            return false;
        }
        for (String oldImport : IMPORT_MAPPINGS.keySet()) {
            if (content.contains(oldImport)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String apply(String content, MigrationConfig config, MigrationResult result) {
        String newContent = content;
        for (Map.Entry<String, String> entry : IMPORT_MAPPINGS.entrySet()) {
            newContent = replaceImport(newContent, entry.getKey(), entry.getValue(), result);
        }
        return newContent;
    }
}
