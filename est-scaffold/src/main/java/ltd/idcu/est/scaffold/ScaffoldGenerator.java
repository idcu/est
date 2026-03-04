package ltd.idcu.est.scaffold;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScaffoldGenerator {

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }

        String command = args[0];
        String projectName = args[1];

        try {
            switch (command) {
                case "new":
                    generateNewProject(projectName);
                    break;
                case "web":
                    generateWebProject(projectName);
                    break;
                case "api":
                    generateApiProject(projectName);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    printUsage();
            }
        } catch (IOException e) {
            System.err.println("Error generating project: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("EST Scaffold Generator");
        System.out.println("Usage:");
        System.out.println("  java -jar est-scaffold.jar new <project-name>  - Create basic EST project");
        System.out.println("  java -jar est-scaffold.jar web <project-name>  - Create web application project");
        System.out.println("  java -jar est-scaffold.jar api <project-name>  - Create REST API project");
    }

    public static void generateNewProject(String projectName) throws IOException {
        System.out.println("Generating basic EST project: " + projectName);
        
        Path basePath = Paths.get(projectName);
        Files.createDirectories(basePath);
        
        createProjectStructure(basePath, projectName, "basic");
        createBasicPomXml(basePath, projectName);
        createBasicMainClass(basePath, projectName);
        createGitignore(basePath);
        createReadme(basePath, projectName, "Basic EST Application");
        
        System.out.println("Project created successfully at: " + basePath.toAbsolutePath());
        System.out.println("\nNext steps:");
        System.out.println("  cd " + projectName);
        System.out.println("  mvn clean install");
        System.out.println("  mvn exec:java -Dexec.mainClass=\"com.example." + projectName.replace("-", "").toLowerCase() + ".Main\"");
    }

    public static void generateWebProject(String projectName) throws IOException {
        System.out.println("Generating web EST project: " + projectName);
        
        Path basePath = Paths.get(projectName);
        Files.createDirectories(basePath);
        
        createProjectStructure(basePath, projectName, "web");
        createWebPomXml(basePath, projectName);
        createWebMainClass(basePath, projectName);
        createWebController(basePath, projectName);
        createGitignore(basePath);
        createReadme(basePath, projectName, "Web Application with EST Framework");
        
        System.out.println("Web project created successfully at: " + basePath.toAbsolutePath());
        System.out.println("\nNext steps:");
        System.out.println("  cd " + projectName);
        System.out.println("  mvn clean install");
        System.out.println("  mvn exec:java -Dexec.mainClass=\"com.example." + projectName.replace("-", "").toLowerCase() + ".Main\"");
    }

    public static void generateApiProject(String projectName) throws IOException {
        System.out.println("Generating REST API EST project: " + projectName);
        
        Path basePath = Paths.get(projectName);
        Files.createDirectories(basePath);
        
        createProjectStructure(basePath, projectName, "api");
        createApiPomXml(basePath, projectName);
        createApiMainClass(basePath, projectName);
        createApiController(basePath, projectName);
        createApiModel(basePath, projectName);
        createGitignore(basePath);
        createReadme(basePath, projectName, "REST API with EST Framework");
        
        System.out.println("API project created successfully at: " + basePath.toAbsolutePath());
        System.out.println("\nNext steps:");
        System.out.println("  cd " + projectName);
        System.out.println("  mvn clean install");
        System.out.println("  mvn exec:java -Dexec.mainClass=\"com.example." + projectName.replace("-", "").toLowerCase() + ".Main\"");
    }

    private static void createProjectStructure(Path basePath, String projectName, String type) throws IOException {
        String packagePath = "com/example/" + projectName.replace("-", "").toLowerCase();
        
        Files.createDirectories(basePath.resolve("src/main/java/" + packagePath));
        Files.createDirectories(basePath.resolve("src/main/resources"));
        Files.createDirectories(basePath.resolve("src/test/java/" + packagePath));
        Files.createDirectories(basePath.resolve("src/test/resources"));
        
        if ("web".equals(type) || "api".equals(type)) {
            Files.createDirectories(basePath.resolve("src/main/java/" + packagePath + "/controller"));
            Files.createDirectories(basePath.resolve("src/main/java/" + packagePath + "/service"));
            Files.createDirectories(basePath.resolve("src/main/java/" + packagePath + "/model"));
        }
    }

    private static void createBasicPomXml(Path basePath, String projectName) throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>" + projectName + "</artifactId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "\n" +
                "    <name>" + projectName + "</name>\n" +
                "    <description>Basic EST Framework Application</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>21</maven.compiler.source>\n" +
                "        <maven.compiler.target>21</maven.compiler.target>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <est.version>1.3.0-SNAPSHOT</est.version>\n" +
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
                "    </dependencies>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.11.0</version>\n" +
                "                <configuration>\n" +
                "                    <source>21</source>\n" +
                "                    <target>21</target>\n" +
                "                    <compilerArgs>\n" +
                "                        <arg>--enable-preview</arg>\n" +
                "                    </compilerArgs>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.codehaus.mojo</groupId>\n" +
                "                <artifactId>exec-maven-plugin</artifactId>\n" +
                "                <version>3.1.0</version>\n" +
                "                <configuration>\n" +
                "                    <mainClass>com.example." + projectName.replace("-", "").toLowerCase() + ".Main</mainClass>\n" +
                "                    <commandlineArgs>--enable-preview</commandlineArgs>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "\n" +
                "</project>";
        
        writeFile(basePath.resolve("pom.xml"), content);
    }

    private static void createWebPomXml(Path basePath, String projectName) throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>" + projectName + "</artifactId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "\n" +
                "    <name>" + projectName + "</name>\n" +
                "    <description>Web Application with EST Framework</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>21</maven.compiler.source>\n" +
                "        <maven.compiler.target>21</maven.compiler.target>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <est.version>1.3.0-SNAPSHOT</est.version>\n" +
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
                "            <artifactId>est-features-logging-api</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-features-logging-console</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.11.0</version>\n" +
                "                <configuration>\n" +
                "                    <source>21</source>\n" +
                "                    <target>21</target>\n" +
                "                    <compilerArgs>\n" +
                "                        <arg>--enable-preview</arg>\n" +
                "                    </compilerArgs>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.codehaus.mojo</groupId>\n" +
                "                <artifactId>exec-maven-plugin</artifactId>\n" +
                "                <version>3.1.0</version>\n" +
                "                <configuration>\n" +
                "                    <mainClass>com.example." + projectName.replace("-", "").toLowerCase() + ".Main</mainClass>\n" +
                "                    <commandlineArgs>--enable-preview</commandlineArgs>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "\n" +
                "</project>";
        
        writeFile(basePath.resolve("pom.xml"), content);
    }

    private static void createApiPomXml(Path basePath, String projectName) throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>" + projectName + "</artifactId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "\n" +
                "    <name>" + projectName + "</name>\n" +
                "    <description>REST API with EST Framework</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>21</maven.compiler.source>\n" +
                "        <maven.compiler.target>21</maven.compiler.target>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <est.version>1.3.0-SNAPSHOT</est.version>\n" +
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
                "            <artifactId>est-features-cache-api</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-features-cache-memory</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-features-logging-api</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-features-logging-console</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>ltd.idcu</groupId>\n" +
                "            <artifactId>est-utils-format-json</artifactId>\n" +
                "            <version>${est.version}</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.11.0</version>\n" +
                "                <configuration>\n" +
                "                    <source>21</source>\n" +
                "                    <target>21</target>\n" +
                "                    <compilerArgs>\n" +
                "                        <arg>--enable-preview</arg>\n" +
                "                    </compilerArgs>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.codehaus.mojo</groupId>\n" +
                "                <artifactId>exec-maven-plugin</artifactId>\n" +
                "                <version>3.1.0</version>\n" +
                "                <configuration>\n" +
                "                    <mainClass>com.example." + projectName.replace("-", "").toLowerCase() + ".Main</mainClass>\n" +
                "                    <commandlineArgs>--enable-preview</commandlineArgs>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "\n" +
                "</project>";
        
        writeFile(basePath.resolve("pom.xml"), content);
    }

    private static void createBasicMainClass(Path basePath, String projectName) throws IOException {
        String packageName = projectName.replace("-", "").toLowerCase();
        String content = "package com.example." + packageName + ";\n" +
                "\n" +
                "import ltd.idcu.est.core.api.Container;\n" +
                "import ltd.idcu.est.core.impl.DefaultContainer;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"=== EST Framework Application ===\");\n" +
                "        \n" +
                "        Container container = new DefaultContainer();\n" +
                "        \n" +
                "        container.registerSingleton(String.class, \"Hello from EST!\");\n" +
                "        \n" +
                "        String message = container.get(String.class);\n" +
                "        System.out.println(\"Message: \" + message);\n" +
                "        \n" +
                "        System.out.println(\"Application started successfully!\");\n" +
                "    }\n" +
                "}";
        
        Path javaPath = basePath.resolve("src/main/java/com/example/" + packageName + "/Main.java");
        writeFile(javaPath, content);
    }

    private static void createWebMainClass(Path basePath, String projectName) throws IOException {
        String packageName = projectName.replace("-", "").toLowerCase();
        String content = "package com.example." + packageName + ";\n" +
                "\n" +
                "import ltd.idcu.est.web.DefaultWebApplication;\n" +
                "import ltd.idcu.est.web.LoggingMiddleware;\n" +
                "import ltd.idcu.est.web.api.WebApplication;\n" +
                "import com.example." + packageName + ".controller.HomeController;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"=== EST Web Application ===\");\n" +
                "        \n" +
                "        WebApplication app = new DefaultWebApplication();\n" +
                "        \n" +
                "        app.use(new LoggingMiddleware());\n" +
                "        \n" +
                "        HomeController controller = new HomeController();\n" +
                "        controller.registerRoutes(app);\n" +
                "        \n" +
                "        app.onStartup(() -> {\n" +
                "            System.out.println(\"Server started on http://localhost:8080\");\n" +
                "            System.out.println(\"Available routes:\");\n" +
                "            System.out.println(\"  - GET /          - Home page\");\n" +
                "            System.out.println(\"  - GET /hello/:name - Greeting\");\n" +
                "            System.out.println(\"\\nPress Ctrl+C to stop the server\");\n" +
                "        });\n" +
                "        \n" +
                "        app.run(8080);\n" +
                "    }\n" +
                "}";
        
        Path javaPath = basePath.resolve("src/main/java/com/example/" + packageName + "/Main.java");
        writeFile(javaPath, content);
    }

    private static void createWebController(Path basePath, String projectName) throws IOException {
        String packageName = projectName.replace("-", "").toLowerCase();
        String content = "package com.example." + packageName + ".controller;\n" +
                "\n" +
                "import ltd.idcu.est.web.api.WebApplication;\n" +
                "import ltd.idcu.est.web.api.Request;\n" +
                "import ltd.idcu.est.web.api.Response;\n" +
                "\n" +
                "public class HomeController {\n" +
                "    \n" +
                "    public void registerRoutes(WebApplication app) {\n" +
                "        app.routes(router -> {\n" +
                "            router.get(\"/\", this::home);\n" +
                "            router.get(\"/hello/:name\", this::hello);\n" +
                "        });\n" +
                "    }\n" +
                "    \n" +
                "    public void home(Request request, Response response) {\n" +
                "        String html = \"<!DOCTYPE html>\\n\" +\n" +
                "            \"<html>\\n\" +\n" +
                "            \"<head>\\n\" +\n" +
                "            \"    <title>EST Web App</title>\\n\" +\n" +
                "            \"    <style>\\n\" +\n" +
                "            \"        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }\\n\" +\n" +
                "            \"        h1 { color: #2c3e50; }\\n\" +\n" +
                "            \"        .container { background: #f8f9fa; padding: 30px; border-radius: 8px; }\\n\" +\n" +
                "            \"    </style>\\n\" +\n" +
                "            \"</head>\\n\" +\n" +
                "            \"<body>\\n\" +\n" +
                "            \"    <div class=\\\"container\\\">\\n\" +\n" +
                "            \"        <h1>Welcome to EST Framework!</h1>\\n\" +\n" +
                "            \"        <p>This is a sample web application built with EST.</p>\\n\" +\n" +
                "            \"        <p>Try: <a href=\\\"/hello/World\\\">/hello/World</a></p>\\n\" +\n" +
                "            \"    </div>\\n\" +\n" +
                "            \"</body>\\n\" +\n" +
                "            \"</html>\";\n" +
                "        response.html(html);\n" +
                "    }\n" +
                "    \n" +
                "    public void hello(Request request, Response response) {\n" +
                "        String name = request.param(\"name\");\n" +
                "        String html = \"<!DOCTYPE html>\\n\" +\n" +
                "            \"<html>\\n\" +\n" +
                "            \"<head>\\n\" +\n" +
                "            \"    <title>Hello</title>\\n\" +\n" +
                "            \"    <style>\\n\" +\n" +
                "            \"        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }\\n\" +\n" +
                "            \"        h1 { color: #3498db; }\\n\" +\n" +
                "            \"        .container { background: #f8f9fa; padding: 30px; border-radius: 8px; }\\n\" +\n" +
                "            \"    </style>\\n\" +\n" +
                "            \"</head>\\n\" +\n" +
                "            \"<body>\\n\" +\n" +
                "            \"    <div class=\\\"container\\\">\\n\" +\n" +
                "            \"        <h1>Hello, \" + name + \"!</h1>\\n\" +\n" +
                "            \"        <p>Welcome to EST Framework.</p>\\n\" +\n" +
                "            \"        <p><a href=\\\"/\\\">Back to home</a></p>\\n\" +\n" +
                "            \"    </div>\\n\" +\n" +
                "            \"</body>\\n\" +\n" +
                "            \"</html>\";\n" +
                "        response.html(html);\n" +
                "    }\n" +
                "}";
        
        Path javaPath = basePath.resolve("src/main/java/com/example/" + packageName + "/controller/HomeController.java");
        writeFile(javaPath, content);
    }

    private static void createApiMainClass(Path basePath, String projectName) throws IOException {
        String packageName = projectName.replace("-", "").toLowerCase();
        String content = "package com.example." + packageName + ";\n" +
                "\n" +
                "import ltd.idcu.est.web.DefaultWebApplication;\n" +
                "import ltd.idcu.est.web.LoggingMiddleware;\n" +
                "import ltd.idcu.est.web.api.WebApplication;\n" +
                "import com.example." + packageName + ".controller.UserController;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"=== EST REST API ===\");\n" +
                "        \n" +
                "        WebApplication app = new DefaultWebApplication();\n" +
                "        \n" +
                "        app.use(new LoggingMiddleware());\n" +
                "        \n" +
                "        UserController controller = new UserController();\n" +
                "        controller.registerRoutes(app);\n" +
                "        \n" +
                "        app.onStartup(() -> {\n" +
                "            System.out.println(\"API Server started on http://localhost:8080\");\n" +
                "            System.out.println(\"Available endpoints:\");\n" +
                "            System.out.println(\"  - GET    /api/users       - List all users\");\n" +
                "            System.out.println(\"  - GET    /api/users/:id   - Get user by ID\");\n" +
                "            System.out.println(\"  - POST   /api/users       - Create new user\");\n" +
                "            System.out.println(\"  - PUT    /api/users/:id   - Update user\");\n" +
                "            System.out.println(\"  - DELETE /api/users/:id   - Delete user\");\n" +
                "            System.out.println(\"\\nPress Ctrl+C to stop the server\");\n" +
                "        });\n" +
                "        \n" +
                "        app.run(8080);\n" +
                "    }\n" +
                "}";
        
        Path javaPath = basePath.resolve("src/main/java/com/example/" + packageName + "/Main.java");
        writeFile(javaPath, content);
    }

    private static void createApiController(Path basePath, String projectName) throws IOException {
        String packageName = projectName.replace("-", "").toLowerCase();
        String content = "package com.example." + packageName + ".controller;\n" +
                "\n" +
                "import ltd.idcu.est.web.api.WebApplication;\n" +
                "import ltd.idcu.est.web.api.Request;\n" +
                "import ltd.idcu.est.web.api.Response;\n" +
                "import com.example." + packageName + ".model.User;\n" +
                "import java.util.*;\n" +
                "import java.util.concurrent.ConcurrentHashMap;\n" +
                "\n" +
                "public class UserController {\n" +
                "    \n" +
                "    private final Map<String, User> users = new ConcurrentHashMap<>();\n" +
                "    private int nextId = 1;\n" +
                "    \n" +
                "    public UserController() {\n" +
                "        User user1 = new User(String.valueOf(nextId++), \"John Doe\", \"john@example.com\");\n" +
                "        User user2 = new User(String.valueOf(nextId++), \"Jane Smith\", \"jane@example.com\");\n" +
                "        users.put(user1.getId(), user1);\n" +
                "        users.put(user2.getId(), user2);\n" +
                "    }\n" +
                "    \n" +
                "    public void registerRoutes(WebApplication app) {\n" +
                "        app.routes(router -> {\n" +
                "            router.get(\"/api/users\", this::listUsers);\n" +
                "            router.get(\"/api/users/:id\", this::getUser);\n" +
                "            router.post(\"/api/users\", this::createUser);\n" +
                "            router.put(\"/api/users/:id\", this::updateUser);\n" +
                "            router.delete(\"/api/users/:id\", this::deleteUser);\n" +
                "        });\n" +
                "    }\n" +
                "    \n" +
                "    public void listUsers(Request request, Response response) {\n" +
                "        response.json(new ArrayList<>(users.values()));\n" +
                "    }\n" +
                "    \n" +
                "    public void getUser(Request request, Response response) {\n" +
                "        String id = request.param(\"id\");\n" +
                "        User user = users.get(id);\n" +
                "        if (user != null) {\n" +
                "            response.json(user);\n" +
                "        } else {\n" +
                "            response.status(404).json(Map.of(\"error\", \"User not found\"));\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    public void createUser(Request request, Response response) {\n" +
                "        String body = request.body();\n" +
                "        String name = extractValue(body, \"name\");\n" +
                "        String email = extractValue(body, \"email\");\n" +
                "        \n" +
                "        if (name != null && email != null) {\n" +
                "            User user = new User(String.valueOf(nextId++), name, email);\n" +
                "            users.put(user.getId(), user);\n" +
                "            response.status(201).json(user);\n" +
                "        } else {\n" +
                "            response.status(400).json(Map.of(\"error\", \"Name and email are required\"));\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    public void updateUser(Request request, Response response) {\n" +
                "        String id = request.param(\"id\");\n" +
                "        User user = users.get(id);\n" +
                "        \n" +
                "        if (user != null) {\n" +
                "            String body = request.body();\n" +
                "            String name = extractValue(body, \"name\");\n" +
                "            String email = extractValue(body, \"email\");\n" +
                "            \n" +
                "            if (name != null) user.setName(name);\n" +
                "            if (email != null) user.setEmail(email);\n" +
                "            \n" +
                "            response.json(user);\n" +
                "        } else {\n" +
                "            response.status(404).json(Map.of(\"error\", \"User not found\"));\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    public void deleteUser(Request request, Response response) {\n" +
                "        String id = request.param(\"id\");\n" +
                "        User user = users.remove(id);\n" +
                "        \n" +
                "        if (user != null) {\n" +
                "            response.json(Map.of(\"message\", \"User deleted successfully\"));\n" +
                "        } else {\n" +
                "            response.status(404).json(Map.of(\"error\", \"User not found\"));\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    private String extractValue(String body, String key) {\n" +
                "        String pattern = \"\\\"\" + key + \"\\\"\\\\s*:\\\\s*\\\"([^\\\"]+)\\\"\";\n" +
                "        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);\n" +
                "        java.util.regex.Matcher m = p.matcher(body);\n" +
                "        if (m.find()) {\n" +
                "            return m.group(1);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "}";
        
        Path javaPath = basePath.resolve("src/main/java/com/example/" + packageName + "/controller/UserController.java");
        writeFile(javaPath, content);
    }

    private static void createApiModel(Path basePath, String projectName) throws IOException {
        String packageName = projectName.replace("-", "").toLowerCase();
        String content = "package com.example." + packageName + ".model;\n" +
                "\n" +
                "public class User {\n" +
                "    private String id;\n" +
                "    private String name;\n" +
                "    private String email;\n" +
                "    \n" +
                "    public User() {}\n" +
                "    \n" +
                "    public User(String id, String name, String email) {\n" +
                "        this.id = id;\n" +
                "        this.name = name;\n" +
                "        this.email = email;\n" +
                "    }\n" +
                "    \n" +
                "    public String getId() { return id; }\n" +
                "    public void setId(String id) { this.id = id; }\n" +
                "    \n" +
                "    public String getName() { return name; }\n" +
                "    public void setName(String name) { this.name = name; }\n" +
                "    \n" +
                "    public String getEmail() { return email; }\n" +
                "    public void setEmail(String email) { this.email = email; }\n" +
                "}";
        
        Path javaPath = basePath.resolve("src/main/java/com/example/" + packageName + "/model/User.java");
        writeFile(javaPath, content);
    }

    private static void createGitignore(Path basePath) throws IOException {
        String content = "target/\n" +
                "*.class\n" +
                "*.jar\n" +
                "*.war\n" +
                "*.ear\n" +
                ".mvn/\n" +
                "mvnw\n" +
                "mvnw.cmd\n" +
                ".DS_Store\n" +
                ".idea/\n" +
                "*.iml\n" +
                ".vscode/\n" +
                ".settings/\n" +
                ".classpath\n" +
                ".project";
        
        writeFile(basePath.resolve(".gitignore"), content);
    }

    private static void createReadme(Path basePath, String projectName, String description) throws IOException {
        String content = "# " + projectName + "\n" +
                "\n" +
                description + "\n" +
                "\n" +
                "## Prerequisites\n" +
                "\n" +
                "- JDK 21+\n" +
                "- Maven 3.6+\n" +
                "\n" +
                "## Getting Started\n" +
                "\n" +
                "### Build\n" +
                "\n" +
                "```bash\n" +
                "mvn clean install\n" +
                "```\n" +
                "\n" +
                "### Run\n" +
                "\n" +
                "```bash\n" +
                "mvn exec:java -Dexec.mainClass=\"com.example." + projectName.replace("-", "").toLowerCase() + ".Main\"\n" +
                "```\n" +
                "\n" +
                "## Project Structure\n" +
                "\n" +
                "```\n" +
                projectName + "/\n" +
                "├── src/\n" +
                "│   ├── main/\n" +
                "│   │   ├── java/com/example/" + projectName.replace("-", "").toLowerCase() + "/\n" +
                "│   │   └── resources/\n" +
                "│   └── test/\n" +
                "│       ├── java/com/example/" + projectName.replace("-", "").toLowerCase() + "/\n" +
                "│       └── resources/\n" +
                "└── pom.xml\n" +
                "```\n" +
                "\n" +
                "## Built With\n" +
                "\n" +
                "- [EST Framework](https://github.com/idcu/est) - Zero-dependency Java framework\n" +
                "\n" +
                "## License\n" +
                "\n" +
                "This project is licensed under the MIT License.";
        
        writeFile(basePath.resolve("README.md"), content);
    }

    private static void writeFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(content);
        }
        System.out.println("  Created: " + path);
    }
}
