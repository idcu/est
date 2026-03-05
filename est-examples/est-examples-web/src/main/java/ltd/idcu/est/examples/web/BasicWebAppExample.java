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
        System.out.println("EST Web 框架 - 多种实现方式示例");
        System.out.println("=".repeat(60));
        System.out.println("\n请选择要运行的示例：");
        System.out.println("  1. 最简 Hello World（适合入门）");
        System.out.println("  2. Lambda 表达式路由（现代写法）");
        System.out.println("  3. 控制器模式（MVC 架构）");
        System.out.println("  4. REST API 模式");
        System.out.println("  5. 完整示例（包含中间件、会话、模板等）");
        System.out.println("\n本演示将运行第 1 个示例（最简 Hello World）");
        System.out.println("如需运行其他示例，请修改 main 方法中的调用\n");
        
        simplestHelloWorld();
    }
    
    public static void run() {
        main(new String[]{});
    }
    
    public static void simplestHelloWorld() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("【方式一】最简 Hello World");
        System.out.println("=".repeat(60));
        System.out.println("\n这是最简单的 Web 应用，只需 3 行代码！");
        System.out.println("适合快速了解 EST Web 框架的核心概念\n");
        
        System.out.println("代码步骤：");
        System.out.println("  1. 创建 Web 应用");
        System.out.println("  2. 定义路由（当访问 / 时返回什么）");
        System.out.println("  3. 启动服务器\n");
        
        WebApplication app = Web.create("我的第一个 Web 应用", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, World! 你好，世界！");
        });
        
        app.get("/api/greeting", (req, res) -> {
            res.json(Map.of(
                "message", "欢迎使用 EST Web 框架！",
                "version", "1.3.0",
                "status", "success"
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("\n".repeat(2));
            System.out.println("✓ 服务器启动成功！");
            System.out.println("\n请在浏览器中访问：");
            System.out.println("  - http://localhost:8080          (Hello World)");
            System.out.println("  - http://localhost:8080/api/greeting  (JSON API)");
            System.out.println("\n按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void lambdaRoutingExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("【方式二】Lambda 表达式路由");
        System.out.println("=".repeat(60));
        System.out.println("\n使用 Lambda 表达式可以更灵活地定义路由");
        System.out.println("支持路径参数、查询参数、表单数据等\n");
        
        WebApplication app = Web.create("Lambda 路由示例", "1.0.0");
        
        System.out.println("定义的路由：");
        System.out.println("  - GET  /                    - 首页");
        System.out.println("  - GET  /hello/:name         - 带路径参数的问候");
        System.out.println("  - GET  /user                 - 带查询参数");
        System.out.println("  - POST /login                - 处理表单提交");
        System.out.println("  - GET  /api/users            - REST API");
        System.out.println("  - GET  /api/users/:id        - REST API（详情）");
        System.out.println();
        
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Web 示例</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                        h1 { color: #333; }
                        .nav { margin: 20px 0; }
                        .nav a { display: inline-block; margin: 5px; padding: 10px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                    </style>
                </head>
                <body>
                    <h1>欢迎使用 EST Web 框架！</h1>
                    <p>这是一个使用 Lambda 表达式定义路由的示例。</p>
                    <div class="nav">
                        <a href="/hello/小明">问候小明</a>
                        <a href="/hello/小红">问候小红</a>
                        <a href="/user?id=1001&name=张三">查看用户</a>
                        <a href="/api/users">查看用户列表 API</a>
                    </div>
                </body>
                </html>
                """);
        });
        
        app.get("/hello/:name", (req, res) -> {
            String name = req.param("name");
            String title = req.queryParam("title", "同学");
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>问候</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; margin-top: 100px; }
                        h1 { color: #007bff; }
                    </style>
                </head>
                <body>
                    <h1>你好，%s %s！</h1>
                    <p>欢迎来到 EST Web 世界！</p>
                    <p><a href="/">返回首页</a></p>
                </body>
                </html>
                """.formatted(title, name));
        });
        
        app.get("/user", (req, res) -> {
            String id = req.queryParam("id", "未知");
            String name = req.queryParam("name", "未知");
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
                    "message", "登录成功",
                    "user", username
                ));
            } else {
                res.status(401).json(Map.of(
                    "success", false,
                    "message", "用户名或密码错误"
                ));
            }
        });
        
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.get("/users", (req, res) -> {
                    res.json(Map.of(
                        "users", new Object[]{
                            Map.of("id", 1, "name", "张三", "age", 25),
                            Map.of("id", 2, "name", "李四", "age", 30),
                            Map.of("id", 3, "name", "王五", "age", 28)
                        },
                        "total", 3
                    ));
                });
                
                r.get("/users/:id", (req, res) -> {
                    String id = req.param("id");
                    res.json(Map.of(
                        "id", Integer.parseInt(id),
                        "name", "用户" + id,
                        "email", "user" + id + "@example.com",
                        "role", "user"
                    ));
                });
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ 服务器启动成功！");
            System.out.println("\n访问地址：http://localhost:8080");
            System.out.println("按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void controllerExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("【方式三】控制器模式（MVC 架构）");
        System.out.println("=".repeat(60));
        System.out.println("\n对于大型应用，推荐使用控制器模式");
        System.out.println("这种方式将相关的路由组织在一个控制器类中，代码更清晰\n");
        
        WebApplication app = Web.create("控制器示例", "1.0.0");
        
        app.use(new LoggingMiddleware());
        
        HomeController homeController = new HomeController();
        UserController userController = new UserController();
        
        app.get("/", homeController::index);
        app.get("/about", homeController::about);
        app.get("/users", userController::list);
        app.get("/users/:id", userController::show);
        app.post("/users", userController::create);
        
        app.onStartup(() -> {
            System.out.println("\n✓ 服务器启动成功！");
            System.out.println("\n访问地址：");
            System.out.println("  - http://localhost:8080");
            System.out.println("  - http://localhost:8080/about");
            System.out.println("  - http://localhost:8080/users");
            System.out.println("\n按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void restApiExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("【方式四】REST API 模式");
        System.out.println("=".repeat(60));
        System.out.println("\n使用 AbstractRestController 创建 RESTful API");
        System.out.println("提供标准的 CRUD 操作接口\n");
        
        WebApplication app = Web.create("REST API 示例", "1.0.0");
        
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
            System.out.println("\n✓ REST API 服务器启动成功！");
            System.out.println("\n可用的 API 端点：");
            System.out.println("  - GET    /api/v1/users       - 获取用户列表");
            System.out.println("  - GET    /api/v1/users/:id   - 获取单个用户");
            System.out.println("  - POST   /api/v1/users       - 创建用户");
            System.out.println("  - PUT    /api/v1/users/:id   - 更新用户");
            System.out.println("  - DELETE /api/v1/users/:id   - 删除用户");
            System.out.println("\n按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(60));
        });
        
        app.run(8080);
    }
    
    public static void completeExample() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("【方式五】完整示例");
        System.out.println("=".repeat(60));
        System.out.println("\n展示 Web 框架的完整功能：");
        System.out.println("  - 中间件（日志、性能监控）");
        System.out.println("  - 会话管理");
        System.out.println("  - 静态文件服务");
        System.out.println("  - 模板引擎");
        System.out.println("  - 错误处理");
        System.out.println();
        
        WebApplication app = Web.create("完整 Web 应用", "1.0.0");
        
        app.use(new LoggingMiddleware());
        app.use(new PerformanceMonitorMiddleware());
        
        app.get("/", (req, res) -> {
            req.session().set("visitCount", (int) req.session().getOrDefault("visitCount", 0) + 1);
            int visitCount = (int) req.session().get("visitCount");
            
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST 完整示例</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                        .card { border: 1px solid #ddd; padding: 20px; margin: 10px 0; border-radius: 5px; }
                        .success { background: #d4edda; color: #155724; }
                        .info { background: #d1ecf1; color: #0c5460; }
                    </style>
                </head>
                <body>
                    <h1>EST Web 框架 - 完整示例</h1>
                    
                    <div class="card info">
                        <h3>会话信息</h3>
                        <p>这是你第 %d 次访问本页面</p>
                    </div>
                    
                    <div class="card">
                        <h3>功能演示</h3>
                        <ul>
                            <li><a href="/api/status">查看 API 状态</a></li>
                            <li><a href="/error">测试错误处理</a></li>
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
                "version", "1.3.0"
            ));
        });
        
        app.get("/error", (req, res) -> {
            throw new RuntimeException("这是一个测试错误，用于演示错误处理");
        });
        
        app.onError((req, res, error) -> {
            res.status(500).json(Map.of(
                "error", "Internal Server Error",
                "message", error.getMessage(),
                "path", req.path()
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ 完整 Web 应用启动成功！");
            System.out.println("\n访问地址：http://localhost:8080");
            System.out.println("按 Ctrl+C 停止服务器");
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
                <title>首页</title>
                <style>
                    body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                    nav a { margin: 0 10px; color: #007bff; }
                </style>
            </head>
            <body>
                <nav>
                    <a href="/">首页</a>
                    <a href="/about">关于</a>
                    <a href="/users">用户</a>
                </nav>
                <h1>欢迎来到首页！</h1>
                <p>这是使用控制器模式构建的 Web 应用。</p>
            </body>
            </html>
            """);
    }
    
    public void about(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>关于我们</title>
                <style>
                    body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                    nav a { margin: 0 10px; color: #007bff; }
                </style>
            </head>
            <body>
                <nav>
                    <a href="/">首页</a>
                    <a href="/about">关于</a>
                    <a href="/users">用户</a>
                </nav>
                <h1>关于我们</h1>
                <p>EST 是一个零依赖的现代 Java Web 框架。</p>
                <p>简单、高效、易于学习！</p>
            </body>
            </html>
            """);
    }
}

class UserController extends AbstractController {
    
    public void list(Request req, Response res) {
        res.json(Map.of(
            "users", new Object[]{
                Map.of("id", 1, "name", "张三"),
                Map.of("id", 2, "name", "李四"),
                Map.of("id", 3, "name", "王五")
            }
        ));
    }
    
    public void show(Request req, Response res) {
        String id = req.param("id");
        res.json(Map.of(
            "id", id,
            "name", "用户" + id
        ));
    }
    
    public void create(Request req, Response res) {
        String name = req.formParam("name");
        res.status(201).json(Map.of(
            "success", true,
            "message", "用户创建成功",
            "name", name
        ));
    }
}

class UserRestController extends AbstractRestController {
    
    public void list(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", new Object[]{
                Map.of("id", 1, "name", "张三", "email", "zhangsan@example.com"),
                Map.of("id", 2, "name", "李四", "email", "lisi@example.com")
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
                "name", "用户" + id,
                "email", "user" + id + "@example.com",
                "createdAt", System.currentTimeMillis()
            )
        ));
    }
    
    public void create(Request req, Response res) {
        String name = req.formParam("name", "未命名用户");
        String email = req.formParam("email", "");
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "用户创建成功",
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
            "message", "用户更新成功",
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
            "message", "用户删除成功",
            "deletedId", id
        ));
    }
}
