package ltd.idcu.est.migration;

import ltd.idcu.est.migration.rules.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MigrationEngine {
    private final MigrationConfig config;
    private final List<MigrationRule> rules;

    public MigrationEngine(MigrationConfig config) {
        this.config = config;
        this.rules = new ArrayList<>();
        registerDefaultRules();
    }

    private void registerDefaultRules() {
        rules.add(new SpringBootImportMigrationRule());
        rules.add(new SpringBootAnnotationMigrationRule());
        rules.add(new SpringBootMainClassMigrationRule());
        rules.add(new SolonImportMigrationRule());
        rules.add(new ltd.idcu.est.migration.rules.Est1xTo2xModuleMigrationRule());
        rules.add(new ltd.idcu.est.migration.rules.Est1xTo2xPomMigrationRule());
        rules.sort(Comparator.comparingInt(MigrationRule::getPriority).reversed());
    }

    public void addRule(MigrationRule rule) {
        rules.add(rule);
        rules.sort(Comparator.comparingInt(MigrationRule::getPriority).reversed());
    }

    public MigrationResult migrate() {
        MigrationResult result = new MigrationResult();

        if (config.getSourceDirectory() == null || !config.getSourceDirectory().exists()) {
            result.addError("Source directory does not exist: " + config.getSourceDirectory());
            return result;
        }

        if (config.getTargetDirectory() == null) {
            config.setTargetDirectory(config.getSourceDirectory());
        }

        try {
            if (config.isBackup()) {
                createBackup(result);
            }

            migrateDirectory(config.getSourceDirectory(), config.getTargetDirectory(), result);

            result.setSummary(generateSummary(result));

        } catch (Exception e) {
            result.addError("Migration failed: " + e.getMessage());
        }

        return result;
    }

    private void createBackup(MigrationResult result) throws IOException {
        File backupDir = new File(config.getSourceDirectory().getParent(), config.getSourceDirectory().getName() + ".backup");
        if (backupDir.exists()) {
            deleteDirectory(backupDir.toPath());
        }
        copyDirectory(config.getSourceDirectory().toPath(), backupDir.toPath());
        result.addWarning("Created backup at: " + backupDir.getAbsolutePath());
    }

    private void migrateDirectory(File sourceDir, File targetDir, MigrationResult result) {
        if (!sourceDir.isDirectory()) {
            return;
        }

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File[] files = sourceDir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }

            if (!config.shouldIncludeFile(file.getName())) {
                continue;
            }

            if (file.isDirectory()) {
                migrateDirectory(file, new File(targetDir, file.getName()), result);
            } else {
                migrateFile(file, new File(targetDir, file.getName()), result);
            }
        }
    }

    private void migrateFile(File sourceFile, File targetFile, MigrationResult result) {
        try {
            String content = Files.readString(sourceFile.toPath());
            String migratedContent = content;
            boolean changed = false;

            for (MigrationRule rule : rules) {
                if (rule.canApply(migratedContent, config)) {
                    String newContent = rule.apply(migratedContent, config, result);
                    if (!newContent.equals(migratedContent)) {
                        migratedContent = newContent;
                        changed = true;
                    }
                }
            }

            if (changed || !sourceFile.equals(targetFile)) {
                if (targetFile.exists() && !config.isOverwrite()) {
                    result.addWarning("Skipped existing file: " + targetFile.getAbsolutePath());
                    return;
                }
                Files.writeString(targetFile.toPath(), migratedContent);
                result.addMigratedFile(targetFile.getAbsolutePath());
            }

        } catch (IOException e) {
            result.addError("Failed to migrate file: " + sourceFile.getAbsolutePath() + " - " + e.getMessage());
        }
    }

    private void copyDirectory(Path source, Path target) throws IOException {
        Files.walk(source).forEach(path -> {
            try {
                Path targetPath = target.resolve(source.relativize(path));
                if (Files.isDirectory(path)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void deleteDirectory(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private String generateSummary(MigrationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== EST Framework Migration Summary ===\n\n");
        sb.append("Source Framework: ").append(config.getSourceFramework()).append("\n");
        sb.append("Source Directory: ").append(config.getSourceDirectory()).append("\n");
        sb.append("Target Directory: ").append(config.getTargetDirectory()).append("\n\n");
        sb.append("Total files migrated: ").append(result.getMigratedFiles().size()).append("\n");
        sb.append("Warnings: ").append(result.getWarnings().size()).append("\n");
        sb.append("Errors: ").append(result.getErrors().size()).append("\n\n");
        sb.append("Next steps:\n");
        sb.append("1. Review the migrated files\n");
        sb.append("2. Check for any warnings that need manual attention\n");
        sb.append("3. Update your pom.xml dependencies to use EST Framework\n");
        sb.append("4. Build and test your application\n");
        return sb.toString();
    }
}
