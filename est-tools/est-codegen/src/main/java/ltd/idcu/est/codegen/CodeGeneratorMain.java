package ltd.idcu.est.codegen;

import ltd.idcu.est.codegen.db.DatabaseCodeGenerator;
import ltd.idcu.est.codegen.db.config.ConfigFileParser;
import ltd.idcu.est.codegen.db.config.GeneratorConfig;
import ltd.idcu.est.codegen.db.database.DatabaseType;
import ltd.idcu.est.codegen.pojo.PojoGenerator;

import java.io.IOException;

public class CodeGeneratorMain {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            String command = args[0];
            
            switch (command) {
                case "db":
                    runDatabaseGenerator(args);
                    break;
                case "pojo":
                    runPojoGenerator(args);
                    break;
                case "init-db-config":
                    System.out.println(ConfigFileParser.generateSampleConfig());
                    break;
                default:
                    printUsage();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void runDatabaseGenerator(String[] args) throws Exception {
        GeneratorConfig config = parseDatabaseArguments(args);
        DatabaseCodeGenerator generator;
        
        String dbTypeArg = getArgumentValue(args, "--db-type");
        if (dbTypeArg != null) {
            DatabaseType dbType = DatabaseType.fromName(dbTypeArg);
            generator = new DatabaseCodeGenerator(config, dbType);
        } else {
            generator = new DatabaseCodeGenerator(config);
        }
        
        generator.generate();
    }

    private static GeneratorConfig parseDatabaseArguments(String[] args) throws IOException {
        String configFile = getArgumentValue(args, "--config");
        if (configFile != null) {
            return ConfigFileParser.loadFromFile(configFile);
        }

        GeneratorConfig config = new GeneratorConfig();
        
        for (String arg : args) {
            if (arg.startsWith("--url=")) {
                config.setJdbcUrl(arg.substring("--url=".length()));
            } else if (arg.startsWith("--username=")) {
                config.setUsername(arg.substring("--username=".length()));
            } else if (arg.startsWith("--password=")) {
                config.setPassword(arg.substring("--password=".length()));
            } else if (arg.startsWith("--driver=")) {
                config.setDriverClassName(arg.substring("--driver=".length()));
            } else if (arg.startsWith("--package=")) {
                config.setPackageName(arg.substring("--package=".length()));
            } else if (arg.startsWith("--output=")) {
                config.setOutputDir(arg.substring("--output=".length()));
            } else if (arg.startsWith("--tables=")) {
                String tablesStr = arg.substring("--tables=".length());
                for (String table : tablesStr.split(",")) {
                    config.addIncludeTable(table.trim());
                }
            } else if (arg.startsWith("--exclude-tables=")) {
                String tablesStr = arg.substring("--exclude-tables=".length());
                for (String table : tablesStr.split(",")) {
                    config.addExcludeTable(table.trim());
                }
            } else if (arg.equals("--no-entity")) {
                config.setGenerateEntity(false);
            } else if (arg.equals("--no-repository")) {
                config.setGenerateRepository(false);
            } else if (arg.equals("--no-service")) {
                config.setGenerateService(false);
            } else if (arg.equals("--no-controller")) {
                config.setGenerateController(false);
            } else if (arg.equals("--no-dto")) {
                config.setGenerateDto(false);
            } else if (arg.startsWith("--entity-package=")) {
                config.setEntityPackage(arg.substring("--entity-package=".length()));
            } else if (arg.startsWith("--repository-package=")) {
                config.setRepositoryPackage(arg.substring("--repository-package=".length()));
            } else if (arg.startsWith("--service-package=")) {
                config.setServicePackage(arg.substring("--service-package=".length()));
            } else if (arg.startsWith("--controller-package=")) {
                config.setControllerPackage(arg.substring("--controller-package=".length()));
            } else if (arg.startsWith("--dto-package=")) {
                config.setDtoPackage(arg.substring("--dto-package=".length()));
            } else if (arg.equals("--lombok")) {
                config.setUseLombok(true);
            } else if (arg.equals("--swagger")) {
                config.setUseSwagger(true);
            } else if (arg.equals("--mybatis-plus")) {
                config.setUseMybatisPlus(true);
            } else if (arg.equals("--relations")) {
                config.setUseRelations(true);
            }
        }

        if (config.getJdbcUrl() == null) {
            throw new IllegalArgumentException("JDBC URL is required. Use --url= or --config=");
        }
        if (config.getOutputDir() == null) {
            config.setOutputDir(".");
        }

        return config;
    }

    private static void runPojoGenerator(String[] args) throws IOException {
        PojoGenerator.PojoConfig config = new PojoGenerator.PojoConfig();
        
        for (String arg : args) {
            if (arg.startsWith("--package=")) {
                config.setPackageName(arg.substring("--package=".length()));
            } else if (arg.startsWith("--output=")) {
                config.setOutputDir(arg.substring("--output=".length()));
            } else if (arg.equals("--lombok")) {
                config.setUseLombok(true);
            }
        }

        if (config.getPackageName() == null) {
            throw new IllegalArgumentException("Package name is required. Use --package=");
        }

        PojoGenerator.PojoDefinition userDef = new PojoGenerator.PojoDefinition();
        userDef.setClassName("User");
        userDef.setDescription("User entity");
        userDef.addField(new PojoGenerator.PojoField("id", "Long"));
        userDef.addField(new PojoGenerator.PojoField("username", "String"));
        userDef.addField(new PojoGenerator.PojoField("email", "String"));
        userDef.addField(new PojoGenerator.PojoField("age", "Integer"));
        
        config.addDefinition(userDef);

        PojoGenerator generator = new PojoGenerator(config);
        generator.generate();
    }

    private static String getArgumentValue(String[] args, String prefix) {
        for (String arg : args) {
            if (arg.startsWith(prefix + "=")) {
                return arg.substring((prefix + "=").length());
            }
        }
        return null;
    }

    private static void printUsage() {
        System.out.println("EST Code Generator");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar est-codegen.jar <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  db                  Generate code from database");
        System.out.println("  pojo                Generate POJO classes");
        System.out.println("  init-db-config      Generate sample database config file");
        System.out.println();
        System.out.println("Database Code Generation:");
        System.out.println("  java -jar est-codegen.jar db [options]");
        System.out.println();
        System.out.println("POJO Code Generation:");
        System.out.println("  java -jar est-codegen.jar pojo --package=com.example [--output=.] [--lombok]");
        System.out.println();
        System.out.println("For more database options, use:");
        System.out.println("  java -jar est-codegen.jar db");
    }
}
