package ltd.idcu.est.migration.rules;

import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.MigrationResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpringBootMainClassMigrationRule extends AbstractMigrationRule {
    private static final Pattern SPRING_BOOT_APPLICATION_PATTERN = Pattern.compile(
            "@SpringBootApplication\\s+public\\s+class\\s+(\\w+)"
    );
    private static final Pattern SPRING_APPLICATION_RUN_PATTERN = Pattern.compile(
            "SpringApplication\\.run\\((\\w+)\\.class,\\s*args\\)"
    );

    public SpringBootMainClassMigrationRule() {
        super(
                "spring-boot-main-class-migration",
                "Migrate Spring Boot main application class to EST Framework",
                80
        );
    }

    @Override
    public boolean canApply(String content, MigrationConfig config) {
        if (config.getSourceFramework() != MigrationConfig.SourceFramework.SPRING_BOOT) {
            return false;
        }
        return content.contains("@SpringBootApplication") || content.contains("SpringApplication.run");
    }

    @Override
    public String apply(String content, MigrationConfig config, MigrationResult result) {
        String newContent = content;

        Matcher classMatcher = SPRING_BOOT_APPLICATION_PATTERN.matcher(newContent);
        if (classMatcher.find()) {
            String className = classMatcher.group(1);
            newContent = newContent.replace("@SpringBootApplication", "");
            result.addWarning("Removed @SpringBootApplication from class: " + className);
        }

        Matcher runMatcher = SPRING_APPLICATION_RUN_PATTERN.matcher(newContent);
        if (runMatcher.find()) {
            String className = runMatcher.group(1);
            newContent = newContent.replace(
                    "SpringApplication.run(" + className + ".class, args)",
                    "ESTApplication.run(" + className + ".class, args)"
            );
            newContent = newContent.replace(
                    "import org.springframework.boot.SpringApplication;",
                    "import ltd.idcu.est.core.api.ESTApplication;"
            );
            result.addWarning("Replaced SpringApplication.run with ESTApplication.run");
        }

        return newContent;
    }
}
