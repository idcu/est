package ltd.idcu.est.codegen.db.config;

import ltd.idcu.est.utils.format.yaml.YamlUtils;
import ltd.idcu.est.utils.io.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ConfigFileParser {

    public static GeneratorConfig loadFromFile(String configPath) throws IOException {
        Path path = Paths.get(configPath);
        if (!Files.exists(path)) {
            throw new IOException("Config file not found: " + configPath);
        }
        String yamlContent = Files.readString(path);
        return parseYaml(yamlContent);
    }

    public static GeneratorConfig parseYaml(String yamlContent) {
        Map<String, Object> yamlMap = YamlUtils.parse(yamlContent);
        GeneratorConfig config = new GeneratorConfig();
        
        parseDatabaseSection(config, yamlMap);
        parsePackageSection(config, yamlMap);
        parseOutputSection(config, yamlMap);
        parseTableSection(config, yamlMap);
        parseComponentSection(config, yamlMap);
        
        return config;
    }

    private static void parseDatabaseSection(GeneratorConfig config, Map<String, Object> yamlMap) {
        Map<String, Object> dbMap = YamlUtils.getMap(yamlMap, "database");
        if (dbMap != null) {
            config.setJdbcUrl(YamlUtils.getString(dbMap, "url"));
            config.setUsername(YamlUtils.getString(dbMap, "username"));
            config.setPassword(YamlUtils.getString(dbMap, "password"));
            config.setDriverClassName(YamlUtils.getString(dbMap, "driver"));
        }
    }

    private static void parsePackageSection(GeneratorConfig config, Map<String, Object> yamlMap) {
        Map<String, Object> pkgMap = YamlUtils.getMap(yamlMap, "packages");
        if (pkgMap != null) {
            config.setPackageName(YamlUtils.getString(pkgMap, "base"));
            config.setEntityPackage(YamlUtils.getString(pkgMap, "entity"));
            config.setRepositoryPackage(YamlUtils.getString(pkgMap, "repository"));
            config.setServicePackage(YamlUtils.getString(pkgMap, "service"));
            config.setControllerPackage(YamlUtils.getString(pkgMap, "controller"));
            config.setDtoPackage(YamlUtils.getString(pkgMap, "dto"));
        }
    }

    private static void parseOutputSection(GeneratorConfig config, Map<String, Object> yamlMap) {
        String output = YamlUtils.getString(yamlMap, "output");
        if (output != null) {
            config.setOutputDir(output);
        }
    }

    private static void parseTableSection(GeneratorConfig config, Map<String, Object> yamlMap) {
        Map<String, Object> tableMap = YamlUtils.getMap(yamlMap, "tables");
        if (tableMap != null) {
            List<Object> includeList = YamlUtils.getList(tableMap, "include");
            if (includeList != null) {
                for (Object item : includeList) {
                    if (item != null) {
                        config.addIncludeTable(item.toString());
                    }
                }
            }
            
            List<Object> excludeList = YamlUtils.getList(tableMap, "exclude");
            if (excludeList != null) {
                for (Object item : excludeList) {
                    if (item != null) {
                        config.addExcludeTable(item.toString());
                    }
                }
            }
        }
    }

    private static void parseComponentSection(GeneratorConfig config, Map<String, Object> yamlMap) {
        Map<String, Object> componentMap = YamlUtils.getMap(yamlMap, "components");
        if (componentMap != null) {
            config.setGenerateEntity(YamlUtils.getBoolean(componentMap, "entity", true));
            config.setGenerateRepository(YamlUtils.getBoolean(componentMap, "repository", true));
            config.setGenerateService(YamlUtils.getBoolean(componentMap, "service", true));
            config.setGenerateController(YamlUtils.getBoolean(componentMap, "controller", true));
            config.setGenerateDto(YamlUtils.getBoolean(componentMap, "dto", true));
            config.setUseLombok(YamlUtils.getBoolean(componentMap, "lombok", false));
            config.setUseSwagger(YamlUtils.getBoolean(componentMap, "swagger", false));
            config.setUseMybatisPlus(YamlUtils.getBoolean(componentMap, "mybatisPlus", false));
            config.setUseRelations(YamlUtils.getBoolean(componentMap, "relations", false));
        }
    }

    public static String generateSampleConfig() {
        return """
database:
  url: jdbc:mysql://localhost:3306/mydb
  username: root
  password: 123456
  driver: com.mysql.cj.jdbc.Driver

packages:
  base: com.example
  entity: com.example.entity
  repository: com.example.repository
  service: com.example.service
  controller: com.example.controller
  dto: com.example.dto

output: ./src/main/java

tables:
  include:
    - user
    - order
    - product
  exclude:
    - flyway_schema_history
    - databasechangelog

components:
  entity: true
  repository: true
  service: true
  controller: true
  dto: true
  lombok: false
  swagger: false
  mybatisPlus: false
  relations: false
""";
    }
}
