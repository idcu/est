package com.example.tools;

import ltd.idcu.est.ide.support.*;
import ltd.idcu.est.migration.*;

import java.io.File;
import java.util.List;

public class MigrationExample {

    public static void main(String[] args) {
        System.out.println("=== EST IDE Support and Migration Tool Examples ===\n");

        demonstrateAnnotationRegistry();
        demonstrateTemplateRegistry();
        demonstrateCodeAnalyzer();
        demonstrateProjectScanner();
        demonstrateMigrationConfig();
    }

    private static void demonstrateAnnotationRegistry() {
        System.out.println("1. Annotation Registry Demo");
        System.out.println("---------------------------");

        List<String> annotationNames = ESTAnnotationRegistry.getAnnotationNames();
        System.out.println("Available EST annotations (" + annotationNames.size() + "):");

        for (String name : annotationNames) {
            AnnotationInfo info = ESTAnnotationRegistry.getAnnotation(name);
            System.out.println("  - " + name + ": " + info.getDescription());
        }
        System.out.println();
    }

    private static void demonstrateTemplateRegistry() {
        System.out.println("2. Template Registry Demo");
        System.out.println("-------------------------");

        List<CodeTemplate> javaTemplates = ESTTemplateRegistry.getTemplatesByFileType(FileType.JAVA);
        System.out.println("Available Java templates (" + javaTemplates.size() + "):");

        for (CodeTemplate template : javaTemplates) {
            System.out.println("  - " + template.getName() + ": " + template.getDescription());
        }

        System.out.println("\nREST Controller Template Preview:");
        CodeTemplate restTemplate = ESTTemplateRegistry.getTemplate("rest-controller");
        if (restTemplate != null) {
            System.out.println(restTemplate.getTemplate().substring(0, Math.min(300, restTemplate.getTemplate().length())) + "...");
        }
        System.out.println();
    }

    private static void demonstrateCodeAnalyzer() {
        System.out.println("3. Code Analyzer Demo");
        System.out.println("---------------------");

        ESTCodeAnalyzer analyzer = new ESTCodeAnalyzer();

        String javaCode = "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    // TODO: Implement user service\n" +
                "}\n";

        System.out.println("Analyzing Java code:");
        System.out.println(javaCode);

        List<Diagnostic> diagnostics = analyzer.analyze("UserService.java", javaCode);
        System.out.println("\nDiagnostics found: " + diagnostics.size());
        for (Diagnostic diag : diagnostics) {
            System.out.println("  [" + diag.getSeverity() + "] Line " + diag.getLine() + ": " + diag.getMessage());
        }

        System.out.println("\nCode completions:");
        List<CodeCompletionItem> completions = analyzer.getCompletions("UserService.java", 3, 1, javaCode);
        for (CodeCompletionItem item : completions.subList(0, Math.min(5, completions.size()))) {
            System.out.println("  - " + item.getLabel() + " (" + item.getKind() + ")");
        }
        System.out.println();
    }

    private static void demonstrateProjectScanner() {
        System.out.println("4. Project Scanner Demo");
        System.out.println("-----------------------");

        ESTProjectScanner scanner = new ESTProjectScanner(System.getProperty("user.dir"));

        System.out.println("Is EST project: " + scanner.isESTProject());
        System.out.println("Project name: " + scanner.getProjectName());

        List<File> modules = scanner.findESTModules();
        System.out.println("Found EST modules: " + modules.size());

        List<File> javaFiles = scanner.findJavaSourceFiles();
        System.out.println("Found Java source files: " + javaFiles.size());
        System.out.println();
    }

    private static void demonstrateMigrationConfig() {
        System.out.println("5. Migration Config Demo");
        System.out.println("------------------------");

        MigrationConfig config = new MigrationConfig();
        config.setSourceFramework(MigrationConfig.SourceFramework.SPRING_BOOT);
        config.setSourceDirectory(".");
        config.setBackup(true);
        config.setOverwrite(false);
        config.addExcludePattern(".*\\.bak");

        System.out.println("Migration Configuration:");
        System.out.println("  Source Framework: " + config.getSourceFramework());
        System.out.println("  Source Directory: " + config.getSourceDirectory());
        System.out.println("  Backup: " + config.isBackup());
        System.out.println("  Overwrite: " + config.isOverwrite());
        System.out.println();

        System.out.println("Migration Rule Registry:");
        System.out.println("  - SpringBootImportMigrationRule: Migrates Spring Boot imports");
        System.out.println("  - SpringBootAnnotationMigrationRule: Migrates Spring Boot annotations");
        System.out.println("  - SpringBootMainClassMigrationRule: Migrates Spring Boot main class");
        System.out.println("  - SolonImportMigrationRule: Migrates Solon imports");
        System.out.println();

        System.out.println("=== To run the migration tool: ===");
        System.out.println("java -jar est-migration-tool.jar <framework> <source-dir> [target-dir]");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  # Migrate Spring Boot project in-place");
        System.out.println("  java -jar est-migration-tool.jar spring-boot /path/to/project");
        System.out.println();
        System.out.println("  # Migrate Solon project to a new directory");
        System.out.println("  java -jar est-migration-tool.jar solon /path/to/source /path/to/target");
    }
}
