package ltd.idcu.est.cli.ide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESTTemplateRegistry {
    private static final Map<String, CodeTemplate> templates = new HashMap<>();

    static {
        registerJavaTemplates();
        registerYamlTemplates();
        registerPomTemplates();
    }

    private static void registerJavaTemplates() {
        templates.put("rest-controller", new CodeTemplate(
                "rest-controller",
                "EST REST Controller",
                "Create a REST API controller",
                "import ltd.idcu.est.web.api.RestController;\n" +
                "import ltd.idcu.est.web.api.GetMapping;\n" +
                "import ltd.idcu.est.web.api.PostMapping;\n" +
                "import ltd.idcu.est.web.api.RequestBody;\n" +
                "import ltd.idcu.est.web.api.PathVariable;\n" +
                "\n" +
                "@RestController\n" +
                "public class ${NAME}Controller {\n" +
                "\n" +
                "    @GetMapping(\"/${NAME.toLowerCase()}\")\n" +
                "    public List<${NAME}> getAll() {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @GetMapping(\"/${NAME.toLowerCase()}/{id}\")\n" +
                "    public ${NAME} getById(@PathVariable(\"id\") Long id) {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @PostMapping(\"/${NAME.toLowerCase()}\")\n" +
                "    public ${NAME} create(@RequestBody ${NAME} entity) {\n" +
                "        return null;\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                1
        ));

        templates.put("controller", new CodeTemplate(
                "controller",
                "EST Web Controller",
                "Create a web controller with view rendering",
                "import ltd.idcu.est.web.api.Controller;\n" +
                "import ltd.idcu.est.web.api.GetMapping;\n" +
                "\n" +
                "@Controller\n" +
                "public class ${NAME}Controller {\n" +
                "\n" +
                "    @GetMapping(\"/${NAME.toLowerCase()}\")\n" +
                "    public String index() {\n" +
                "        return \"${NAME.toLowerCase()}/index\";\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                2
        ));

        templates.put("component", new CodeTemplate(
                "component",
                "EST Component",
                "Create a dependency injection component",
                "import ltd.idcu.est.core.api.annotation.Component;\n" +
                "import ltd.idcu.est.core.api.annotation.Inject;\n" +
                "\n" +
                "@Component\n" +
                "public class ${NAME}Service {\n" +
                "\n" +
                "    @Inject\n" +
                "    private OtherService otherService;\n" +
                "\n" +
                "    public void doSomething() {\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                3
        ));

        templates.put("repository", new CodeTemplate(
                "repository",
                "EST Repository",
                "Create a data repository",
                "import ltd.idcu.est.data.api.Repository;\n" +
                "import ltd.idcu.est.data.api.Entity;\n" +
                "import ltd.idcu.est.data.api.Id;\n" +
                "import ltd.idcu.est.data.api.Column;\n" +
                "\n" +
                "@Repository\n" +
                "public class ${NAME}Repository {\n" +
                "\n" +
                "    public ${NAME} findById(Long id) {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public List<${NAME}> findAll() {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public ${NAME} save(${NAME} entity) {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public void delete(Long id) {\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                4
        ));

        templates.put("entity", new CodeTemplate(
                "entity",
                "EST Entity",
                "Create a database entity",
                "import ltd.idcu.est.data.api.Entity;\n" +
                "import ltd.idcu.est.data.api.Id;\n" +
                "import ltd.idcu.est.data.api.Column;\n" +
                "\n" +
                "@Entity(table = \"${NAME.toLowerCase()}\")\n" +
                "public class ${NAME} {\n" +
                "\n" +
                "    @Id\n" +
                "    @Column(name = \"id\")\n" +
                "    private Long id;\n" +
                "\n" +
                "    public Long getId() {\n" +
                "        return id;\n" +
                "    }\n" +
                "\n" +
                "    public void setId(Long id) {\n" +
                "        this.id = id;\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                5
        ));

        templates.put("configuration", new CodeTemplate(
                "configuration",
                "EST Configuration",
                "Create a configuration class",
                "import ltd.idcu.est.core.api.annotation.Configuration;\n" +
                "import ltd.idcu.est.core.api.annotation.Value;\n" +
                "import ltd.idcu.est.core.api.annotation.Component;\n" +
                "\n" +
                "@Configuration\n" +
                "public class ${NAME}Config {\n" +
                "\n" +
                "    @Value(\"${NAME.toLowerCase()}.property1\")\n" +
                "    private String property1;\n" +
                "\n" +
                "    @Value(\"${NAME.toLowerCase()}.property2\")\n" +
                "    private int property2;\n" +
                "\n" +
                "    public String getProperty1() {\n" +
                "        return property1;\n" +
                "    }\n" +
                "\n" +
                "    public int getProperty2() {\n" +
                "        return property2;\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                6
        ));

        templates.put("scheduled", new CodeTemplate(
                "scheduled",
                "EST Scheduled Task",
                "Create a scheduled task",
                "import ltd.idcu.est.core.api.annotation.Component;\n" +
                "import ltd.idcu.est.scheduler.api.Scheduled;\n" +
                "\n" +
                "@Component\n" +
                "public class ${NAME}Task {\n" +
                "\n" +
                "    @Scheduled(cron = \"0 0 * * * ?\")\n" +
                "    public void executeHourly() {\n" +
                "    }\n" +
                "\n" +
                "    @Scheduled(fixedRate = 60000)\n" +
                "    public void executeEveryMinute() {\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                7
        ));

        templates.put("event-listener", new CodeTemplate(
                "event-listener",
                "EST Event Listener",
                "Create an event listener",
                "import ltd.idcu.est.core.api.annotation.Component;\n" +
                "import ltd.idcu.est.event.api.EventListener;\n" +
                "\n" +
                "@Component\n" +
                "public class ${NAME}EventListener {\n" +
                "\n" +
                "    @EventListener\n" +
                "    public void on${NAME}Event(${NAME}Event event) {\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                8
        ));

        templates.put("aspect", new CodeTemplate(
                "aspect",
                "EST AOP Aspect",
                "Create an AOP aspect",
                "import ltd.idcu.est.patterns.api.Aspect;\n" +
                "import ltd.idcu.est.patterns.api.Before;\n" +
                "import ltd.idcu.est.patterns.api.After;\n" +
                "import ltd.idcu.est.patterns.api.Around;\n" +
                "\n" +
                "@Aspect\n" +
                "public class ${NAME}Aspect {\n" +
                "\n" +
                "    @Before(pointcut = \"execution(* com.example..*.*(..))\")\n" +
                "    public void beforeMethod() {\n" +
                "    }\n" +
                "\n" +
                "    @After(pointcut = \"execution(* com.example..*.*(..))\")\n" +
                "    public void afterMethod() {\n" +
                "    }\n" +
                "\n" +
                "    @Around(pointcut = \"execution(* com.example..*.*(..))\")\n" +
                "    public Object aroundMethod() {\n" +
                "        return null;\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                9
        ));

        templates.put("main-app", new CodeTemplate(
                "main-app",
                "EST Main Application",
                "Create the main application entry point",
                "import ltd.idcu.est.core.api.ESTApplication;\n" +
                "\n" +
                "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        ESTApplication.run(Main.class, args);\n" +
                "    }\n" +
                "}",
                FileType.JAVA,
                10
        ));
    }

    private static void registerYamlTemplates() {
        templates.put("est-config", new CodeTemplate(
                "est-config",
                "EST Configuration",
                "Create a complete EST configuration file",
                "server:\n" +
                "  port: 8080\n" +
                "  host: 0.0.0.0\n" +
                "  context-path: /\n" +
                "\n" +
                "logging:\n" +
                "  level: INFO\n" +
                "  file: logs/application.log\n" +
                "  pattern: \"%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n\"\n" +
                "\n" +
                "database:\n" +
                "  url: jdbc:mysql://localhost:3306/mydb\n" +
                "  username: root\n" +
                "  password: password\n" +
                "  driver-class-name: com.mysql.cj.jdbc.Driver\n" +
                "\n" +
                "cache:\n" +
                "  type: memory\n" +
                "  ttl: 3600\n" +
                "\n" +
                "monitor:\n" +
                "  enabled: true\n" +
                "  endpoint: /actuator/metrics",
                FileType.YAML,
                1
        ));

        templates.put("simple-config", new CodeTemplate(
                "simple-config",
                "Simple EST Configuration",
                "Create a minimal EST configuration file",
                "server:\n" +
                "  port: 8080\n" +
                "\n" +
                "logging:\n" +
                "  level: INFO",
                FileType.YAML,
                2
        ));
    }

    private static void registerPomTemplates() {
        templates.put("est-pom", new CodeTemplate(
                "est-pom",
                "EST Project POM",
                "Create a POM file for an EST project",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>my-est-app</artifactId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "\n" +
                "    <name>My EST Application</name>\n" +
                "    <description>A sample application using EST Framework</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>17</maven.compiler.source>\n" +
                "        <maven.compiler.target>17</maven.compiler.target>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <est.version>2.1.0</est.version>\n" +
                "    </properties>\n" +
                "\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-core-api</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-core-impl</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-web-api</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-web-impl</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-test-impl</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "</project>",
                FileType.POM_XML,
                1
        ));
    }

    public static CodeTemplate getTemplate(String id) {
        return templates.get(id);
    }

    public static List<CodeTemplate> getTemplatesByFileType(FileType fileType) {
        List<CodeTemplate> result = new ArrayList<>();
        for (CodeTemplate template : templates.values()) {
            if (template.getFileType() == fileType) {
                result.add(template);
            }
        }
        return result;
    }

    public static List<CodeTemplate> getAllTemplates() {
        return new ArrayList<>(templates.values());
    }
}
