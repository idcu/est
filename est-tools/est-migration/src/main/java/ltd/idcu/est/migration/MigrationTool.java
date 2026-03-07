package ltd.idcu.est.migration;

public class MigrationTool {
    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        String framework = args[0];
        String sourceDir = args[1];
        String targetDir = args.length > 2 ? args[2] : null;

        MigrationConfig config = new MigrationConfig();
        config.setSourceDirectory(sourceDir);

        if (targetDir != null) {
            config.setTargetDirectory(targetDir);
        }

        switch (framework.toLowerCase()) {
            case "spring-boot":
            case "springboot":
                config.setSourceFramework(MigrationConfig.SourceFramework.SPRING_BOOT);
                break;
            case "solon":
                config.setSourceFramework(MigrationConfig.SourceFramework.SOLON);
                break;
            case "quarkus":
                config.setSourceFramework(MigrationConfig.SourceFramework.QUARKUS);
                break;
            case "micronaut":
                config.setSourceFramework(MigrationConfig.SourceFramework.MICRONAUT);
                break;
            case "est-1x":
            case "est1x":
            case "est-1.0":
            case "est-1.3":
                config.setSourceFramework(MigrationConfig.SourceFramework.EST_1X);
                break;
            default:
                System.err.println("Unknown framework: " + framework);
                printUsage();
                System.exit(1);
        }

        System.out.println("Starting migration from " + config.getSourceFramework() + " to EST Framework...");
        System.out.println("Source directory: " + config.getSourceDirectory());
        if (config.getTargetDirectory() != null) {
            System.out.println("Target directory: " + config.getTargetDirectory());
        }
        System.out.println();

        MigrationEngine engine = new MigrationEngine(config);
        MigrationResult result = engine.migrate();

        System.out.println(result);

        if (result.isSuccess()) {
            System.out.println("\nMigration completed successfully!");
        } else {
            System.err.println("\nMigration completed with errors!");
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("EST Framework Migration Tool");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar est-migration-tool.jar <framework> <source-dir> [target-dir]");
        System.out.println();
        System.out.println("Frameworks:");
        System.out.println("  spring-boot  - Migrate from Spring Boot");
        System.out.println("  solon        - Migrate from Solon");
        System.out.println("  quarkus      - Migrate from Quarkus");
        System.out.println("  micronaut    - Migrate from Micronaut");
        System.out.println("  est-1x       - Migrate from EST 1.X to EST 2.0");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  # Migrate Spring Boot project in-place");
        System.out.println("  java -jar est-migration-tool.jar spring-boot /path/to/project");
        System.out.println();
        System.out.println("  # Migrate Solon project to a new directory");
        System.out.println("  java -jar est-migration-tool.jar solon /path/to/source /path/to/target");
        System.out.println();
        System.out.println("  # Migrate EST 1.X project to EST 2.0");
        System.out.println("  java -jar est-migration-tool.jar est-1x /path/to/est-1.x-project");
    }
}
