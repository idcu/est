package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.LoggingMiddleware;
import ltd.idcu.est.web.PerformanceMonitorMiddleware;
import ltd.idcu.est.web.AbstractController;
import ltd.idcu.est.web.AbstractRestController;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.Router;

import java.util.Map;

public class BasicWebAppExample {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST Web Framework - Multiple Implementation Examples");
        System.out.println("=".repeat(60));
        System.out.println("\nPlease select which example to run:");
        System.out.println("  1. Simplest Hello World (great for beginners)");
        System.out.println("  2. Lambda Expression Routing (modern style)");
        System.out.println("  3. Controller Pattern (MVC architecture)");
        System.out.println("  4. REST API Pattern");
        System.out.println("  5. Complete Example (with middleware, session, templates, etc.)");
        System.out.println("\nThis demo will run the 1st example (Simplest Hello World)");
        System.out.println("To run other examples, please modify the call in main method\n");
        
        simplestHelloWorld();
    }
    
    public static void run() {
        main(new String[]{});
    }
    
    public static void simplestHelloWorld() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("[Mode 1] Simplest Hello World");
        System.out.println("=".repeat(60));
        System.out.println("\nThis is the simplest web application, just 3 lines of code!");
        System.out.println("Great for quickly understanding EST Web framework core concepts\n");
        
        System.out.println("Code Steps:");
        System.out.println("  1. Create Web application");
        System.out.println("  2. Define route (what to return when accessing /)");
        System.out.println("  3. Start server\n");
        
        WebApplication app = Web.create("My First Web App", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, World!");
        });
        
        app.get("/api/greeting", (req, res) -> {
            res.json(Map.of(
                "message", "Welcome to EST Web Framework",
                "version", "2.1.0",
                "status", "success"
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("\n".repeat(2));
            System.out.println("[X] Server started successfully!");
            System.out.println("\nPlease visit in your browser:");
            System.out.println("  - http://localhost:8080          (Hello World)");
            System.out.println("  - http://localhost:8080/api/greeting  (JSON API)");
            System.out.println("\n[X] Press Ctrl+C to stop server");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void lambdaRoutingExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("[Mode 2] Lambda Expression Routing");
        System.out.println("=".repeat(60));
        System.out.println("\nUsing Lambda expressions allows more flexible route definition");
        System.out.println("Supports path parameters, query parameters, form data, etc.\n");
        
        WebApplication app = Web.create("Lambda Routing Example", "1.0.0");
        
        System.out.println("Defined routes:");
        System.out.println("  - GET  /                    - Homepage");
        System.out.println("  - GET  /hello/:name         - Greeting with path parameter");
        System.out.println("  - GET  /user                 - With query parameters");
        System.out.println("  - POST /login                - Handle form submission");
        System.out.println("  - GET  /api/users            - REST API");
        System.out.println("  - GET  /api/users/:id        - REST API (detail)");
        System.out.println();
        
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Web Example</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                        h1 { color: #333; }
                        .nav { margin: 20px 0; }
                        .nav a { display: inline-block; margin: 5px; padding: 10px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                    </style>
                </head>
                <body>
                    <h1>Welcome to EST Web Framework</h1>
                    <p>This is an example using Lambda expressions to define routes</p>
                    <div class="nav">
                        <a href="/hello/John">Greet John</a>
                        <a href="/hello/Jane">Greet Jane</a>
                        <a href="/user?id=1001&name=John">View User</a>
                        <a href="/api/users">View User List API</a>
                    </div>
                </body>
                </html>
                """);
        });
        
        app.get("/hello/:name", (req, res) -> {
            String name = req.param("name");
            String title = req.queryParam("title", "Friend");
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Greeting</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; margin-top: 100px; }
                        h1 { color: #007bff; }
                    </style>
                </head>
                <body>
                    <h1>Hello, %s %s!</h1>
                    <p>Welcome to the EST Web World!</p>
                    <p><a href="/">Back to Homepage</a></p>
                </body>
                </html>
                """.formatted(title, name));
        });
        
        app.get("/user", (req, res) -> {
            String id = req.queryParam("id", "Unknown");
            String name = req.queryParam("name", "Unknown");
            res.json(Map.of(
                "id", id,
                "name", name,
                "source", "query_param"
            ));
        });
        
        app.post("/login", (req, res) -> {
            String username = req.formParam("username");
            String password = req.formParam("password");
            
            if ("admin".equals(username) && "123456".equals(password)) {
                res.json(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "user", username
                ));
            } else {
                res.status(401).json(Map.of(
                    "success", false,
                    "message", "Invalid username or password"
                ));
            }
        });
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.get("/users", (req, res) -> {
                    res.json(Map.of(
                        "users", new Object[]{
                            Map.of("id", 1, "name", "John", "age", 25),
                            Map.of("id", 2, "name", "Jane", "age", 30),
                            Map.of("id", 3, "name", "Bob", "age", 28)
                        },
                        "total", 3
                    ));
                });
                
                r.get("/users/:id", (req, res) -> {
                    String id = req.param("id");
                    res.json(Map.of(
                        "id", Integer.parseInt(id),
                        "name", "User" + id,
                        "email", "user" + id + "@example.com",
                        "role", "user"
                    ));
                });
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n[X] Server started successfully!");
            System.out.println("\nAccess URL: http://localhost:8080");
            System.out.println("[X] Press Ctrl+C to stop server");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void controllerExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("[Mode 3] Controller Pattern (MVC Architecture)");
        System.out.println("=".repeat(60));
        System.out.println("\nFor large applications, we recommend using controller pattern");
        System.out.println("This way organizes related routes in one controller class, cleaner code\n");
        
        WebApplication app = Web.create("Controller Example", "1.0.0");
        
        app.use(new LoggingMiddleware());
        
        HomeController homeController = new HomeController();
        UserController userController = new UserController();
        
        app.get("/", homeController::index);
        app.get("/about", homeController::about);
        app.get("/users", userController::list);
        app.get("/users/:id", userController::show);
        app.post("/users", userController::create);
        
        app.onStartup(() -> {
            System.out.println("\n[X] Server started successfully!");
            System.out.println("\nAccess URLs:");
            System.out.println("  - http://localhost:8080");
            System.out.println("  - http://localhost:8080/about");
            System.out.println("  - http://localhost:8080/users");
            System.out.println("\n[X] Press Ctrl+C to stop server");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void restApiExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("[Mode 4] REST API Pattern");
        System.out.println("=".repeat(60));
        System.out.println("\nUsing AbstractRestController to create RESTful API");
        System.out.println("Provides standard CRUD operation interfaces\n");
        
        WebApplication app = Web.create("REST API Example", "1.0.0");
        
        app.use(new LoggingMiddleware());
        app.use(new PerformanceMonitorMiddleware());
        
        UserRestController userRestController = new UserRestController();
        
        app.routes(router -> {
            router.group("/api/v1", (r, group) -> {
                r.get("/users", userRestController::list);
                r.get("/users/:id", userRestController::get);
                r.post("/users", userRestController::create);
                r.put("/users/:id", userRestController::update);
                r.delete("/users/:id", userRestController::delete);
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n[X] REST API server started successfully!");
            System.out.println("\nAvailable API endpoints:");
            System.out.println("  - GET    /api/v1/users       - Get user list");
            System.out.println("  - GET    /api/v1/users/:id   - Get single user");
            System.out.println("  - POST   /api/v1/users       - Create user");
            System.out.println("  - PUT    /api/v1/users/:id   - Update user");
            System.out.println("  - DELETE /api/v1/users/:id   - Delete user");
            System.out.println("\n[X] Press Ctrl+C to stop server");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void completeExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("[Mode 5] Complete Example");
        System.out.println("=".repeat(60));
        System.out.println("\nDemonstrating complete web framework features:");
        System.out.println("  - Middleware (logging, performance monitoring)");
        System.out.println("  - Session management");
        System.out.println("  - Static file serving");
        System.out.println("  - Template engine");
        System.out.println("  - Error handling");
        System.out.println();
        
        WebApplication app = Web.create("Complete Web App", "1.0.0");
        
        app.use(new LoggingMiddleware());
        app.use(new PerformanceMonitorMiddleware());
        
        app.get("/", (req, res) -> {
            req.session().set("visitCount", (int) req.session().getOrDefault("visitCount", 0) + 1);
            int visitCount = (int) req.session().get("visitCount");
            
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Complete Example</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                        .card { border: 1px solid #ddd; padding: 20px; margin: 10px 0; border-radius: 5px; }
                        .success { background: #d4edda; color: #155724; }
                        .info { background: #d1ecf1; color: #0c5460; }
                    </style>
                </head>
                <body>
                    <h1>EST Web Framework - Complete Example</h1>
                    
                    <div class="card info">
                        <h3>Session Information</h3>
                        <p>This is your %d visit to this page</p>
                    </div>
                    
                    <div class="card">
                        <h3>Feature Demonstration</h3>
                        <ul>
                            <li><a href="/api/status">View API Status</a></li>
                            <li><a href="/error">Test Error Handling</a></li>
                        </ul>
                    </div>
                </body>
                </html>
                """.formatted(visitCount));
        });
        
        app.get("/api/status", (req, res) -> {
            res.json(Map.of(
                "status", "ok",
                "timestamp", System.currentTimeMillis(),
                "service", "EST Web Framework",
                "version", "2.1.0"
            ));
        });
        
        app.get("/error", (req, res) -> {
            throw new RuntimeException("This is a test error for demonstrating error handling");
        });
        
        app.onError((req, res, error) -> {
            res.status(500).json(Map.of(
                "error", "Internal Server Error",
                "message", error.getMessage(),
                "path", req.path()
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("\n[X] Complete Web App started successfully!");
            System.out.println("\nAccess URL: http://localhost:8080");
            System.out.println("[X] Press Ctrl+C to stop server");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
}

class HomeController extends AbstractController {
    
    public void index(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Home</title>
                <style>
                    body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                    nav a { margin: 0 10px; color: #007bff; }
                </style>
            </head>
            <body>
                <nav>
                    <a href="/">Home</a>
                    <a href="/about">About</a>
                    <a href="/users">Users</a>
                </nav>
                <h1>Welcome to Homepage</h1>
                <p>This is a web application built with controller pattern</p>
            </body>
            </html>
            """);
    }
    
    public void about(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>About Us</title>
                <style>
                    body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                    nav a { margin: 0 10px; color: #007bff; }
                </style>
            </head>
            <body>
                <nav>
                    <a href="/">Home</a>
                    <a href="/about">About</a>
                    <a href="/users">Users</a>
                </nav>
                <h1>About Us</h1>
                <p>EST is a zero-dependency modern Java Web framework</p>
                <p>Simple, efficient, and easy to learn!</p>
            </body>
            </html>
            """);
    }
}

class UserController extends AbstractController {
    
    public void list(Request req, Response res) {
        res.json(Map.of(
            "users", new Object[]{
                Map.of("id", 1, "name", "John"),
                Map.of("id", 2, "name", "Jane"),
                Map.of("id", 3, "name", "Bob")
            }
        ));
    }
    
    public void show(Request req, Response res) {
        String id = req.param("id");
        res.json(Map.of(
            "id", id,
            "name", "User" + id
        ));
    }
    
    public void create(Request req, Response res) {
        String name = req.formParam("name");
        res.status(201).json(Map.of(
            "success", true,
            "message", "User created successfully",
            "name", name
        ));
    }
}

class UserRestController extends AbstractRestController {
    
    public void list(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", new Object[]{
                Map.of("id", 1, "name", "John", "email", "john@example.com"),
                Map.of("id", 2, "name", "Jane", "email", "jane@example.com")
            },
            "total", 2
        ));
    }
    
    public void get(Request req, Response res) {
        String id = req.param("id");
        res.json(Map.of(
            "success", true,
            "data", Map.of(
                "id", Integer.parseInt(id),
                "name", "User" + id,
                "email", "user" + id + "@example.com",
                "createdAt", System.currentTimeMillis()
            )
        ));
    }
    
    public void create(Request req, Response res) {
        String name = req.formParam("name", "Unnamed User");
        String email = req.formParam("email", "");
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "User created successfully",
            "data", Map.of(
                "id", 100,
                "name", name,
                "email", email
            )
        ));
    }
    
    public void update(Request req, Response res) {
        String id = req.param("id");
        String name = req.formParam("name");
        
        res.json(Map.of(
            "success", true,
            "message", "User updated successfully",
            "data", Map.of(
                "id", Integer.parseInt(id),
                "name", name
            )
        ));
    }
    
    public void delete(Request req, Response res) {
        String id = req.param("id");
        
        res.json(Map.of(
            "success", true,
            "message", "User deleted successfully",
            "deletedId", id
        ));
    }
}
