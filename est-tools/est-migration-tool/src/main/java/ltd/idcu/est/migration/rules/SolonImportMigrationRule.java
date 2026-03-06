package ltd.idcu.est.migration.rules;

import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.MigrationResult;

import java.util.HashMap;
import java.util.Map;

public class SolonImportMigrationRule extends AbstractMigrationRule {
    private static final Map<String, String> IMPORT_MAPPINGS = new HashMap<>();

    static {
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Controller", "ltd.idcu.est.web.api.Controller");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.RestController", "ltd.idcu.est.web.api.RestController");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Mapping", "ltd.idcu.est.web.api.RequestMapping");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Get", "ltd.idcu.est.web.api.GetMapping");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Post", "ltd.idcu.est.web.api.PostMapping");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Put", "ltd.idcu.est.web.api.PutMapping");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Delete", "ltd.idcu.est.web.api.DeleteMapping");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Path", "ltd.idcu.est.web.api.PathVariable");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Param", "ltd.idcu.est.web.api.RequestParam");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Body", "ltd.idcu.est.web.api.RequestBody");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Component", "ltd.idcu.est.core.api.annotation.Component");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Service", "ltd.idcu.est.core.api.annotation.Component");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Dao", "ltd.idcu.est.features.data.api.Repository");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Configuration", "ltd.idcu.est.core.api.annotation.Configuration");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Inject", "ltd.idcu.est.core.api.annotation.Inject");
        IMPORT_MAPPINGS.put("org.noear.solon.cloud.annotation.CloudConfig", "ltd.idcu.est.core.api.annotation.Value");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.Transaction", "ltd.idcu.est.features.data.api.Transactional");
        IMPORT_MAPPINGS.put("org.noear.solon.scheduling.annotation.Scheduled", "ltd.idcu.est.features.scheduler.api.Scheduled");
        IMPORT_MAPPINGS.put("org.noear.solon.annotation.EventListener", "ltd.idcu.est.features.event.api.EventListener");
    }

    public SolonImportMigrationRule() {
        super(
                "solon-import-migration",
                "Migrate Solon imports to EST Framework imports",
                100
        );
    }

    @Override
    public boolean canApply(String content, MigrationConfig config) {
        if (config.getSourceFramework() != MigrationConfig.SourceFramework.SOLON) {
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
