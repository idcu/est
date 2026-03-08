package ltd.idcu.est.codegen.db;

import ltd.idcu.est.codegen.CodeGenerator;
import ltd.idcu.est.codegen.db.config.GeneratorConfig;
import ltd.idcu.est.codegen.db.database.DatabaseType;
import ltd.idcu.est.codegen.db.generator.ControllerGenerator;
import ltd.idcu.est.codegen.db.generator.DtoGenerator;
import ltd.idcu.est.codegen.db.generator.EntityGenerator;
import ltd.idcu.est.codegen.db.generator.MapperXmlGenerator;
import ltd.idcu.est.codegen.db.generator.RepositoryGenerator;
import ltd.idcu.est.codegen.db.generator.ServiceGenerator;
import ltd.idcu.est.codegen.db.generator.TestGenerator;
import ltd.idcu.est.codegen.db.metadata.Table;
import ltd.idcu.est.codegen.db.reader.DatabaseMetadataReader;
import ltd.idcu.est.scaffold.FileWriterUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DatabaseCodeGenerator implements CodeGenerator {

    private final GeneratorConfig config;
    private final DatabaseMetadataReader reader;
    private final EntityGenerator entityGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final ServiceGenerator serviceGenerator;
    private final ControllerGenerator controllerGenerator;
    private final DtoGenerator dtoGenerator;
    private final TestGenerator testGenerator;
    private final MapperXmlGenerator mapperXmlGenerator;

    public DatabaseCodeGenerator(GeneratorConfig config) {
        this(config, DatabaseType.fromJdbcUrl(config.getJdbcUrl()));
    }

    public DatabaseCodeGenerator(GeneratorConfig config, DatabaseType dbType) {
        this.config = config;
        this.reader = dbType.createReader();
        this.entityGenerator = new EntityGenerator(config);
        this.repositoryGenerator = new RepositoryGenerator(config);
        this.serviceGenerator = new ServiceGenerator(config);
        this.controllerGenerator = new ControllerGenerator(config);
        this.dtoGenerator = new DtoGenerator(config);
        this.testGenerator = new TestGenerator(config);
        this.mapperXmlGenerator = new MapperXmlGenerator(config);
    }

    @Override
    public void generate() throws IOException {
        try (Connection connection = getConnection()) {
            List<Table> tables = readTables(connection);
            
            System.out.println("Found " + tables.size() + " tables to generate");
            
            for (Table table : tables) {
                System.out.println("Processing table: " + table.getName());
                generateForTable(table);
            }
            
            System.out.println("Code generation completed!");
        } catch (SQLException e) {
            throw new IOException("Database error during code generation", e);
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
        
        if (config.isGenerateMapperXml()) {
            String mapperXmlCode = mapperXmlGenerator.generate(table);
            String mapperXmlFileName = table.getClassName() + "Mapper.xml";
            Path mapperXmlPath = outputDir.resolve("resources").resolve(toPath(config.getMapperXmlPackage())).resolve(mapperXmlFileName);
            FileWriterUtil.writeFile(mapperXmlPath, mapperXmlCode, false);
            System.out.println("  Generated Mapper XML: " + mapperXmlPath);
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
        
        if (config.isGenerateTest()) {
            String testCode = testGenerator.generate(table);
            String testFileName = table.getClassName() + "ServiceTest.java";
            Path testPath = outputDir.resolve(toPath(config.getTestPackage())).resolve(testFileName);
            FileWriterUtil.writeFile(testPath, testCode, false);
            System.out.println("  Generated Test: " + testPath);
        }
    }

    private String toPath(String packageName) {
        return packageName.replace('.', '/');
    }
}
