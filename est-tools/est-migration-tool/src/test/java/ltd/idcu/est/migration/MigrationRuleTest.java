package ltd.idcu.est.migration;

import ltd.idcu.est.migration.rules.SpringBootImportMigrationRule;
import ltd.idcu.est.migration.rules.SpringBootMainClassMigrationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MigrationRuleTest {

    @Test
    public void testSpringBootImportMigrationRule() {
        SpringBootImportMigrationRule rule = new SpringBootImportMigrationRule();
        MigrationConfig config = new MigrationConfig();
        config.setSourceFramework(MigrationConfig.SourceFramework.SPRING_BOOT);

        String content = "import org.springframework.stereotype.Component;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "\n" +
                "public class TestClass {\n" +
                "}";

        assertTrue(rule.canApply(content, config));

        MigrationResult result = new MigrationResult();
        String migrated = rule.apply(content, config, result);

        assertTrue(migrated.contains("import ltd.idcu.est.core.api.annotation.Component;"));
        assertTrue(migrated.contains("import ltd.idcu.est.web.api.RestController;"));
        assertFalse(migrated.contains("import org.springframework.stereotype.Component;"));
    }

    @Test
    public void testSpringBootMainClassMigrationRule() {
        SpringBootMainClassMigrationRule rule = new SpringBootMainClassMigrationRule();
        MigrationConfig config = new MigrationConfig();
        config.setSourceFramework(MigrationConfig.SourceFramework.SPRING_BOOT);

        String content = "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                "\n" +
                "@SpringBootApplication\n" +
                "public class MyApplication {\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(MyApplication.class, args);\n" +
                "    }\n" +
                "}";

        assertTrue(rule.canApply(content, config));

        MigrationResult result = new MigrationResult();
        String migrated = rule.apply(content, config, result);

        assertFalse(migrated.contains("@SpringBootApplication"));
        assertTrue(migrated.contains("ESTApplication.run(MyApplication.class, args)"));
        assertTrue(migrated.contains("import ltd.idcu.est.core.api.ESTApplication;"));
    }

    @Test
    public void testMigrationConfig() {
        MigrationConfig config = new MigrationConfig();
        config.setSourceFramework(MigrationConfig.SourceFramework.SOLON);
        config.setBackup(false);
        config.setOverwrite(true);
        config.addExcludePattern(".*\\.bak");

        assertEquals(MigrationConfig.SourceFramework.SOLON, config.getSourceFramework());
        assertFalse(config.isBackup());
        assertTrue(config.isOverwrite());
        assertFalse(config.shouldIncludeFile("test.bak"));
        assertTrue(config.shouldIncludeFile("test.java"));
    }

    @Test
    public void testMigrationResult() {
        MigrationResult result = new MigrationResult();
        assertTrue(result.isSuccess());

        result.addMigratedFile("Test.java");
        result.addWarning("Test warning");
        result.addError("Test error");

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMigratedFiles().size());
        assertEquals(1, result.getWarnings().size());
        assertEquals(1, result.getErrors().size());
    }
}
