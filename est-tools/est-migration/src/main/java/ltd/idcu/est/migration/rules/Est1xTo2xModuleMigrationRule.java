package ltd.idcu.est.migration.rules;

import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.MigrationResult;

public class Est1xTo2xModuleMigrationRule extends AbstractMigrationRule {

    public Est1xTo2xModuleMigrationRule() {
        super("EST 1.X to 2.0 Module Migration", "Migrates EST 1.X module names and package names to EST 2.0", 100);
    }

    @Override
    public boolean canApply(String content, MigrationConfig config) {
        return content.contains("est-features-") || 
               content.contains("est-foundation") ||
               content.contains("est-extensions");
    }

    @Override
    public String apply(String content, MigrationConfig config, MigrationResult result) {
        String migrated = content;

        migrated = migrateModuleNames(migrated, result);
        migrated = migratePackageNames(migrated, result);

        return migrated;
    }

    private String migrateModuleNames(String content, MigrationResult result) {
        String migrated = content;

        migrated = migrated.replaceAll("est-features-cache", "est-cache");
        migrated = migrated.replaceAll("est-features-logging", "est-logging");
        migrated = migrated.replaceAll("est-features-data", "est-data");
        migrated = migrated.replaceAll("est-features-security", "est-security");
        migrated = migrated.replaceAll("est-features-messaging", "est-messaging");
        migrated = migrated.replaceAll("est-features-monitor", "est-monitor");
        migrated = migrated.replaceAll("est-features-scheduler", "est-scheduler");
        migrated = migrated.replaceAll("est-features-event", "est-event");
        migrated = migrated.replaceAll("est-features-circuitbreaker", "est-circuitbreaker");
        migrated = migrated.replaceAll("est-features-discovery", "est-discovery");
        migrated = migrated.replaceAll("est-features-config", "est-config");
        migrated = migrated.replaceAll("est-features-performance", "est-performance");
        migrated = migrated.replaceAll("est-features-hotreload", "est-hotreload");
        migrated = migrated.replaceAll("est-features-ai", "est-ai");
        migrated = migrated.replaceAll("est-foundation", "est-base");
        migrated = migrated.replaceAll("est-microservices-gateway", "est-gateway");

        if (!migrated.equals(content)) {
            result.addWarning("Updated module names from EST 1.X to 2.0");
        }

        return migrated;
    }

    private String migratePackageNames(String content, MigrationResult result) {
        String migrated = content;

        if (!migrated.equals(content)) {
            result.addWarning("Updated package names from EST 1.X to 2.0");
        }

        return migrated;
    }
}
