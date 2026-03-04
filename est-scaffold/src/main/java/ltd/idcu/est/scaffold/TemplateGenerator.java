package ltd.idcu.est.scaffold;

import java.io.IOException;
import java.nio.file.Path;

public class TemplateGenerator {

    public static void createBasicMainClass(Path basePath, String projectName) throws IOException {
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
        FileWriterUtil.writeFile(javaPath, content);
    }

    public static void createWebMainClass(Path basePath, String projectName) throws IOException {
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
        FileWriterUtil.writeFile(javaPath, content);
    }

    public static void createWebController(Path basePath, String projectName) throws IOException {
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
                "            \"    <title>Hello - EST Web App</title>\\n\" +\n" +
                "            \"    <style>\\n\" +\n" +
                "            \"        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }\\n\" +\n" +
                "            \"        h1 { color: #2c3e50; }\\n\" +\n" +
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
        FileWriterUtil.writeFile(javaPath, content);
    }
}
