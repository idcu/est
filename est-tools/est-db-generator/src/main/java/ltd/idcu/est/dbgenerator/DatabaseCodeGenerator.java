package ltd.idcu.est.dbgenerator;

import ltd.idcu.est.dbgenerator.config.GeneratorConfig;
import ltd.idcu.est.dbgenerator.generator.EntityGenerator;
import ltd.idcu.est.dbgenerator.generator.RepositoryGenerator;
import ltd.idcu.est.dbgenerator.generator.ServiceGenerator;
import ltd.idcu.est.dbgenerator.generator.ControllerGenerator;
import ltd.idcu.est.dbgenerator.generator.DtoGenerator;
import ltd.idcu.est.dbgenerator.metadata.Table;
import ltd.idcu.est.dbgenerator.reader.DatabaseMetadataReader;
import ltd.idcu.est.dbgenerator.reader.MySQLMetadataReader;
import ltd.idcu.est.scaffold.FileWriterUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DatabaseCodeGenerator {

    private final GeneratorConfig config;
    private final DatabaseMetadataReader reader;
    private final EntityGenerator entityGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final ServiceGenerator serviceGenerator;
    private final ControllerGenerator controllerGenerator;
    private final DtoGenerator dtoGenerator;

    public DatabaseCodeGenerator(GeneratorConfig config) {
        this.config = config;
        this.reader = new MySQLMetadataReader();
        this.entityGenerator = new EntityGenerator(config);
        this.repositoryGenerator = new RepositoryGenerator(config);
        this.serviceGenerator = new ServiceGenerator(config);
        this.controllerGenerator = new ControllerGenerator(config);
        this.dtoGenerator = new DtoGenerator(config);
    }

    public void generate() throws SQLException, IOException {
        try (Connection connection = getConnection()) {
            List<Table> tables = readTables(connection);
            
            System.out.println("Found " + tables.size() + " tables to generate");
            
            for (Table table : tables) {
                System.out.println("Processing table: " + table.getName());
                generateForTable(table);
            }
            
            System.out.println("Code generation completed!");
        }
    }

    private Connection getConnection() throws SQLException {
        if (config.getDriverClassName() != null) {
            try {
                Class.forName(config.getDriverClassName());
            } catch (ClassNotFoundException e) {
                System.err.println("Warning: Driver class not found: " + config.getDriverClassName());
            }
        }
        return DriverManager.getConnection(
            config.getJdbcUrl(),
            config.getUsername(),
            config.getPassword()
        );
    }

    private List<Table> readTables(Connection connection) throws SQLException {
        if (config.getIncludeTables().isEmpty()) {
            return reader.readAllTables(connection).stream()
                .filter(t -> config.shouldGenerateTable(t.getName()))
                .toList();
        } else {
            return reader.readTables(connection, config.getIncludeTables()).stream()
                .filter(t -> config.shouldGenerateTable(t.getName()))
                .toList();
        }
    }

    private void generateForTable(Table table) throws IOException {
        Path outputDir = Paths.get(config.getOutputDir());
        
        if (config.isGenerateEntity()) {
            String entityCode = entityGenerator.generate(table);
            String entityFileName = table.getClassName() + ".java";
            Path entityPath = outputDir.resolve(toPath(config.getEntityPackage())).resolve(entityFileName);
            FileWriterUtil.writeFile(entityPath, entityCode, false);
            System.out.println("  Generated Entity: " + entityPath);
        }
        
        if (config.isGenerateRepository()) {
            String repoCode = repositoryGenerator.generate(table);
            String repoFileName = table.getClassName() + "Repository.java";
            Path repoPath = outputDir.resolve(toPath(config.getRepositoryPackage())).resolve(repoFileName);
            FileWriterUtil.writeFile(repoPath, repoCode, false);
            System.out.println("  Generated Repository: " + repoPath);
        }
        
        if (config.isGenerateService()) {
            String serviceCode = serviceGenerator.generate(table);
            String serviceFileName = table.getClassName() + "Service.java";
            Path servicePath = outputDir.resolve(toPath(config.getServicePackage())).resolve(serviceFileName);
            FileWriterUtil.writeFile(servicePath, serviceCode, false);
            System.out.println("  Generated Service: " + servicePath);
        }
        
        if (config.isGenerateController()) {
            String controllerCode = controllerGenerator.generate(table);
            String controllerFileName = table.getClassName() + "Controller.java";
            Path controllerPath = outputDir.resolve(toPath(config.getControllerPackage())).resolve(controllerFileName);
            FileWriterUtil.writeFile(controllerPath, controllerCode, false);
            System.out.println("  Generated Controller: " + controllerPath);
        }
        
        if (config.isGenerateDto()) {
            String dtoCode = dtoGenerator.generate(table);
            String dtoFileName = table.getClassName() + "Dto.java";
            Path dtoPath = outputDir.resolve(toPath(config.getDtoPackage())).resolve(dtoFileName);
            FileWriterUtil.writeFile(dtoPath, dtoCode, false);
            System.out.println("  Generated DTO: " + dtoPath);
        }
    }

    private String toPath(String packageName) {
        return packageName.replace('.', '/');
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            GeneratorConfig config = parseArguments(args);
            DatabaseCodeGenerator generator = new DatabaseCodeGenerator(config);
            generator.generate();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static GeneratorConfig parseArguments(String[] args) {
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
            }
        }

        if (config.getJdbcUrl() == null) {
            throw new IllegalArgumentException("JDBC URL is required. Use --url=");
        }
        if (config.getOutputDir() == null) {
            config.setOutputDir(".");
        }

        return config;
    }

    private static void printUsage() {
        System.out.println("EST Database Code Generator");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar est-db-generator.jar [options]");
        System.out.println();
        System.out.println("Required Options:");
        System.out.println("  --url=<jdbc-url>          JDBC connection URL");
        System.out.println();
        System.out.println("Database Options:");
        System.out.println("  --username=<username>     Database username");
        System.out.println("  --password=<password>     Database password");
        System.out.println("  --driver=<driver>         JDBC driver class name");
        System.out.println();
        System.out.println("Generation Options:");
        System.out.println("  --package=<package>       Base package name");
        System.out.println("  --output=<dir>            Output directory (default: .)");
        System.out.println("  --tables=<t1,t2>          Include specific tables (comma-separated)");
        System.out.println("  --exclude-tables=<t1,t2>  Exclude specific tables (comma-separated)");
        System.out.println();
        System.out.println("Component Options (default: all enabled):");
        System.out.println("  --no-entity               Skip Entity generation");
        System.out.println("  --no-repository           Skip Repository generation");
        System.out.println("  --no-service              Skip Service generation");
        System.out.println("  --no-controller           Skip Controller generation");
        System.out.println("  --no-dto                  Skip DTO generation");
        System.out.println();
        System.out.println("Package Options:");
        System.out.println("  --entity-package=<pkg>    Custom package for Entity classes");
        System.out.println("  --repository-package=<pkg> Custom package for Repository classes");
        System.out.println("  --service-package=<pkg>   Custom package for Service classes");
        System.out.println("  --controller-package=<pkg> Custom package for Controller classes");
        System.out.println("  --dto-package=<pkg>       Custom package for DTO classes");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java -jar est-db-generator.jar \\");
        System.out.println("    --url=jdbc:mysql://localhost:3306/mydb \\");
        System.out.println("    --username=root \\");
        System.out.println("    --password=123456 \\");
        System.out.println("    --package=com.example \\");
        System.out.println("    --output=./src/main/java \\");
        System.out.println("    --tables=user,order,product");
    }
}
